<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<title>Partida</title>
<script type="text/javascript">
	$(function(){
		//dialogoEdicion(380,600);
		$('#tablaPartida').fixheadertable({
	        caption : 'Partida',
	        colratio: [25,300,500], 
	         height : 500, 
	         width  : 900,  
	        zebra   : true
	   });
		$("#dialogGrupoPartida").dialog({
			autoOpen: false,
			height:600,
			width:770,
			modal: true,
			buttons: {
				'Cerrar': function(event) {
					$("#reListar").submit();
					$("#dialogGrupoPartida").dialog('close');
				}
			},
			close: function(){
				allFields.val('').removeClass('ui-state-error');
			}
		});
		
	});
	function eliminarGrupoDePartida(idPartida,idGrupo,desc){
		var nombreTabla = "grupoPartida"+idPartida;
		if(seguroEliminar("el Grupo: " + desc)){
			$.post('PartidaServlet?operacion=eliminarGrupoDePartida&idpartida='+idPartida +'&idgrupo='+idGrupo,'', function(data){
				if(data.status == "OK"){
					$('#'+nombreTabla+' tr[id=' + idGrupo + ']').clone().appendTo("#grupoPartida228");
					$('#'+nombreTabla+' tr[id=' + idGrupo + ']').remove();
				};
			});
		}
	}
	
	function mostrarPartida(facturaItemTipo,operacion){
		var rubroCobro = facturaItemTipo.rubroId;
		if(operacion == "actualizar"){
			$('#descripcion').val(facturaItemTipo.descripcion);
			$('#partida option[value=" + facturaItemTipo.partida.id + "]').attr('selected', 'selected');
			$('#precio').val(facturaItemTipo.precio);
			$('#codigo').val(facturaItemTipo.codigo);
			$("#rubroCobro option[value="+rubroCobro+"]").attr("selected",true);
			$('#id').val(facturaItemTipo.id);
		}
		$('#operacion').val(operacion);
		$('#dialog-form').dialog('open');
	} 
	function openDialogGrupoPartida(id,descripcion){
		var partida = id + " - " + descripcion;
		$("#idpartidaAddGrupo").val(id);
		$("#partidaDiv").html(partida);
		$('#dialogGrupoPartida').dialog('open');
	} 
	function agregarGrupoPartida(idGrupo){
		var idPartida = $("#idpartidaAddGrupo").val();
	/* 	var nombreTabla = "grupoPartida"+idPartida;
		var descripcionGrupo = "descripcionGrupo"+idGrupo;
		console.log($('#'+nombreTabla+' td[id=' + idGrupo + ']').children()); */
		
		if(confirm("Esta seguro de agregar este grupo a la partida?")){
			$.post('PartidaServlet?operacion=agregarGrupoDePartida&idpartida='+idPartida+'&idgrupo='+idGrupo,'', function(data){
				if(data.status == "OK"){
					Alert("Grupo agregado con exito");	
						
				}
			});
		}
	}
</script>
</head>
<body>
<div class="recuadro">
<%@ include file="/menu.jsp"%>

	<form id="reListar" action="PartidaServlet" method="post" name="reListar">
		<input type="hidden" id="operacion" name="operacion" value="listar">
	</form> 
	<input type="hidden" name="cartel" value="${msgError}" id="cartel">

	<c:set var="gruposLista" scope="session" value="${grupos}"/>
	
	<table id="tablaPartida"  class="fixheadertable">
		<thead>
			<tr>
				<th><img src="images/hash.png" width="24px" title="Numero Partida" /></th>
				<th>Descripcion</th>
				<th>Grupos</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${partidas}"  var="partida">
				<tr>
					<td>${partida.id}</td>
					<td width="300">
						<table>
							<tr>
								<td width="300">${partida.descripcion}</td>
								<td align="right">
									<c:if test="${partida.id!=228}">
										<a href="javascript:openDialogGrupoPartida('${partida.id}','${partida.descripcion}');"><img src="images/add.png" width="24px" title="Agregar Grupo a la Partida"></a>
									</c:if>
								</td>
							</tr>
						</table>
					</td>
					<td width="500">
						<table id="grupoPartida${partida.id}">
								<c:forEach items="${grupos}"  var="g">
									<c:if test="${(g.partida.id == partida.id)}">
										<tr id="${g.id}">
											<td id="descripcionGrupo${g.id}" width="500">${g.codigo} - ${g.descripcion}</td>
											<td align="right">
												<c:if test="${partida.id!=228}">
													<a href="javascript:eliminarGrupoDePartida('${partida.id}','${g.id}','${g.descripcion}');" title="Eliminar"><img src="images/delete.png" width="24px" title="Eliminar" /></a>
												</c:if>
											</td>
										</tr>
							 		</c:if>
						 		</c:forEach>
						 </table>  
					 </td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div id="dialogGrupoPartida" title="Partida">
		<table  id="partidaEdicionTabla">
			<tr>
				<td>Partida: </td>
				<td><input type="hidden" value="" id="idpartidaAddGrupo"><div id="partidaDiv" ></div></td>
			</tr>
			<tr>
				<td>Grupos: </td>
				<td>
					<select id="grupoPartida"  size='22' ondblclick="javascript:agregarGrupoPartida(this[selectedIndex].value);">							
						<c:forEach items="${grupos}"  var="g">
							<option value="${g.id}">${g.codigo} - ${g.descripcion}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	</div>
</div>

</div>
</html>

 