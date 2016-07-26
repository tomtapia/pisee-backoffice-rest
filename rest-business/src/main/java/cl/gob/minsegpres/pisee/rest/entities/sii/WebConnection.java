package cl.gob.minsegpres.pisee.rest.entities.sii;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.naming.OperationNotSupportedException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.w3c.dom.Document;


public class WebConnection {

	private String userAgent;
	private String soapAction;
	private int readTimeout;
	private int connectTimeout;
	private URL url;
	private HttpsURLConnection conn;
	private String contentType;
	private Message message;
	private Credentials credentials;

	public static WebConnection newInstance(String uri, Message message) throws IOException {
		return new WebConnection(uri, message);
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public void setCredentials(String user, String password) {
		this.credentials = Credentials.newInstance(user, password);
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	private WebConnection(String uri, Message message) throws IOException {
		this.userAgent = "Jakarta Commons-HttpClient/3.1";
		this.contentType = "text/xml";
		this.soapAction = "";
		this.connectTimeout = 120000;
		this.readTimeout = 120000;
		this.url = new URL(uri);
		this.message = message;
		this.setProperties();
	}

	private void setProperties() throws IOException {
		this.conn = (HttpsURLConnection) this.url.openConnection();
		this.conn.setRequestMethod("POST");
		this.conn.setRequestProperty("User-Agent", this.userAgent);
		this.conn.setRequestProperty("Content-Type", this.contentType);
		this.conn.setReadTimeout(readTimeout);
		this.conn.setConnectTimeout(connectTimeout);
		this.conn.setDoOutput(true);
		// this.conn.setDoInput(true);
		if (this.credentials != null){
			this.conn.setRequestProperty("Authorization", "Basic " + credentials);
		}
		this.conn.setRequestProperty("SOAPAction", soapAction);
	}

	private void setSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, new TrustManager[] { new CustomX509TrustManager() }, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	public Document response() throws IOException, KeyManagementException, NoSuchAlgorithmException, OperationNotSupportedException {
		try {
			this.setSSLContext();
			String message = XmlHelpers.xmlToString(!this.message.isSigned() ? this.message.getMessage() : this.message.getSignedMessage());
			this.setProperties();
			OutputStream reqStream = conn.getOutputStream();
			reqStream.write(message.getBytes());
			InputStream resStream = conn.getInputStream();
			byte[] byteBuf = new byte[1024];
			int len = resStream.read(byteBuf);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while (len > -1) {
				outStream.write(byteBuf, 0, len);
				len = resStream.read(byteBuf);
			}
			outStream.close();
			resStream.close();
			return XmlHelpers.byteArrayToXml(outStream);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Document response(boolean decrypt) throws KeyManagementException, NoSuchAlgorithmException, IOException, OperationNotSupportedException {
		if (!this.message.isSigned() || !decrypt){
			return response();
		}
		Document response = response();
		SignedPiseeMessage spm = (SignedPiseeMessage) this.message;
		spm.getSecurityOperations().decrypt(response);
		return response;
	}

	public Document response(Filter filter) {
		return response(filter, false);
	}

	public Document response(Filter filter, boolean decrypt) {
		try {
			return filter.apply(response(decrypt));
		} catch (Exception e) {
			return null;
		}
	}
}
