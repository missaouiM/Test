package com.filrouge.first;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadImage extends HttpServlet {
	private final static String	ATT_CHEMIN			= new String("chemin");
	private final static int	DEFAULT_TOMPON_SIZE	= 10240;
	private final static int	TOMPON_SIZE			= 10240;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chemin = this.getServletConfig().getInitParameter(ATT_CHEMIN);
		String imageRequis = request.getPathInfo();
		if (imageRequis == null || "/".equals(imageRequis)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		imageRequis = URLDecoder.decode(imageRequis, "UTF-8");
		File image = new File(chemin, imageRequis);
		if (!image.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;

		}
		String type = getServletContext().getMimeType(image.getName());
		if (type == null) {
			type = "application/octet-stream";
		}
		response.reset();
		response.setContentType(type);
		response.setHeader("content-length", String.valueOf(image.length()));
		response.setHeader("content-disposition", "attachement; filenema=\"" + image.getName() + "\"");
		/* Envoie de l'image */
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
			entree = new BufferedInputStream(new FileInputStream(image), TOMPON_SIZE);
			sortie = new BufferedOutputStream(response.getOutputStream(), TOMPON_SIZE);

			byte[] tompon = new byte[TOMPON_SIZE];
			int longueur;
			while ((longueur = entree.read(tompon)) > 0) {
				sortie.write(tompon, 0, longueur);

			}

		} finally {
			try {
				entree.close();
			} catch (IOException e) {

			}
			try {
				sortie.close();

			} catch (IOException e) {

			}
		}

	}
}
