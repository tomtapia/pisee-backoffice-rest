package cl.gob.minsegpres.pisee.rest.entities.sii;

import cl.gob.minsegpres.pisee.rest.entities.security.Certificate;

public interface SecurityOperationsFactory {
	
	SecurityOperations createSiiSecurityOperations(Certificate CertificateInfo);

	
}
