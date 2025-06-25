 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<title>Factura</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
	<table id='cabeceraFactura' width="60%">
		<tr>
			<td>
				<label for="tipoDocumento">Tipo Documento:</label>
 				<input name='tipoDocumento' id='tipoDocumento' value='' readonly="readonly" class="nobord">
 				 
				<label for="numeroDocumento">Nro Documento:</label>
				<input name='numeroDocumento' id='numeroDocumento' value='' readonly="readonly" class="nobord"> 
			</td>
			<td>
				<label for="numeroFacturaDialog">Nro Factura:</label>
				<input name='numeroFacturaDialog' id='numeroFacturaDialog' value='' readonly="readonly" class="nobord">
			</td>
			<td>
				<label for="fechaDePago">Fecha:</label>
				<input name='fechaDePago' id='fechaDePago' value='' readonly="readonly" class="nobord">
			</td>
		</tr>
		<tr>
			<td>
				<label for="descripcionExtendida">Detalle:</label>
				<input name='descripcionExtendida' id='descripcionExtendida' value='' readonly="readonly" class="nobord">
			</td>
			<td></td>
			<td colspan="3" align="right">
				<label for="sucursalCobro">Sucursal:</label>
				<input name='sucursalCobro' id='sucursalCobro' value='' readonly="readonly" class="nobord">
			</td>
		</tr>
		<tr>
			<td>
				<input name='descripcionBasica' id='descripcionBasica' value='' readonly="readonly" class="nobord">
			</td>
			<td colspan="2" align="right">F.Pago
				<input name='formaDePago' id='formaDePago' value='' readonly="readonly" class="nobord" >
			</td>
		</tr>
	</table>
	<table id='items'>
		<thead>
			<tr>
				<th>R.</th>
				<th>Concepto</th>
				<th>Descripcion</th>
				<th>Pr.Unitario</th>
				<th>Rubro</th>	
				<th>Codigo</th>
			</tr>
		</thead>
		<tbody  id="datos_facturaItems"></tbody>
	</table>
</body>
</html>