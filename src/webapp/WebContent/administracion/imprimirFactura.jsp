<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<LINK rel="stylesheet" type="text/css"  media="print" href="css/impresion_factura.css" />
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(function(){
	var tab = $('#tabs').tabs();

	
	$("#volver")
		.button()
		.click(function(){
			window.print();
	});
		
});
</script>
</head>
<body onload="doOnLoad();">
<DIV class="sin_block">
<form action="FacturaServlet?operacion=listar&limpiarSession=1" method="post" name="button">
		<button id="volver" name="operacion" >Imprimir</button>
</form>
</DIV>
<!-- <input type="button" value="Print this page" onclick="">-->
<vel:velocity strictaccess="true">
#set($factura = $scopetool.getSessionScope("factura"))
<DIV class="print_block">
<table width="700" border="0" bgcolor="#ffffff">
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><div align="right">$factura.fechaCarga</div></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>$factura.persona.nombreCompleto</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>#if ($factura.persona.direccion) $factura.persona.direccion.domicilioCompleto #else &nbsp; #end</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td>&nbsp;</td>
    <td>Consumidor Final </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td>&nbsp;</td>
    <td>$factura.formaPago </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td colspan="4">
	<table width="100%" border="0">
		  <tr>
			<td>Cod.</td>
			<td>Cant.</td>
			<td>Detalle</td>
			<td>Precio</td>
			<td>Importe</td>
		  </tr>
		#foreach ($item in $factura.facturaItems)
		  <tr>
			<td>$item.codigo</td>
			<td>$item.cantidad</td>
			<td>$item.facturaItemTipo.descri</td>
			<td>$item.precio</td>
			<td>$item.precioTotal</td>
		  </tr>
		#end
		  <tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		  </tr>
		</table>


	</td>
  </tr>
    <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><div align="right">$factura.monto</div></td>
    <td>&nbsp;</td>
  </tr>
 
</table>
</DIV>

</vel:velocity>
</body>
</html>