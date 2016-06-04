package com.filrouge.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.filrouge.beans.Client;
import com.filrouge.beans.Commande;
import com.filrouge.dao.ClientDao;
import com.filrouge.dao.CommandeDao;

@WebFilter(urlPatterns = "/*")
public class SetListe implements Filter {
	private static final String	ATT_SESSION_CLIENT		= "liste_client";
	private static final String	ATT_SESSION_COMMANDE	= "liste_commande";

	@EJB
	private ClientDao			clientDao;
	@EJB
	private CommandeDao			commandeDao;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		if (session.getAttribute(ATT_SESSION_CLIENT) == null) {
			List<Client> liste_client = clientDao.lister();
			Map<Long, Client> mapClients = new HashMap<Long, Client>();
			for (Client client : liste_client) {
				mapClients.put(client.getId(), client);
			}
			session.setAttribute(ATT_SESSION_CLIENT, mapClients);
		}
		if (session.getAttribute(ATT_SESSION_COMMANDE) == null) {
			List<Commande> listCommande = commandeDao.lister();
			Map<Long, Commande> mapCommande = new HashMap<Long, Commande>();
			for (Commande commande : listCommande) {
				mapCommande.put(commande.getId(), commande);
			}
			session.setAttribute(ATT_SESSION_COMMANDE, mapCommande);

		}
		chain.doFilter(request, resp);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
