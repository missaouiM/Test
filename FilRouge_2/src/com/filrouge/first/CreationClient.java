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
import com.filrouge.dao.ClientDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.forms.CreationClientForm;

@WebServlet(name = "CreationClient", urlPatterns = "/creationClient")
public class CreationClient extends HttpServlet {
	private static final String	VUE					= new String("/WEB-INF/creationClient.jsp");
	private static final String	VUE_AFF				= new String("/WEB-INF/affichierClient.jsp");
	private static final String	ATT_FORM			= new String("form");
	private static final String	ATT_CLIENT			= new String("client");
	private static final String	SESSION_CLIENT		= new String("liste_client");
	private static final String	ATT_CHEMIN			= new String("chemin");
	private static final String	CONF_DAO_FACTORY	= new String("daoFactory");

	private ClientDao			clientDao;

	public void init() {
		this.clientDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chemin = this.getServletConfig().getInitParameter(ATT_CHEMIN);
		CreationClientForm creationclientform = new CreationClientForm(this.clientDao);
		Client client = creationclientform.creerClient(request, chemin);
		request.setAttribute(ATT_CLIENT, client);
		request.setAttribute(ATT_FORM, creationclientform);
		/* Recupération de la session actuelle */
		HttpSession session = request.getSession();
		/* Récupération de la liste des clients */
		Map<Long, Client> liste_client = (Map<Long, Client>) session.getAttribute(SESSION_CLIENT);
		if (liste_client == null) {
			liste_client = new HashMap<Long, Client>();
		}
		if (creationclientform.getResultat()) {
			liste_client.put(client.getId(), client);
			session.setAttribute(SESSION_CLIENT, liste_client);
			this.getServletContext().getRequestDispatcher(VUE_AFF).forward(request, response);

		} else {
			this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

		}

	}
}
