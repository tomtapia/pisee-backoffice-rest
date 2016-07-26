package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.xml.bind.DatatypeConverter;

public class Credentials {
	private static final String ToBase64Stringformat = "%s:%s";

	private String user;
	private String pwd;

	private Credentials(String user, String password) {
		this.user = user;
		this.pwd = password;
	}

	public static Credentials newInstance(String user, String password) {
		return new Credentials(user, password);
	}

	@Override
	public String toString() {
		return DatatypeConverter.printBase64Binary(String.format(
				ToBase64Stringformat, this.user, this.pwd).getBytes());
	}
}
