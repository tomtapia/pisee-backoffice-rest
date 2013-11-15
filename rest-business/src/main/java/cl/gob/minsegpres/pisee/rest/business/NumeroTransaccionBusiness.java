package cl.gob.minsegpres.pisee.rest.business;

import java.text.DecimalFormat;
import java.util.Calendar;

public class NumeroTransaccionBusiness {

	private static final String _ZERO_FORMAT = "0000000";
	private Long correlativo = null;	
	private Calendar diaActual;
	private static NumeroTransaccionBusiness _instance = null;
	
	private NumeroTransaccionBusiness(){
		diaActual = Calendar.getInstance();
		correlativo = 0L;
	}
	
	public static synchronized NumeroTransaccionBusiness getInstance(){
		if (null == _instance){
			_instance = new NumeroTransaccionBusiness();
		}
		return _instance;		
	}
	
	public String getNumeroTransaccion(){
		Calendar ahora = Calendar.getInstance();
		if (isSameDay(diaActual, ahora)){
			correlativo = correlativo + 1;
		}else{		
			diaActual = ahora;
			correlativo = 1L;
		}
		return customFormat(_ZERO_FORMAT, correlativo);
	}
	
	private boolean isSameDay(Calendar diaActual, Calendar ahora){
		if ( (diaActual.get(Calendar.YEAR) == ahora.get(Calendar.YEAR))  
				&& (diaActual.get(Calendar.MONTH) == ahora.get(Calendar.MONTH)) 
					&& (diaActual.get(Calendar.DAY_OF_MONTH) == ahora.get(Calendar.DAY_OF_MONTH))
						){
			return true;
		}
		return false;
	}
		
	private static String customFormat(String pattern, double value ) {
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		return myFormatter.format(value);
	}	
	
}
