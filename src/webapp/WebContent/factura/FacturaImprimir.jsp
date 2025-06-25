<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<LINK rel="stylesheet" type="text/css" media="print" href="css/impresion_factura.css" />
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
$(function(){
	var tab = $('#tabs').tabs();
	$("#volver")
		.button()
		.click(function(){
			window.print();
	});
	$("#cerrar")
	.button()
	.click(function(){		
	}); 
});
</script>
</head>
<body onload="doOnLoad();">
<DIV class="sin_block">
<form action="ReporteServlet?operacion=duplicadoFactura&idfactura=${factura.id}&numeroFactura=${factura.nro}" method="post" name="button">
		<button id="volver" name="operacion" >Imprimir</button>
		<button id="cerrar" name="operacion" >Cerrar</button>
</form>
</DIV>
<div class='print_block'>
<!-- <input type="button" value="Print this page" onclick="">-->
<table width="100%" border="0" bgcolor="#ffffff">
<tr><td width=100>&nbsp;</td>
<td width="950">
	<table width="950" border="0" bgcolor="#ffffff">	  
	 
	 <!--  <tr> se comento ya que salia desfasada la factura
	    <td colspan=4>&nbsp;</td>
	  </tr> -->
	   <tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
	<tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
	  <tr>
	    <td width=200>${factura.tipoFactura.descripcion}</td>
	    <td width=670>&nbsp;</td>
	    
	    <td width=100>${factura.fechaCarga}</td>
	    <td width=80>&nbsp;</td>
	  </tr>
	   <tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
 <tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
	   <tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan=4>&nbsp;</td>
	  </tr>
	  <tr>
	  <td>&nbsp;</td>
	    <td>${factura.persona.nombreCompleto}</td>
	    <td COLSPAN=2>Razon Social:${factura.razonSocial}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>
	    	<c:if test="${not empty factura.persona.direccion}"> 
	    		${factura.persona.direccion.domicilioCompleto}
	    	</c:if> 
	    	<c:if test="${empty factura.persona.direccion}"> 
	    		&nbsp; 
	    	</c:if> 
	    </td>
	   <td COLSPAN=2><!-- Cuit:${factura.cuit} --></td>
	   
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>
	    	<c:if test="${factura.descripcionExtendida!=''}">
	    		${factura.descripcionExtendida}
			</c:if>
	    	<c:if test="${factura.descripcionExtendida==''}">
	    		${factura.persona.carrerasActiva}
	   		</c:if>
	    </td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	    <tr>
	    <td>&nbsp;</td>
	    <td>${factura.responsableIva}</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	 
	  
	    <tr>
	    <td>&nbsp;</td>
	    <td>
	    	 <c:out value="${factura.formaPago.descripcion}"></c:out> 
	    	<!-- 
	    	<c:if test="${factura.formaPago.id==1}"> 
	    		Contado
	    	</c:if> 
	    	<c:if test="${factura.formaPago.id==2}"> 
	    		Cheque
	    	</c:if> 
	    	<c:if test="${factura.formaPago.id==3}"> 
	    		Otros 
	    	</c:if>
	    	 --> 
	    </td>
	    <td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${factura.persona.documento.numero}</td>
	    <!-- <td>&nbsp;</td>  -->
	  </tr>
	   
	  
	    <tr>
	    <td colspan="4">
		<table width="100%" border="0">
			  <tr>	
			  	<TD width=40></TD>		  	
				<td width=50>Cod.</td>
				<td width=50>Cant.</td>
				<td width=640>Detalle</td>
				<td width=85>Precio</td>
				<td width=85>Importe</td>
				
			  </tr>
			<c:set var="i" value="${0}"/>
			<c:forEach items="${factura.facturaItems}"  var="item">
			  <tr>
			  	<TD></TD>
				<td>${item.codigo}</td>
				<td>${item.cantidad}</td>
				<td>
					<c:if test="${item.facturaItemTipo.rubroCobro.codigo==9}">
						${item.descripcion}
					</c:if>
					<c:if test="${item.facturaItemTipo.rubroCobro.codigo!=9}">
						${item.facturaItemTipo.descripcion}
					</c:if>
				</td>
				<td>${item.precio}0</td>
				<td>${item.precioTotal}0</td>
			  </tr>
			  <c:set var="i" value="${i+1}"/>
			</c:forEach>
			<!-- se cambio el END del foreach de 10 a 9 para eliminar 1 tr se comento ya que salia desfasada la factura -->
			<c:forEach var="a" begin="${i}" end="9">
			  <tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			  </tr>
			  </c:forEach>
			</table>
		</td>
	  </tr>
	  
	  
	    <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</div></td>
	    <td>${factura.monto}0</td>
	  </tr>
	  <!-- <tr> se comento ya que salia desfasada la factura
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr> -->
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	 
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td colspan=2>NºR. ${factura.nro}</td>
	    
	  </tr>
	  
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	 </table>
	 </td></tr></table>
</div>
</body>
</html>