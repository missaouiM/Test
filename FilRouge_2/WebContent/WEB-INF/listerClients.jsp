<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des clients</title>
		<link type="text/css" rel="stylesheet" href="inc/style.css" />        
    </head>
    <body>
    	<c:import url="/inc/menu.jsp"></c:import>
		<div id="corps">    	
    	<c:choose>
    	
    		<c:when test="${!empty sessionScope.liste_client }">
		        <table id="tableau">
		          <tr>
		            <th >Nom</th>
		            <th >Prénom</th>
		            <th >Adresse</th>
		            <th >Téléphone</th>
		            <th >Email</th>
		            <th >Image</th>
		            <th class="action">Action</th>
		          </tr>    		
    			  <c:forEach items="${sessionScope.liste_client }" var="client" varStatus="boucle">    				
		          <tr class="${boucle.index%2 == 0 ? 'pair': 'impair' }">		          
		            <td >
		            ${client.value.nom }
		            </td>
		            <td>
		            ${client.value.prenom }
		            </td>
		            <td>
		            ${client.value.adresseLivraison }
		            </td>
		            <td>
		            ${client.value.numTel }
		            </td>
		            <td>
		            ${client.value.adresseMail }
		            </td>
		            <td>
		            <c:if test="${!empty client.value.nomImage }">         
		            <a href ="<c:url value="/downloadImage/${client.value.nomImage }"/>">Voir</a>
		            </c:if>	
		            </td>		            	
		            <td class="action">
		            <a href="<c:url value="/supprimerClient"><c:param name="client_supprimer" value="${client.value.id }"/></c:url>" >
		            <img alt="Supprimer" src="<c:url value="/inc/supprimer.png"/>">
		            </a>
		            </td>			            	            
		          </tr>
    			</c:forEach>		          
        </table>    				
    		</c:when>
    		<c:otherwise>
    			<p class="info">Aucun client enregistré</p>
    		</c:otherwise>
    	</c:choose>
    	</div>
    </body>
</html>