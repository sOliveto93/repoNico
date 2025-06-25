 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../interfaces/headers.jsp"%> 
<script type="text/javascript">
$(function(){
	$("#adjuntar")
		.button()
		.click(function(){
			document.update_anexo.submit();
		});
}); 
function mostrarAdjuntar(){
	if (document.getElementById('importFile').value != ''){
		document.getElementById('adjuntar').style.visibility = "visible";
	}
}
</script>
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="/Cuotas/InterfaceServlet" name="update_anexo" method="post"  enctype="multipart/form-data">

<h3>Importar datos de Pago Facil</h3>
<input id="importFile" name="importFile" type="file" value="Buscar..." onchange="mostrarAdjuntar();" size="100">
</br>
<button id="adjuntar" name="adjuntar" style="visibility: hidden">Adjuntar</button>
<vel:velocity strictaccess="true"> 
#set($paso = $scopetool.getRequestScope("paso"))
</vel:velocity>
</form>
<h3>Descripcion del proceso:</h3>
<p>
Pide la fecha de importación, y la verifica con la fecha del archivo (fecha de pago). Si no conincide no permite seguir tirando errore de fecha
</p>
<p>
Si concide pide confiración de los datos a importar.
</p>
<p>
Lo muestra por pantalla, con tres diferentes mensajes:
</p>
<p>
DN__037611995__2023__20121002__yaPago_0000140,00 31/10/12__ -->0937015301400012276232672023014000151792
DN__028227530__0051__20121101__Pago_0000350,00 31/10/12__ -->0937015303500012306239940051035000152090
DN__028227530__0051__20120901__No existe -->0937015303500012245239940051035000151488
</p>
</form>
</body>
</html>