<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<table id="tablaGrupo" class="">
	<thead>
		<tr>
			<th><img src="images/hash.png" width="24px" title="Codigo"/></th>
			<th>Descripcion Grupo</th>
			<th>Nombre Carrera</th>
			<th><img src="images/check.png" width="24px" title="Crear chequeras"/></th>
		</tr>
	</thead> 
	<tbody>
		<c:forEach items="${grupos}"  var="grupo">
			<c:if test="${grupo.estado.activado==true}">
				<tr id="${grupo.id}">
					<td>${grupo.id}</td>
					<td>${grupo.descripcion}</td>
					<td>${grupo.carrera.nombreCarrera}</td>
					<td><a href="javascript:buscarPersonasPorGrupo(${grupo.id});"><img src="images/check.png" width="20px" title="Personas Grupo/Chequera"/></a></td>
				</tr>
			</c:if>
		</c:forEach>
	</tbody>
</table>
<form action="ChequeraServlet" name="grupoChequeraForm" id="grupoChequeraForm">
	<input type="hidden" name="idGrupo" id="idGrupo" value="">
	<input type="hidden" name="operacion" id="operacion" value="buscaPersonaPorCarrera">
</form>
 
<script type="text/javascript">
$(document).ready(function() {
    $('#tablaGrupo').fixheadertable({
         caption   : 'Grupos',
         colratio    : [30, 450,450,30],
         height      : 400, 
         width       : 1000,
         zebra       : true
         
    });
});
function buscarPersonasPorGrupo(idGrupo){
	$("#idGrupo").val(idGrupo);
	$("#grupoChequeraForm").submit();
}
</script>
