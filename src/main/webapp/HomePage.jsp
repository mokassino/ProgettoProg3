<%@ page language="java" contentType="text/html;" pageEncoding="UTF-8"%>

    <%@ page import="hoppin.*,java.util.List,java.util.ArrayList" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>Area Riservata</title>
</head>
<body>

<h2>Benvenuto <c:out value="${username}"/> </h2>

<div>
<h3> Hotel <c:out value="${hotelInfo.name}"/> </h3> <br> <br> <br>
<c:forEach items="${fns}" var="filename">
		<img id="${filename}" src="/images/${filename}" alt="hotel1" height="200" width="400">
	</c:forEach>
</div>

<div id="noHiddenDiv">
	<br>
	<b>Indirizzo:</b> Via <c:out value="${hotelInfo.via}"/>  <c:out value="${hotelInfo.city}"/> <c:out value="${hotelInfo.postcode}"/> <br>
	Stelle: <c:out value="${hotelInfo.stars}"/> <br>
	Descrizione: <c:out value="${hotelInfo.description}"/> <br>
	<br>
</div>

<div id="hiddenDiv" hidden="hidden">
<br> <br>
<form action="./HotelInfoManagement" method="post">
   Via<input id="${hotelInfo.via}Hidden" type="text" name="via" value="${hotelInfo.via}"></input>  <br>
   Città <input id="${hotelInfo.city}Hidden" type="text" name="city" value="${hotelInfo.city}"></input> <br>
   Codice postale <input id="${hotelInfo.postcode}Hidden" type="text" name="postcode" value="${hotelInfo.postcode}"></input> <br>
   Stelle <input id="${hotelInfo.stars}Hidden" type="text" name="stars" value="${hotelInfo.stars}"></input> <br>
   Descrizione <br> <textarea name="description" rows="5" cols="80" id="${hotelInfo.description}"></textarea> <br> <br>
   <input name="ConfirmEditInfo" type="submit" value="Conferma modifica informazioni"></input> <br>
</form>
</div>


<br>
<button id="AddPhoto">Aggiungi foto</button>
<button id="EditInfo">Modifica informazioni</button>
    			
<div id="invisibleDiv" hidden="hidden">

<!--  enctype="multipart/form-data" -->
 <form action="./HotelInfoManagement" method="post" enctype="multipart/form-data">
        	Aggiungi immagine <br> <input type="file" name="image" ></input> <br> <br>
        	Via <input type="text" name="addressVia"></input> <br>
<input name="AddImage" type="submit" value="Invia"></input> <br>
</form>
</div>
<br>
<br>
<br>
<br>
<br>
<div>
	<button onclick="window.location='FinanceManagement';" type="button">Clicca qui per vedere la parte economica dell'Hotel</button>
	<br> <br>
	<button onclick="window.location='EmployeeManagement';" value="employeeManagement">Clicca qui per gestire l'account dei tuoi dipendenti</button>
	<br> <br>
	<button onclick="window.location='ReservationManagement'" value="reservationManagement">Clicca qui per gestire le prenotazioni al tuo hotel</button>
	<br> <br>
	<button onclick="window.location='PackagesManagement'" value="reservationManagement">Clicca qui per gestire i pacchetti del tuo Hotel</button>
</div>

</body>

<script>
$(document).ready(function() {
    $("#AddPhoto").click(
            function() {
                let element = document.getElementById("invisibleDiv");
                var op = element.getAttribute("hidden");

                if ( op == "hidden"){
                    element.removeAttribute("hidden");
                    $("#AddPhoto").html("Annulla");
                }else{
                    element.setAttribute("hidden","hidden");
                    $("#AddPhoto").html("Aggiungi foto");
                }
            }
    )
});  



$(document).ready(function() {
	$("#EditInfo").click (
			function() {
				let hide1 = document.getElementById("noHiddenDiv");
                let hide2 = document.getElementById("hiddenDiv");
                var op = hide2.getAttribute("hidden");

                if ( op == "hidden"){
                	hide1.setAttribute("hidden","hidden");
                    hide2.removeAttribute("hidden");
                    
                    $("#EditInfo").html("Annulla");
                }else{
                	hide2.setAttribute("hidden","hidden");
                	hide1.removeAttribute("hidden");
                    $("#EditInfo").html("Modifica informazioni");
                }
            }
	)
	
}); 


</script>

</html>




