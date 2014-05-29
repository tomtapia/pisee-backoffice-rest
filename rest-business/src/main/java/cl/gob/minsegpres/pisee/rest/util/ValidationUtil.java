package cl.gob.minsegpres.pisee.rest.util;

import org.apache.commons.lang.StringUtils;

import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

public class ValidationUtil {
	
	public static boolean validarRut(String rut, String digitoVerificador) {
		try {
			int rutAux = Integer.parseInt(rut);
			char dv = digitoVerificador.charAt(0);
			int m = 0, s = 1;
			for (; rutAux != 0; rutAux /= 10) {
				s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
			}
			if (dv == (char) (s != 0 ? s + 47 : 75)) {
				return true;
			}		
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}

	public PiseeRespuesta validateRutParameters(InputParameter inputParameter){
		PiseeRespuesta respuesta = null;
		StringBuffer mensaje = new StringBuffer();
		if (!ValidationUtil.validarRut((String)inputParameter.getBodyParameter(ParametersName.RUT), 
				(String)inputParameter.getBodyParameter(ParametersName.DV))){ 
			respuesta = new PiseeRespuesta();
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_RUT_PARAMETER);
			return respuesta;
		}
		if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.RUT)) 
				|| StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.DV)) 
					|| StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN))){
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.RUT))){
				mensaje.append(ParametersName.RUT);
				mensaje.append(AppConstants.SPACE);
			}
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.DV))){
				mensaje.append(ParametersName.DV);
				mensaje.append(AppConstants.SPACE);
			}			
			if (StringUtils.isEmpty((String)inputParameter.getBodyParameter(ParametersName.PISEE_TOKEN))){
				mensaje.append(ParametersName.PISEE_TOKEN);
				mensaje.append(AppConstants.SPACE);
			}		
			respuesta = new PiseeRespuesta();
			respuesta.getEncabezado().setEmisorSobre(AppConstants._EMISOR_PISEE);
			respuesta.getEncabezado().setEstadoSobre(AppConstants._CODE_ERROR_CONSUMIDOR_PARAM_ENTRADA);
			respuesta.getEncabezado().setGlosaSobre(AppConstants._MSG_INVALID_NULL_PARAMETER + mensaje.toString());
			return respuesta;
		}				
		return respuesta;
	}	

}
