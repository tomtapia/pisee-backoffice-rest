package cl.gob.minsegpres.pisee.rest.util;

public final class AppConstants {

	public final static String SLASH = "/";
	public final static String BLANK = "";
	public final static String SPACE = " ";
	public final static String LINE_NEW = "\n";
	public final static String LINE_TAB = "\t";
	public final static String QUESTION = "?";
	public final static String AMPERSAND = "&";
	public final static String EQUAL = "=";
	public final static String KEY_LEFT = "{";
	public final static String KEY_RIGHT = "}";
	
	public final static String PREFIX_EL = "ELPISEE.";
	public final static String LOWER_THAN = "<";
	public final static String SLASH_SLASH_DOT = "\\.";
	
	public final static String HOME_DIR = System.getProperty("jboss.server.home.dir");
	public final static String CONFIG_DIR = "/deploy/base/piseeConf/";
	public final static String CONFIG_SERVICES_DIR = "/deploy/base/piseeConf/rest_services/";
	
	public final static String PREFIX_SERVICE_SOAP_SII = "SOAP_SII_";
	public final static String PREFIX_SERVICE_SOAP = "SOAP_";
	public final static String PREFIX_SERVICE_REST = "REST_";	
	public final static String PREFIX_CONFIG_SERVICES_CONFIG = "_config.xml";
	public final static String PREFIX_CONFIG_SERVICES_INPUT = "_input.xml";
	public final static String PREFIX_CONFIG_SERVICES_OUTPUT = "_output.xml";
	public final static String PREFIX_CONFIG_SERVICES_KEYSTORE = "_keystore.xml";
	
	public final static String BRACKET_INI = "[";
	public final static String BRACKET_END = "]";
	
	
	public static final String _CODE_OK = "00";
	public static final String _CODE_OK_PROVEEDOR_NO_DATA_FOUND = "05";
	public static final String _CODE_OK_PROVEEDOR_NO_DATA_AVAILABLE = "07";
	public static final String _CODE_ERROR_PROVEEDOR_TIMEOUT = "06";
	public static final String _CODE_ERROR_PROVEEDOR_INTERNO_01 = "-01";
	public static final String _CODE_ERROR_PROVEEDOR_INTERNO_02 = "-02";
	public static final String _CODE_ERROR_PROVEEDOR_INTERNO_03 = "-03";
	public static final String _CODE_ERROR_PROVEEDOR_INTERNO_04 = "-04";
	public static final String _CODE_ERROR_CONSUMIDOR_AUTENTICACION = "01";
	public static final String _CODE_ERROR_CONSUMIDOR_AUTORIZACION = "02";
	public static final String _CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA = "03";
	public static final String _CODE_ERROR_CONSUMIDOR_XML_ENTRADA = "04";
	public static final String _CODE_ERROR_CONSUMIDOR_TRAMITE_NO_EXISTE = "08";
	
	public static final String _MSG_CODE_OK = "RESPUESTA EXISTOSA";
	public static final String _MSG_INTERNAL_ERROR = "Error en el proceso de llamado al Proveedor";
	public static final String _MSG_INVALID_TOKEN = "Token invalido para consumo de servicio";
	public static final String _MSG_BLOCKED_SERVICE = "El servicio se encuentra bloqueado para consumo via REST";	
	public static final String _MSG_INVALID_NULL_PARAMETER = "El siguiente parametro es obligatorio y tiene valor en null: ";
	public static final String _MSG_INVALID_RUT_PARAMETER = "Rut no valido";
	public static final String _MSG_INVALID_PARAMETERS = "Parametros de entrada son invalidos";
	public static final String _MSG_VALIDATE_CALL_OAUTH = "Ha ocurrido un error en la validacion OAUTH";
	public static final String _MSG_VALIDATE_DATA_OAUTH = "Usuario OAUTH no correponde para consumir REST";
	public static final String _MSG_ERROR_HTTP = "Error HTTP: ";
	public static final String _MSG_ERROR = "Error: ";
	public static final String _MSG_TIMEOUT = "El servicio no respondio, TIMEOUT";
	public static final String _MSG_UNSUPPORT_HTTP_METHOD = "El siguiente metodo HTTP no se encuentra soportado: ";
	public static final String _MSG_INVALID_TOKEN_OAUTH = "El Token de OAUTH es invalido";
	
	public static final String _EMISOR_PISEE = "PISEE";
	public static final String _EMISOR_CONSUMIDOR = "CONSUMIDOR";
	public static final String _EMISOR_PROVEEDOR = "PROVEEDOR";
	
	public static final String _HTTP_METHOD_GET = "GET";
	public static final String _HTTP_METHOD_POST = "POST";
	public static final String _HTTP_METHOD_PUT = "PUT";
	
	public static final String _READ_TIMED_OUT = "Read timed out";
}
