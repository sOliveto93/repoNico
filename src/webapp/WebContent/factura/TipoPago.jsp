<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(function(){
	$("#agregar").button()
				.click(function(){
					$("#operacion").val("alta");
					var peso = $('#peso').children('option').length + 1;
					$("#peso").append('<option value='+peso+'>'+peso+'</option>');
					$("#idTipoPago").val('');
					$("#descripcion").val('');
					$("#pesoTh").attr("style","visibility:hidden");
					$("#pesoTd").attr("style","visibility:hidden");					
					$('#dialogTipoPagoModificar').dialog('open');						
		});;
		
	$("#dialogTipoPagoModificar").dialog({
		autoOpen: false,
		height:197,
		width:350,
		modal: true,
		buttons: {
			'Aceptar': function() {
				$("#modificarTipoPagoS").submit();
				$(this).dialog('close');
			},
			Cancel: function(){
				
				$(this).dialog('close');
			}
		},
		close: function(){
			$("#pesoTh").removeAttr("style");
			$("#pesoTd").removeAttr("style");
			allFields.val('').removeClass('ui-state-error');
		}
	});
	
	if($("#usr").val() == "pyro" ||$("#usr").val() == "tiana"){
		$('#tablaTipoPago').fixheadertable({
	        caption   : 'Personas',
	        colratio    : [26, 150, 150,26,26], 
	        height      : 150, 
	        width       : 386, 
	        zebra       : true
	   });
	}else{
		$('#tablaTipoPago').fixheadertable({
	        caption   : 'Personas',
	        colratio    : [26, 150, 150,26], 
	        height      : 150, 
	        width       : 360, 
	        zebra       : true
	   });	
	}
	
});
function modificarTipoPago(id,descripcion,pesoOrdenamiento){
	$("#idTipoPago").val(id);
	$("#descripcion").val(descripcion);
	$("#operacion").val("modificar");
	$("#peso option[value="+pesoOrdenamiento +"]").attr('selected', 'selected');
	$('#dialogTipoPagoModificar').dialog('open');
}
function BorrarTipoPago(id){
	
}
</script>
</head>
<body onload="doOnLoad();" onKeyDown="javascript:Verificar();">
<div class="recuadro">
<%@ include file="/menu.jsp"%>
	<input type="hidden" name="usr" id="usr" value="${usuario.usuario}">
	<table id="tablaTipoPago" class="fixheadertable">
		<thead>
			<tr>
				<th ><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
				<th>Descripcion</th>
				<th>Ordenamiento</th>
				<th><img src="images/pencil.png" width="24px" title="Modificar Tipo Pago"/></th>
				
				<c:if test="${(usuario.usuario == 'tiana') || (usuario.usuario == 'pyro')}">
					<th><img src="images/delete.png" width="24px" title="Borrar Tipo Pago"/></th>
				</c:if> 
			</tr>
		</thead>
		<tbody>
			<c:set var="tipoPagoNumero" value="1"/> 
			<c:forEach items="${tiposPago}"  var="tipoPago">
				<tr>
					<td>${tipoPagoNumero}</td>
					<td>${tipoPago.descripcion}</td>
					<td>
						${tipoPago.pesoOrdenamiento}
					</td>
					<td>
						<c:if test="${usuario.tipo != AUDITORIA}">
							<a href="javascript:modificarTipoPago('${tipoPago.id}','${tipoPago.descripcion}','${tipoPago.pesoOrdenamiento}');">
								<img src="images/pencil.png" width="15px" title="Modificar Tipo Pago"/>
							</a>
						</c:if>
						<c:if test="${usuario.tipo == AUDITORIA}">
							<a href="#">
								<img src="images/pencilGris.png" width="20px" title="Modificar Factura Item Tipo"/>
							</a>
						</c:if>
					</td>
					<c:if test="${(usuario.usuario == 'tiana') || (usuario.usuario == 'pyro')}">
						<td>
							<a href="javascript:BorrarTipoPago(${tipoPago.id});">
								<img src="images/delete.png" width="24px" title="Eliminar Tipo Pago" />
							</a>
						</td>
						
					</c:if> 
				</tr>
				<c:set var="tipoPagoNumero" value="${tipoPagoNumero+ 1}"/>	
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${usuario.tipo != AUDITORIA}">
		<button id="agregar">Agregar<img src="images/add.png" style="" /></button>
	</c:if>
</div>

<div id="dialogTipoPagoModificar" style="display: none">
	<form name="modificarTipoPagoS" id="modificarTipoPagoS"  action="TipoPagoServlet">
		<table id="tableTipoPago">
			<thead>
				<tr>
					<th>Descripcion</th>
					<th id="pesoTh">Peso</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input name="descripcion" id="descripcion"></td>
					<td id="pesoTd">
						<c:set var="endCount" value="${countTipoPago}"></c:set>
						<select id="peso" name="peso">
							<c:forEach var="i" begin="1" end="${endCount}">
		   						<option value="${i}">${i}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" name="operacion" id="operacion">
		<input type="hidden" name="idTipoPago" id="idTipoPago">
	</form>
</div>
</body> 
</html>