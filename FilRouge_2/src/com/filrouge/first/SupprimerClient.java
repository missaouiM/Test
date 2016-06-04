package com.filrouge.first;

import java.io.IOException;
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

@WebServlet(name = "SupprimerClient", urlPatterns = "/supprimerClient")
public class SupprimerClient extends HttpServlet {
	private static final String	SESSION_CLIENT		= new String("liste_client");
	private static final String	PARAM_CLIENT		= new String("client_supprimer");
	private static final String	LISTE_CLIENT		= new String("/listerClients");
	private static final String	CONF_DAO_FACTORY	= new String("daoFactory");

	private ClientDao			clientDao;

	public void init() {
		this.clientDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<Long, Client> liste_client = (Map<Long, Client>) session.getAttribute(SESSION_CLIENT);
		Long idClient = null;

		try {
			String s = request.getParameter(PARAM_CLIENT);
			if (s != null) {
				idClient = Long.parseLong(request.getParameter(PARAM_CLIENT));
			}
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		}
		clientDao.supprimer(idClient);
		liste_client.remove(idClient);
		session.setAttribute(SESSION_CLIENT, liste_client);
		response.sendRedirect(request.getContextPath() + LISTE_CLIENT);
	}
}
