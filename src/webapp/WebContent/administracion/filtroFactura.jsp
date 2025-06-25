	<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

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
	$("#volver").button()
	.click(function(){

	});
		
	$("#buscar")
	.button()
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
		'<td> '+ factura.descripcionExtendida +'</td> '+
		'<td colspan="2" align="right"> F.Pago: ' + factura.formaDePago + '</td> '+
	'</tr> ';
	$('#cabeceraFactura').html(string);
	var stringItems="";
	var facturaItems = factura.items;
 
	for(i=0;i<facturaItems.length ;i++){
		stringItems = stringItems +
			'<td>' + facturaItems[i].codigo + '</td> ' +
			'<td>' + facturaItems[i].facturaItemTipo.descripcion + '</td> '+
			'<td>' + facturaItems[i].facturaItemTipo.rubroDescripcion + '</td> '+
			'<td>' + facturaItems[i].precio + '</td> '+
			'<td>' + facturaItems[i].facturaItemTipo.rubro + '</td> ' +
			'<td>' + facturaItems[i].facturaItemTipo.codigo + '</td> ';
	}
	$('#datos_facturaItems').html(stringItems);
	$('#dialog-vistaFactura').dialog('open');
}
</script>
</head>
<body onload="doOnLoad();">

<%@ include file="/menu.jsp"%>

<form action="FacturaServlet" method="get" name="edicion" id="edicion">
	<input type="hidden" name="id" id="id" value="">
	<input type="hidden" name="operacion" id="operacion" value="">
</form>

<fieldset><legend>Filtro Facturas</legend>
<form action="FacturaServlet" name="filtro" id="filtro">
	<table id="filtroFacturas">
	  <tr>
	    <td>Numero Factura:
	      <input id="numeroFactura" name="numeroFactura"></input>
		</td>
		<td>
			Fecha desde:<input id="fechaFacturaDesde" name="fechaFacturaDesde">
	      	Hasta:<input id="fechaFacturaHasta" name="fechaFacturaHasta">
		</td>
		<td>
			
		</td>
	</tr>
	<tr>
		<td>Tipo Documento
			<input id="tipoDocumento" name="tipoDocumento"></input>
		</td>
		<td>Numero Documento:
	      <input id="numeroDocumento" name="numeroDocumento"></input>
		</td>
	    <td><button id="buscar">Buscar</button></td>
	  </tr>
	</table>
	<input type="hidden" id="operacion" name="operacion" value="filtrar">
</form>
</fieldset>

<%@ include file="AgregarVolver.jsp"%>

<vel:velocity strictaccess="true">
#set($facturas = $scopetool.getRequestScope("facturas"))
#set($paginaNumero = $scopetool.getRequestScope("paginaNumero"))
	#if($facturas)
		#if($paginaNumero >0)
		 #set($paginaAnterior = $paginaNumero - 1)
		 <div align="left">
			<a href="FacturaServlet?operacion=filtrar&paginaNumero=$paginaAnterior">
				Prev page
			</a>	
		 </div>
		#end
		#set($paginaSiguiente = $paginaNumero + 1)
		<div align="right">
			<a href="FacturaServlet?operacion=filtrar&paginaNumero=$paginaSiguiente">
				Next page
			</a>		
		</div>
	#end
</vel:velocity>
<table id="tablaPersonas" class="fixheadertable">
	<thead>
		<tr>
			<th>Nro. Factura</th>
			<th>Descripcion</th>
			<th>Monto</th>
			<th>Fecha de Pago</th>
			<th>Persona</th>
		</tr>
	</thead>
	<tbody>
	<vel:velocity strictaccess="true">
		#set($facturas = $scopetool.getRequestScope("facturas"))
		#set($paginaNumero = $scopetool.getRequestScope("paginaNumero"))
		#foreach($factura in $facturas)
			#if($factura.nro)
				<tr>
					<td><a href="javascript:verFactura($factura.id);">$factura.nro</a></td>
					<td>$!factura.descripcionBasica</td>
					<td>$!factura.monto</td>
					<td>$!factura.fechaPago</td>
					<td><a href="javascript:verPersona($!factura.persona.id);">$!factura.persona.getNombreCompleto()</a></td>
				</tr>
			 #end
		#end
	</vel:velocity>
	</tbody>
</table>
<vel:velocity strictaccess="true">
#set($personas = $scopetool.getRequestScope("personas"))
#set($paginaNumero = $scopetool.getRequestScope("paginaNumero"))
	#if($personas)
		#if($paginaNumero >0)
		 #set($paginaAnterior = $paginaNumero - 1)
		 <div align="left">
			
			<a href="PersonaServlet?operacion=filtrar&paginaNumero=$paginaAnterior">
				Prev page
			</a>	
		 </div>
		#end

		#set($paginaSiguiente = $paginaNumero + 1)
		<div align="right">
			<a href="PersonaServlet?operacion=filtrar&paginaNumero=$paginaSiguiente">
				Next page
			</a>		
		</div>
	#end
</vel:velocity>

<div id="dialog-vistaFactura" title="Carrera">
	<table id='cabeceraFactura' width="100%">
		<!-- <tbody id="cabeceraFactura"></tbody> -->
	</table>
	<table id='items'>
		<thead  width="100%">
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
</div>


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