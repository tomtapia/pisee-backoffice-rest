package cl.gob.minsegpres.pisee.rest.util;

public final class AppConstants {

	public final static String SLASH = "/";
	public final static String BLANK = "";
	public final static String LINE_NEW = "\n";
	public final static String LINE_TAB = "\t";
	
	public final static String PREFIX_EL = "ELPISEE.";
	public final static String LOWER_THAN = "<";
	public final static String SLASH_SLASH_DOT = "\\.";
	
	public final static String HOME_DIR = System.getProperty("jboss.server.home.dir");
	public final static String CONFIG_SERVICES_DIR = "/deploy/base/piseeConf/rest_services/";

	public final static String PREFIX_CONFIG_SERVICES_INPUT = "_input.xml";
	public final static String PREFIX_CONFIG_SERVICES_OUTPUT = "_output.xml";
	
	public final static String BRACKET_INI = "[";
	public final static String BRACKET_END = "]";
	
	
	///---- MENSAJES DE RESPUESTA
	public static final String _MSG_INTERNAL_ERROR = "Error en el proceso de llamado al Proveedor";
	public static final String _MSG_INVALID_TOKEN = "Token invalido";
	public static final String _MSG_INVALID_NULL_PARAMETER = "Error en parametro null: ";
	
	public static final String _CODE_OK = "00";
	public static final String _CODE_NOK_TOKEN = "NOK_TOKEN";
	public static final String _CODE_NOK_PARAMETER = "NOK_PARAMETER";
	public static final String _CODE_NOK_INTERNAL = "NOK_INTERNAL_ERROR";
	
	public static final String _EMISOR_PISEE = "PISEE";
	
	
}
