<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="idPersona" value=""/>
<table id="cuotasTabla">
	<thead>
		<tr>
			<th>N°</th>
			<th>Fecha Cuota</th>
			<th>Grupo</th>
			<th>Monto Total</th>
			<th>Monto Venc.</th>
			<th>1° Venc.</th>
			<th>2° Venc.</th>
			<th>Observaciones</th>
			<th>Estado Pago</th>
			<th>Pagado</th>
			<th>Fecha Pago</th>
			<th>Tipo</th>
			<th>Origen de Pago</th>
			<th><img src="images/pencil.png" width="24px" title="Modificar Cuota"/></th>
			<th><img src="images/delete.png" width="24px" title="Eliminar Cuota" /></th>
			<th><img src="images/pesosDelete.png" width="24px" title="Anular Pago Cuota" /></th>
		<!-- <th><img src="images/signoPeso2.png" width="24px" title=""/></th>  -->
		</tr>
	</thead>
	<tbody id="cuotas">
		<c:forEach items="${pagos}"  var="pago">
			<c:if test="${pago.estado.valorNumerico!=0}">
				<tr id="${pago.id}">
					<td><c:out value="${pago.numero}"></c:out></td>
					<td><c:out value="${pago.fecha}"></c:out></td>
					<td><c:out value="${pago.grupo.codigo} - ${pago.grupo.descripcion}"></c:out></td>
					<td><c:out value="${pago.monto1 + pago.monto2}"></c:out></td>
					<td><c:out value="${pago.vtoMonto}"></c:out></td>
					<td><c:out value="${pago.grupo.vtoDias}"></c:out></td>				
					<td><c:out value="${pago.grupo.vtoPlus}"></c:out></td>
					<td><c:out value="${pago.observaciones}"></c:out></td>
					<td class="ui-sort hover sortedDown">
						<c:if test="${not empty pago.fechaPgo}">
							<c:out value="Pagado"></c:out>
						</c:if>
						<c:if test="${empty pago.fechaPgo}">
							<c:out value="No"></c:out>
						</c:if>
					</td>
					<td>
						<c:if test="${not empty pago.fechaPgo}">
							<c:out value="${pago.monPgo}"></c:out>
						</c:if>	
					</td>
					<td><c:out value="${pago.fechaPgo}"></c:out></td>
					<td><c:out value="${pago.tipo}"></c:out></td>
					
					<!-- se agrega el origen de pago -->
					<td>
						<c:if test="${not empty pago.fechaPgo}">
							<!-- MERCADO PAGO -->
							<c:if test="${pago.origenPago.id==5}">
								<c:out value="${pago.origenPago.descripcion}"></c:out>  - <c:out value="${pago.codigoTodoPago}"></c:out>
							</c:if>
							<!--  TODO PAGO -->
							<c:if test="${pago.origenPago.id==4}">
								<c:out value="${pago.origenPago.descripcion}"></c:out>  - <c:out value="${pago.codigoTodoPago}"></c:out>
							</c:if>
							<!--   3 PAGOFACIL 	-->
							<c:if test="${pago.origenPago.id==3}">
								<c:out value="${pago.origenPago.descripcion}"></c:out>
							</c:if>
							 <!--  2 VENTANILLA -->
							<c:if test="${pago.origenPago.id==2}">
								<c:out value="${pago.origenPago.descripcion}"></c:out> - <c:out value="${pago.facturaItem.factura.tipoPago.descripcion}"></c:out> - <c:out value="${pago.facturaItem.factura.nro}"></c:out>
							</c:if>
						</c:if>	
					</td>
					
					<td align="center">
						<c:if test="${pago.estado.valor == 'Incierto' || (not empty pago.fechaPgo)}">	
							<img src="images/pencilGris.png" width="15px" title="Cuota Paga"/>
						</c:if>
						<c:if test="${(pago.estado.activado) && (empty pago.fechaPgo)}">
							<img src="images/pencilGris.png" width="15px" title="Modificar Cuota"/>
						</c:if>
					</td>
						<td align="center">
							<img src="images/deleteGris.png" width="15px" title="Operacion deshabilitada" />				
					</td>
					<td>
							<img src="images/pesosDeleteGris.png" width="15px" title="Operacion deshabilitada" />
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</tbody>
</table>
<div style="display:none" id="dialog-cuota"> 
	<form name="edicionCuotaPago" id="edicionCuotaPago" method="post">
		<input type="hidden" name="idCuota" id="idCuota" value="">
		<input type="hidden" name="idPersona" id="idPersona" value="${persona.id}">
		<input type="hidden" name="operacion" id="operacion" value="">
		<fieldset>
		<legend>Datos del pago:</legend>
		<ul>
			<li>
   	   			<label for="monto1">Monto1:</label>
				<input type="text" id="monto1" name="monto1" >
			</li>
			<li>
				<label for="monto2">Monto2:</label>
				<input type="text" id="monto2" name="monto2" >
			</li>
			<li>
				<label for="vtoMonto">Monto Vencimiento:</label>
				<input type="text" id="vtoMonto" name="vtoMonto" >
			</li>
   			<li>
   	   			<label for="fechaPago">Fecha:</label>
				<input type="text" id="fechaPgo" name="fechaPgo" >
			</li>
			<li>
				<label for="monto">Monto Pago:</label>
				<input type="text" id="montoPgo" name="montoPgo" >
			</li>
			<li>
				<label for="tipo">Tipo:</label>
				<select name="pagoTipo" id="idPagoTipo">						
					<option value="E">E</option>
					<option value="M">M</option>						
				</select>
			</li>
			<li>
				<label for="observacionesModificar">Observacion:</label>
				<input type="text" id="observacionesModificar" name="observacionesModificar" >
			</li>
			<li>
				<label for="oringenP">Origen de Pago:</label>
				<select name="origenPagoS" id="origenPagoS">
					<c:forEach items="${origenesPago}"  var="origenPago">
							<option value="${origenPago.id}">${origenPago.descripcion}</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label for="codigoTP">Codigo TodoPago:</label>
				<input type="text" id="codigoTodoPago" name="codigoTodoPago">
			</li>
			<li>
				<label for="fechaPagoC">Fecha Pago Carga:</label>
				<input type="text" id="fechaPagoCarga" name="fechaPagoCarga" disabled="disabled">
			</li>
			<li>
				<label for="ExceptuarCuotaLabel">Exceptuar Cuota:</label>
				<input type="checkbox" id="ExceptuarCuota" name="ExceptuarCuota">
			</li>
		</ul>
		</fieldset>
	</form>
</div>

<c:if test="${(usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN) || (usuario.tipo == ADM)}">
	<table id="botones">
		<tr>
			<td>
				<button name="imprimir" id="imprimir" >Imprimir Chequera</button>
			</td>
		</tr>
	</table>
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
<div id="dialogEliminar" title="Registro Baja" style="display: none">
		Comentario:<input type="text" id="comentarioDelete" name="comentarioDelete">
</div>
<form action="ChequeraServlet" id="formCarrerasPersona" name="formCarrerasPersona">
	<input type="hidden" id="idCarreraChequera" name="idCarreraChequera" value="">
	<input type="hidden" id="idPersonaChequera" name="idPersonaChequera" value="">
	<input type="hidden" id="idSoloImprimir" name="soloImprimir" value="si">
	<input type="hidden" id="idSaltarAFactura" name="saltarAFactura" value="no">
	<input type="hidden" id="operacion" name="operacion" value="generarChequerasPersona">
</form>
<script type="text/javascript">
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

function anulaPagoPersona(idCuota,idPersona){
	if(confirm("Desea borrar el pago de la cuota?")){
		$.post('PersonaServlet?operacion=anularCuotaPersona&idCuota='+idCuota+'&idPersona='+idPersona,'', function(data){
			if(data.status == "OK"){
				var newRow = filaJson(data.lista[0]);		
				$('#'+data.lista[0].id).replaceWith(newRow);
			}
		});
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
function buscaCarreraPersonaCupones(idPersona){
	$("#idPersonaChequera").val(idPersona);
	$.post('PersonaServlet?operacion=ver_persona&id='+idPersona,'', function(data){
		if(data.status == "OK"){
			mostrarCarrerasCupones(data.lista[0].inscripciones);
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
function mostrarCarrerasCupones(inscripciones){
	var string="";
	for(i=0;i<inscripciones.length;i++){
		string = string +  
				'<tr>'+
					'<td>'+
						inscripciones[i].nombreCarrera +
					'</td>'+
					'<td>'+
						'<input type="radio" name="idCarreraRadio" id="idCarreraRadio" value="'+inscripciones[i].idCarrera+'"onclick="javascript:cargarCarreraPersona('+inscripciones[i].idCarrera+')">'+
					'</td>'+
				'</tr>';
		}
	$("#idSoloImprimir").val("no");
	$("#idSaltarAFactura").val("si");
	$("#listaCarreraPersona").show();
	$('#listaCarreraPersona').html(string);
	$('#dialogInscripcionesPersona').dialog('open');
}
function cargarCarreraPersona(idCarrera){
	$("#idCarreraChequera").val(idCarrera);
}
</script>