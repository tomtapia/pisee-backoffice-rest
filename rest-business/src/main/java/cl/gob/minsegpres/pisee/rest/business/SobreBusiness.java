package cl.gob.minsegpres.pisee.rest.business;

import java.text.SimpleDateFormat;
import java.util.Date;

import cl.gob.minsegpres.pisee.backoffice.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;

public class SobreBusiness {

	public static final String NOMBRE_TRAMITE = "nombreTramite";
	public static final String NOMBRE_CONSUMIDOR = "nombreConsumidor";
	public static final String NOMBRE_SERVICIO = "nombreServicio";
	public static final String NOMBRE_PROVEEDOR = "nombreProveedor";
	public static final String FECHA_HORA = "fechaHora";
	public static final String ID_SOBRE = "idSobre";
	
	private static final String FORMAT_AGNO_MES_DIA = "yyyyMMdd";
	private static final String FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String CODE_NO_ORQUESTA = "00";
	
	public InputParameter fillSobre(InputParameter input, ConfiguracionServicio config) {
		StringBuffer idSobre;
		idSobre = new StringBuffer();
		idSobre.append(config.getCodigoInstitucion());
		idSobre.append(config.getCodigoTramite());
		idSobre.append(idSobreFechaActual());
		idSobre.append(idSobreNumeroTransaccion());
		idSobre.append(CODE_NO_ORQUESTA);
		input.addHeaderParameter(ID_SOBRE, idSobre.toString());
		input.addHeaderParameter(FECHA_HORA, sobreFechaHora());
		input.addHeaderParameter(NOMBRE_PROVEEDOR, config.getServicioTramite().getServicio().getOrganismo().getSigla());
		input.addHeaderParameter(NOMBRE_SERVICIO, config.getServicioTramite().getServicio().getNombreCorto());
		input.addHeaderParameter(NOMBRE_CONSUMIDOR, config.getServicioTramite().getTramite().getOrganismo().getSigla());
		input.addHeaderParameter(NOMBRE_TRAMITE, config.getServicioTramite().getTramite().getNombre());
		input.addHeaderParameter("fechaHoraReq", sobreFechaHora());
		return input;
	}
	
	private String sobreFechaHora(){
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_FULL);
		return sdf.format(today);
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
