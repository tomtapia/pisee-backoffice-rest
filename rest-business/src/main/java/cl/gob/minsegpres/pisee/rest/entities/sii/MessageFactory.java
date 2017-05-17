package cl.gob.minsegpres.pisee.rest.entities.sii;

import org.w3c.dom.Document;

public interface MessageFactory {
	
	Message createSeedMessage();
	
	Message createTokenMessage(Seed seed, SecurityOperations securityOperations);
	
//	Message createSimplePiseeMessage(Sobre sobre);
//	
//	Message createSignedPiseeMessage(Sobre sobre, SecurityOperations securityOperations);
	
	Message createGenericMessage(Document message);
	
}