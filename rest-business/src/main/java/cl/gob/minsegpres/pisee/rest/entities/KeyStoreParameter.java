package cl.gob.minsegpres.pisee.rest.entities;

public class KeyStoreParameter {

	private String serviceName;
	private String keystoreType;
	private String keystoreFile;
	private String keystorePass;
	private String privateKeyAlias;
	private String privateKeyPass;
	
	public String getServiceName() {
		return serviceName;
	}
	public String getKeystoreType() {
		return keystoreType;
	}
	public String getKeystoreFile() {
		return keystoreFile;
	}
	public String getKeystorePass() {
		return keystorePass;
	}
	public String getPrivateKeyAlias() {
		return privateKeyAlias;
	}
	public String getPrivateKeyPass() {
		return privateKeyPass;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}	
	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}
	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}
	public void setKeystorePass(String keystorePass) {
		this.keystorePass = keystorePass;
	}
	public void setPrivateKeyAlias(String privateKeyAlias) {
		this.privateKeyAlias = privateKeyAlias;
	}
	public void setPrivateKeyPass(String privateKeyPass) {
		this.privateKeyPass = privateKeyPass;
	}
	
	
}
