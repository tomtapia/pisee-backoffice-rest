package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

public class SignedPiseeMessage extends SimplePisseMessage {
	
	private SecurityOperations securityOperations;

	private SignedPiseeMessage(SecurityOperations securityOperations) {
		this.securityOperations = securityOperations;
	}

	public static Message newInstance(SecurityOperations securityOperations) {
		return new SignedPiseeMessage(securityOperations);
	}	
	
	/*
	private SignedPiseeMessage(Sobre sobre, SecurityOperations securityOperations) {
		super(sobre);
		this.securityOperations = securityOperations;
	}

	public static Message newInstance(Sobre sobre, SecurityOperations securityOperations) {
		return new SignedPiseeMessage(sobre, securityOperations);
	}
	 */
	
	@Override
	public boolean isSigned() {
		return true;
	}

	@Override
	public Document getMessage() {
		return super.getMessage();
	}
	
	public SecurityOperations getSecurityOperations()
	{
		return this.securityOperations;
	}
	
	@Override
	public Document getSignedMessage() throws OperationNotSupportedException {
		return this.securityOperations.sign(this);
	}
}
