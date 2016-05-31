<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'un client</title>
        <link type="text/css" rel="stylesheet" href="inc/style.css" />
    </head>
    <body>
    <c:import url="/inc/menu.jsp"></c:import>
    <div id="corps">
        <p class="info"> ${form.message }</p>
		    <p>Nom				   : <c:out value="${client.nom }"/></p>
		    <p>Prénom              : <c:out value="${client.prenom }"/></p>
		    <p>Adresse             : <c:out value="${client.adresseLivraison }"/></p>
		    <p>Numéro de téléphone : <c:out value="${client.numTel }"/></p>
		    <p>Email               : <c:out value="${client.adresseMail }"/></p> 
		    <p>Image               : <c:out value="${client.nomImage }"/></p>   
	</div>
    </body>
</html>