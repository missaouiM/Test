package com.filrouge.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.filrouge.dao.DAOFactory;

public class InitialisationDaoFactory implements ServletContextListener {
	private static final String	ATT_DAO_FACTORY	= "daoFactory";
	private DAOFactory			daoFactory;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		this.daoFactory = DAOFactory.getInstance();
		servletContext.setAttribute(ATT_DAO_FACTORY, this.daoFactory);

	}

}
