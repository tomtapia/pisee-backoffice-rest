package cl.gob.minsegpres.pisee.rest.pruebas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.impl.dv.util.Base64;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DescifrarMensaje {

	public static String keystoreType = "PKCS12";
	public static String keystoreFile = "/Users/diegotorres/Work/IDE/workspace/SoapClient/src/certificadoDigital.p12";
	public static String respuesta = "/Users/diegotorres/Work/IDE/workspace/SoapClient/src/enc2.xml";
	public static String descifrado = "/Users/diegotorres/Work/IDE/workspace/SoapClient/src/dec2.xml";
	public static String keystorePass = "genchi2013"; // password de mi keystore
	public static String privateKeyAlias = "id e-sign s.a. de ricardo david nesvara herrera"; // alias
																								// en
																								// el
																								// keystore
																								// de
																								// la
																								// llave
																								// privada
																								// correspondiente
																								// al
																								// certificado
	public static String privateKeyPass = "genchi2013"; // password de la llave
														// privada
	public static String certificateAlias = "id e-sign s.a. de ricardo david nesvara herrera"; // alias
																								// en
																								// keystore
																								// del
																								// certificado
																								// (es
																								// lo
																								// mismo
																								// que
																								// privateKeyAlias)

	static {
		org.apache.xml.security.Init.init();
	}

	private static Document loadEncryptionDocument() throws Exception {
		// carga el documento con la respuesta encriptada:
		File encryptionFile = new File(respuesta);
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(encryptionFile);
		return document;
	}

	private static Key loadDecryptionKey(Document document) throws Exception {
		// método para recuperar la llave simétrica utilizada en el cifrado:
		Element e = (Element) document.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);
		XMLCipher cipher = XMLCipher.getInstance();
		cipher.init(XMLCipher.DECRYPT_MODE, null);
		EncryptedData encryptedData = cipher.loadEncryptedData(document, e);
		if (encryptedData == null) {
			throw new Exception("EncryptedData es null");
		} else if (encryptedData.getKeyInfo() == null) {
			throw new Exception("KeyInfo de EncryptedData es null");
		}

		// se carga el keystore y se obtiene la llave privada
		KeyStore keyStore = KeyStore.getInstance(keystoreType);
		keyStore.load(new FileInputStream(keystoreFile), keystorePass.toCharArray());
		KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(privateKeyAlias, new KeyStore.PasswordProtection(privateKeyPass.toCharArray()));

		// se recupera la llave simétrica
		EncryptedKey ek = encryptedData.getKeyInfo().itemEncryptedKey(0);
		Key key = null;
		if (ek != null) {
			XMLCipher keyCipher = XMLCipher.getInstance();
			keyCipher.init(XMLCipher.UNWRAP_MODE, entry.getPrivateKey());
			key = keyCipher.decryptKey(ek, encryptedData.getEncryptionMethod().getAlgorithm());
		}
		return key;
	}

	private static void outputDocToFile(Document doc, String fileName) throws Exception {
		// método que guarda un archivo xml con datos descifrados
		File encryptionFile = new File(fileName);
		FileOutputStream f = new FileOutputStream(encryptionFile);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(f);
		transformer.transform(source, result);

		f.close();
	}

	public static void main(String unused[]) throws Exception {

		// carga documento con respuesta de srcei (encriptada):
		Document document = loadEncryptionDocument();

		Element encryptedDataElement = (Element) document.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);
		Element encryptedKeyElement = (Element) document.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDKEY).item(0);

		// llama método para recuperar llave de encriptación:
		Key secretKey = loadDecryptionKey(document);
		String strkey = Base64.encode(secretKey.getEncoded());
		System.out.println(strkey);

		XMLCipher xmlCipher = XMLCipher.getInstance();
		xmlCipher.init(XMLCipher.DECRYPT_MODE, secretKey);
		// esto es para reemplazar el contenido del documento, con los datos ya
		// descifrados:
		xmlCipher.doFinal(document, encryptedDataElement);

		// se guarda el resultado en un archivo:
		outputDocToFile(document, descifrado);
	}
}