<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(function(){
	dialogoCarrera(550,900);
	$("#generarInformeCarrera")	
		.button()
		.click(function(){
			$("#InformeCarrera").submit();
	});
});
function dialogoCarrera(alto,ancho){
	$("#dialog-carrera").dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Aceptar': function() {
				$("#idCarrera").val($("#carrera"));
				$("#carreraNombre").val($("#carrera option:selected").text());
				document.InformeCarrera.idCarrera.value = document.windowCarrera.carrera.value;
				$(this).dialog('close');
			},
			Cancel: function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
}
</script>
</head>
<body>
<div class="recuadro">
	<%@ include file="/menu.jsp"%>
	<div style="margin:0px auto;text-align:center;">
		<fieldset><legend>Filtro Carrera</legend>
			<form id="InformeCarrera" name="InformeCarrera" action="ReporteServlet" method="post" target="_blank">
			<table border=0>
				<tr><td>Cohorte: </td><td><input type="text" name="cohorte" id="cohorte" value=""></td></tr>
				<tr><td>Numero Documento: </td><td><input type="text" name="numeroDeDocumento" id="numeroDeDocumento" value=""></td></tr>				
					<tr><td>Carrera:   </td><td>
					<input id="carreraNombre" name="carreraNombre" onclick="javascript:openDialog('dialog-carrera');" size="60" readonly="readonly" value="${carreraBuscada.nombreCarrera}"/>
	    			<img  id="carreraNombreImg" src="images/lupa.gif" onfocus="javascript:openDialog('dialog-carrera');" onclick="javascript:openDialog('dialog-carrera');" height="15" width="15" />
	    			<input type="hidden" id="idCarrera" name="idCarrera" value="${carreraBuscada.id}"></input></td></tr>
	    		
				<tr><td colspan="2">
				<div id="estado" align="center">
					Estado: 
					<input type="radio" name="estado" value="A" id="estado" checked>Activo
					<input type="radio" name="estado" value="E" id="estado">Egresado
					<input type="radio" name="estado" value="P" id="estado">Pasivo
					<input type="radio" name="estado" value="I" id="estado">Ingresante
					<input type="radio" name="estado" value="N" id="estado">Abandono
					<input type="radio" name="estado" value="" id="estado">Todos
				</div>
				</td></tr>
				<tr><td>Solo becados</td><td><input type="checkbox" name="becado"></td></tr>
				<tr><td>Inlcluir Mail</td><td><input type="checkbox" name="incluirMail"></td></tr>
				</table>
				<hr></hr>
				<div align="right">
					<button id="generarInformeCarrera" name="generarInformeCarrera">Generar Informe</button>
				</div>
				<input id="operacion" name="operacion" value="reporteCarreras" type="hidden">
			</form>
		</fieldset>
	</div>
</div>
<div id="dialog-carrera" title="Carreras" style="display: none">
	<form name="windowCarrera" id="windowCarrera">
		<select name="carrera" id="carrera" size="20">
			<option value="">NINGUNA OPCION</option>
			<c:forEach items="${carreras}"  var="carrera">
				<c:if test="${carrera.estado.activado}">
					<option value="${carrera.id}"> ${carrera.codigo} - ${carrera.nombreCarrera}</option>
				</c:if> 
			</c:forEach>
		</select>
	</form>
</div>
</body>
</html>