package cl.gob.minsegpres.pisee.rest.entities.sii;

import org.w3c.dom.Document;

public class InteroperabilityMessageFactory implements MessageFactory {

	private InteroperabilityMessageFactory() {
	}

	public static MessageFactory newInstance() {
		return new InteroperabilityMessageFactory();
	}

	public Message createSeedMessage() {
		return SeedMessage.newInstance();
	}

	public Message createTokenMessage(Seed seed, SecurityOperations securityOperations) {
		return TokenSignedMessage.newInstance(seed, securityOperations);
	}

//	@Override
//	public Message createSimplePiseeMessage(Sobre sobre) {
//		return SimplePisseMessage.newInstance(sobre);
//	}
//
//	@Override
//	public Message createSignedPiseeMessage(Sobre sobre, SecurityOperations securityOperations) {
//		return SignedPiseeMessage.newInstance(sobre, securityOperations);
//	}

	public Message createGenericMessage(Document message) {
		return GenericMessage.newInstance(message);
	}
}