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
import com.filrouge.beans.Commande;

public class CommandeDaoImpl implements CommandeDao {
	private static final String	SQL_SELECT_BY_ID	= "SELECT id_client,nom, prenom, adresseLivraison,numeroTelephone, "
															+ "adresseMail, nomImage, id_commandemontant, modePaiement,statutPaiement,"
															+ "modeLivraison,statutLivraison,dateCreation FROM COMMANDE C, CLIENT CL WHERE"
															+ " CL.id = C.id_client and id_commande = ?   ;";
	private static final String	SQL_SELECT			= "SELECT id_client,nom, prenom, adresseLivraison,numeroTelephone, "
															+ "adresseMail, nomImage, id_commande,montant, modePaiement,statutPaiement,"
															+ "modeLivraison,statutLivraison,dateCreation FROM COMMANDE C, CLIENT CL WHERE"
															+ " CL.id = C.id_client   ;";
	private static final String	SQL_INSERT			= "INSERT INTO COMMANDE (id_client,montant,modePaiement,statutPaiement,statutLivraison,"
															+ "modeLivraison,dateCreation) VALUES (?,?,?,?,?,?,NOW());";
	private static final String	SQL_DELETE_BY_ID	= "DELETE FROM COMMANDE WHERE id_commande = ?";

	private DAOFactory			daoFactory;

	public CommandeDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Commande trouver(Long id) {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Commande commande = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_BY_ID, false, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				commande = map(resultSet);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return commande;

	}

	@Override
	public List<Commande> lister() {
		List<Commande> list = new ArrayList<Commande>();
		Commande commande = new Commande();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT, false, null);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				commande = map(resultSet);
				list.add(commande);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return list;

	}

	@Override
	public void supprimer(Long id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_DELETE_BY_ID, false, id);
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Echec de suppression de la commande, aucun suppression n'est effectuée");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement, connexion);
		}

	}

	@Override
	public void creer(Commande commande) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeurAutoGenere = null;
		try {
			connexion = daoFactory.getConection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true,
					commande.getClient().getId(), commande.getMontant(), commande.getModePaiement(),
					commande.getStatutPaiement(), commande.getStatutLivraison(), commande.getModeLivraison());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Echec de la creation de la commande, aucune ligne ajouté dans la table");
			}
			valeurAutoGenere = preparedStatement.getGeneratedKeys();
			if (valeurAutoGenere.next()) {
				commande.setId(valeurAutoGenere.getLong(1));

			} else {
				throw new DAOException("Echec de l'ajout de la commande dans la base, aucun ID auto-généré retourné");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(valeurAutoGenere, preparedStatement, connexion);
		}
	}

	private static Commande map(ResultSet resultSet) throws SQLException {
		Commande commande = new Commande();
		Client client = new Client();
		client.setAdresseLivraison(resultSet.getString("adresseLivraison"));
		client.setAdresseMail(resultSet.getString("adresseMail"));
		client.setId(resultSet.getLong("id_client"));
		client.setNom(resultSet.getString("nom"));
		client.setNomImage(resultSet.getString("nomImage"));
		client.setNumTel(resultSet.getString("numeroTelephone"));
		client.setPrenom(resultSet.getString("prenom"));
		commande.setClient(client);
		commande.setDate((String.valueOf(resultSet.getTimestamp("dateCreation"))));
		commande.setId(resultSet.getLong("id_commande"));
		commande.setModeLivraison("modeLivraison");
		commande.setModePaiement("modePaiement");
		commande.setMontant(resultSet.getString("montant"));
		commande.setStatutLivraison(resultSet.getString("statutLivraison"));
		commande.setStatutPaiement(resultSet.getString("statutPaiement"));
		return commande;
	}
}
