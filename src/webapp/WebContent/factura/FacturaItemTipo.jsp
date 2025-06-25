<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>

<script type="text/javascript">
var operacionPartida;
$(function(){
	dialogoEdicion(380,600);
	$("#agregar")
		.button()
		.click(function(){
			mostrarFacturaItemTipo('','alta');
		});
	$("#dialogFormPartida").dialog({
		autoOpen: false,
		height:250,
		width:500,
		modal: true,
		buttons: {
			'Grabar': function(event) {
				var descripcion = $("#descripcionPartida").val();
				operacionesGrupoPartida(descripcion);
				$("#dialogFormPartida").dialog('close');
			},
			Cancel: function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
});

	$(document).ready(function() {
		$('#tablaFacturaItemTipo').fixheadertable({
	        caption   : 'Factura Item Tipo',
	        colratio    : [25,150,125,300,200,50,25,25], 
	        height      : 400, 
	        width       : 920, 
	        zebra       : true
	   });
	});
function operacionesGrupoPartida(descripcion){
	var idPartida = $("#idPartida").val();
	$.post('FacturaItemTipoServlet?operacion='+operacionPartida+'&descripcion='+descripcion+'&idPartida='+idPartida,'', function(data){
		partida = data.lista[0];
		if ($("#partida option[value="+partida.id+"]").length == 0){
			$('#partida').append($('<option>', {
			    value:partida.id,
			    text: partida.descripcion,
			    selected: true
			}));
		}else if($("#partida option[value="+partida.id+"]").text() != partida.descripcion){
			$("#partida option[value="+partida.id+"]").text(partida.descripcion);
		}else{
			$("#partida option[text=" + partida.id +"]").attr("selected","selected") ;
		}
	});
}
function openDialogPartida(operacion,cargarDescripcion){
	operacionPartida = operacion;
	if(cargarDescripcion == "true"){
		$("#descripcionPartida").val($("#partida option:selected").text());
		var idPartida = $("#partida").val();
		$("#idPartida").val(idPartida);
	}
	$('#dialogFormPartida').dialog('open');
}
function eliminar(id){
	if(seguroEliminar("Factura Item Tipo")){
		document.edicion.id.value = id;
		document.edicion.operacion.value = 'borrar';
		document.edicion.submit();
	}
}
function modificar(id,operacion){
	$.post('FacturaItemTipoServlet?operacion=verFacturaItemJson&id='+id,'', function(data){
		if(data.status == "OK"){
			mostrarFacturaItemTipo(data.lista[0],operacion);
		};
	});
}
function mostrarFacturaItemTipo(facturaItemTipo,operacion){
	var rubroCobro = facturaItemTipo.rubroId;
	if(operacion == "actualizar"){
		$('#descripcion').val(facturaItemTipo.descripcion);
		$("#partida option[value=" + facturaItemTipo.partida.id + "]").attr('selected', 'selected');
		$('#precio').val(facturaItemTipo.precio);
		$('#codigo').val(facturaItemTipo.codigo);
		$("#rubroCobro option[value="+rubroCobro+"]").attr("selected",true);
		$('#id').val(facturaItemTipo.id);
	}
	$('#operacion').val(operacion);
	$('#dialog-form').dialog('open');
}
</script>
<title>Factura Item Tipo</title>
</head>
<body onload="doOnLoad();">
<div class="recuadro">

<%@ include file="/menu.jsp"%>

<div id="dialogFormPartida" title="Alta Partida">
	<form name="partidaAltaModifica" id="partidaAltaModifica">
		Descripcion: <input type="text" name="descripcionPartida" id="descripcionPartida"  value=""></input>
		<input type="hidden" name="idPartida" id="idPartida" value="" ></input>
	</form>
</div>

<input type="hidden" name="cartel" value="${msgError}" id="cartel">
<table id="tablaFacturaItemTipo" class="fixheadertable">
	<thead>
		<tr>
			<th ><img src="images/hash.png" width="24px" title="rubro Cobro Codigo" /></th>
			<th>Codigo</th>
			<th>Rubro</th>
			<th>Partida</th>
			<th>Descripcion</th>
			<th>precio</th>
			<th><img src="images/pencil.png" width="24px" title="Modificar Carrera"/></th>
			<th><img src="images/delete.png" width="24px" title="Eliminar Carrera" /></th>
		</tr>
	</thead>
	<tbody>
		<c:set var="facturaItemTipoNumero" value="0"/>
		<c:forEach items="${facturaItemTipos}"  var="facturaItemTipo">
			<c:if test="${facturaItemTipo.estado.activado}">
				<c:set var="facturaItemTipoNumero" value="${facturaItemTipoNumero + 1}"/>
				<tr>
					<td>${facturaItemTipoNumero}</td> 
					<!-- <td></td> -->
					<td>${facturaItemTipo.rubroCobro.codigo} <c:if test="${facturaItemTipo.codigo  < 10}">0</c:if>${facturaItemTipo.codigo} </td>
					<td>${facturaItemTipo.rubroCobro.descripcion}</td>
					<td>${facturaItemTipo.partida.descripcion}</td>
					<td>${facturaItemTipo.descripcion}</td>
					<td>${facturaItemTipo.precio}</td>
					<c:if test="${(usuario.tipo == ADM)}">
						<td><a href="javascript:modificar('${facturaItemTipo.id}','actualizar');"><img src="images/pencil.png" width="20px" title="Modificar Factura Item Tipo"/></a></td>
						<td><a href="javascript:eliminar('${facturaItemTipo.id}')" title="Eliminar Factura Item Tipo"><img src="images/delete.png" width="24px" title="Eliminar Factura Item Tipo" /></a></td>
					</c:if>
					<c:if test="${(usuario.tipo == DEP)||(usuario.tipo == DRIYC) || (usuario.tipo == AUDITORIA)}">
						<td><a href="#"></a><img src="images/pencilGris.png" width="20px" title="Modificar Factura Item Tipo"/></a></td>
						<td><a href="#"></a><img src="images/deleteGris.png" width="24px" title="Eliminar Factura Item Tipo" /></a></td>
					</c:if>
				</tr>
			</c:if>
		</c:forEach>	
	</tbody>
</table>
<c:if test="${(usuario.tipo == ADM)}">
	<%@ include file="/administracion/AgregarVolver.jsp"%>
</c:if>
<div id="dialog-form" title="Factura Item Tipo">
	<form action="FacturaItemTipoServlet" method="post" name="edicion" id="edicion">
		<table  border="0">
			<tr>
				<td><label for="codigo">Codigo : </label></td>
				<td><input name="codigo" type="text" id="codigo" value="" size="7" maxlength="250"></td>
			</tr>
			<tr>
				<td><label for="partida">Partida:</label></td>
				<td>
					<select name="partida" id="partida">
						<c:forEach items="${partidas}"  var="partida">
							<option value="${partida.id}">${partida.descripcion}</option>
						</c:forEach>
					</select>
					<a href="javascript:openDialogPartida('altaPartidaJson','');"><img src="images/add.png" width="15px" title="Agregar Partida"/></a>
					<a href="javascript:openDialogPartida('modificarPartidaJson','true');"><img src="images/pencil.png" width="15px" title="Modificar Partida"/></a>
				</td>
			</tr>
			<tr>
				<td><label for="descripcion">Descripcion:</label></td>
				<td><input name="descripcion" type="text" id="descripcion" value="" size="50" maxlength="250"></td>
			</tr> 
			<tr>
				<td><label for="precio">Precio:</label></td>
				<td><input name="precio" type="text" id="precio" value="" size="7" maxlength="6"></td>
			</tr>
			<tr>
				<td><label for="rubro">Rubro:</label></td>
				<td>
					<select name="rubroCobro" id="rubroCobro">
						<c:forEach items="${rubrosCobro}"  var="rubroCobro">
							<option value="${rubroCobro.id}">${rubroCobro.descripcion}</option>
						</c:forEach>
					</select>
				</td>
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
		'descripcion': {'l': 'Descripcion', 'r': true,'mx':250},
		'precio': {'l': 'Precio', 'r': true}
},
o_config = {
		'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>
 