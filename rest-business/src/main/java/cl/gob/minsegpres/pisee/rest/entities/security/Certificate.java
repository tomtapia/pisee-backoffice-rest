package cl.gob.minsegpres.pisee.rest.entities.security;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public interface Certificate {
	X509Certificate getCertificate();
	KeyPair getKeyPair();
}
