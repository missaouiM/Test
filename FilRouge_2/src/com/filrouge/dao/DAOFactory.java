package com.filrouge.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DAOFactory {
	private static final String	FICHIER_PROPERTIES		= "/com/filrouge/dao/dao.properties";
	private static final String	PROPERTIES_URL			= "url";
	private static final String	PROPERTIES_DRIVER		= "driver";
	private static final String	PROPERTIES_UTILISATEUR	= "nomUtilisateur";
	private static final String	PROPERTIES_MOT_DE_PASSE	= "motdepasse";

	BoneCP						connectionPool			= null;

	DAOFactory(BoneCP connectionPool) {
		this.connectionPool = connectionPool;
	}

	public static DAOFactory getInstance() throws DAOConfigurationException {
		Properties properties = new Properties();
		String url;
		String driver;
		String nomUtilisateur;
		String motdepasse;
		BoneCP connectionPool = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
		if (fichierProperties == null) {
			throw new DAOConfigurationException("Le fichier properties " + FICHIER_PROPERTIES + "introuvables.");
		}

		try {
			properties.load(fichierProperties);
			url = properties.getProperty(PROPERTIES_URL);
			driver = properties.getProperty(PROPERTIES_DRIVER);
			nomUtilisateur = properties.getProperty(PROPERTIES_UTILISATEUR);
			motdepasse = properties.getProperty(PROPERTIES_MOT_DE_PASSE);
		} catch (IOException e) {
			throw new DAOConfigurationException("Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e);
		}

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DAOConfigurationException("Le driver est introuvable dans le classpath.", e);
		}
		try {
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(url);
			config.setUser(nomUtilisateur);
			config.setPassword(motdepasse);

			config.setMinConnectionsPerPartition(2);
			config.setMaxConnectionsPerPartition(5);
			config.setPartitionCount(2);

			connectionPool = new BoneCP(config);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Erreur de configuration de la pool de connexions.", e);
		}
		DAOFactory instance = new DAOFactory(connectionPool);
		return instance;
	}

	Connection getConection() throws SQLException {
		return connectionPool.getConnection();
	}

	public ClientDao getClientDao() {
		return new ClientDaoImpl(this);
	}

	public CommandeDao getCommandeDao() {
		return new CommandeDaoImpl(this);
	}

}
