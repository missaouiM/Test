package com.filrouge.dao;

import java.util.List;

import com.filrouge.beans.Commande;

public interface CommandeDao {
	Commande trouver(Long id);

	List<Commande> lister();

	void supprimer(Long id) throws DAOException;

	void creer(Commande commande) throws DAOException;

}
