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
    	
    		<c:when test="${!empty sessionScope.liste_commande }">
		        <table id="tableau">
		          <tr>
		            <th >Client</th>
		            <th >Date</th>
		            <th >Montant</th>
		            <th >Mode de paiement</th>
		            <th >Statut de paiement</th>
		            <th >Mode de livraison</th>
		            <th >Statut de livraison</th>
		            <th class="action">Action</th>
		          </tr>    		
    			  <c:forEach items="${sessionScope.liste_commande }" var="commande" varStatus="boucle">    				
		          <tr class="${boucle.index%2 == 0 ? 'pair': 'impair' }">	
		            <td >
		            ${commande.value.client.prenom } ${commande.value.client.nom }
		            </td>
		            <td>
		            ${commande.value.date }
		            </td>
		            <td>
		            ${commande.value.montant }
		            </td>
		            <td>
		            ${commande.value.modePaiement }
		            </td>
		            <td>
		            ${commande.value.statutPaiement }
		            </td>	
		            <td>
		            ${commande.value.modeLivraison }
		            </td>	
		            <td>
		            ${commande.value.statutLivraison }
		            </td>			            		            
		            <td class="action">
					<a href="<c:url value="/supprimerCommande"><c:param name="commande_supprimer" value="${commande.value.id }"/></c:url>" >           
		            <img alt="Supprimer" src="<c:url value="/inc/supprimer.png"/>">
		            </a> 
		            </td>			            	            
		          </tr>
    			</c:forEach>		          
        </table>    				
    		</c:when>
    		<c:otherwise>
    			<p class="info">Aucune commande enregistrée</p>
    		</c:otherwise>
    	</c:choose>
    	</div>
    </body>
</html>