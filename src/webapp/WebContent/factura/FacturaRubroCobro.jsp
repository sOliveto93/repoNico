 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>

<script type="text/javascript">
$(function(){
	dialogoEdicion(200,510);
	$("#agregar")
		.button()
		.click(function(){
			mostrarFacturaRubroCobro('','alta');
		});
  });
	$(document).ready(function() {
		$('#tablaFacturaRubroCobro').fixheadertable({
	        caption   : 'Factura Rubro Cobro',
	        colratio    : [35, 55,350, 26,26], 
	        height      : 200, 
	        width       : 515, 
	        zebra       : true
	   });
	});	
function eliminar(id){
	if(seguroEliminar("Factura Item Tipo")){
		document.edicion.id.value = id;
		document.edicion.operacion.value = 'borrar';
		document.edicion.submit();
	}
}
function modificar(id,operacion){
	$.post('FacturaRubroCobroServlet?operacion=verFacturaRubroCobroJson&id='+id,'', function(data){
		if(data.status == "OK"){
			mostrarFacturaRubroCobro(data.lista[0],operacion);
		};
	});
}
function mostrarFacturaRubroCobro(facturaRubro,operacion){
	$('#descripcion').val(facturaRubro.descripcion);
	$('#codigo').val(facturaRubro.codigo);
	$('#id').val(facturaRubro.id);
	$('#operacion').val(operacion);
	$('#dialog-form').dialog('open');
}
</script>
<title>Factura Rubro Cobro</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body onload="doOnLoad();">
<div class="recuadro">

<%@ include file="/menu.jsp"%>

<input type="hidden" name="cartel" value="${msgError}" id="cartel">

<table id="tablaFacturaRubroCobro" class="fixheadertable" width="400px">
	<thead>
		<tr>
			<th><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
			<th>Codigo</th>
			<th>Descripcion</th>
			<th><img src="images/pencil.png" width="24px" title="Modificar Rubro Cobro"/></th>
			<th><img src="images/delete.png" width="24px" title="Eliminar Rubro Cobro" /></th>
		</tr>
	</thead>
	<tbody>
		<c:set var="facturaRubroNumero" value="0"/>
		<c:forEach items="${rubrosCobro}"  var="rubroCobro">
			<c:if test="${rubroCobro.estado.activado}">
				<c:set var="facturaRubroNumero" value="${facturaRubroNumero + 1}"/>
				<tr>
					<td>${facturaRubroNumero}</td>
					<td>${rubroCobro.codigo}</td>
					<td>${rubroCobro.descripcion}</td>
					<c:if test="${usuario.tipo == ADM}">
						<td><a href="javascript:modificar('${rubroCobro.id}','actualizar');"><img src="images/pencil.png" width="20px" title="Modificar Factura Rubro Cobro"/></a></td>
						<td><a href="javascript:eliminar('${rubroCobro.id}')" title="Eliminar Factura Item Tipo"><img src="images/delete.png" width="24px" title="Eliminar Factura Rubro Cobro" /></a></td>
					</c:if>
					<c:if test="${usuario.tipo == AUDITORIA}">
						<td><a href="javascript:alert('operacion deshabilitada');"><img src="images/pencilGris.png" width="20px" title="Modificar Factura Rubro Cobro"/></a></td>
						<td><a href="javascript:alert('operacion deshabilitada');" title="Eliminar Factura Item Tipo"><img src="images/deleteGris.png" width="24px" title="Eliminar Factura Rubro Cobro" /></a></td>
					</c:if>
				</tr>
			</c:if>
		</c:forEach>	
	</tbody>
</table>
<c:if test="${usuario.tipo == ADM}">
	<%@ include file="/administracion/AgregarVolver.jsp"%>
</c:if>
<div id="dialog-form" title="Factura Rubro Cobro">
	<form action="FacturaRubroCobroServlet" method="post" name="edicion" id="edicion">
		<table  border="0">
			<tr>
				<td><label for="codigo">Codigo : </label></td>
				<td><input name="codigo" type="text" id="codigo" value="" size="7" maxlength="250"></td>
			</tr>
			<tr>
				<td><label for="descripcion">Descripcion:</label></td>
				<td><input name="descripcion" type="text" id="descripcion" value="" size="50" maxlength="250"></td>
			</tr>
		</table>
		<input type="hidden" name="operacion" id="operacion" value="">
		<input type="hidden" name="id" id="id" value="">
	</form>
</div>
</div>
</body>
<script language="JavaScript">
if($('#cartel').val()!=""){
	window.alert($('#cartel').val());
}

var a_fields = {
		'codigo': {'l': 'Codigo', 'r': true,'mx':250},
		'descripcion': {'l': 'Descripcion', 'r': true,'mx':250}
},
o_config = {
		'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>
 