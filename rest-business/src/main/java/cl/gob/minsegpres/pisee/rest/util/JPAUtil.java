package cl.gob.minsegpres.pisee.rest.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public final class JPAUtil {

	private static JPAUtil _instance = null;
	
	private static final Logger LOGGER = Logger.getLogger(JPAUtil.class);
	
	private static EntityManagerFactory entityManagerFactory = null;

	private static InheritableThreadLocal<EntityManager> entityManagerThreadLocal = new InheritableThreadLocal<EntityManager>();

	private JPAUtil(){
		initEntityManagerFactory();
	}
	
	private void initEntityManagerFactory(){
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("PiseeRest");
		} catch (Throwable ex) {
			LOGGER.error("Initial EntityManagerFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public EntityManager getEntityManager() {
		if (entityManagerThreadLocal.get() == null || entityManagerThreadLocal.get().isOpen() == false) {
			entityManagerThreadLocal.set(entityManagerFactory.createEntityManager());
		}
		return entityManagerThreadLocal.get();
	}
	
	public static JPAUtil getInstance(){
		if (null == _instance){
			_instance = new JPAUtil();
		}
		return _instance;
	}
	
}