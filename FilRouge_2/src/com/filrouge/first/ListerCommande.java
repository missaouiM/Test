package com.filrouge.first;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListerCommande extends HttpServlet {
	private static final String	VUE_COMMANDE	= new String("/WEB-INF/listerCommande.jsp");

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(VUE_COMMANDE).forward(request, response);
	}

}
