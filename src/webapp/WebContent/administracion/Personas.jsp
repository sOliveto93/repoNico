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
    
    $('#tablaPersonas').fixheadertable({
        caption   : 'Personas',
        colratio    : [26, 320, 320,50,80, 80, 26], 
        height      : 200, 
        width       : 938, 
        zebra       : true
        
   });
});
$(function(){
	document.getElementById("apellido").focus();
	dialogoCarrera(550,900);
	funcionActualizar('edicion');
	$("#volver").button()
	.click(function(){

	});
		
	$("#buscar")
	.button()
	.click(function(){
	//		document.edicion.operacion.value = 'mantenimiento_persona';
	//		document.getElementById("edicion").submit();
	});
	
	$("#agregar")
	.button()
	.click(function(){
		document.edicion.operacion.value = 'mantenimiento_persona';
		document.edicion.submit();
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
function dialogoCarrera(alto,ancho){
	$("#dialog-carrera").dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Aceptar': function() {
				document.filtro.idCarrera.value = document.windowCarrera.carrera.value;
				$(this).dialog('close');
			},
			Cancel: function(){
				limpiarForm('edicion');
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
}
function windowCarreras(){
	$('#dialog-carrera').dialog('open');
}
</script>
</head>
<body onload="doOnLoad();">

<%@ include file="/menu.jsp"%>

<form action="PersonaServlet" method="get" name="edicion" id="edicion">
	<input type="hidden" name="id" id="id" value="">
	<input type="hidden" name="operacion" id="operacion" value="">
</form>
<fieldset><legend>Filtro Personas</legend>
<form action="PersonaServlet" name="filtro" id="filtro">
	<table id="filtroPersonas">
	  <tr>
	    <td>Apellido:<input id="apellido" name="apellido"></input></td>
	    <td>Nombre:<input id="nombre" name="nombre"></input></td>
	    <td>Tipo Documento:<input id="tipoDocumento" name="tipoDocumento"></input></td>
	    <td>Numero Documento:<input id="numeroDocumento" name="numeroDocumento"></input></td>
	  </tr>
	  <tr>
	    <td>Cohorte:<input id="cohorte" name="cohorte"></input></td>
	    <td colspan="2">
	    	Carrera:<input id="carreraNombre" name="carreraNombre" onclick="javascript:windowCarreras();" readonly="readonly"/>
	    	<img src="images/lupa.gif" onfocus="javascript:windowCarreras();" onclick="javascript:windowCarreras();" height="25" width="25" />
	    	<input type="hidden" id="idCarrera" name="idCarrera"></input>	
	    </td>
	    	
	    <td><button id="buscar">Buscar</button></td>
	  </tr>
	</table>
	<input type="hidden" id="operacion" name="operacion" value="filtrar">
</form>
</fieldset>

<%@ include file="AgregarVolver.jsp"%>

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

<div id="dialog-carrera" title="Carreras">
	<form name="windowCarrera" id="windowCarrera">
		<vel:velocity strictaccess="true">
			#set($carreras = $scopetool.getRequestScope("carreras"))
			<select name="carrera" id="carrera" size="20">
			<option value="">NINGUNA OPCION</option>
			#foreach($carrera in $carreras)
				#if($carrera.estado.isActivo())
					<option value="$carrera.id">$carrera.nombreCarrera</option>
				#end
			#end
			</select>
		</vel:velocity>
	</form>
</div>

<table id="tablaPersonas" class="fixheadertable">
	<thead>
		<tr>
			<th ><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
			<th>Nombre</th>
			<th>Apellido</th>
			<th>Tipo. Doc.</th>
			<th>Documento</th>
			<th>Fec. Ingreso</th>
			<th><img src="images/delete.png" width="24px" title="Eliminar Persona" /></th>
		</tr>
	</thead>
	<tbody>
	<vel:velocity strictaccess="true">
		#set($personas = $scopetool.getRequestScope("personas"))
		#set($paginaNumero = $scopetool.getRequestScope("paginaNumero"))
		#set($personaNumero = $paginaNumero * 300)
		#foreach($persona in $personas)
			#if($persona.estado.isActivo())
				
				<tr>
					<td>$personaNumero</td>
					<td><a href="PersonaServlet?operacion=mantenimiento_persona&id=$persona.id">
						#if($persona.nombre != "")
							$!persona.nombre
						#else
							Sin Nombre
						#end	
							
						</a>
					</td>
			<!--	<td><a href="javascript:modificar('$persona.id','actualizar');">$!persona.nombre</a></td>	-->
					<td>$!persona.apellido</td>
					<td>$!persona.documento.tipo.abreviacion</td>
					<td>$!persona.documento.numero</td>
					<td>$!persona.fechaIngreso</td>
					<td><a href="javascript:eliminar('$persona.id');">X</a></td>
				</tr>
				#set($personaNumero = $personaNumero + 1)
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