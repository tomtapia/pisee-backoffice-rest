package cl.gob.minsegpres.pisee.rest.entities.sii;

import javax.naming.OperationNotSupportedException;

import org.w3c.dom.Document;

class TokenMessage implements Message {

	private Seed seed;
	private static final String TOKEN_MESSAGE_FORMAT = "<gettoken><item><Semilla>%s</Semilla></item></gettoken>";
	
	public static Message newInstance(Seed seed)
	{
		return new TokenMessage(seed);
	}
	
	protected TokenMessage(Seed seed)
	{
		this.seed = seed;
	}
	
	@Override
	public boolean isSigned() {
		return true;
	}

	@Override
	public boolean hasMessage() {
		return true;
	}

	@Override
	public Document getMessage() {
		return XmlHelpers.stringToXml(String.format(TOKEN_MESSAGE_FORMAT, seed.getValue()));
	}

	@Override
	public Document getSignedMessage() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}
}
