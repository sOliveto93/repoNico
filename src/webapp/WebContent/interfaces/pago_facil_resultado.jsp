<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK rel="stylesheet" type="text/css" media="print" href="css/impresion_factura.css" />
<title>Insert title here</title>
</head>
<body>
<div class="recuadro">
<DIV class="sin_block">
<%@ include file="/menu.jsp"%>
<input type="button" value="Imprimir" onclick="javascript:window.print()" />
</div>
<div class='print_block2'>
<h3>Resultados del Proceso</h3>
<vel:velocity strictaccess="true"> 
#set($pagos = $scopetool.getSessionScope("pagos"))
<table bgcolor="#ffffff" border="1" cellSpacing="0" cellPadding="3"><tr><td>Tipo</td><td>Numero</td><td>Grupo</td><td>Estado</td><td>Fecha Cuota</td><td>Importe</td><td>Fecha</td><td>Codigo</td></tr>
#foreach($pago in $pagos)
	
		<tr><td>$pago.tipo_doc</td><td>$pago.num_doc</td><td>$pago.nro_grupo</td><td>$pago.estado</td><td>$pago.fecha</td><td aling="right">$ $pago.importe</td><td>$pago.fecha_pago</td><td>$pago.cod_barra</td></tr>
	
#end
</table>

</vel:velocity>
</form>
</div>
<DIV class="sin_block">
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
<tr><td>Tipo</td><td>Numero</td><td>Grupo</td><td>Estado</td><td>Mes de Cuota</td><td>Importe</td><td>Fecha de Pago</td><td>Codigo</td></tr>
<tr><td>DN</td><td>24847053</td><td>73</td><td>Ya Pago</td><td>01-10-2012</td><td>$ 200.0</td><td>31-10-2012</td><td>0937015302000012214230100073020000151170</td></tr>
<tr><td>DN</td><td>24847053</td><td>73</td><td>Pago</td><td>01-11-2012</td><td>$ 200.0</td><td>31-10-2012</td><td>0937015302000012245230100073020000151484</td></tr>
<tr><td>DN</td><td>31032088</td><td>7</td><td>No existe</td><td>01-09-2012</td><td>$ 350.0</td><td>31-10-2012</td><td>0937015303500012214230470007035000151177</td></tr>
</table>
</p>
</div>
</div>
</body>
</html>