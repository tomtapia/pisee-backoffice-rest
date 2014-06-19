package cl.gob.minsegpres.pisee.rest.api.business;

import cl.gob.minsegpres.pisee.rest.api.reader.ApiLinkReader;
import cl.gob.minsegpres.pisee.rest.api.reader.ApiServiceReader;

import com.google.gson.Gson;

public class ApiBusiness {

	private static final String _API_LINKS = "api_links";
	private static final String _API_SERVICES = "api_services";

	public String findJSON(String typeClass) {
		if (_API_SERVICES.equals(typeClass)){
			return toJSON(ApiServiceReader.getInstance().getApiServices());
		}else if(_API_LINKS.equals(typeClass)){
			return toJSON(ApiLinkReader.getInstance().getApiLinks());
		}
		return null;
	}

	private String toJSON(Object theObject) {
		Gson gson = new Gson();
		return gson.toJson(theObject);
	}
}
