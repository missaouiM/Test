<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'un client</title>
        <link type="text/css" rel="stylesheet" href="inc/style.css" />
    </head>
    <body>
        <div>
            <form method="post" action="creationClient" enctype="multipart/form-data">
                <fieldset>
                    <legend>Informations client</legend>
 					<c:import url="/inc/inc_form_clie.jsp"/>
 					<label for ="image">Image</label>
 					<input type="file" id="image" name="image"/>
 					<span class="erreur"><c:out value="${form.erreurs['image']}"/></span>
 					<p class="info">${ form.message }</p>
                </fieldset>
                <input type="submit" value="Valider"  />
                <input type="reset" value="Remettre à zéro" /> <br />
            </form>
        </div>
    </body>
</html>