package com.filrouge.dao;

import java.util.List;

import com.filrouge.beans.Client;

public interface ClientDao {
	void creer(Client client) throws DAOException;

	Client trouver(Long id);

	List<Client> lister();

	void supprimer(Long id) throws DAOException;

}
