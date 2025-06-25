<select name='tipoDocumento'>
	<option value=''>Tipo Documento</option>
	<c:forEach items="${tiposDocumento}"  var="tipoDocumento">	
		<option value="${tipoDocumento}">${tipoDocumento}</option>
	</c:forEach>
</select>