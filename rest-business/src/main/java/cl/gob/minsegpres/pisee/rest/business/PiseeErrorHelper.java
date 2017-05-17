package cl.gob.minsegpres.pisee.rest.business;

import java.util.List;

import cl.gob.minsegpres.pisee.core.service.ErrorService;
import cl.gob.minsegpres.pisee.core.model.Error;

public class PiseeErrorHelper {

	private static PiseeErrorHelper instance = null;
	private List<Error> errores;
	
	public static synchronized PiseeErrorHelper getInstance() {
		if (instance == null){
			instance = new PiseeErrorHelper();
		}
		return instance;
	} 
	
	private PiseeErrorHelper(){
		readErrors();
	}

	private void readErrors(){
		ErrorService theService = new ErrorService();
		errores = theService.findErrores();
	}
	
	public Error findErrorByCodigo(String codigo){
		Error theError = null;
		if (null != codigo){
			for (Error e: errores){
				if (codigo.equals(e.getCodigo())){
					theError = e;
				}
			}			
		}
		return theError;
	}
	
}
