package com.filrouge.first;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.beans.Client;
import com.filrouge.beans.Commande;
import com.filrouge.dao.ClientDao;
import com.filrouge.dao.CommandeDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.forms.CreationCommandeForm;

@WebServlet(name = "CreationCommande", urlPatterns = "/creationCommande")
public class CreationCommande extends HttpServlet {
	private static final String	VUE					= new String("/WEB-INF/affichierCommande.jsp");
	private static final String	VUEC				= new String("/WEB-INF/creationCommande.jsp");
	private static final String	ATT_CHEMIN			= new String("chemin");
	private static final String	ATT_FORM			= new String("form");
	private static final String	ATT_CMD				= new String("commande");
	private static final String	SESSION_COMMANDE	= new String("liste_commande");
	private static final String	SESSION_CLIENT		= new String("liste_client");
	private static final String	CONF_DAO_FACTORY	= new String("daoFactory");

	private ClientDao			clientDao;
	private CommandeDao			commandeDao;

	public void init() {
		this.clientDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
		this.commandeDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getCommandeDao();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(VUEC).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CreationCommandeForm creationCommande = new CreationCommandeForm(clientDao, commandeDao);
		String chemin = this.getServletConfig().getInitParameter(ATT_CHEMIN);
		Commande commande = creationCommande.creerCommande(request, chemin);
		request.setAttribute(ATT_FORM, creationCommande);
		request.setAttribute(ATT_CMD, commande);
		if (creationCommande.getErreurs().isEmpty()) {
			/* Récuperation de la session actuelle */
			HttpSession session = request.getSession();
			/* Ajout du client */
			Map<Long, Client> liste_client = (Map<Long, Client>) session.getAttribute(SESSION_CLIENT);
			if (liste_client == null) {
				liste_client = new HashMap<Long, Client>();
			}
			liste_client.put(commande.getClient().getId(), commande.getClient());
			session.setAttribute(SESSION_CLIENT, liste_client);
			/* Ajout de la commande */
			Map<Long, Commande> liste_commande = (Map<Long, Commande>) session.getAttribute(SESSION_COMMANDE);
			if (liste_commande == null) {
				liste_commande = new HashMap<Long, Commande>();
			}
			liste_commande.put(commande.getId(), commande);
			session.setAttribute(SESSION_COMMANDE, liste_commande);
			this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

		} else {
			this.getServletContext().getRequestDispatcher(VUEC).forward(request, response);
		}
	}
}
