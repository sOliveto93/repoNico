<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<LINK rel="stylesheet" type="text/css" media="print" href="css/impresion_factura.css" />
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(document).ready(function() { 
    $("#carrera").change(function() {
    	document.filtro.carreraNombre.value = $('#carrera option:selected').html();
    });
    
    $(function($){
		   $("#fechaFacturaDesde").mask("99-99-9999");
		   $("#fechaFacturaHasta").mask("99-99-9999");
	});
});
$(function(){

    
	dialogoVista(350,"100%","dialog-vistaPersona");
	dialogoVista(350,"100%","dialog-vistaFactura");
	//dialogoCarrera(550,900);
	$("#dialog-cambiarTipoImpresion").dialog({
		autoOpen: false,
		height:250,
		width:400,
		modal: true,
		buttons: {
			'Grabar': function() {
				var idTipoImpresionPredeterminado = $("#idTipoImpresionPredeterminado").val();
				$("#tipoImpresionForm").submit();
				
				//$.post('FacturaServlet?operacion=setearTipoImpresion&idTipoImpresionPredeterminado='+idTipoImpresionPredeterminado);
			},
			Cancel: function(){
				//limpiarForm('edicion');
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
	
	
	
	$("#cambiarTipoImpresion")
	.button()
	.click(function(){
		$('#dialog-cambiarTipoImpresion').dialog('open');
	});	
	$("#imprimir")
		.button()
		.click(function(){
			window.print();
	});
  $('#tablaFacturas').fixheadertable({
	    caption   : 'Facturas',
	    colratio    : [50, 300,50,150,200,250,35,35,35], 
	    height      : 350, 
	    //width       : 710, 
	    zebra       : true
	});
    $("#buscar")
	.button()
	.click(function(){
		
	});
	$("#volver").button()
	.click(function(){
	});	
	
	$("#agregar")
	.button()
	.click(function(){
		document.edicion.operacion.value = 'actualizar';
		document.edicion.submit();
	});
});

function doOnLoad(){
	myCalendar = new dhtmlXCalendarObject(["fechaFacturaDesde","fechaFacturaHasta"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
}
function verPersona(idPersona){
	$.post('FacturaServlet?operacion=verPersonaJson&idPersona='+idPersona,'', function(data){
		if(data.status == "OK"){
			mostrarDatosPersona(data.lista[0]);
		};
	});
}
function mostrarDatosPersona(persona){
	$('#apellido').val(persona.apellido);
	$('#nombre').val(persona.nombre);
	$('#tipoDocumentoDialog').val(persona.tipoDocumento);
	$('#numeroDocumentoDialog').val(persona.numeroDocumento);
	$('#telefono').val(persona.telefono);
	$('#provincia').val(persona.direccion.provincia);
	$('#localidad').val(persona.direccion.localidad);
	$('#codigoPostal').val(persona.direccion.cp);
	$('#calle').val(persona.direccion.calle);
	$('#numero').val(persona.direccion.numero);
	$('#depto').val(persona.direccion.depto);
	$('#piso').val(persona.direccion.peso);
	$('#dialog-vistaPersona').dialog('open');
}
function verFactura(idFactura){
	$.post('FacturaServlet?operacion=verFacturaJson&idFactura='+idFactura,'', function(data){
		if(data.status == "OK"){
			mostrarDatosFactura(data.lista[0]);
		};
	});
}
function mostrarDatosFactura(factura){
	var string = "";
	/*
	string = string +
	'<tr> '+
		'<td width="20%"> Tipo Documento:'+ factura.persona.tipoDocumento +'</td> '+
		'<td width="20%"> Nro Documento:'+ factura.persona.numeroDocumento +'</td> '+
		'<td width="20%"> Nro Factura:'+ factura.numero + '</td>'+
		'<td width="40%"> Fecha:'+ factura.fechaPago + '</td>'+
	'</tr> '+
	'<tr> '+
		'<td> Detalle:'+ factura.descripcionBasica + '</td> '+
		'<td colspan="3" align="right">' + 'Sucursal:'+ factura.sucursal + '</td> '+
	'</tr> '+
	'<tr> '+
		'<td> '+ factura.carrerasActiva +'</td> '+
		'<td colspan="2" align="right"> F.Pago: ' + factura.formaPago.descripcion + '</td> '+
	'</tr> ';
	$('#cabeceraFactura').html(string);
	var stringItems="";
	var facturaItems = factura.items;
 
	for(i=0;i<facturaItems.length ;i++){
		stringItems = stringItems +
			'<tr><td>' + facturaItems[i].codigo + '</td> ' +
			'<td>' + facturaItems[i].facturaItemTipo.descripcion + '</td> '+
			'<td>' + facturaItems[i].facturaItemTipo.rubroDescripcion + '</td> '+
			'<td>' + facturaItems[i].precio + '</td> '+
			'<td>' + facturaItems[i].facturaItemTipo.rubro + '</td> ' +
			'<td>' + facturaItems[i].facturaItemTipo.codigo + '</td></tr> ';
	}
	$('#datos_facturaItems').html(stringItems);
	*/
	
	$('#dialog-vistaFactura').dialog('open');
}
function eliminarFactura(idFactura){
	if(seguroEliminar('Factura')){
		document.edicion.id.value = idFactura;
		document.edicion.operacion.value = 'borrar';
		$("#edicion").attr("action","FacturaServlet");		
		document.edicion.submit();
	}
}
function validarCamposDocumento(){
	numeroDocumento = $("#numeroDocumento").val();
	if(numeroDocumento == null || numeroDocumento == ""){
		$("#tipoDocumento").val("");
	}
}
function predeterminar(idTipoImpresion){
	console.log("idTipoImpresion: "+idTipoImpresion);
	if(!isEmpty(idTipoImpresion)){
		$("#idTipoImpresionPredeterminado").val(idTipoImpresion);	
	}
}
</script>
</head>
<body onload="doOnLoad();">
<DIV class="sin_block">
<div class="recuadro">
<%@ include file="/menu.jsp"%>

<form action="FacturaServlet" method="get" name="edicion" id="edicion">
	<input type="hidden" name="id" id="id" value="">
	<input type="hidden" name="operacion" id="operacion" value="">
</form>
<c:set var="session" value="<%=session%>"/>

<fieldset><legend>Filtro Facturas</legend>
<form action="FacturaServlet" name="filtro" id="filtro">
	<table id="filtroFacturas">
	  <tr>
	    <td>Numero Factura:
	      <input id="numeroFactura" name="numeroFactura" value="${sessionScope.facturaBuscada.nro}"></input>
		</td>
		<td colspan="3">
			Fecha desde:<input id="fechaFacturaDesde" name="fechaFacturaDesde" value="${sessionScope.fechaDesdeBuscada}" >
	      	Hasta:<input id="fechaFacturaHasta" name="fechaFacturaHasta" value="${sessionScope.fechaHastaBuscada}" onfocus="javaScript:copiarValor('fechaFacturaDesde','fechaFacturaHasta');">
		</td>
	</tr>
	<tr>
		<td>Tipo Documento
			<input id="tipoDocumento" name="tipoDocumento" value="${sessionScope.personaBuscada.documento.tipo.abreviacion}"></input>
		</td>
		<td>Numero Documento:
			<input id="numeroDocumento" name="numeroDocumento" value="${sessionScope.personaBuscada.documento.numero}" onblur="javaScript:validarCamposDocumento();"></input>
		</td>
		<td>Tipo de Pago:
			<select name="tipoPago" id="tipoPago">
			<c:forEach items="${tiposPago}"  var="tipoPago">
				<option value="${tipoPago.id}"
					<c:if test="${factura.tipoPago.id==tipoPago.id}">
						selected="selected"
					</c:if>
					>${tipoPago.descripcion}</option>
			</c:forEach>
			</select>
	  	</td>
	    <td><button id="buscar">Buscar</button></td>
	  </tr>
	  <tr>
	  	<td>Facturas Anuladas:
			<input type="checkbox" name="facturasAnuladas" id="facturasAnuladas">
		</td>
	  </tr>
	</table>
	<input type="hidden" id="operacion" name="operacion" value="filtrar">
</form>
</fieldset>
<%@ include file="/administracion/AgregarVolver.jsp"%>

<c:set var="PADE" value="tiana"></c:set>
<c:set var="FORASTIER" value="pyro"></c:set>
<c:if test="${(usuario.usuario == PADE)||(usuario.usuario == FORASTIER)}">
	<br><button id="cambiarTipoImpresion">Cambiar Impresion</button>
</c:if>

<c:set var="facturas" value="${facturas}"/>
<c:set var="paginaNumero" value="${paginaNumero}"/>

<c:if test="${paginaNumero > 0}">
	<c:set var="paginaAnterior" value="${paginaNumero - 1}"/>
	<div align="left">
		<a href="FacturaServlet?operacion=filtrar&paginaNumero=${paginaAnterior}&facturasAnuladas=${facturasAnuladas}&salto=${salto}">
			Prev page
		</a>
	</div>
</c:if>
<c:set var="paginaSiguiente" value="${paginaNumero + 1}"/>
<div align="right">
	<a href="FacturaServlet?operacion=filtrar&paginaNumero=${paginaSiguiente}&facturasAnuladas=${facturasAnuladas}&salto=${salto}">
		Next page
	</a>
</div>
<table id="tablaFacturas" class="fixheadertable">
	<thead>
		<tr>
			<th>Nro. Factura</th>
			<th>Descripcion</th>
			<th>Monto</th>
			<th>Fecha de Pago</th>
			<th>Persona</th>
			<th>Carrera/s</th>
			<th><img src="images/invoice.png" height="24px" / title="Ver Factura" /></th>
			<th><img src="images/people.png" height="24px" title="Ver Persona" /></th>
			<th><img src="images/delete.png" width="24px" title="Eliminar Factura" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${facturas}"  var="factura">
				<tr>
					<td>${factura.nro}</td>
					<c:set var="itemCount" value="0"/>
					<td>
						<c:forEach items="${factura.facturaItems}"  var="item">
							<c:if test="itemCount==1"><br></c:if>
							${item.facturaItemTipo.descripcion}
							<c:set var="itemCount" value="1"/>
						</c:forEach>
					</td>
					<td>${factura.monto}</td>
					<td>${factura.fechaPago}</td>
					<td>${factura.persona.nombreCompleto}</td>
					<td>
						${factura.persona.carrerasActiva}
					</td>
					<td><a href="javascript:verFactura(${factura.id});"><img src="images/invoice.png" height="15px" title="Ver Factura Numero: ${factura.nro}"/></a></td>
					<td><a href="javascript:verPersona(${factura.persona.id});"><img src="images/people.png" height="15px" title="Ver Persona: ${factura.persona.nombreCompleto}"/></a></td>
					<td>
						<c:if test="${factura.estado.activado}">
							<a href="javascript:eliminarFactura(${factura.id});">
								<img src="images/delete.png" width="15px" title="Eliminar Factura Numero: ${factura.nro}" />
							</a>
						</c:if>
						<c:if test="${not factura.estado.activado}">
							<img src="images/deleteGris.png" width="15px" title="Factura Eliminada" />
						</c:if>	
					</td>
				</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${paginaNumero > 0}">
 	<c:set var="paginaAnterior" value="${paginaNumero - 1}"/>
	 <div align="left">
		<a href="FacturaServlet?operacion=filtrar&paginaNumero=${paginaAnterior}&facturasAnuladas=${facturasAnuladas}&salto=${salto}">
			Prev page
		</a>	
	 </div>
 </c:if>
<c:set var="paginaSiguiente" value="${paginaNumero + 1}"/>
<div align="right">
	<a href="FacturaServlet?operacion=filtrar&paginaNumero=${paginaSiguiente}&facturasAnuladas=${facturasAnuladas}&salto=${salto}">
		Next page
	</a>		
</div>
</div>
<div class='print_block'>

<div id="dialog-cambiarTipoImpresion" title="Cambiar Tipo Impresion">
	<form name="tipoImpresionForm" id="tipoImpresionForm"  action="FacturaServlet">
	<c:set var="pre" value=""/>
	<table id='tipoImpresion'>
		<thead>
			<tr>
				<th>#</th>
				<th>Descripcion</th>
				<th>Predeterminado</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tiposDeImpresion}"  var="tipoImpresion">
				<tr>
					<td>${tipoImpresion.id}</td>
					<td>${tipoImpresion.descripcion}</td>
					<td><input onchange="javaScript:predeterminar(${tipoImpresion.id});" id="predeterminado"  name="predeterminado" type="radio"
						 <c:if test="${tipoImpresion.predeterminado==true}">
						 checked="checked"
						 <c:set var="pre" value="${tipoImpresion.id}"/>	
						 </c:if>>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<input type="hidden" id="idTipoImpresionPredeterminado" name="idTipoImpresionPredeterminado" value="${pre}">
	<input type="hidden" id="operacion" name="operacion" value="setearTipoImpresion">
	
	</form>
</div>

<div id="dialog-vistaFactura" title="Factura">
	<DIV class="sin_block">
			<button id="imprimir" name="operacion" >Imprimir</button>
	</DIV>
	<!-- 
	<table id='cabeceraFactura' width="80%">
		 <tbody id="cabeceraFactura"></tbody>
	</table>
	<table id='items'>
		<thead  width="80%">
			<tr>
				<th width="5%">R.</th>
				<th width="40%">Concepto</th>
				<th width="40%">Descripcion</th>
				<th width="5%">Pr.Unitario</th>
				<th width="5%">Rubro</th>	
				<th width="5%">Codigo</th>
			</tr>
		</thead>
		<tbody  id="datos_facturaItems" align="center"></tbody>
	</table>
	 -->
</div>
</div>
<DIV class="sin_block">
<div id="dialog-vistaPersona" title="Persona">
	<form name="vista" id="vista">
		<fieldset><legend>Datos Personales</legend>
			<table id='datosPersonales'>
				<tr>
					<td >Apellido:<input name='apellido' id='apellido' value='' size="45" class="nobord"></td>				
					<td>Nombre:<input name='nombre' id='nombre' value='' size="45" class="nobord"></td>
				</tr>
				<tr>
					<td>Tipo Documento: <input name='tipoDocumentoDialog' id='tipoDocumentoDialog' value='' class="nobord"></td>
					<td>Numero Documento: <input name='numeroDocumentoDialog' id='numeroDocumentoDialog' value='' class="nobord"></td>
					<td>Telefono:<input name='telefono' id='telefono' value='' class="nobord"></td>
				</tr>
			</table>
		</fieldset>	
		<fieldset><legend>Domicilio</legend>
			<table>
				<tr>
					<td>Provincia:<input name='provincia' id='provincia' value='' class="nobord"></td>
					<td>Localidad:<input name='localidad' id='localidad' value='' class="nobord">
					Codigo Postal:<input name='codigoPostal' id='codigoPostal' value='' size="9" class="nobord"></td>
				</tr>
				<tr>
					<td>Calle:<input name='calle' id='calle' value='' size="50" class="nobord"></td>
					<td>Numero:<input name='numeroCalle' id='numeroCalle' value='' size="5" class="nobord">
						Depto:<input name='depto' id='depto' value=''size="3" class="nobord">
						Piso:<input name='piso' id='piso' value='' size="2" class="nobord"></td>
				</tr>
			</table>
		</fieldset>
	</form>
</div>
</div>
</div>
</body>
<script language="JavaScript">

var a_fields = {
		'nombre':{'l': 'Nombre de la persona', 'r': true,'mx':240},
		'apellido':{'l': 'Apellido de la persona', 'r': true,'mx':240}
},
o_config = {
'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);


</script>
</html>