package cl.gob.minsegpres.pisee.rest.entities.sii;

import org.w3c.dom.Document;

public interface Filter {
	Document apply(Document document);
}
