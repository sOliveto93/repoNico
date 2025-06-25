 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/interfaces/headers.jsp"%> 
<script type="text/javascript">
$(function(){	
	var ch = false;
	$("#importar")
		.button()
		.click(function(){
			$("#importar").attr("disabled", true);
			document.formulario.submit();
			
	});
	$("#importar")
	.button()
	.click(function(){
		$("#importar").attr("disabled", true);
		document.formulario.submit();
		
	});
	$("#seleccionar").click(function(e){
		if(ch){
			$(".seleccionados").removeAttr('checked');
			ch = false;
		}else{
			$(".seleccionados").attr('checked','checked');
			ch = true;
		}	
		return false;
	});
}); 

$(document).ready(function() {
	$('#tablaResultados').fixheadertable({
		caption   : 'Listado de Personas a ser Importadas que coinciden por Documento',
        colratio    : [100, 350, 350, 50],
        height      : 200,
        width       : 880,
        zebra       : true
   });
    
});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div class="recuadro">

<%@ include file="/menu.jsp"%>
<form action="InterfaceServlet" name="formulario" method="POST">
<fieldset>
<legend>Importar de Sistemas Externos</legend>
<vel:velocity strictaccess="true"> 
#set($resultado = $scopetool.getRequestScope("resultado"))
#set($msg = $scopetool.getRequestScope("msg"))
#set($sistema = $scopetool.getSessionScope("sistema"))
#if($resultado)
<p>Se cargaron en la tabla auxiliar $resultado registros de alumnos del Sistema de $sistema.</p>
<p>Los que se detallan a continuación no coincidian por Legajo, pero si por tipo y número de documento. Indique cuales son personas eistentes, los restantes será importados como usuarios nuevos: </p>
	<button id="importar">Importar al Sistema</button>
<p></p>
#if($msg != "")
	<p><b>$msg</b></p>
#end
#set($resultados = $scopetool.getSessionScope("resultados"))
<table  class="fixheadertable" id="tablaResultados">
<thead>
	<tr>
		<th>DNI</th>
		<th>Apellido y Nombre en cuotas</th>
		<th>Apellido y Nombre Importado</th>
		<th><a href="" id="seleccionar"><img src="images/hash.png" width="24px" title="Posicion en tabla"/></a></th>
	</tr>
</thead>
#else
	<p>Se finalizo la unificación</p>
#end
#foreach($guarani in $resultados)
<tbody>
<tr><td>$guarani.persona.documento.tipo $guarani.persona.documento.numero </td>	
	<td>$guarani.persona.apellido, $guarani.persona.nombre</td>
	<td>$guarani.apellido, $guarani.nombres</td>
	<td><input type="checkbox" name="id_seleccionados" value="$guarani.id" class="seleccionados"></td></tr>
</tbody>
#end		
</table>
<p></p>	
</vel:velocity>
<input type="hidden" name="operacion" value="confirmar_importacion" id="operacion"/>
</form>
</fieldset>
<br>
<br>
<i>El proceso de carga de tablas temporales puede demorar un tiempo</i>
<br>
<h3>Descripcion del proceso:</h3>
<p>Cuando seleccionan la base de datos de la que van a importar los alumnos se realizan los siguientes pasos</p>
<ul>
	<li>En primer lugar se eliminan los registros de la tabla auxiliar de alumnos</li>	
	<li>Luego se cargan los datos de los alumnos en una tabla auxiliar, se informa cuantos alumnos son</li>
	<li>En el segundo paso se actualizan los alumnos de la base de cuotas siguiendo el siguiente criterio:
		<ul> 
			<li>Si el alumno ya existe con ese legajo se lo actualiza.</li>
			<li>Si no existe, se buscar por DNI, si existe se lo deja pendiente de confirmación</li> 
			<li>Si no existe por DNI,sino se lo inserta como nuevo en el sistema</li>
		</ul>	
	</li>
	<li>En el tercer paso se informan los pendientes de actualización para que el operador pueda desidir si importarlos o no</li>
</ul>
</div>
</body>
</html>