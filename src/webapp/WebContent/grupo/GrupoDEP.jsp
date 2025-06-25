<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<title>Grupo</title>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
var operacionPartida;
var idFilaAnterior;
	$(function(){
		 $('#tablaGrupo').fixheadertable({
	         caption   : 'Grupos', 
	         colratio    : [50, 500, 450,120], 
	         height      : 300, 
	         width       : 1145, 
	         zebra       : true
	    });
	  });
		
	function onLoad(idGrupo){
		if(idGrupo){
			setBackgroundColor(idGrupo,'#666666');
			idFilaAnterior=idGrupo;
		}
	}
</script>
</head>
<body onload="javascript:onLoad(${idGrupoAnterior});">
<div class="recuadro">
<%@ include file="/menu.jsp"%>
<input type="hidden" name="cartel" value="${msgError}" id="cartel">
<table id="tablaGrupo" class="fixheadertable" >
	<thead>
		<tr>
			<th><img src="images/hash.png" width="24px" title="Codigo" /></th>
			<th>Descripcion Grupo</th>
			<th>Nombre Carrera</th>
			<th>CodigoItem</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${grupos}"  var="grupo">
			<c:if test="${grupo.estado.activado==true}">
			<tr id="${grupo.id}">
				<td>${grupo.codigo}</td>
				<td>${grupo.descripcion}</td>
				<td>${grupo.carrera.nombreCarrera}</td>
				<td>${grupo.facturaItemTipo.rubroCobro.codigo}<c:if test="${grupo.facturaItemTipo.codigo  < 10}">0</c:if>${grupo.facturaItemTipo.codigo} </td>
			</tr>
			</c:if>
		</c:forEach>
	</tbody>
</table>
</div>
</body>

</html>