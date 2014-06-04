package org.jboss.tools.hibernate.util;

import java.util.ServiceLoader;

import org.hibernate.console.spi.HibernateService;

public class HibernateHelper {
	
	public static HibernateHelper INSTANCE = new HibernateHelper();
	
	private HibernateService hibernateService = null;
	
	public HibernateService getHibernateService() {
		if (hibernateService == null) {
			hibernateService = loadHibernateService();
		}
		return hibernateService;
	}
	
	private HibernateService loadHibernateService() {
		ServiceLoader<HibernateService> loader = 
				ServiceLoader.load(HibernateService.class);
		return loader.iterator().next();
	}

}