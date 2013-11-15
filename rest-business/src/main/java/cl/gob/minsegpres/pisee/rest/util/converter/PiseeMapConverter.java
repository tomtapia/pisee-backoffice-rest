package cl.gob.minsegpres.pisee.rest.util.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class PiseeMapConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
	   return type.equals(HashMap.class) ;
	}
	
	@SuppressWarnings("rawtypes")
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
	      Map map = (Map) source;
	      for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
	         Map.Entry entry = (Map.Entry) iterator.next();
	         writer.startNode(entry.getKey().toString());
	         writer.setValue(entry.getValue().toString());
	         writer.endNode();
	       }
	}
	
	@SuppressWarnings("rawtypes")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	   Map map = new HashMap();
	   return map;
	}
	
}