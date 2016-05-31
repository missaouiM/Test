package com.filrouge.forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.filrouge.beans.Client;
import com.filrouge.dao.ClientDao;
import com.filrouge.dao.DAOException;

import eu.medsea.mimeutil.MimeUtil;

public class CreationClientForm {
	private static final String	CHAMP_NOMCLIENT			= new String("nomClient");
	private static final String	CHAMP_PRENOMCLIENT		= new String("prenomClient");
	private static final String	CHAMP_ADRESSECLIENT		= new String("adresseClient");
	private static final String	CHAMP_EMAILCLIENT		= new String("emailClient");
	private static final String	CHAMP_TELEPHONECLIENT	= new String("telephoneClient");
	private static final String	CHAMP_IMAGE				= new String("image");
	private static final int	TAILLE_TOMPON			= 256;
	private boolean				resultat;
	private Map<String, String>	erreurs					= new HashMap<String, String>();
	private String				message;
	private ClientDao			clientDao;

	public CreationClientForm(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	/*
	 * Recupération de la valeur d'un champ
	 */

	public Client creerClient(HttpServletRequest request, String chemin) {
		String nomClient = getValeurChamp(request, CHAMP_NOMCLIENT);
		String prenomClient = getValeurChamp(request, CHAMP_PRENOMCLIENT);
		String adresseClient = getValeurChamp(request, CHAMP_ADRESSECLIENT);
		String emailClient = getValeurChamp(request, CHAMP_EMAILCLIENT);
		String telephoneClient = getValeurChamp(request, CHAMP_TELEPHONECLIENT);

		Client client = new Client();
		/* Recuperation du nom de l'image */
		String nomImage = null;
		InputStream contenuImage = null;
		try {
			Part part = request.getPart(CHAMP_IMAGE);
			nomImage = getNomImage(part);
			if (nomImage != null && !nomImage.isEmpty()) {
				nomImage = nomImage.substring(nomImage.lastIndexOf("/") + 1).substring(nomImage.lastIndexOf("\\") + 1);
				contenuImage = part.getInputStream();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			setErreur(CHAMP_IMAGE, "La taile de l'image ne doit pas dépasser 1Mo");
		} catch (IOException e) {
			e.printStackTrace();
			setErreur(CHAMP_IMAGE, "Erreur de configuartion du serveur");
		} catch (ServletException e) {
			e.printStackTrace();
			setErreur(CHAMP_IMAGE, "Ce type de requête n'est pas supportée");

		}
		if (erreurs.isEmpty()) {
			try {
				validationEmail(emailClient);

			} catch (Exception e) {
				setErreur(CHAMP_EMAILCLIENT, e.getMessage());
			}
			client.setAdresseMail(emailClient);

			try {
				validationPrenom(prenomClient);

			} catch (Exception e) {
				setErreur(CHAMP_PRENOMCLIENT, e.getMessage());
			}
			client.setPrenom(prenomClient);

			try {
				validationAdresse(adresseClient);

			} catch (Exception e) {
				setErreur(CHAMP_ADRESSECLIENT, e.getMessage());
			}
			client.setAdresseLivraison(adresseClient);

			try {
				validationNom(nomClient);

			} catch (Exception e) {
				setErreur(CHAMP_NOMCLIENT, e.getMessage());
			}
			client.setNom(nomClient);

			try {
				validationNumeroTelephone(telephoneClient);

			} catch (Exception e) {
				setErreur(CHAMP_TELEPHONECLIENT, e.getMessage());
			}
			client.setNumTel(telephoneClient);

			try {
				if (contenuImage != null) {
					validationImage(contenuImage, nomImage);
				}
			} catch (Exception e) {
				setErreur(CHAMP_IMAGE, e.getMessage());
			}
			client.setNomImage(nomImage);
		}
		if (erreurs.isEmpty() && contenuImage != null) {
			try {
				ecrireImage(contenuImage, nomImage, chemin);
			} catch (Exception e) {
				e.printStackTrace();
				setErreur(CHAMP_IMAGE, "Problème d'ecriture dans le disque dur");
			}
		}
		try {
			if (erreurs.isEmpty()) {
				clientDao.creer(client);
				message = "Succés de la création du client";
				resultat = true;
			} else {
				message = "Echec de le céation du client";
				resultat = false;
			}
		} catch (DAOException e) {

		}
		return client;

	}

	/*
	 * Fonction de validation de l'adresse mail
	 */

	public void validationEmail(String email) throws Exception {
		if (email != null) {
			if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
				throw new Exception("Merci de saisir une adresse mail valide");
			}
		}
	}

	/*
	 * Fonction de validation du prénom
	 */

	public void validationPrenom(String prenom) throws Exception {
		if (prenom != null && prenom.length() < 2) {
			throw new Exception("Le prenom de client doit contenir au moins deux caratères");
		}
	}

	/*
	 * Fonction de validation du nom
	 */

	public void validationNom(String nom) throws Exception {
		if (nom != null) {
			if (nom.length() < 2) {
				throw new Exception("Le nom de client doit contenir au moins deux caratères");
			}
		} else {
			throw new Exception("Merci de saisir le nom du client");
		}
	}

	/*
	 * Fonction de validation de l'adresse de livraison
	 */

	public void validationAdresse(String adresse) throws Exception {
		if (adresse != null) {
			if (adresse.length() < 10) {
				throw new Exception("L'adresse de livraison doit contenir au moins 10 caractère");
			}
		} else {
			throw new Exception("Merci de saisir l'adresse de livraison");
		}

	}

	/*
	 * Fonction de validation du numéro de telephone
	 */

	public void validationNumeroTelephone(String numeroTelephone) throws Exception {
		if (numeroTelephone != null) {
			if (numeroTelephone.length() < 4) {
				throw new Exception("Le numéro de téléphone doit contenir au moins 4 chiffres");
			} else {
				Integer num;
				try {
					num = Integer.parseInt(numeroTelephone);
				} catch (NumberFormatException e) {
					throw new Exception("Merci de saisir un numéro de téléphone valid");
				}
			}
		} else {
			throw new Exception("Merci de saisir le numero de telephone");
		}
	}

	/* Fonction de validation du contenu de l'image */
	public void validationImage(InputStream contenuImage, String nomImage) throws Exception {
		/*
		 * Le fichier doit être une image
		 */
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(contenuImage);
		if (!mimeTypes.toString().startsWith("image")) {
			throw new Exception("Le fichier insérer doit être une image");
		}

	}

	/* Fonction de récupration de la valeur d'un champ */
	private String getValeurChamp(HttpServletRequest request, String champ) {
		String valeur = request.getParameter(champ);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur.trim();
		}

	}

	/* Fonction de récuperation du nom de l'image */
	private String getNomImage(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(contentDisposition.indexOf("=") + 1).trim().replace("\"", "");

			}

		}
		return null;
	}

	/* Fonction d'ecriture de l'image dans le disque */
	public void ecrireImage(InputStream contenuImage, String nomImage, String chemin) throws Exception {
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
			entree = new BufferedInputStream(contenuImage);
			sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomImage)));
			byte[] tompon = new byte[TAILLE_TOMPON];
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

	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public boolean getResultat() {
		return resultat;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
