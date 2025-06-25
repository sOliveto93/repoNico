<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(function(){
$("#volver").button()
	.button()
	.click(function(){
		document.volver.submit();
	});
$("#guardar").button()
	.button()
	.click(function(){
		if(document.getElementById("id").value != ''){
			document.edicion.operacion.value = 'actualizar';
		}else{
			document.edicion.operacion.value = 'alta';
		}
		//if (v.exec()){

			document.getElementById("edicion").submit();	
		//}
	});

});

function doOnLoad(){

	myCalendar = new dhtmlXCalendarObject(["fechaIngreso"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
	document.getElementById("apellido").focus();
}

// Para agregar y eliminar Inscripciones
function modificarInscripcion(operacion,idPersona,idCarrera){
	document.formModificarInscripcion.operacion.value = operacion;
	document.formModificarInscripcion.idPersona.value= idPersona;
	document.formModificarInscripcion.idCarrera.value= idCarrera;
	
	document.formModificarInscripcion.submit();	
}

</script>
</head>
<body onload="doOnLoad();">
<form action="PersonaServlet" method="post"  name="formModificarInscripcion" id="formModificarInscripcion">
	<input type="hidden" id="idCarrera" name="idCarrera" value="">
	<input type="hidden" id="idPersona" name="idPersona" value="">
	<input type="hidden" id="operacion" name="operacion" value="">	
</form>
<table id="botones">
	<tr>
		<td>
			<form action="PersonaServlet" method="post" name="guardar">
				<button id="guardar">Guardar</button>
			</form>
		</td>
		<td>
			<form action="PersonaServlet" method="post" name="volver">
				<button id="volver">Volver</button>
				<input type="hidden" name="operacion" value="listar">
			</form>
		</td>
	</tr>
</table>
<form action="PersonaServlet" method="post" name="edicion" id="edicion">

<vel:velocity strictaccess='true'>
#set($carreras = $scopetool.getRequestScope('carreras'))
#set($persona = $scopetool.getRequestScope('persona'))
#set($msg = $scopetool.getRequestScope("msg"))
<input type="hidden" name="cartel" value="$!{msg}" id="cartel" />
	<fieldset><legend>Datos Personales</legend>
		<table id='datosPersonales'>
			<tr>
				<td>Apellido:<input name='apellido' id='apellido' value='$!persona.apellido' size="45"></td>				
				<td>Nombre:<input name='nombre' id='nombre' value='$!persona.nombre' size="45"></td>
			</tr>
			<tr>
				<td>Tipo Documento: <input name='tipoDocumento' id='tipoDocumento' value='$!persona.documento.tipo'></td>
				<td>Numero Documento: <input name='numeroDocumento' id='numeroDocumento' value='$!persona.documento.numero'></td>
				<td>Telefono:<input name='telefono' id='telefono' value='$!persona.telefono.numero'></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset><legend>Domicilio</legend>
		<table>
			<tr>
				<td>Provincia:<input name='provincia' id='provincia' value='$!persona.direccion.pcia'></td>
				<td>Localidad:<input name='localidad' id='localidad' value='$!persona.direccion.localidad'>
				Codigo Postal:<input name='codigoPostal' id='codigoPostal' value='$!persona.direccion.cp' size="9"></td>
			</tr>
			<tr>
				<td>Calle:<input name='calle' id='calle' value='$!persona.direccion.calle' size="50"></td>
				<td>Numero:<input name='numeroCalle' id='numeroCalle' value='$!persona.direccion.numero' size="5">
					Depto:<input name='depto' id='depto' value='$!persona.direccion.depto'size="3">
					Piso:<input name='piso' id='piso' value='$!persona.direccion.piso' size="2"></td>
			</tr>
		</table>
	</fieldset>
	<fieldset><legend>Datos Academicos</legend>
		<table id='datosAcademicos'>
			<tr>
				<td>Numero de Orden: <input name='numeroOrden' id='numeroOrden' value='$!persona.nroOrden'></td>
			</tr>
			<tr> 	
				<td colspan="1">Fecha de Ingreso:<input name='fechaIngreso' id='fechaIngreso' value='$!persona.fechaIngreso'></td>
			</tr>
		</table>
		<fieldset><legend>Carreras</legend>
			<table id='carrerasPersona'>
				<tr>
					<td>
						<ul id='carreraPersona'>
							#foreach($carrera in $carreras)
								#foreach($inscripcion in $persona.inscripciones)
									#if($inscripcion.carrera.id == $carrera.id)
										<li>$!carrera.nombreCarrera <a href="javascript:modificarInscripcion('borrarInscripcion','$persona.id','$carrera.id');">X</a></li>
									#end
								#end
							#end
						</ul>
					</td>
					<td>
						<select id='carreras' name='carreras' size='10' ondblclick="javascript:modificarInscripcion('agregarInscripcion',$persona.id,this[selectedIndex].value);" 
							#if($persona.id) > #else disabled="disabled"> #end
							#foreach($carrera in $carreras)
								<option value='$carrera.id' > $carrera.nombreCarrera </option>								
							#end
						</select>
					</td>
				</tr>
			</table>
		</fieldset>	
	</fieldset>
	<input type="hidden" id="operacion" name="operacion" value="">
	<input type="hidden" id="id" name="id" value="$!persona.id">
</vel:velocity>
</form>
</body>
<script language="JavaScript">
if($('#cartel').val()!="" && $('#cartel').val()!=null){
	window.alert($('#cartel').val());
}
var a_fields = {
		'nombre':{'l': 'Nombre de la persona', 'r': true,'mx':240},
		'apellido':{'l': 'Apellido de la persona', 'r': true,'mx':240},
		'tipoDocumento':{'l': 'Tipo Documento', 'r': true,'mx':240},
		'numeroDocumento':{'l': 'Numero Documento', 'r': true,'mx':240},
		'telefono':{'l': 'Telefono', 'r': true,'mx':240},
		'provincia':{'l': 'Provincia', 'r': true,'mx':240},
		'localidad':{'l': 'Localidad', 'r': true,'mx':240},
		'codigoPostal':{'l': 'Provincia', 'r': true,'mx':240},
		'calle':{'l': 'Provincia', 'r': true,'mx':240},
		'numeroCalle':{'l': 'Provincia', 'r': true,'mx':240},
		'depto':{'l': 'Provincia', 'r': true,'mx':240},
		'piso':{'l': 'Provincia', 'r': true,'mx':240},
		'numeroOrden':{'l': 'Provincia', 'r': true,'mx':240},
		'fechaIngreso':{'l': 'Provincia', 'r': true,'mx':240}		
},
o_config = {
'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>