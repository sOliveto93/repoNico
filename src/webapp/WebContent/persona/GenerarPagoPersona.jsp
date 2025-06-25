<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">
var personaNueva = false;
$(function(){
	$("#fechaCuota").mask("99-99-9999");
	myCalendar = new dhtmlXCalendarObject("fechaCuota");
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
	
    $('#generados').fixheadertable({
        caption   : 'Pagos Generados',
        colratio    : [400,450, 100,100], 
        height      : 180, 
        width       : 1050, 
        zebra       : true
   });
    
   $("#mail").change(function( event) {
	   var idpersona = $("#idpersona").val();
		$.post('PersonaServlet?operacion=actualizarMail&mail='+this.value+'&idPersona='+idpersona,'', function(data){
			persona = data.lista[0];
			if(!isEmpty(persona.msj)){
				var msjSplit = persona.msj.split(" ");
				var idpersonaJson = msjSplit[0];

				console.log(idpersonaJson,"!=",idpersona);
				if(idpersonaJson!=idpersona){
					alert(persona.msj + "   -3");
					event.target.focus();
					event.target.select();
				}
			}else{
				$("#mail").val(persona.mail);
			}
		});

   });

   $("#agregarPagoPersona").button().click(function(event) {
		var nro = $("#nroDocumento").val();
		var m = $("#mail").val();
		var fCuota = $("#fechaCuota").val();
		var mPago = $("#montoPago").val();
		var nombre = $("#nombreCompleto").val();

		if(!isEmpty(nro) && !isEmpty(fCuota) && !isEmpty(m) && !isEmpty(mPago)){
			$("#cuotaPersona").submit();
		}else{
			if(isEmpty(nro)){
				$("#nroDocumento").focus();
				$("#nroDocumento").select();
			}else if(isEmpty(m)){
				$("#mail").focus();
				$("#mail").select();
			}else if(isEmpty(fCuota)){
				$("#fechaCuota").focus();
				$("#fechaCuota").select();
			}else if(isEmpty(mPago)){
				 $("#montoPago").focus();
				 $("#montoPago").select();
			}else if(isEmpty(mPago)){
				 $("#nombreCompleto").focus();
				 $("#nombreCompleto").select();
			}
		}
	});
});

function validarPersona(nroDocumento){
	var tipoDocumento = $("#tipoDocumento").val();
	var nroDocumento = nroDocumento;
	$.post('PersonaServlet?operacion=altaPersonaFactura&numeroDocumento='+nroDocumento+"&tipoDocumento="+tipoDocumento,'', function(data){
		if(data.status == "OK"){
			var persona = data.lista[0];
			if(persona.id){
				mostrarDatosPersona(persona);
			}else{
				personaNueva = true;
			}
		};
	});
}
function mostrarDatosPersona(persona){
	$("#nombreCompleto").val(persona.nombreCompleto);
	if(persona.mail!=null){
		$("#mail").val(persona.mail);
		$("#mail").attr("disabled",true);
	}
	if(persona!="")
		$("#idpersona").val(persona.id);	
}
</script>
<title></title>
</head>
<body>
	<div class="recuadro">
		<%@ include file="/menu.jsp"%>
		<div style="margin:0px auto;text-align:center;">
		<fieldset><legend>Cuota</legend>
			<form name="cuotaPersona" id="cuotaPersona" action="PersonaServlet">
					<c:set var="pagosGenerados" scope="session" value="${pagosGenerados}"/>
					<table id="contenedorDePago" >
						<tr>
							<td>Tipo Documento:</td>
							<td>
								<select name="tipoDocumento" id="tipoDocumento" >
								<option value="0">Seleccione Tipo Documento</option> 
									<c:forEach items="${tiposDocumento}" var="tipoDocumento">
										<option value="${tipoDocumento}"
											<c:if test="${tipoDocumento == 'DN'}">
												selected="selected"
											</c:if>
										>${tipoDocumento}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>Documento:</td>
							<td><input type="text" name="nroDocumento" id="nroDocumento" value="" onblur="javaScript:validarPersona(this.value);"></td>
						</tr>
						<tr>
							<td>Nombre Completo:</td>
							<td><input type="text" name="nombreCompleto" id="nombreCompleto" value=""></td>
						</tr>
						<tr>
							<td>Mail:</td>
							<td><input type="text" name="mail" id="mail" value=""></td>
						</tr>
						<tr>
							<td>Grupo/Carrera:</td>
							<td>
								<select name="grupos" id="grupos">
										<c:forEach items="${gruposUsuario}" var="gusuario">
												<option value="${gusuario.id}" 
													<c:if test="${gusuario.id == 8036}">
														selected="selected"
													</c:if>
												>
													${gusuario.codigo} - ${gusuario.descripcion } - ${gusuario.carrera.nombreCarrera }
												</option>
										</c:forEach>
										<c:set var="grupoSeleccionado" value="${grupo.conceptoUnoMonto}"></c:set>
								</select>
							</td>
						</tr>
						<tr>
							<td>Descripcion Cuota:</td><td><input id="observaciones" name="observaciones" value="" size="60"></td>
						</tr>
						<tr>
							<td>Fecha:</td><td><input id="fechaCuota" name="fechaCuota" value="" ></td>
						</tr>
						<tr>
							<td>Monto:</td><td><input type="text" name="montoPago" id="montoPago" value="${grupoSeleccionado}"></td>
						</tr>
					</table>
				<input type="hidden" name="idpersona" id="idpersona" value="">
				<input type="hidden" name="operacion" id="operacion" value="altaPagoPersona">
			</form>
			<button id="agregarPagoPersona" name="agregarPagoPersona">Agregar</button>
			</fieldset>
		</div>
	</div>

	<c:if test="${pagosGenerados.size()>0}">
		<div class="recuadro" align="center">
				<table id="generados" class="fixheadertable">
					<thead>
						<tr>
							<th>Nombre y Documento</th>
							<th>Descripcion</th>
							<th>Fecha</th>
							<th>Monto</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pagosGenerados}" var="pago">
						<tr>
							<td>${pago.documento} - ${pago.nombre}</td>
							<td>${pago.observaciones}</td>
							<td>${pago.fecha}</td>
							<td>$${pago.monto}</td>
						</tr>
						</c:forEach>
					</tbody> 
				</table>
		</div>
	</c:if>
</body>
</html>