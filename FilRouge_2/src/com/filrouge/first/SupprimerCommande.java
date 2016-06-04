package com.filrouge.first;

import java.io.IOException;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.beans.Commande;
import com.filrouge.dao.CommandeDao;

@WebServlet(name = "SupprimerCommande", urlPatterns = "/supprimerCommande")
public class SupprimerCommande extends HttpServlet {
	private static final String	SESSION_COMMANDE	= new String("liste_commande");
	private static final String	LISTE_COMMANDE		= new String("/listerCommande");
	private static final String	PARAM_COMMANDE		= new String("commande_supprimer");
	private static final String	CONF_DAO_FACTORY	= new String("daoFactory");

	@EJB
	private CommandeDao			commandeDao;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<Long, Commande> liste_commande = (Map<Long, Commande>) session.getAttribute(SESSION_COMMANDE);

		Long idCommande;
		try {
			idCommande = Long.parseLong(request.getParameter(PARAM_COMMANDE));
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		}
		commandeDao.supprimer(idCommande);
		liste_commande.remove(idCommande);
		session.setAttribute(SESSION_COMMANDE, liste_commande);
		response.sendRedirect(request.getContextPath() + LISTE_COMMANDE);
	}
}
