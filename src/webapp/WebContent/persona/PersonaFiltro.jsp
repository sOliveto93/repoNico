<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(function(){
	dialogoCarrera(550,900);
	$('#filtro').submit(function() {
		if(verificarCamposFiltro()){
			$("#filtro").submit();
			return true;
		}else{
			alert("Filtro: agregue un filtro como minimo");
			return false;
		}
	});
	$("#buscar")
	.button()
	.click(function(){
	});
});
function verificarCamposFiltro(){
	var respuesta = false;
	var tipoDocumento = document.filtro.tipoDocumento.value;
	if($("#apellido").val() ||	$("#nombre").val() ||	tipoDocumento ||
	   $("#numeroDocumento").val() || $("#cohorte").val() || $("#carreraNombre").val() || $("#saltarVerificacion").val() || $("#email").val())  {
			respuesta = true;
	}
	return respuesta;
}
function dialogoCarrera(alto,ancho){
	$("#dialog-carrera").dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Aceptar': function() {
				//document.filtro.idCarrera.value = document.windowCarrera.carrera.value;
				$("#idCarrera").val($("#carrera"));
				$("#carreraNombre").val($("#carrera option:selected").text());
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
</script>
<!-- style="text-align: right" -->
<fieldset><legend>Filtro Personas</legend>

	
	

<form action="PersonaServlet" name="filtro" id="filtro">
	<table id="filtroPersonas">
	  <tr>
	    <td>Apellido:<input id="apellido" name="apellido" value="${personaBuscada.apellido }"></input></td>
	    <td>Nombre:<input id="nombre" name="nombre" value="${personaBuscada.nombre }"></input></td>
	    <td>Tipo Documento:<%@ include file="selectTipoDocumento.jsp"%></td>
	    <td>Numero Documento:<input id="numeroDocumento" name="numeroDocumento" value="${personaBuscada.documento.numero }"></input></td>
	  </tr>
	  <tr>
	  	<td>Mail:<input id="email" name="email"></input></td>
	  	<td>Cohorte:<input id="cohorte" name="cohorte" value="${cohorte}"></input></td>
	    <td id="carreraTd" colspan="2"> <!-- colspan="2" -->
	    	Carrera:<input id="carreraNombre" name="carreraNombre" onclick="javascript:openDialog('dialog-carrera');" readonly="readonly" value="${carreraBuscada.nombreCarrera }"/>
	    	<img  id="carreraNombreImg" src="images/lupa.gif" onfocus="javascript:openDialog('dialog-carrera');" onclick="javascript:openDialog('dialog-carrera');" height="25" width="25" />
	    	<input type="hidden" id="saltarVerificacion" name="saltarVerificacion" value=""></input>
	    	<input type="hidden" id="idCarrera" name="idCarrera" value="${carreraBuscada.id}"></input>	
	    </td>
		<td><button id="buscar">Buscar</button></td> <!--  align="rigth" -->
	  </tr>
	</table>
	 
	<c:if test="${sancionarPersonas==true}">
		<input type="checkbox" id="adeudaCuotasCheck" name="adeudaCuotasCheck" onblur="javascript:traerPersonasMora();"  
			<c:if test="${adeudaCuotas=='on'}">
				checked="checked"
			</c:if>
		>Adeuda
		
		<input type="checkbox" id="aniosTope" name="aniosTope"   
			<c:if test="${aniosTope =='on'}">
				checked="checked"
			</c:if>
		>Solo Año Actual
		<!--  <select name="aniosTope" id="aniosTope">
			<option value="0">Seleccione año</option>
			<c:forEach items="${anios}"  var="anio">
				<option value="${anio}">${anio}</option>
			</c:forEach>
		</select>-->		
		<input type="hidden" id="sancionarPersonas" name="sancionarPersonas" value="${sancionarPersonas}">
		<input type="hidden" id="filtrarJsp" name="filtrarJsp" value="true">
		<input type="hidden" id="operacionDos" name="operacionDos" value="filtrarMora">
	</c:if>
	
	<input type="hidden" id="operacion" name="operacion" value="filtrar">
	<input type="hidden" id="salto" name="salto" value="/persona/Personas.jsp">
</form>
<div id="dialog-carrera" title="Carreras" style="display: none">
	<form name="windowCarrera" id="windowCarrera">
		<select name="carrera" id="carrera" size="20">
			<option value="">NINGUNA OPCION</option>
			<c:if test="${sancionarPersonas==true}">
				<option value="0">TODO</option>
			</c:if>
			<c:forEach items="${carreras}"  var="carrera">
				<c:if test="${carrera.estado.activado}">
					<option value="${carrera.id}">${carrera.codigo} - ${carrera.nombreCarrera}</option>
				</c:if> 
			</c:forEach>
		</select>
	</form>
</div>
</fieldset>