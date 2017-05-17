package cl.gob.minsegpres.pisee.rest.entities.soap;

public class KeyStoreParameterSOAP {

	private String serviceName;
	private String keystoreType;
	private String keystoreFile;
	private String keystorePass;
	
	//---
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
	
	//---
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
	
}
