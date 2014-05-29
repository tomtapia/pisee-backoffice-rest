package cl.gob.minsegpres.pisee.rest.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.core.model.LogEsb;
import cl.gob.minsegpres.pisee.core.model.LogTiempoProveedor;
import cl.gob.minsegpres.pisee.core.model.LogTiempoProveedorId;
import cl.gob.minsegpres.pisee.core.model.Sobre;
import cl.gob.minsegpres.pisee.core.service.LogEsbService;
import cl.gob.minsegpres.pisee.core.service.LogTiempoProveedorService;
import cl.gob.minsegpres.pisee.core.service.SobreService;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.PropertiesManager;

public class TrazabilidadBusiness {

//	private final static Log LOGGER = LogFactory.getLog(TrazabilidadBusiness.class);
	
	private final static Character LETTER_C = 'C';
	private final static Character LETTER_P = 'P';
	private final static Character LETTER_R = 'R';
	private final static Character LETTER_N = 'N';
	private final static Character LETTER_E = 'E';
	private final static Character LETTER_F = 'F';
	
	private final static String ZERO_ZERO   = "00";
	private final static String ZERO_ONE   = "01";
	private final static String ZERO_TWO   = "02";
	private final static String ZERO_THREE = "03";
	private final static String ZERO_FOUR  = "04";
	private final static String ZERO_FIVE  = "05";
	private final static String ZERO_SEVEN = "07";
	private final static String ZERO_EIGHT = "08";
	
	public Long insertSobreConsumidor(PiseeRespuesta respuesta) {
		Sobre sobre = new Sobre();
		sobre.setFecha(Calendar.getInstance().getTime());
		sobre.setTipo(LETTER_C);
		sobre.setIdSobre(respuesta.getEncabezado().getIdSobre());
		sobre.setEmisor(respuesta.getEncabezado().getEmisorSobre());
		sobre.setEstado(respuesta.getEncabezado().getEstadoSobre());
		sobre.setGlosa(respuesta.getEncabezado().getIdSobre());
		sobre.setConsumidor(respuesta.getEncabezado().getNombreConsumidor());
		sobre.setProveedor(respuesta.getEncabezado().getNombreProveedor());
		sobre.setServicio(respuesta.getEncabezado().getNombreServicio());
		sobre.setTramite(respuesta.getEncabezado().getNombreTramite());

		SobreService theService = new SobreService();
		return theService.saveSobre(sobre);
	}

	public Long insertSobreProveedor(PiseeRespuesta respuesta) {
		Sobre sobre = new Sobre();
		sobre.setFecha(Calendar.getInstance().getTime());
		sobre.setTipo(LETTER_P);
		sobre.setIdSobre(respuesta.getEncabezado().getIdSobre());
		sobre.setEmisor(respuesta.getEncabezado().getEmisorSobre());
		sobre.setEstado(respuesta.getEncabezado().getEstadoSobre());
		sobre.setGlosa(respuesta.getEncabezado().getIdSobre());
		sobre.setConsumidor(respuesta.getEncabezado().getNombreConsumidor());
		sobre.setProveedor(respuesta.getEncabezado().getNombreProveedor());
		sobre.setServicio(respuesta.getEncabezado().getNombreServicio());
		sobre.setTramite(respuesta.getEncabezado().getNombreTramite());

		SobreService theService = new SobreService();
		return theService.saveSobre(sobre);
	}
	
	public Long insertLogEsb(PiseeRespuesta respuesta, Long idSobreEntrada, Long idSobreSalida, ConfiguracionServicio configuracionServicio) {
		LogEsb log = new LogEsb();
		SobreService sobreService = new SobreService();
				
		log.setServer(PropertiesManager.getInstance().getPropertyAmbiente("nombreServidor"));
		log.setClusterNodo(PropertiesManager.getInstance().getPropertyAmbiente("nombreCluster"));
		log.setIpNodo(PropertiesManager.getInstance().getPropertyAmbiente("ip"));
		log.setNodo(PropertiesManager.getInstance().getPropertyAmbiente("host"));
		log.setTipoTransaccion(LETTER_R);
		log.setFechaFin(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		if (null != respuesta.getTemporalData()){
			log.setDuracion(respuesta.getTemporalData().getDuracionLlamadaServicio() );
			log.setFecha(new Timestamp(respuesta.getTemporalData().getFechaConsultaRecibida().getTimeInMillis()));
		}
		
		log.setConsumidor(respuesta.getEncabezado().getIdSobre());		
		log.setError(PiseeErrorHelper.getInstance().findErrorByCodigo(respuesta.getEncabezado().getEstadoSobre()));
		log.setEstadoError(translateErrorRespuestaServicio(respuesta.getEncabezado().getEstadoSobre()));
		
		if (null != configuracionServicio){
			log.setTramite(configuracionServicio.getServicioTramite().getTramite());
			log.setOrganismo(configuracionServicio.getServicioTramite().getServicio().getOrganismo());
			log.setServicio(configuracionServicio.getServicioTramite().getServicio());			
		}
		
		log.setSobreEntrada(sobreService.findSobre(idSobreEntrada));
		if (null != idSobreSalida){
			log.setSobreSalida(sobreService.findSobre(idSobreSalida));	
		}
		
		LogEsbService theService = new LogEsbService();
		return theService.saveLogEsb(log);		
	}
	
	public BigDecimal insertLogTiempoProveedor(PiseeRespuesta respuesta, Long idLogEsb, ConfiguracionServicio configuracionServicio) {
		LogTiempoProveedor log = new LogTiempoProveedor();
		LogTiempoProveedorId logId = new LogTiempoProveedorId();
		logId.setConsumidor(respuesta.getEncabezado().getIdSobre());

		//Esto da error si se llama desde REST -> SOAP
		logId.setDuracion(new BigDecimal(respuesta.getTemporalData().getDuracionLlamadaServicio()));
		
		logId.setFechaInicio(new Timestamp(respuesta.getTemporalData().getFechaConsultaRecibida().getTimeInMillis()));
		logId.setFechaFin(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		logId.setIdServicio(new BigDecimal(configuracionServicio.getServicioTramite().getServicio().getId()));
		log.setId(logId);
		
		log.setAsignado(idLogEsb.toString());
		log.setConsumidor(configuracionServicio.getServicioTramite().getTramite().getOrganismo().getSigla());
		log.setProveedor(configuracionServicio.getServicioTramite().getServicio().getOrganismo().getSigla());
		log.setTipoTransaccion(LETTER_R);
		log.setTramite(configuracionServicio.getServicioTramite().getTramite().getNombre());
		
		LogTiempoProveedorService theService = new LogTiempoProveedorService();
		return theService.saveLogTiempoProveedor(log);			
	}	
	
	public void updateLogEsb(Long idLogEsb, BigDecimal idLogTiempoProveedor){
		LogEsbService theService = new LogEsbService();
		theService.updateLogEsb(idLogEsb, idLogTiempoProveedor);	
	}
	
	private static Character translateErrorRespuestaServicio(String errorRespuestaServicio){
		Character estado = LETTER_E; //Erroneas Proveedor
		if (ZERO_ZERO.equals(errorRespuestaServicio) || 
				ZERO_FIVE.equals(errorRespuestaServicio) || 
					ZERO_SEVEN.equals(errorRespuestaServicio)) {
			estado = LETTER_N; // Exitosas
		}
		if (ZERO_ONE.equals(errorRespuestaServicio) || ZERO_TWO.equals(errorRespuestaServicio) || 
				ZERO_THREE.equals(errorRespuestaServicio) || ZERO_FOUR.equals(errorRespuestaServicio) || 
					ZERO_EIGHT.equals(errorRespuestaServicio)) {
			estado = LETTER_F; //Erroneas Consumidor
		}
		return estado;
	}
	
	
}
