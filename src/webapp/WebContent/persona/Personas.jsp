<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
var usuarioGlobalTipoUsuario;
$(function(){
	document.getElementById("apellido").focus();	

	dialogoGenerico(550,900,"dialog-InformesPago","informePago");

	funcionActualizar('edicion');

	$("#volver").button()
	.click(function(){
	});
		
	$("#agregar")
	.button()
	.click(function(){
		document.edicion.operacion.value = 'mantenimiento_persona';
		document.edicion.submit();
	});

	$("#dialogInscripcionesPersona").dialog({
		autoOpen: false,
		height:260,
		width:350,
		modal: true,
		buttons: {
			'Aceptar': function() {
				if(!$.isEmptyObject($("#idCarreraChequera").val())){
					$("#idCarreraChequera").val();
					$("#formCarrerasPersona").submit();
					$(this).dialog('close');
				}
			},
			Cancel: function(){
				$("#idCarreraChequera").val('');
				$("#idPersonaChequera").val(''); 
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
    $("#carrera").change(function() {
    	document.filtro.carreraNombre.value = $('#carrera option:selected').html();
    });
    $('#tablaPersonas').fixheadertable({
        caption   : 'Personas',
        colratio    : [26, 320, 320,50,80,80,26,26,26,26], 
        height      : 200, 
        width       : 995, 
        zebra       : true
   });
});

function modificar(id,operacion){
	$.post('PersonaServlet?operacion=ver_persona&id='+id,'', function(data){
		if(data.status == "OK"){
			tablaPersona(data.lista[0],id,operacion);
		}
	});
}

function eliminar(id){
	if(seguroEliminar('Persona')){
		document.edicion.id.value = id;
		document.edicion.operacion.value = 'borrar';
		document.edicion.submit();
	}	
}

function buscaCarreraPersona(idPersona){
	$("#idPersonaChequera").val(idPersona);
	$.post('PersonaServlet?operacion=ver_persona&id='+idPersona,'', function(data){
		if(data.status == "OK"){
			mostrarCarreras(data.lista[0].inscripciones);
		}
	});
}
function mostrarCarreras(inscripciones){
	var string="";
	for(i=0;i<inscripciones.length;i++){
			console.log(" usuarioGlobalTipoUsuario: ",usuarioGlobalTipoUsuario, " - tipoCarrera: "+inscripciones[i].tipoCarrera, " - " +inscripciones[i].nombreCarrera);
			if( (usuarioGlobalTipoUsuario == "DEP_ADMIN") && (inscripciones[i].tipoCarrera == "dep") ){
				string = string +  
				'<tr>'+
					'<td>'+
						inscripciones[i].nombreCarrera +
					'</td>'+
					 '<td>'+
					'<input type="radio" name="idCarreraRadio" id="idCarreraRadio" value="'+inscripciones[i].idCarrera+'"onclick="javascript:cargarCarreraPersona('+inscripciones[i].idCarrera+')">'+
				'</td>';
			}
			if( (usuarioGlobalTipoUsuario == "DRC_ADMIN") && (inscripciones[i].tipoCarrera == "driyc") ){
				string = string +  
				'<tr>'+
					'<td>'+
						inscripciones[i].nombreCarrera +
					'</td>'+
					 '<td>'+
					'<input type="radio" name="idCarreraRadio" id="idCarreraRadio" value="'+inscripciones[i].idCarrera+'"onclick="javascript:cargarCarreraPersona('+inscripciones[i].idCarrera+')">'+
				'</td>';
			}
			if(usuarioGlobalTipoUsuario == "admin" ){
				string = string +  
				'<tr>'+
					'<td>'+
						inscripciones[i].nombreCarrera +
					'</td>'+
					 '<td>'+
					'<input type="radio" name="idCarreraRadio" id="idCarreraRadio" value="'+inscripciones[i].idCarrera+'"onclick="javascript:cargarCarreraPersona('+inscripciones[i].idCarrera+')">'+
				'</td>';
			}
		'</tr>';
}
	$("#listaCarreraPersona").show();
	$('#listaCarreraPersona').html(string);
	$('#dialogInscripcionesPersona').dialog('open');
}
function cargarCarreraPersona(idCarrera){
	$("#idCarreraChequera").val(idCarrera);
	$("#soloImprimir").val('no');
}
function mantenimientoPersona(idPersona){
	$("#operacion").val("mantenimiento_persona");
	$("#id").val(idPersona);
	$("#edicion").submit();
}
function loadUser(usuario){
	usuarioGlobalTipoUsuario = usuario;
	//alert("usuarioGlobal:" + usuarioGlobal.TipoUsuario);
}
</script>
</head>
<body onload="javascript:loadUser('${usuario.tipo}');">
	<div class="recuadro">
		<%@ include file="/menu.jsp"%>
		
		<form action="PersonaServlet" method="get" name="edicion" id="edicion">
			<input type="hidden" name="id" id="id" value="">
			<input type="hidden" name="operacion" id="operacion" value="">
		</form>
		
		<c:set var="salto" value="/persona/Personas.jsp"/>
		<%@ include file="PersonaFiltro.jsp"%>
		
		<c:if test="${usuario.tipo == ADM}">
			<%@ include file="/administracion/AgregarVolver.jsp"%>
		</c:if>
		
		<c:set var="personas" value="${personas}"/>
		<c:set var="paginaNumero" value="${paginaNumero}"/>
		<c:if test="${empty personas}">
			<c:if test="${paginaNumero > 0}">
			 	<c:set var="paginaAnterior" value="${paginaNumero - 1}"/>
			 </c:if>
			 <div align="left">
				<a href="PersonaServlet?operacion=filtrar&paginaNumero=${paginaAnterior}&salto=${salto}">
					Prev page
				</a>	
			 </div>
			<c:set var="paginaSiguiente" value="${paginaNumero + 1}"/>
			<div align="right">
				<a href="PersonaServlet?operacion=filtrar&paginaNumero=${paginaSiguiente}&salto=${salto}">
					Next page
				</a>		
			</div>
		</c:if>

		<table id="tablaPersonas" class="fixheadertable">
			<thead>
				<tr>
					<th ><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
					<th>Nombre</th>
					<th>Apellido</th>
					<th>Tipo. Doc.</th>
					<th>Documento</th>
					<th>Fec. Ingreso</th>
					<th><img src="images/pencil.png" width="24px" title="Modificar datos personales"/></th>
					<th><img src="images/check.png" width="24px" title="Generar Chequera"/></th>
					<th><img src="images/signoPeso2.png" width="24px" title="Modificar Cuotas"/></th>
					<th><img src="images/delete.png" width="24px" title="Eliminar Persona" /></th>
				</tr>
			</thead>
			<tbody id="personasTbody">
			
			<c:set var="personaNumero" value="${paginaNumero * 300}"/>
				<c:forEach items="${personas}"  var="persona">
					<c:if test="${persona.estado.activado}"> 
						<tr>
							<td>${personaNumero}</td>
							<td>
								<c:if test="${not empty persona.nombre}">
									${persona.nombre} 
								</c:if>
								<c:if test="${empty persona.nombre}">
									Sin Nombre
								</c:if>
							</td>
							<td>${persona.apellido}</td>
							<td>${persona.documento.tipo.abreviacion}</td>
							<td>${persona.documento.numero}</td>
							<td>${persona.fechaIngreso}</td>
							<c:if test="${(usuario.tipo == ADM)}">
								<td><a href="javascript:mantenimientoPersona('${persona.id}');"><img src="images/pencil.png" width="15px"/ title="Modificar datos personales (${persona.nombre})"></a></td>
								<td><a href="javascript:buscaCarreraPersona('${persona.id}');"><img src="images/check.png" width="15px" title="Generar Chequeras"/></a></td>
								<td><a href="PersonaServlet?operacion=cuotasPersona&id=${persona.id}"><img src="images/signoPeso2.png" width="15px" title="Modificar Cuotas"/></a></td>
								<td><a href="javascript:eliminar('${persona.id }');"><img src="images/delete.png" width="15px" title="Eliminar ${persona.nombre}"/></a></td>
							</c:if>
							<c:if test="${(usuario.tipo == AUDITORIA)}">
								<td><a href="javascript:mantenimientoPersona('${persona.id}');"><img src="images/pencil.png" width="15px"/ title="Modificar datos personales (${persona.nombre})"></a></td>
								<td><a href="#"><img src="images/checkGris.png" width="15px" title="Operacion no disponibles"/></a></td>
								<td><a href="PersonaServlet?operacion=cuotasPersona&id=${persona.id}"><img src="images/signoPeso2.png" width="15px" title="Ver Cuotas"/></a></td>
								<td><a href="#"><img src="images/deleteGris.png" width="15px" title="Operacion no disponible"/></a></td>
							</c:if>
							<c:if test="${(usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN)  }">
								<td><a href="javascript:mantenimientoPersona('${persona.id}');"><img src="images/pencil.png" width="15px"/ title="Modificar datos personales (${persona.nombre})"></a></td>
								<td><a href="javascript:buscaCarreraPersona('${persona.id}');"><img src="images/check.png" width="15px" title="Generar Chequeras"/></a></td>
								<td><a href="PersonaServlet?operacion=cuotasPersona&id=${persona.id}"><img src="images/signoPeso2.png" width="15px" title="Modificar Cuotas"/></a></td>
								<td><a href="#"><img src="images/deleteGris.png" width="15px" title="Operacion no disponible"/></a></td>
							</c:if>
							<c:if test="${(usuario.tipo == CONSULTA)}">
								<td><a href="javascript:mantenimientoPersona('${persona.id}');"><img src="images/pencil.png" width="15px"/ title="Modificar datos personales (${persona.nombre})"></a></td>
								<td><img src="images/checkGris.png" width="15px" title="Operacion no disponibles"/></td>
								<td><a href="PersonaServlet?operacion=cuotasPersona&id=${persona.id}"><img src="images/signoPeso2.png" width="15px" title="Modificar Cuotas"/></a></td>
								<td><a href="#"><img src="images/deleteGris.png" width="15px" title="Operacion no disponible"/></a></td>
							</c:if>
						</tr>
						<c:set var="personaNumero" value="${personaNumero + 1}"/>
					</c:if>				
				</c:forEach>
			</tbody>
		</table>
		<c:if test="${not empty personas}">
			<c:if test="${paginaNumero > 0}">
			 	<c:set var="paginaAnterior" value="${paginaNumero - 1}"/>
			 </c:if>
			 <div align="left">
				<a href="PersonaServlet?operacion=filtrar&paginaNumero=${paginaAnterior}&salto=${salto}">
					Prev page
				</a>
			 </div>
			<c:set var="paginaSiguiente" value="${paginaNumero + 1}"/>
			<div align="right">
				<a href="PersonaServlet?operacion=filtrar&paginaNumero=${paginaSiguiente}&salto=${salto}">
					Next page
				</a>
			</div>
		</c:if>
		<div id="dialogInscripcionesPersona" style="display: none">
			<table id="tableCarrerasPersona">
				<thead>
					<tr>
						<th>
							Nombre Carrera
						</th>
					</tr>
				</thead>
				<tbody id="listaCarreraPersona"></tbody>
			</table>
		</div>
		<form action="ChequeraServlet" id="formCarrerasPersona" name="formCarrerasPersona">
			<input type="hidden" id="idCarreraChequera" name="idCarreraChequera" value="">
			<input type="hidden" id="idPersonaChequera" name="idPersonaChequera" value="">
			<input type="hidden" id="soloImprimir" name="soloImprimir" value="no">			
			<input type="hidden" id="operacion" name="operacion" value="generarChequerasPersona">
		</form>
	</div>
</body>
<script language="JavaScript">
var a_fields = {
		'idCarreraChequera':{'l': 'Seleccione una Carrera', 'r': true}
},
o_config = {
'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('formCarrerasPersona', a_fields, o_config);
</script>
</html>
