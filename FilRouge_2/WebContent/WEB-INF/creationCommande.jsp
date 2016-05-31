<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'une commande</title>
        <link type="text/css" rel="stylesheet" href="inc/style.css" />
    </head>
    <body>
        <div>
            <form method="post" action="creationCommande" enctype="multipart/form-data">
                <fieldset>
                	<c:set var="client" scope="request" value="${commande.client }"></c:set>
                    <legend>Informations client</legend>
                    <c:if test="${ !empty sessionScope.liste_client }">
                        <label for="choixNouveauClient">Nouveau client ? <span class="requis">*</span></label>
                        <input type="radio" id="choixNouveauClient" name="choixNouveauClient" value="nouveauClient" checked /> Oui
                        <input type="radio" id="choixNouveauClient" name="choixNouveauClient" value="ancienClient" /> Non
                        <span class="erreur"><c:out value="${form.erreurs['choixNouveauClient']}"/></span>
                        <br/><br />
                    </c:if>     
                    <div id="nouveauClient">
                    	<c:import url="/inc/inc_form_clie.jsp"></c:import>
 						<label for ="image">Image</label>
 						<input type="file" id="image" name="image"/>
 						<span class="erreur"><c:out value="${form.erreurs['image']}"/></span>                    	
                    </div> 
                    <c:if test="${ !empty sessionScope.liste_client }">
                    <div id="ancienClient">
                        <select name="listeClients" id="listeClients">
                            <option value="">Choisissez un client...</option>
                            <%-- Boucle sur la map des clients --%>
                            <c:forEach items="${ sessionScope.liste_client }" var="mapClients">
                            <%--  L'expression EL ${mapClients.value} permet de cibler l'objet Client stocké en tant que valeur dans la Map, 
                                  et on cible ensuite simplement ses propriétés nom et prenom comme on le ferait avec n'importe quel bean. --%>
                            <option value="${ mapClients.value.id }">${ mapClients.value.prenom } ${ mapClients.value.nom }</option>
                            </c:forEach>
                        </select>
                    </div>
                    </c:if>                                 
                </fieldset>
                <fieldset>
                    <legend>Informations commande</legend>
                    
                    <label for="dateCommande">Date <span class="requis">*</span></label>
                    <input type="text" id="dateCommande" name="dateCommande" value="<c:out value="${commande.date }"/>" size="20" maxlength="20" disabled />
                    <span class="erreur"><c:out value="${form.erreurs['dateCommande']}"/></span>
                    <br />
                    
                    <label for="montantCommande">Montant <span class="requis">*</span></label>
                    <input type="text" id="montantCommande" name="montantCommande" value="<c:out value="${commande.montant }"/>" size="20" maxlength="20" />
                    <span class="erreur"><c:out value="${form.erreurs['montantCommande']}"/></span>
                    <br />
                    
                    <label for="modePaiementCommande">Mode de paiement <span class="requis">*</span></label>
                    <input type="text" id="modePaiementCommande" name="modePaiementCommande" value="<c:out value="${commande.modePaiement }"/>" size="20" maxlength="20" />
                    <span class="erreur"><c:out value="${form.erreurs['modePaiementCommande']}"/></span>
                    <br />
                    
                    <label for="statutPaiementCommande">Statut du paiement</label>
                    <input type="text" id="statutPaiementCommande" name="statutPaiementCommande" value="<c:out value="${commande.statutPaiement }"/>" size="20" maxlength="20" />
                    <span class="erreur"><c:out value="${form.erreurs['statutPaiementCommande']}"/></span>
                    <br />
                    
                    <label for="modeLivraisonCommande">Mode de livraison <span class="requis">*</span></label>
                    <input type="text" id="modeLivraisonCommande" name="modeLivraisonCommande" value="<c:out value="${commande.modeLivraison }"/>" size="20" maxlength="20" />
                    <span class="erreur"><c:out value="${form.erreurs['modeLivraisonCommande']}"/></span>
                    <br />
                    
                    <label for="statutLivraisonCommande">Statut de la livraison</label>
                    <input type="text" id="statutLivraisonCommande" name="statutLivraisonCommande" value="<c:out value="${commande.statutLivraison }"/>" size="20" maxlength="20" />
                    <span class="erreur"><c:out value="${form.erreurs['statutLivraisonCommande']}"/></span>
                    <br />
                    <p class="info">${ form.resultat }</p>
                </fieldset>
                <input type="submit" value="Valider"  />
                <input type="reset" value="Remettre à zéro" /> <br />
            </form>
        </div>
        <script src="<c:url value="/inc/jquery.js"/>"></script>
        
        <%-- Petite fonction jQuery permettant le remplacement de la première partie du formulaire par la liste déroulante, au clic sur le bouton radio. --%>
        <script>
        	jQuery(document).ready(function(){
        		/* 1 - Au lancement de la page, on cache le bloc d'éléments du formulaire correspondant aux clients existants */
        		$("div#ancienClient").hide();
        		/* 2 - Au clic sur un des deux boutons radio "choixNouveauClient", on affiche le bloc d'éléments correspondant (nouveau ou ancien client) */
                jQuery('input[name=choixNouveauClient]:radio').click(function(){
                	$("div#nouveauClient").hide();
                	$("div#ancienClient").hide();
                    var divId = jQuery(this).val();
                    $("div#"+divId).show();
                });
            });
        </script>        
    </body>
</html>