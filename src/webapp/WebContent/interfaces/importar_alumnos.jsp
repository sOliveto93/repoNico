 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/interfaces/headers.jsp"%> 
<script type="text/javascript">
$(function(){
	
	$("#cargar")
		.button()
		.click(function(){
			$("#cargar").attr("disabled", true);
			document.formulario.submit();
			
	});
	$("#importar")
	.button()
	.click(function(){
		$("#importar").attr("disabled", true);
		$("#operacion").val("sincronizar_alumnos");
		document.formulario.submit();
	});

}); 
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

	if(!isEmpty(value) && (usuarioTipo == "DEP_ADMIN" || usuarioTipo == "DRC_ADMIN")){
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
			
			if(resp==false){
				$("#"+id).val(usuarioGrupos[0]);	
			}
			$("#"+id).select();
		}
	}
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div class="recuadro">

<%@ include file="/menu.jsp"%>
<form action="InterfaceServlet" name="formulario">
<fieldset><legend>Importar de Sistemas Externos</legend>


<input type="hidden" name="operacion" value="realizar_importacion" id="operacion"/>


<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />

<table border=0><tr><td>Base de datos a importar:</td><td>
<select name="sistema" method="post">
	<c:if test="${(usuario.tipo == ADM)}">
		<option value="grado">Guarani Grado</option>
		<option value="ingreso">Guarani Ingresantes</option>
		<option value="posgrado">Guarani Posgrado</option>
		<option value="cursos">Cursos DEP</option>
		<option value="preinscripcion">Preinscripcion</option>
	</c:if>
	<c:if test="${(usuario.tipo == DEPADMIN)}">
		<option value="cursos">Cursos DEP</option>
	</c:if>
	<c:if test="${(usuario.tipo == DRCADMIN)}">
		<option value="cursos">Cursos DRC</option>
	</c:if>
</select></td></tr></table>
<br>

<input type="hidden" name="usuarioCodigoGrupos" id="usuarioCodigoGrupos" value='${usuarioCodigoGrupos}'> 
<input type="hidden" name="tipo_usuario" id="tipo_usuario" value='${sessionScope.usuario.tipo}'>
		
Opcional codigo de carrera: <input type="text" name="carrera" id="carrera" onblur="javascript:verificaGrupo(this);">
<br>
Opcional año de ingreso: 
<select name="anio" method="post">
	<option value="" selected>Todos</option>
	<c:forEach begin="2010" end="${currentYear}" varStatus="loop">
   	  <c:if test="${currentYear == loop.index}"><option value="${loop.index}" selected>${loop.index}</option></c:if> 
   	  <c:if test="${currentYear != loop.index}"><option value="${loop.index}" >${loop.index}</option></c:if>
	</c:forEach>
	
</select></td></tr></table>
<br>
<p></p>
	<button id="cargar">Cargar Tablas Auxiliares</button>
	<p></p>	
	
En caso de los cursos de la <c:if test="${(usuario.tipo == DEPADMIN)}"> DEP el codigo de carrera es el del Sistema de Cuotas (20xx) </c:if><c:if test="${(usuario.tipo == DRCADMIN)}">DRC el codigo de carrera es el del Sistema de Cuotas (30xx)</c:if>
En los casos de las importaciones de los Sistemas de Alumnos, la carrera es el del Sistema de Alumnos.
Lo que es la importación de Ingresantes corresponde a los alumnos de CCC que todavia no tienen legajo generado. Todos los demas son Grado.
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
	<li>Todos los alumnos del Sistema de la Base seleccionada (grado,posgrado o dep/drc) se ponen como no regulares en esa carrera</li>		
	<li>Luego se cargan los datos de los alumnos en una tabla auxiliar, se informa cuantos alumnos son</li>
	<li>En el segundo paso se actualizan los alumnos de la base de cuotas siguiendo el siguiente criterio:
		<ul> 
			<li>Si el alumno ya existe con ese legajo se lo actualiza y se lo pone como regular en su carrera.</li>
			<li>Si no existe, se buscar por DNI, si existe se lo deja pendiente de confirmación</li> 
			<li>Si no existe por DNI,sino se lo inserta como nuevo en el sistema y se lo pone como regular en su carrera</li>
		</ul>	
	</li>
	<li>En el tercer paso se informan los pendientes de actualización para que el operador pueda decidir si importarlos o no</li>
</ul>
</div>
</body>
</html>