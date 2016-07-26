package cl.gob.minsegpres.pisee.rest.entities.sii;

import cl.gob.minsegpres.pisee.rest.entities.security.Certificate;

public class OrganismSecurityOperationsFactory implements SecurityOperationsFactory {

	private OrganismSecurityOperationsFactory() { }
	public static SecurityOperationsFactory newInstance()
	{
		return new OrganismSecurityOperationsFactory();
	}
	
	@Override
	public SecurityOperations createSiiSecurityOperations(Certificate certificate) {
		return SiiSecurityOperations.newInstance(certificate);
	}

}
