package cl.gob.minsegpres.pisee.rest.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import cl.gob.minsegpres.pisee.rest.model.ConfiguracionServicio;
import cl.gob.minsegpres.pisee.rest.util.JPAUtil;

public class ConfiguracionServicioDao {

	private static final Logger LOGGER = Logger.getLogger(ConfiguracionServicioDao.class);
	private static final String FLAG_SEARCH_ALL = "-1";
	
	@SuppressWarnings({"rawtypes"})
	public ConfiguracionServicio findConfiguracionServicio(String token){
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT configuracionServicio FROM ConfiguracionServicio configuracionServicio");
		buffer.append(" WHERE configuracionServicio.token = :token");
		Query query = manager.createQuery(buffer.toString());
		query.setParameter("token", token);
		List result = query.getResultList();
		if (!CollectionUtils.isEmpty(result)){
			return (ConfiguracionServicio)result.get(0);
		}
		return null;
	}

	@SuppressWarnings({"rawtypes"})
	public ConfiguracionServicio findConfiguracionServicioByServicioTramite(Long idServicioTramite){
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT configuracionServicio FROM ConfiguracionServicio configuracionServicio");
		buffer.append(" WHERE configuracionServicio.servicioTramite.id = :idServicioTramite");
		Query query = manager.createQuery(buffer.toString());
		query.setParameter("idServicioTramite", idServicioTramite);
		List result = query.getResultList();
		if (!CollectionUtils.isEmpty(result)){
			return (ConfiguracionServicio)result.get(0);
		}
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	public List<ConfiguracionServicio> findConfiguracionesServicio(Long idConsumidor) {
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT configuracionServicio FROM ConfiguracionServicio configuracionServicio");
		if (!FLAG_SEARCH_ALL.equalsIgnoreCase(idConsumidor.toString())){
			buffer.append(" WHERE configuracionServicio.servicioTramite.tramite.organismo.id = :idConsumidor");			
		}
		Query query = manager.createQuery(buffer.toString());
		if (!FLAG_SEARCH_ALL.equalsIgnoreCase(idConsumidor.toString())){
			query.setParameter("idConsumidor", idConsumidor);	
		}
		return (List<ConfiguracionServicio>)query.getResultList();
	}	
	
	public Long saveConfiguracionServicio(ConfiguracionServicio configuracionServicio){
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		EntityTransaction tx = null;
		Long result = null;
		try {
			tx = manager.getTransaction();
			tx.begin();
			manager.merge(configuracionServicio);
			result = configuracionServicio.getId();
			tx.commit();
		}catch(Exception e){
			tx.rollback();
			LOGGER.error("saveConfiguracionServicio - Exception == ",e);
		}finally{
			manager.close();
		}		
		return result;
	}	
	
}
