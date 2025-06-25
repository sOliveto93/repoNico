<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<table id="personaFacturasDepDriyc" class="fixheadertable">
	<thead>
		<tr>
			<th>N°Factura</th>
			<th>Descripcion</th>
			<th>Monto</th>
			<th>Pagado</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${persona.facturas}" var="factura">
			<tr>
				<td>${factura.nro}</td>
				<td>
					<c:forEach items="${factura.facturaItems}" var="facturaItem">
						${facturaItem.facturaItemTipo.descripcion} - $ ${facturaItem.facturaItemTipo.precio}<br>
					</c:forEach>
				</td>
				<td>${factura.monto}</td>
				<td align="center">
					<c:if test="${not empty factura.fechaPago}">
						<c:out value="Pagado"></c:out>
					</c:if>
					<c:if test="${empty factura.fechaPago}">
						<c:out value="No"></c:out>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>