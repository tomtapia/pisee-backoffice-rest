package cl.gob.minsegpres.pisee.rest.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;

public class PiseeStringUtils {
	
	private final static Log LOGGER = LogFactory.getLog(PiseeStringUtils.class);
	
	public static String prettyFormat(String content) {
		String tmp;
		tmp = content;
		tmp = tmp.replaceAll(AppConstants.LINE_NEW, AppConstants.BLANK);
		tmp = tmp.replaceAll(AppConstants.LINE_TAB, AppConstants.BLANK);
		return tmp;
	}
	public static Element elementRecursive(final String nodesPath, final Element element) {
		String nodeName = StringUtils.substringBefore(nodesPath, AppConstants.SLASH);
		String restNodes = StringUtils.substringAfter(nodesPath, AppConstants.SLASH);
		if (element == null){
			return element;
		}
		if (StringUtils.isEmpty(restNodes)) {
			return element.element(nodeName);
		}
		return elementRecursive(restNodes, element.element(nodeName));
	}
	
	public static org.dom4j.Document getDom4jDocument(org.w3c.dom.Document w3cDocument) {
        org.dom4j.Document dom4jDocument  = null;
        DOMReader xmlReader  = null;
        try{
            xmlReader = new DOMReader();            
            dom4jDocument = xmlReader.read(w3cDocument);
        }catch(Exception e){
        	LOGGER.error("General Exception :- "+e.getMessage());
        }
        return dom4jDocument;   
    } 	

}
