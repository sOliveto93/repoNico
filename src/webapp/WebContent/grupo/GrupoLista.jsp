<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%@ include file="/administracion/abm_headers.jsp"%>
<script language="javascript">
$(function() {
	dialogGrupos(530,530,functionAceptar);
});
function dialogGrupos(alto,ancho,functionAceptar){
	$("#dialog-grupos").dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Aceptar': function() {
				functionAceptar();
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
function windowGrupo(functionAceptar){
	$(function(){
		dialogGrupos(530,530,functionAceptar);
	});
	$('#dialog-grupos').dialog('open');
}
</script>
<div id="dialog-grupos" title="Grupos">
	<form name="windowGrupos" id="windowGrupos">
			<select name="gruposDisponibles" id="gruposDisponibles" size="20">
			<option value="">NINGUNA OPCION</option>
			<c:forEach items="${grupos}"  var="grupo">
				<c:if test="${grupo.estado.activado==true}">
					<option value="${grupo.codigo}">${grupo.codigo} - ${grupo.descripcion}</option>
				</c:if>
			</c:forEach>
			</select>
	</form>
</div>
</body>
</html>