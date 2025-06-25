<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
	dialogoCarrera(550,900);
	$('#carrerasDep').fixheadertable({
		caption : 'Vinculacion de Carrerras',
		colratio : [26, 400, 450, 26], 
		height : 400, 
		width : 930, 
		zebra : true
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
				var idCarreraCuotas = $("#carrera option:selected").val();
				//if(idCarreraCuotas){
				$("#idCarreraCuotas").val(idCarreraCuotas);
				$("#operacion").val("vincularCarreras");
				$("#edicion").submit();
				$(this).dialog('close');
				/*}else{
					alert("Seleccion una carrera a vincular");
					event.preventDefault();
				}*/
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
function openDialogVincuarlCarreras(idDep){
	$("#idDep").val(idDep);
	openDialog('dialog-carrera');
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<div class="recuadro">
<%@ include file="/menu.jsp"%>

		<c:set var="CARRERA_DRC" value="driyc"></c:set> <!-- se usa para los perfiles de dep_admin y drc_admin --> 
		<c:set var="CARRERA_DEP" value="dep"></c:set>
	<c:if test="${(usuario.tipo == ADM)|| (usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN)}">
		<table id="carrerasDep" class="fixheadertable">
			<thead>
				<tr>
					<th><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
					<th>Carrera
					<c:if test="${(usuario.tipo == DEPADMIN)}">
					 	DEP
					 </c:if>
					<c:if test="${(usuario.tipo == DRCADMIN)}">
					 	DRC
					 </c:if>
					 <c:if test="${(usuario.tipo == ADM)}">
					 	DEP/DRC
					 </c:if> 
					 </th>
					<th>Carrera Cuotas</th>
					<th><a><img src="images/pencil.png" width="20px" title="Relacionar Carreras"/></a></th>
					
				</tr>
			</thead>
			<tbody>
				<c:set var="carreraNumero" value="0"/>
				<c:forEach items="${carrerasDep}"  var="carreraDep">
				<c:set var="flag" value="false" />
					<c:set var="carreraNumero" value="${carreraNumero + 1}"/>
					<tr>
						<td>${carreraNumero}</td>
						<td>${carreraDep.descripcionDep}</td>					
						<td>${carreraDep.carrera.nombreCarrera}</td>
						<td>
<%-- 						<c:if test="${carreraDep.carrera.tipo==CARRERA_DRC && (usuario.tipo == DRCADMIN)}">
							<a href="javascript:openDialogVincuarlCarreras(${carreraDep.id});"><img src="images/pencil.png" width="15px" title="Relacionar Carreras"/></a>
							<c:set var="flag" value="true" />
						</c:if> 
						<c:if test="${carreraDep.carrera.tipo==CARRERA_DEP && (usuario.tipo == DEPADMIN)}">
							<a href="javascript:openDialogVincuarlCarreras(${carreraDep.id});"><img src="images/pencil.png" width="15px" title="Relacionar Carreras"/></a>
							<c:set var="flag" value="true" />
						</c:if>  --%>
						<c:if test="${(usuario.tipo == ADM)}">
							<a href="javascript:openDialogVincuarlCarreras(${carreraDep.id});"><img src="images/pencil.png" width="15px" title="Relacionar Carreras"/></a>
							<c:set var="flag" value="true" />
						</c:if> 
					<%-- 	<c:if test="${(carreraDep.carrera.tipo!=CARRERA_DEP) && (carreraDep.carrera.tipo!=CARRERA_DRC)  && (usuario.tipo != ADM)}">
							<a href="javascript:openDialogVincuarlCarreras(${carreraDep.id});"><img src="images/pencil.png" width="15px" title="Relacionar Carreras"/></a>
							<c:set var="flag" value="true" />
						</c:if>  --%>
						<c:if test="${(flag == 'false')}">
							<img src="images/pencilGris.png" width="15px" title="Relacionar Carreras"/></a>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</div>
<div id="dialog-carrera" title="Carreras" style="display: none">
	<form name="windowCarrera" id="windowCarrera">
		<select name="carrera" id="carrera" size="20">
			<option value="">NINGUNA OPCION</option>
			<c:forEach items="${carreras}"  var="carrera">
				<c:if test="${carrera.estado.activado}">
<%-- 					<c:if test="${carrera.tipo==CARRERA_DRC && (usuario.tipo == DRCADMIN)}">
						<option value="${carrera.id}">${carrera.nombreCarrera}</option>
					</c:if> 
					<c:if test="${carrera.tipo==CARRERA_DEP && (usuario.tipo == DEPADMIN)}">
						<option value="${carrera.id}">${carrera.nombreCarrera}</option>
					</c:if>  --%>
					<c:if test="${(usuario.tipo == ADM)}">
						<option value="${carrera.id}">${carrera.codigo} - ${carrera.nombreCarrera}</option>
					</c:if> 
				
				
				</c:if> 
			</c:forEach>
		</select>
	</form>
</div>
<form  action="CarrerasDepCuotasServlet" method="post"  name="edicion" id="edicion">
	<input type="hidden" name="idDep" id="idDep" value="">
	<input type="hidden" name="idCarreraCuotas" id="idCarreraCuotas" value="">
	<input type="hidden" name="operacion" id="operacion" value="">
</form>
</body>
</html>