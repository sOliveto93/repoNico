<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">
$(document).ready(function() {

	dialogGrupos(530,530);
	$("#generarInforme").button()
	.click(function(event){
		var usuario = document.getElementById("tipo_usuario").value; 
		var desdeGrupo = document.getElementById("desdeNumeroGrupo").value;
		var hastaGrupo = document.getElementById("hastaNumeroGrupo").value;
		var operacion = document.getElementById("operacion").value ;
		document.getElementById("exportarXls").value = document.getElementById("exportarXls").checked;
		console.log(document.getElementById("exportarXls").value + "--"+ "usuario: ",usuario,"- desdeGrupo: ",desdeGrupo,"- hastaGrupo: ",hastaGrupo,"- operacion: ",operacion );
		document.getElementById("exportarXls").value = document.getElementById("exportarXls").checked;
		
		if((usuario =='DEP'||usuario =='consulta') && !(document.getElementById("nGrupo").value!=''|| (desdeGrupo !='' && hastaGrupo!=''))){
			if(operacion == "reporteUnaPersona"){
				if(document.getElementById("desdeNumeroDocumento").value == ""){
					alert("Seleccione una persona");
					event.preventDefault();
				}else{
					document.getElementById("resumidoCheck").value = document.getElementById("resumido").checked;
					submitForm('informePago');
				}
			}else{
				alert("Elija al menos un grupo o rango de grupos");
				event.preventDefault();
			}
		}else{
			if(operacion!=''){
				if(operacion != "reporteUnaPersona"){
					if(document.getElementById("desdeFechaDePago").value == "" && document.getElementById("desdeFechaDeCuota").value == "" 
																				&& document.getElementById("desdeFechaCargaPago").value == ""){
						alert("Seleccione al menos una fecha de inicio");
						event.preventDefault();
					}else{
						document.getElementById("resumidoCheck").value = document.getElementById("resumido").checked;
						submitForm('informePago');
					}				
				}else{
					if(document.getElementById("desdeNumeroDocumento").value == ""){
						alert("Seleccione una persona");
						event.preventDefault();
					}else{
						document.getElementById("resumidoCheck").value = document.getElementById("resumido").checked;
						submitForm('informePago');
					}
				}
			}else{
				alert("Elija un tipo de REPORTE");
				event.preventDefault();
			}
		}
	});
});
function generarInforme(radioButton){
	var id = radioButton.id ;
	if(id == 'reporteCancelacionesPorGrupoCarrera'){
		$('#hastaGrupo').attr("style","visibility:hidden");
		$('#hastaFechaDePago').attr("style","visibility:visible");
		$('#desdeFechaDePago').attr("style","visibility:visible");
		$('#hastaFechaDeCuota').attr("style","visibility:visible");
		$('#desdeFechaDeCuota').attr("style","visibility:visible");
	}else if(id == 'reporteDeudas' || id == 'reporteDeudasPorGrupoCarrera'){
		$('#hastaGrupo').attr("style","visibility:hidden");
		$('#hastaFechaDePago').attr("style","visibility:hidden");
		$('#desdeFechaDePago').attr("style","visibility:visible");
		$('#hastaFechaDeCuota').attr("style","visibility:visible");
		$('#desdeFechaDeCuota').attr("style","visibility:visible");
	}else if(id == 'reporteUnaPersona'){
		$('#hastaGrupo').attr("style","visibility:hidden");
		$('#hastaFechaDePago').val('');
		$('#desdeFechaDePago').val('');
		$('#hastaFechaDeCuota').val('');
		$('#desdeFechaDeCuota').val('');
		$('#hastaFechaDePago').attr("style","visibility:hidden");
		$('#desdeFechaDePago').attr("style","visibility:hidden");
		$('#hastaFechaDeCuota').attr("style","visibility:hidden");
		$('#desdeFechaDeCuota').attr("style","visibility:hidden");
	}else if (id == 'reporteCancelacionesDeUnPeriodo'){
		$('#hastaGrupo').attr("style","visibility:visible");
		$('#hastaFechaDePago').attr("style","visibility:visible");
		$('#desdeFechaDePago').attr("style","visibility:visible");
		$('#hastaFechaDeCuota').attr("style","visibility:visible");
		$('#desdeFechaDeCuota').attr("style","visibility:visible");
	}
	document.getElementById("operacion").value = id;
}
function doOnLoad(){
	$("#codigoMP").hide();
	myCalendar = new dhtmlXCalendarObject(["desdeFechaDePago","hastaFechaDePago","desdeFechaDeCuota","hastaFechaDeCuota","desdeFechaCargaPago","hastaFechaCargaPago"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
	
	var usuarioTipo = $("#tipo_usuario").val();
	var title = "";
	if(usuarioTipo!="ADMIN"){
		console.log("usr: "+usuarioTipo);
		var usuarioGrupos = limpiarGruposUsuario();
		 title = "Grupos de consulta: " + usuarioGrupos;
	}else{
		console.log("usr: "+usuarioTipo);
		 title = "Grupos de consulta: Todos";
	}
	$("#help").attr( "title",title);
	console.log("help: "+$("#help").attr( "title"));
}

function functionAceptarDesde(){
		$("#nombreGrupo").val($("#gruposDisponibles option:selected").val()) ;
		$("#nGrupo").val($("#gruposDisponibles option:selected").text()) ;
}
function limpiarGruposUsuario(){
	var usuarioGrupos = $("#usuarioCodigoGrupos").val();
	
	usuarioGrupos= usuarioGrupos.replace("[", "");
	usuarioGrupos=usuarioGrupos.replace("]", "");
	usuarioGrupos=usuarioGrupos.split(",");
	for(var i = 0; i < usuarioGrupos.length; i++) {
		usuarioGrupos[i] = usuarioGrupos[i].trim();
	}
	usuarioGrupos=usuarioGrupos.sort();
	 $("#usuarioCodigoGrupos").val(usuarioGrupos);
	return usuarioGrupos;
}
function siEsMercadoPago(id){
	console.log(id);
	if(id==5){
		$("#codigoMP").val("");
		$("#codigoMP").show();
		console.log("show")
	}else{
		$("#codigoMP").val("");
		$("#codigoMP").hide();
		console.log("hide")
	}
}
function verificaGrupo(input){
	var id = input.id;
	var value = input.value;
	var usuarioTipo = $("#tipo_usuario").val();
	var resp = false;
	var usuarioGrupos = limpiarGruposUsuario();
	
	console.log("valueOriginal: ", value);
	value = value.trim();
	console.log("valueTrim: ", value);
	/*for(var i = 0; i < usuarioGrupos.length; i++) {
		usuarioGrupos[i] = usuarioGrupos[i].trim();
	}*/

	if(!isEmpty(value) && (usuarioTipo == "DEP" || usuarioTipo == "consulta")){
		//console.log("CantGrupXusr: ",usuarioGrupos.length);
		for(var i = 0; i < usuarioGrupos.length; i++) {
			if(usuarioGrupos[i] == value){
					resp = true;
			}
		}
		var name = input.name;
		console.log();
		if(resp==false){
			$("#"+id).focus();
			if(usuarioGrupos.length == 1 && usuarioGrupos[0] == ""){
				alert("usuario: " + $("#usuarioNombre").val() + " no tiene grupos asignados");	
			}else if(usuarioGrupos.length == 1 && usuarioGrupos[0] != ""){
				alert("Numero de grupo Incorrecto(Campo: "+ input.name + " ), unico grupo asignado para el usuario ( " + $('#usuarioNombre').val() + " ) " + usuarioGrupos[0]);
			}else if(usuarioGrupos.length > 1 ){
				alert("Numero de grupo Incorrecto(Campo: "+ input.name + " ), debe estar en el rango "+usuarioGrupos[0] + " y " + usuarioGrupos[usuarioGrupos.length-1]);	
			}
			
			if(name.substring(0,5) == "desde"){
				$("#"+id).val(usuarioGrupos[0]);	
			}else if(name.substring(0,5) == "hasta"){
				$("#"+id).val(usuarioGrupos[usuarioGrupos.length-1]);
			}
			$("#"+id).select();
		}
	}
}
</script>
<title></title>
</head>
<body onload="doOnLoad();">
<div class="recuadro">
<%@ include file="/menu.jsp"%>
<div style="margin:0px auto;text-align:center;">
	<c:set var="usuarioSesion" scope="session" value="'${sessionScope.usuario}'"/>
<fieldset ><legend>Informe</legend>
	<table>
		<tr>
       		<td><input type="radio" name="informe" id="reporteCancelacionesDeUnPeriodo" onclick="javascript:generarInforme(this);">Cancelaciones por Periodo, Grupo/Carrera  </td>
       		<td><input type="radio" name="informe" id="reporteDeudas" onclick="javascript:generarInforme(this);">Deudas</td>
       		<td>&nbsp;</td>
    	</tr>
 		<tr>
       		<td><input type="radio" name="informe" id="reporteDeudasPorGrupoCarrera" onclick="javascript:generarInforme(this);">Deudas y Pagos Por Grupo/Carrera</td>
       		<td><input type="radio" name="informe" id="reporteUnaPersona" onclick="javascript:generarInforme(this);">Una Persona</td>
       		<td>&nbsp;</td>
  		</tr>
   		<tr>
   			<td><input type="radio" name="informe" id="reporteCancelacionPorPartida" onclick="javascript:generarInforme(this);">Pagos por Partida</td>
   			<td>&nbsp;</td>
   			<td align="center">
   				<input type="checkbox" id="resumido" name="resumido">Resumido
   			</td>
   		</tr>
  	</table>
</fieldset>
</br>
<fieldset><legend>Filtros</legend>
	<form id="informePago" name="informePago" action="ReporteServlet" method="post" target="_blank">
		<table width="539" border="0">
          <tr>
            <td>&nbsp;</td>
            <td>Desde</td>
            <td>Hasta</td>
          </tr>
          <tr>
            <td>Nro. Cohorte</td>
            <td><input name="desdeCohorte" id="desdeCohorte" value=""></td>
            <td></td>
          </tr>
		  <tr>
            <td>Nro Grupo </td>
            <td>
            	<input type="text" name="desdeNumeroGrupo" id="desdeNumeroGrupo" onblur="javascript:verificaGrupo(this);">            	
            </td>
            <td>
				<input type="text" name="hastaNumeroGrupo" id="hastaNumeroGrupo" onblur="javascript:verificaGrupo(this);">
				<img src="images/help.png" id="help" width="24px" title="${usuarioCodigoGrupos}">           	
            </td>
          </tr>

          <tr>
            <td>Nombre Grupo </td>
            <td colspan=2>
            	<input type="hidden" name="nombreGrupo" id="nombreGrupo" >
            	<input name="nGrupo" id="nGrupo" value="" readonly="readonly" onclick="javascript:windowGrupo(functionAceptarDesde);">
            </td>
          </tr>
          <tr>
            <td>Nro. Documento </td>
            <td><input name="desdeNumeroDocumento" id="desdeNumeroDocumento" value=""></td>
            <td></td>
          </tr>
          <tr>
            <td>Fecha de Pago </td>
            <td><input name="desdeFechaDePago" id="desdeFechaDePago" value=""></td>
            <td><input name="hastaFechaDePago" id="hastaFechaDePago" value=""></td>
          </tr>
          <tr>
            <td>Fecha de Cuota </td>
            <td><input name="desdeFechaDeCuota" id="desdeFechaDeCuota" value=""></td>
            <td><input name="hastaFechaDeCuota" id="hastaFechaDeCuota" value=""></td>
          </tr>
          <tr>
            <td>Fecha Carga Pago </td>
            <td><input name="desdeFechaCargaPago" id="desdeFechaCargaPago" value=""></td>
            <td><input name="hastaFechaCargaPago" id="hastaFechaCargaPago" value=""></td>
          </tr>
        </table>
        Origen Pago:
        <select name="origenPago" id="origenPago" onchange="javascript:siEsMercadoPago(this.value);">
        		<option value="0">Todos</option>
			<c:forEach items="${origenesPago}"  var="origenPago">
				<option value="${origenPago.id}">${origenPago.descripcion}</option>
			</c:forEach>
		</select>
		<div id="codigoMP" >
		    Codigo Mercado Pago:
            <input name="codigoMercadoPago" id="codigoMercadoPago" value=""><br>
            <input type="checkbox" name="exportarXls" id="exportarXls" value="">Exportar a Excel<br>
        </div>
		<input type="hidden" name="operacion" id="operacion" value="">
		<input type="hidden" name="resumidoCheck" id="resumidoCheck" value="">
 		<input type="hidden" name="usuarioNombre" id="usuarioNombre" value='${sessionScope.usuario.usuario}'> 
		<input type="hidden" name="usuarioCodigoGrupos" id="usuarioCodigoGrupos" value='${usuarioCodigoGrupos}'> 
		<input type="hidden" name="tipo_usuario" id="tipo_usuario" value='${sessionScope.usuario.tipo}'>
		
	</form>
	<button id="generarInforme" name="generarInforme">Generar Informe</button>
	</fieldset>
	</div>
</div>
<%@ include file="/grupo/GrupoLista.jsp"%>
</body>
</html>