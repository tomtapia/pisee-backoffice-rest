package cl.gob.minsegpres.pisee.rest.business;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.core.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.entities.DataPiseeToQueue;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class TrazabilidadQueue extends Thread{

	private final static Log LOGGER = LogFactory.getLog(CallerServiceBusiness.class);
	private BlockingQueue<DataPiseeToQueue> queue = new LinkedBlockingQueue<DataPiseeToQueue>();
	private static TrazabilidadQueue _instance = null;
	
	private TrazabilidadQueue(){ 	}
	
	public static synchronized TrazabilidadQueue getInstance() {
		if (_instance == null){
			_instance = new TrazabilidadQueue();
			_instance.start();
		}
		return _instance;
	}	
	
	public void accept(DataPiseeToQueue request) {
		queue.add(request);
	}

	public void run() {
		while (true)
			try {
				execute(queue.take());
			} catch (InterruptedException e) {
				LOGGER.error("Exception: " , e);
			}
	}

	private void execute(final DataPiseeToQueue dataPisee) {
		ConfiguracionServicio configuracionServicio;
		PiseeRespuesta respuesta;
		
		configuracionServicio = dataPisee.getConfiguracionServicio();
		respuesta = dataPisee.getPiseeRespuesta();

		LOGGER.info("Iniciando Proceso de Trazabilidad, ID Sobre: " + respuesta.getEncabezado().getIdSobre());
		
		Long idSobreConsumidor = null, idSobreProveedor = null, idLogEsb = null;
		BigDecimal idLogTpoProveedor = null;
		TrazabilidadBusiness trazabilidadBusiness = new TrazabilidadBusiness();
		idSobreConsumidor = trazabilidadBusiness.insertSobreConsumidor(respuesta);
		//FIXME: Revisar cuando se ingresa aca dado que ahora se elimino el emisor consumidor
		if (null != configuracionServicio || AppConstants._EMISOR_CONSUMIDOR.equals(respuesta.getEncabezado().getEmisorSobre())) {
			idSobreProveedor = trazabilidadBusiness.insertSobreProveedor(respuesta);	
		}
		idLogEsb = trazabilidadBusiness.insertLogEsb(respuesta, idSobreConsumidor, idSobreProveedor, configuracionServicio);
		idLogTpoProveedor = trazabilidadBusiness.insertLogTiempoProveedor(respuesta, idLogEsb, configuracionServicio);
		trazabilidadBusiness.updateLogEsb(idLogEsb, idLogTpoProveedor);				
		
		
		//---
		long current = System.currentTimeMillis();
		int milisecondsToDelay = 500;
		long future = current + milisecondsToDelay;
		while (System.currentTimeMillis() < future) {
			// Dejar pasar el tiempo
		}
				
		LOGGER.info("Termino del Proceso de Trazabilidad, ID Sobre: " + respuesta.getEncabezado().getIdSobre());
		
	}

}
