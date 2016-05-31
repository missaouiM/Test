<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'une commande</title>
        <link type="text/css" rel="stylesheet" href="inc/style.css" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp"></c:import>
        <div id="corps">
	    	<p class="info"> ${form.resultat }</p>
			<p class="titre">Information client :</p>
			<p>Nom : <c:out value="${commande.client.nom }"/></p>
			<p>Prénom : <c:out value="${commande.client.prenom }"/></p>
			<p>Adresse de livraison : <c:out value="${commande.client.adresseLivraison }"/></p>
			<p>Numéro de téléphone : <c:out value="${commande.client.numTel }"/></p>
			<p>Email : <c:out value="${commande.client.adresseMail }"/></p>
			<p>Image               : <c:out value="${commande.client.nomImage }"/></p> 
			<p class="titre">Information commande : </p>
			<p>Date de la commande : <c:out value="${commande.date }"/></p>
			<p>Montant de la commande : <c:out value="${commande.montant }"/></p>
			<p>Mode de paiement : <c:out value="${commande.modePaiement }"/></p>
			<p>Statut de paiement : <c:out value="${commande.statutPaiement } "/></p>
			<p>Mode de livraison : <c:out value="${commande.modeLivraison } "/></p>
			<p>Statut de livraison : <c:out value="${commande.statutLivraison } "/></p>
		</div>
    </body>
</html>