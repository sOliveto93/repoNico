<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
 $(function(){
	 $("#imprimirChequerasGrupoDEP")
		.button().click(function(event){
			$("#personasChequeraImprimir").submit();
			}
		); ;
	 $("#buscarPorFiltro")
		.button()
		.click(function(event){
			$("#personasChequera").submit();
			}
		); 
	 $('#tablaPersonasDEP').fixheadertable({
		    caption   : 'Personas',
		    colratio    : [50, 480,150,150], 
		    height      : 280, 
		    width       : 830, 
		    zebra       : true
		});
	$("#modalLoader").easyModal({
		top: 100,
		autoOpen: false,
		overlayOpacity: 0.3,
		overlayColor: "#333",
		overlayClose: false,
		closeOnEscape: false
	});
});
function selectAll(){	
	if (!$('#aux').is(':checked')){			
		$('.check').attr('checked', false);			
	}else{
		$('.check').attr('checked', true);
	}
}
</script>
<title>Imprimir Chequeras por Grupo</title>
</head>
<body >
<div class="recuadro">
	<%@ include file="/menu.jsp"%>

<jsp:useBean id="now" class="java.util.Date" /> 
<fmt:formatDate var="year" value="${now}" pattern="yyyy" /> 
<c:set var="anioActual" value="${year}"></c:set>

	<form  method="post" name="personasChequera" id="personasChequera" action="ChequeraServlet" >
		<div id="filtrosChequeraPersonas">	
			GRUPO:
			<select name="grupoFiltro" id="grupoFiltro">
				<option value="">SELECCIONE UN GRUPO</option>
				<c:forEach items="${grupos}"  var="grupo">
					<c:if test="${grupo.estado.activado}">
						<option value="${grupo.id}"  <c:if test="${grupoBuscado.id == grupo.id}"> selected="selected" </c:if> 
						>${grupo.codigo} - ${grupo.descripcion}</option>
					</c:if> 
			</c:forEach>
			</select>
			AÑO: 
			<select name="anioFiltro" id="anioFiltro">
   					<c:forEach var = "i" begin = "2000" end = "${anioActual}">
   						<option value="${i}" 
							<c:if test="${anioBuscado == i}"> selected="selected" </c:if> 
						>${i}</option>
   					</c:forEach>
			</select>
			MESES: 
			<select name="mesesFiltro" id="mesesFiltro">
   				<option value="0" selected="selected">TODOS</option>
   				<option value="1-6" <c:if test="${mesBuscado == '1-6'}"> selected="selected" </c:if> >Enero a Junio</option>
   				<option value="7-12" <c:if test="${mesBuscado == '7-12'}"> selected="selected" </c:if> >Julio a Diciembre</option>
			</select>
			DOCUMENTO: <input type="text" name="numeroDeDocumentoFiltro" id="numeroDeDocumentoFiltro" value="${numeroDocumentoBuscado}">
			
			<button id="buscarPorFiltro" name="buscarPorFiltro">Buscar</button><br>
		</div>
		<input type="hidden" name="operacion" id="operacion" value="buscaPersonasConChequeraPorCarrera">
		<input type="hidden" name="imprimir" id="imprimir" value="">
	</form>
	<form  method="post" name="personasChequeraImprimir" id="personasChequeraImprimir" action="ChequeraServlet" >
		<table id="tablaPersonasDEP">
				<thead>
					<tr>
						<th><input type="checkbox" name="aux" onclick="selectAll()" id="aux" checked='checked'></th>
						<th>Nombre</th>
						<th>Tipo. Doc.</th>
						<th>Documento</th>
					</tr>
				</thead>
				<tbody id="personasChequeraBody">
					<c:forEach items="${personas}"  var="persona">
					<tr>
						<c:if test="${persona.estado.activado}">
							<td><input type='checkbox' value="${persona.id}" name='idPersonaCheck' id='idPersonaCheck' checked='checked' class='check'></td>
							<td >${persona.nombreCompleto}</td >
							<td >${persona.documento.tipo.abreviacion}</td >
							<td >${persona.documento.numero}</td >
						</c:if>
					</tr>	
					</c:forEach>
				</tbody>
			</table>
			<input type="hidden" name="grupoChequera" id="grupoChequera" value="${grupoBuscado.id}">
			<input type="hidden" name="anioBuscado" id="anioBuscado" value="${anioBuscado}">
			<input type="hidden" name="imprimir" id="imprimir" value="si">
			<input type="hidden" name="operacion" id="operacion" value="generarChequerasGrupo">
			<br><button id="imprimirChequerasGrupoDEP" name="imprimirChequerasGrupoDEP">Imprimir Chequeras</button>
		</form>
</div>	
</body>
</html>
