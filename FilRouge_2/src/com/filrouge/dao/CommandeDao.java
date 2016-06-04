package com.filrouge.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.filrouge.beans.Commande;

@Stateless
public class CommandeDao {
	private static final String	SQL_SELECT	= "SELECT c FROM Commande c ORDER BY c.id;";
	@PersistenceContext(unitName = "filrouge_PU")
	private EntityManager		em;

	public Commande trouver(Long id) {
		Commande commande = null;
		try {
			commande = em.find(Commande.class, id);

		} catch (Exception e) {
			throw new DAOException(e);
		}
		return commande;

	}

	public List<Commande> lister() {

		try {
			TypedQuery<Commande> query = em.createQuery(SQL_SELECT, Commande.class);
			return query.getResultList();

		} catch (Exception e) {
			throw new DAOException(e);
		}

	}

	public void supprimer(Long id) throws DAOException {
		Commande commande = this.trouver(id);
		try {
			em.remove(em.merge(commande));
		} catch (Exception e) {
			throw new DAOException(e);
		}

	}

	public void creer(Commande commande) throws DAOException {

		try {
			em.persist(commande);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

}
