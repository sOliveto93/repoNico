 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
</head>
<body>
<vel:velocity strictaccess="true">
#set($msgError = $scopetool.getRequestScope("msgError"))
<input type="hidden" name="cartel" value="$!{msgError}" id="cartel">
</vel:velocity>
<form action="GrupoCuotasServlet" method="post" name="grupoCuotas" id="grupoCuotas">
	<table id="grupoCoutas" style="visibility: hidden">
		<thead>
			<td>M/E</td>
			<td>N&ordm;Mes</td>
			<td>Monto 1</td>
			<td>Monto 2</td>
		</thead>
		<tbody id="datos_grupoCuotas"></tbody>
	</table>
</form>
</body>
</html>