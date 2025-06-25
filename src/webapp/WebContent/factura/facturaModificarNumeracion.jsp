<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>

<title>Facturas</title>
</head>
<body onload="doOnLoad();" onKeyDown="javascript:Verificar();">
<div class="recuadro">
<%@ include file="/menu.jsp"%>

<form name="consultaFac" id="consultaFac" action="FacturaServlet" method="post">
	Borrar: <input type="text" id="nroBorrar" name="nroBorrar" value=${nroBorrar}><br>
	<div id="moverNum" displaystyle="display: none">
		Mover numeracion de <input type="text" id="nroMover" name="nroMover" value=${nroMover}> a <input type="text" id="nroNuevo" name="nroNuevo" value=${nroNuevo}><br>
	 </div>
	Desde:<input type="text" id="desdeNro" name="desdeNro" onblur="javascript:copiar(this.id,'hastaNro');" value="${desdeNro}"><br>
	Hasta:<input type="text" id="hastaNro" name="hastaNro" value="${hastaNro}"><br>
	Anulada:<input type="text" id="nroFacAnulada" name="nroFacAnulada" value="${nroFacAnulada}"><br>
	
	<button id="consultar" name="consultar">consultar</button>
	<br>Correr Numeracion:
	<select name="incrementarDecrementarNumeroFactura" id="incrementarNumeroFactura">
		<c:forEach var="i" begin="0" end="2">
			<option value="${i - 1}">${i - 1}</option>
		</c:forEach>
	</select>
	 la numeracion de las facuras.                                           
	 <button id="generarCambioNumeracion" name="generarCambioNumeracion">Generar Cambio</button>
	<input type="hidden" id="operacion" name="operacion" value="consultarNumeroFacuras">
</form>
<table id='facturas'>
	<tr>
		<th>Nro Factura</th>
		<th>fecha</th>
		<th>persona</th>
		<th>Cantidad Item</th>
	</tr>
	<c:forEach items="${facturas}"  var="factura">
		<tr>
			<td>${factura.nro}</td>
			<td>${factura.fechaCarga.getFechaFormateada()}</td>
			<td align="center">${factura.persona.getNombreCompleto()}</td>
			<td align="center">${factura.facturaItems.size()}</td>
		</tr>
	</c:forEach>
</table>
<!-- 
<button id="confirmacionFacturas" name="confirmacionFacturas">Confirma</button>
 -->
</div>
</body>
<script language="JavaScript">
$(function(){
	//$("#generarCambioNumeracion").attr('disabled','disabled');
	$("#consultar")
		.button()
		.click(function(){
		if(verificarMayorMenor()==true){
			$("#consultaFac").submit();
		}	
	});
	
/*
	$("#confirmacionFacturas")
	.button()
	.click(function(){
			var valor = confirm("Confirma las facturas a modificar.");
			//alert(valor);
			if(valor==true){
				alert("entro33");
				$("#generarCambioNumeracion").attr('disabled',false);
				//$("#generarCambioNumeracion").removeAttr();
				//$("#generarCambioNumeracion").removeAttr('disabled');,'enabled'
			}
	});
*/
	$("#generarCambioNumeracion")
		.button()
		.click(function(){
			$("#operacion").val("modificarNumeracionFacturas");
			$("#consultaFac").submit();
		});
	
    $('#facturas').fixheadertable({
        caption   : 'Facturas',
        colratio    : [50, 25,200,25], 
        height      : 200, 
        width       : 695, 
        zebra       : true
   });
	
});
function copiar(idOrigen,idCopia){
	var valOrigen = $("#"+idOrigen).val();
	var valCopia = $("#"+idCopia).val();
	
	if(isEmpty(valCopia)){
		$("#"+idCopia).val(valOrigen);
	}
}
function verificarMayorMenor(){
	var respuesta = false;
	var desde, hasta;
	desde = $("#desdeNro").val();
	hasta = $("#hastaNro").val();
	if((!isEmpty(desde)) && (desde >  hasta)){
		$("#desdeNro").val(hasta);
		$("#hastaNro").val(desde);
		respuesta = true;
	}if(isEmpty(desde) && !isEmpty(hasta)){
		$("#desdeNro").val(hasta);
		respuesta = true;
	}
	return respuesta;
	/*if(isEmpty(desde) && isEmpty(hasta)){
		respuesta = false;
	}*/
}
</script>
