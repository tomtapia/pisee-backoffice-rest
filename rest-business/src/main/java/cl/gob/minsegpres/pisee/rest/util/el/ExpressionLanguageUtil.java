package cl.gob.minsegpres.pisee.rest.util.el;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class ExpressionLanguageUtil {
	
	private static JexlEngine jexlEngine = new JexlEngine();
	private static JexlContext jexlContext = new MapContext();
	
	public ExpressionLanguageUtil(){	}
	
	public Object processExpressionLanguage(String expressionLanguage, Object theObject){
	    Expression expression = jexlEngine.createExpression(expressionLanguage);
		jexlContext.set(extractAliasName(expressionLanguage), theObject);
		return expression.evaluate(jexlContext);
	}
	
	public Object setProperty(Object theObject, String expressionLanguage, String value){
		jexlContext.set(extractAliasName(expressionLanguage), theObject);
		jexlEngine.setProperty(theObject, extractPropertyName(expressionLanguage), value);
		return theObject;
	}
	
	public Object setProperty(Object theObject, String expressionLanguage, String value, String dataType){
		jexlContext.set(extractAliasName(expressionLanguage), theObject);
		Object parseValue = null;
		if("Integer".equals(dataType)){
			//parseValue = ConverterUtil.toIntegerClass(value, null);
			parseValue = Integer.valueOf(value);
		}
		jexlEngine.setProperty(theObject, extractPropertyName(expressionLanguage), parseValue);
		return theObject;
	}	
	
	public Object newInstance(String clazz, Object[] argzz){
		return jexlEngine.newInstance(clazz,  argzz);
	}	
	
	private String extractAliasName(String expressionLanguage){
	    String[] arrExpression = expressionLanguage.split(AppConstants.SLASH_SLASH_DOT);
	    return arrExpression[0];
	}

	private String extractPropertyName(String expressionLanguage){
		String[] arrExpression = expressionLanguage.split(AppConstants.SLASH_SLASH_DOT);
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 2; i < arrExpression.length; i++){
			strBuffer.append(arrExpression[i]);
		}
		return strBuffer.toString();
	}
	
}
