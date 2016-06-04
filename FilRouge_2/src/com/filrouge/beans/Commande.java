package com.filrouge.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;

import com.filrouge.tools.JodaDateTimeConverter;

@Entity
@Table(name = "commande")
public class Commande {
	private String		montant;
	private String		modePaiement;
	private String		statutPaiement;
	private String		modeLivraison;
	private String		statutLivraison;

	@JoinColumn(name = "id_client")
	@ManyToOne(optional = false)
	private Client		client;

	@Column(name = "dateCreation", columnDefinition = "TIMESTAMP")
	@Converter(name = "dateTimeConverter", converterClass = JodaDateTimeConverter.class)
	@Convert("dateTimeConverter")
	private DateTime	date;
	@Column(name = "id_commande")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long		id;

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public String getModePaiement() {
		return modePaiement;
	}

	public void setModePaiement(String modePaiement) {
		this.modePaiement = modePaiement;
	}

	public String getModeLivraison() {
		return modeLivraison;
	}

	public void setModeLivraison(String modeLivraison) {
		this.modeLivraison = modeLivraison;
	}

	public String getStatutPaiement() {
		return statutPaiement;
	}

	public void setStatutPaiement(String statutPaiement) {
		this.statutPaiement = statutPaiement;
	}

	public String getStatutLivraison() {
		return statutLivraison;
	}

	public void setStatutLivraison(String statutLivraison) {
		this.statutLivraison = statutLivraison;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
