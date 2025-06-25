<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat,java.util.Date,java.text.DateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../interfaces/headers.jsp"%> 
<script type="text/javascript">
$(function(){
	$("#adjuntar")
		.button()
		.click(function(){
			//alert("/Cuotas/InterfaceServlet?operacion=upload&fecha="+document.getElementById('fe').value);
			document.update_anexo.action="/Cuotas/InterfaceServlet?operacion=upload&fecha="+document.getElementById('fe').value;
			document.update_anexo.submit();
		});
});
var myCalendar;
function doOnLoad() {
	myCalendar = new dhtmlXCalendarObject(["fe"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
}
 
function mostrarAdjuntar(){
	if (document.getElementById('importFile').value != ''){
		document.getElementById('adjuntar').style.visibility = "visible";
	}
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body onload="doOnLoad();">
<%
DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
String f = df.format(new Date()); 
%>

<div class="recuadro">
<%@ include file="/menu.jsp"%>
<h3>Importar datos de Pago Facil</h3>
<form name="au">
<label>
Fecha de Carga del Pago:
<input name="fe" id="fe" type="text" value="<%=f %>"></label><br>
</form>
<form action="/Cuotas/InterfaceServlet?operacion=upload" name="update_anexo" method="post"  enctype="multipart/form-data">
<input id="importFile" name="importFile" type="file" value="Buscar..." onchange="mostrarAdjuntar();" size="100">
</br>
<button id="adjuntar" name="adjuntar" style="visibility: hidden">Adjuntar</button>
</form>
<hr>
<h3>Descripcion del proceso:</h3>
<p>
Pide la fecha de importación que es la que pondrá como fecha de carga.
</p>

<p>
Lo muestra por pantalla, con tres diferentes mensajes:
</p>
<ul>
	<li>Ya Pago: si el pago ya fue registrado antes.</li>
	<li>Pago: si se cargo el pago con este archivo.</li>
	<li>No existe: si no existe el pago para ese grupo y esa persona (es decir no tiene la cuota cargada).</li>
</ul>
<p>
<table bgcolor="#ffffff" border="1" cellSpacing="0" cellPadding="3">
<tr><td>Tipo</td><td>Numero</td><td>Grupo</td><td>Estado</td><td>Importe</td><td>Fecha de Pago</td><td>Codigo</td></tr>
<tr><td>DN</td><td>24847053</td><td>73</td><td>Ya Pago</td><td>$ 200.0</td><td>31-10-2012</td><td>0937015302000012214230100073020000151170</td></tr>
<tr><td>DN</td><td>24847053</td><td>73</td><td>Pago</td><td>$ 200.0</td><td>31-10-2012</td><td>0937015302000012245230100073020000151484</td></tr>
<tr><td>DN</td><td>31032088</td><td>7</td><td>No existe</td><td>$ 350.0</td><td>31-10-2012</td><td>0937015303500012214230470007035000151177</td></tr>
</table>
</p>
</div>
</body>
</html>