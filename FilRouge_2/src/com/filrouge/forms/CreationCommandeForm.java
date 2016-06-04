package com.filrouge.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import com.filrouge.beans.Client;
import com.filrouge.beans.Commande;
import com.filrouge.dao.ClientDao;
import com.filrouge.dao.CommandeDao;
import com.filrouge.dao.DAOException;

public class CreationCommandeForm {

	private static final String	CHAMP__MODL		= new String("modeLivraisonCommande");
	private static final String	CHAMP__MODP		= new String("modePaiementCommande");
	private static final String	CHAMP__STAL		= new String("statutLivraisonCommande");
	private static final String	CHAMP__STAP		= new String("statutPaiementCommande");
	private static final String	CHAMP__MNTC		= new String("montantCommande");
	private static final String	FMT_DT			= new String("dd/MM/yyyy hh:mm:ss");
	private static final String	CHAMP_BOUTON	= new String("choixNouveauClient");
	private static final String	ANCIEN_CLIENT	= new String("ancienClient");
	private static final String	CHAMP_LISTE		= new String("listeClients");
	private static final String	SESSION_CLIENT	= new String("liste_client");

	private ClientDao			clientDao;
	private CommandeDao			commandeDao;
	private String				resultat;
	private Map<String, String>	erreurs			= new HashMap<String, String>();

	public CreationCommandeForm(ClientDao clientDao, CommandeDao commandeDao) {
		this.clientDao = clientDao;
		this.commandeDao = commandeDao;
	}

	public Commande creerCommande(HttpServletRequest request, String chemin) {
		Client client;
		Commande commande = new Commande();
		String modeLivraisonCommande = getValeurChamp(request, CHAMP__MODL);
		String modePaiementCommande = getValeurChamp(request, CHAMP__MODP);
		String statutLivraisonCommande = getValeurChamp(request, CHAMP__STAL);
		String statutPaiementCommande = getValeurChamp(request, CHAMP__STAP);
		String montantCommande = getValeurChamp(request, CHAMP__MNTC);
		String boutonNouveau = getValeurChamp(request, CHAMP_BOUTON);
		if (ANCIEN_CLIENT.equals(boutonNouveau)) {
			Long idAncienClient = null;
			try {
				idAncienClient = Long.parseLong(getValeurChamp(request, CHAMP_LISTE));
			} catch (NumberFormatException e) {
				setErreur(CHAMP_BOUTON, "Client inconnu, merci d'indiquer un client valid");
				idAncienClient = 0L;
			}
			HttpSession session = request.getSession();
			client = ((Map<Long, Client>) session.getAttribute(SESSION_CLIENT)).get(idAncienClient);
		} else {
			CreationClientForm creationclient = new CreationClientForm(clientDao);
			client = creationclient.creerClient(request, chemin);
			this.erreurs = creationclient.getErreurs();
		}
		commande.setClient(client);
		try {
			validationModeLivraison(modeLivraisonCommande);
		} catch (Exception e) {
			setErreur(CHAMP__MODL, e.getMessage());

		}
		commande.setModeLivraison(modeLivraisonCommande);
		try {
			validationModePaiement(modePaiementCommande);
		} catch (Exception e) {
			setErreur(CHAMP__MODP, e.getMessage());
		}
		commande.setModePaiement(modePaiementCommande);
		try {
			validationStatutLivraison(statutLivraisonCommande);
		} catch (Exception e) {
			setErreur(CHAMP__STAL, e.getMessage());
		}
		commande.setStatutLivraison(statutLivraisonCommande);
		try {
			validationStatutPaiement(statutPaiementCommande);

		} catch (Exception e) {
			setErreur(CHAMP__STAP, e.getMessage());

		}
		commande.setStatutPaiement(statutPaiementCommande);
		try {
			validationMontantCommande(montantCommande);

		} catch (Exception e) {
			setErreur(CHAMP__MNTC, e.getMessage());

		}
		commande.setMontant(montantCommande);
		DateTime date;
		DateTime dt = new DateTime();
		/*
		 * DateTimeFormatter formatter = DateTimeFormat.forPattern(FMT_DT); date
		 * = dt.toString(formatter);
		 */
		commande.setDate(dt);
		try {
			if (erreurs.isEmpty()) {
				if (!ANCIEN_CLIENT.equals(boutonNouveau)) {
					clientDao.creer(commande.getClient());
				}
				commandeDao.creer(commande);
				resultat = "Succés de la création de commande";
			} else {
				resultat = "Echec de le céation du client";
			}
		} catch (DAOException e) {
			resultat = e.getMessage();
		}

		return commande;
	}

	/*
	 * Validation du mode de paiement
	 */
	public void validationModePaiement(String modePaiement) throws Exception {
		if (modePaiement != null) {
			if (modePaiement.length() < 2) {
				throw new Exception("Le mode de paiement doit contenir au moins deux caractères");
			}

		} else {
			throw new Exception("Merci de saisir le mode paiement");
		}

	}

	/*
	 * Validation du mode de livraison
	 */
	public void validationModeLivraison(String modeLivraison) throws Exception {
		if (modeLivraison != null) {
			if (modeLivraison.length() < 2) {
				throw new Exception("Le mode de livraison doit contenir au moins deux caractères");
			}
		} else {
			throw new Exception("Merci de siasir le mode de livraison");
		}
	}

	/*
	 * Validation du statut de livraison
	 */
	public void validationStatutLivraison(String statutLivraison) throws Exception {
		if (statutLivraison != null) {
			if (statutLivraison.length() < 2) {
				throw new Exception("Le statut de livraison doit contenir au moins deux caractères");
			}
		}
	}

	/*
	 * Validation du statut de paiement
	 */
	public void validationStatutPaiement(String statutPaiement) throws Exception {
		if (statutPaiement != null) {
			if (statutPaiement.length() < 2) {
				throw new Exception("Le statut de paiement doit contenir au moins deux caractères");
			}
		}

	}

	/*
	 * Validation du monatant de la commande
	 */
	public void validationMontantCommande(String montantCommande) throws Exception {
		if (montantCommande == null) {
			throw new Exception("Merci de saisir le montant de la commande");
		} else {
			double mnt;
			try {
				mnt = Double.parseDouble(montantCommande);
				if (mnt < 0) {
					throw new Exception("Le montant de la commande doit être positif");
				}
			} catch (NumberFormatException e) {
				throw new Exception("Merci de saisir un montant de commande valide");
			}
		}

	}

	private String getValeurChamp(HttpServletRequest request, String champ) {
		String valeur = request.getParameter(champ);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur.trim();
		}

	}

	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	public String getResultat() {
		return resultat;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

}
