package com.filrouge.dao;

import static com.filrouge.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.filrouge.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.filrouge.beans.Client;

public class ClientDaoImpl implements ClientDao {
	private static final String	SQL_SELECT			= "SELECT id, nom, prenom, adresseLivraison, numeroTelephone,"
															+ " adresseMail, nomImage FROM CLIENT ;";
	private static final String	SQL_SELECT_BY_ID	= "SELECT id, nom, prenom, adresseLivraison, numeroTelephone,"
															+ " adresseMail, nomImage FROM CLIENT where id = ? ;";
	private static final String	SQL_INSERT			= "INSERT INTO CLIENT (nom, prenom, adresseLivraison, "
															+ "numeroTelephone, adresseMail,nomImage ) VALUES (?,?,?,?,?,?) ;";
	private static final String	SQL_DELETE_BY_ID	= "DELETE FROM CLIENT WHERE id = ? ; ";

	private DAOFactory			daoFactory;

	public ClientDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void creer(Client client) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeurAutoGenere = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true, client.getNom(),
					client.getPrenom(), client.getAdresseLivraison(), client.getNumTel(), client.getAdresseMail(),
					client.getNomImage());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Echec de la creation du client, aucune ligne ajoutée dans la table");
			}
			valeurAutoGenere = preparedStatement.getGeneratedKeys();
			if (valeurAutoGenere.next()) {
				client.setId(valeurAutoGenere.getLong(1));
			} else {
				throw new DAOException("Echec de l'ajout du client dans la base, aucun ID auto-généré retourné");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(valeurAutoGenere, preparedStatement, connexion);
		}

	}

	@Override
	public Client trouver(Long id) {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Client client = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_BY_ID, false, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				client = map(resultSet);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return null;
	}

	@Override
	public List<Client> lister() {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Client> listClient = new ArrayList<Client>();
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT, false, null);
			Client client = null;
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				client = map(resultSet);
				listClient.add(client);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return listClient;
	}

	@Override
	public void supprimer(Long id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_DELETE_BY_ID, false, id);
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Echec de suppression du client, aucun suppression n'est effectuée");
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

	}

	private static Client map(ResultSet resultSet) throws SQLException {
		Client client = new Client();
		client.setId(resultSet.getLong("id"));
		client.setNom(resultSet.getString("nom"));
		client.setPrenom(resultSet.getString("prenom"));
		client.setAdresseLivraison(resultSet.getString("adresseLivraison"));
		client.setAdresseMail(resultSet.getString("adresseMail"));
		client.setNumTel(resultSet.getString("numeroTelephone"));
		client.setNomImage(resultSet.getString("nomImage"));
		return client;

	}

}
