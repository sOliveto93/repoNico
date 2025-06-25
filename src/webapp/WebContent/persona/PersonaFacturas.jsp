<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<table id="personaFacturas" class="fixheadertable">
	<thead>
		<tr>
			<th>T.Pago</th>
			<th>N°Factura</th>
			<th>Fecha de Pago</th>
			<th>Descripcion</th>
			<th>Monto</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${persona.facturas}" var="factura">
			<tr>
				<td align="center">
					${factura.tipoPago.descripcion}
				</td>
				<td align="center">
					${factura.nro}
				</td>
				<td align="center">
					${factura.fechaPago}
				</td>
				<td>
					<c:forEach items="${factura.facturaItems}" var="facturaItem">
						${facturaItem.facturaItemTipo.descripcion}<br>
					</c:forEach>
				</td>
				<td align="center">
					${factura.monto}
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>