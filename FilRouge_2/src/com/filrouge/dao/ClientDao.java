package com.filrouge.dao;

import java.sql.Connection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.filrouge.beans.Client;

@Stateless
public class ClientDao {
	private static final String	SQL_SELECT			= "SELECT c FROM Client c ORDER BY c.id ;";
	private static final String	SQL_SELECT_BY_ID	= "SELECT id, nom, prenom, adresseLivraison, numeroTelephone,"
															+ " adresseMail, nomImage FROM CLIENT where id = :id ;";
	private static final String	SQL_INSERT			= "INSERT INTO CLIENT (nom, prenom, adresseLivraison, "
															+ "numeroTelephone, adresseMail,nomImage ) VALUES (?,?,?,?,?,?) ;";
	private static final String	SQL_DELETE_BY_ID	= "DELETE FROM CLIENT WHERE id = ? ; ";
	private static final String	PARAM_ID			= "id";
	@PersistenceContext(unitName = "filrouge_PU")
	private EntityManager		em;

	public void creer(Client client) throws DAOException {
		Connection connexion = null;
		try {
			em.persist(client);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	public Client trouver(Long id) {
		Client client = null;
		try {
			client = em.find(Client.class, id);

		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e);
		}
		return client;
	}

	public List<Client> lister() {
		try {
			TypedQuery<Client> query = em.createQuery(SQL_SELECT, Client.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	public void supprimer(Long id) throws DAOException {
		Client client = this.trouver(id);
		try {
			em.remove(em.merge(client));

		} catch (Exception e) {
			throw new DAOException(e);

		}

	}

}
