package cl.gob.minsegpres.pisee.rest.business;

import java.text.SimpleDateFormat;
import java.util.Date;

import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;

public class SobreBusiness {

	public static final String FECHA_HORA_REQ = "fecha_hora_req";
	public static final String NOMBRE_TRAMITE = "nombre_tramite";
	public static final String NOMBRE_CONSUMIDOR = "nombre_consumidor";
	public static final String NOMBRE_SERVICIO = "nombre_servicio";
	public static final String NOMBRE_PROVEEDOR = "nombre_proveedor";
	public static final String FECHA_HORA = "fecha_hora";
	public static final String ID_SOBRE = "id_sobre";
	
	private static final String FORMAT_AGNO_MES_DIA = "yyyyMMdd";
	private static final String FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String CODE_NO_ORQUESTA = "00";
	
	public InputParameter fillSobre(InputParameter input, ConfiguracionServicio config) {
		input.addHeaderParameter(ID_SOBRE, generateSobreID(config));
		input.addHeaderParameter(FECHA_HORA, generateSobreFechaHora());
		input.addHeaderParameter(NOMBRE_PROVEEDOR, config.getServicioTramite().getServicio().getOrganismo().getSigla());
		input.addHeaderParameter(NOMBRE_SERVICIO, config.getServicioTramite().getServicio().getNombre());
		input.addHeaderParameter(NOMBRE_CONSUMIDOR, config.getServicioTramite().getTramite().getOrganismo().getSigla());
		input.addHeaderParameter(NOMBRE_TRAMITE, config.getServicioTramite().getTramite().getNombre());
		input.addHeaderParameter(FECHA_HORA_REQ, generateSobreFechaHora());
		return input;
	}
	
	public String generateSobreFechaHora(){
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FULL);
		return sdf.format(today);
	}
	
	public String generateSobreID(ConfiguracionServicio config){
		StringBuffer idSobre;
		idSobre = new StringBuffer();
		idSobre.append(config.getCodigoInstitucion());
		idSobre.append(config.getCodigoTramite());
		idSobre.append(idSobreFechaActual());
		idSobre.append(idSobreNumeroTransaccion());
		idSobre.append(CODE_NO_ORQUESTA);
		return idSobre.toString();
	}
	
	private String idSobreFechaActual(){
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_AGNO_MES_DIA);
		return sdf.format(today);
	}		

	private String idSobreNumeroTransaccion(){
		return NumeroTransaccionBusiness.getInstance().getNumeroTransaccion();
	}		
	
}
