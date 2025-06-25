 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>

<script type="text/javascript">
	$(function(){
		dialogoEdicion(380,600);
		$("#agregar").click(function(){
				modificar('','','','','','alta');
			});
		
	});
	
	
	$(document).ready(function() {
	    $('#tablaCarreras').fixheadertable({
	         caption   : 'Carreras',
	         colratio    : [26, 350, 350, 80, 80, 26], 
	         height      : 200, 
	         width       : 938, 
	         zebra       : true
	         
	    });
	});
	
	
	function modificar(id,nombre,titulo,vigenciaDesde,vigenciaHasta,operacion){
		$('#nombreCarrera').val(nombre);
		$('#nombreTitulo').val(titulo);
		$('#vigenciaCarreraDesde').val(vigenciaDesde);
		$('#vigenciaCarreraHasta').val(vigenciaHasta);
		$('#id').val(id);
		$('#operacion').val(operacion);
		$('#dialog-form').dialog('open');
	}
	
	function eliminar(id){
		if(seguroEliminar("Carrera")){
			document.edicion.id.value = id;
			document.edicion.operacion.value = 'borrar';
			document.edicion.submit();
		}
	}
	
	var myCalendar;
	function doOnLoad() {
		myCalendar = new dhtmlXCalendarObject(["vigenciaCarreraDesde","vigenciaCarreraHasta"]);
		myCalendar.loadUserLanguage("esp");
		myCalendar.setDateFormat("%d-%m-%Y");
	}
</script>

<title>Carreras</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

</head>

<body onload="doOnLoad();">
<div class="recuadro">

<%@ include file="/menu.jsp"%>

<vel:velocity strictaccess="true">
#set($msgError = $scopetool.getRequestScope("msgError"))
<input type="hidden" name="cartel" value="$!{msgError}" id="cartel">
</vel:velocity>


<table id="tablaCarreras" class="fixheadertable">
	<thead>
	<tr>
		<th ><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
		<th>Carrera</th>
		<th>Titulo</th>
		<th>Vigencia<br>Desde</th>
		<th>Vigencia<br> Hasta</th>
		<th><img src="images/delete.png" width="24px" title="Eliminar Carrera" /></th>
	</tr>
	</thead>
	<tbody>
	<vel:velocity strictaccess="true">
	#set($carreras = $scopetool.getRequestScope("carreras"))
	#set($carreraNumero = 0)
	#foreach($carrera in $carreras)
		#if ($carrera.estado.isActivo())

		#set($carreraNumero = $carreraNumero + 1)
		<tr>
			<td>$carreraNumero</td>
			<td><a href="javascript:modificar('$!carrera.id','$!carrera.nombreCarrera','$!carrera.nombreTitulo','$!carrera.vigencia.fechaDesde','$!carrera.vigencia.fechaHasta','actualizar');">$!carrera.nombreCarrera<a></td>
			<td>$!carrera.nombreTitulo</td>
			<td>$!carrera.vigencia.fechaDesde</td>
			<td>$!carrera.vigencia.fechaHasta</td>
			<td><a href="javascript:eliminar('$carrera.id') " title="Eliminar Carrera">X<a></td>
		</tr>
		#end
	#end	
	</vel:velocity>
	</tbody>
</table>

<%@ include file="AgregarVolver.jsp"%>

<div id="dialog-form" title="Carrera">
	<form action="CarreraServlet" method="post" name="edicion" id="edicion">
		<table  border="0">
			<tr>
				<td><label for="nombreCarrera">Nombre Carrera : </label></td>
				<td><input name="nombreCarrera" type="text" id="nombreCarrera" value="" size="50" maxlength="250"></td>
			</tr>
			<tr>
				<td><label for="nombreTitulo">Nombre Titulo:</label></td>
				<td><input name="nombreTitulo" type="text" id="nombreTitulo" value="" size="50" maxlength="250"></td>
			</tr>
			<tr>
				<td colspan="2"><p><b>Vigencia Carrera:</b></p></td>
			</tr>
			<tr>
				<td><label for="vigenciaCarreraDesde">Desde:</label></td>
				<td><input name="vigenciaCarreraDesde" type="text" id="vigenciaCarreraDesde" value="" size="15" maxlength="10"></td>
			</tr>
			<tr>
				<td><label for="vigenciaCarreraHasta">Hasta:</label></td>
				<td><input name="vigenciaCarreraHasta" type="text" class="dhxform_textarea" id="vigenciaCarreraHasta" size="15" maxlength="10">	</td>
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
		'nombreCarrera': {'l': 'Nombre Carrera', 'r': true,'mx':250},
		'nombreTitulo': {'l': 'Nombre Titulo', 'r': true,'mx':250},
		'vigenciaCarreraDesde': {'l': 'Vigencia Carrera Desde', 'r': true,'f':'date'},
		'vigenciaCarreraHasta': {'l': 'Vigencia Carrera Hasta',  'r': true,'f':'date'}
},
o_config = {
		'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>
 