<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<LINK rel="stylesheet" type="text/css" media="print" href="css/impresion_factura_nueva.css"/>



<style> 
p.pieLogo {
  font-size: 8px;
}
div.exento {
	font-weight: 600; font-size: 10px
}
div.predefinido{
	font-weight: 600;
	display: inline;
}
div.cuit{
	font-weight: 600; font-size: 10px
}

</style>

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
<body>
<DIV class="sin_block">
<form action="ReporteServlet?operacion=duplicadoFactura&idfactura=${factura.id}&numeroFactura=${factura.nro}" method="post" name="button">
		<button id="volver" name="operacion" >Imprimir</button>
		<button id="cerrar" name="operacion" >Cerrar</button>
</form>
</DIV>
<div class='print_block2'> 
	
	<table width="100%" bgcolor="#ffffff" border="0">
		<tr>
			<td colspan="1" width="3%">
				<img src="images/UnlaLogo.png" style="width:75px;height:75px;">
				<p class="pieLogo">
				29 de septiembre 3901<br>
				(1826) R. de Escalada<br>
				Lanus. Prov de Bs As<br>
				Tel. 011 5533-5600<br>
				</p>
			</td>
			<td colspan="11" width="97%" style="text-align: center;">C<br><div class="exento">IVA: Exento</div></td>
		</tr>
	</table>
	<table width="100%" bgcolor="#ffffff" border="0">	 	
		<tr>
			<td colspan="8" width="80%"  style=""></td>
			<td colspan="4" width="20%" style=""><div class="predefinido">RECIBO</div></td>
		</tr>
		<tr>
			<td colspan="8" width="80%" style="text-align: left; ">&nbsp;</td>
			<td colspan="4" width="20%" style="">Nº ${factura.nro}</td>
			
		</tr>
		<tr>
			<td colspan="8" width="80%" style="">&nbsp;</td>
			<td colspan="4" width="20%" style="">${factura.fechaCarga}</td>
		</tr>
		<tr>	
			<td colspan="8" width="80%" style="">&nbsp;</td>
			<td colspan="4" width="20%" style=""><div class="cuit">CUIT: 30-68287386-4 <br> Ing. Brutos Exento Inicio de Actividad 10/95</div></td>
		</tr>
	</table>
	<table width="100%" bgcolor="#ffffff" border="0">
		<tr>	
			<td colspan="8"  width="80%">&nbsp; </td>
			<td colspan="4"  width="20%">&nbsp; </td>
		</tr>	
		<tr>
			<td colspan="8" width="80%"><div class="predefinido">Señor:</div> ${factura.persona.nombreCompleto}</td>
			<td colspan="4" width="20%"></td>
		</tr>
		<tr>
			<td colspan="8" width="80%">
				<div class="predefinido">Numero Documento:</div>${factura.persona.documento}
			</td>
			<td colspan="4" width="20%" style="text-align: left;"><div class="predefinido">Razon Social:</div>  ${factura.razonSocial}</td>
			<!-- <td colspan="5"></td> -->
		</tr>
		<tr>
			<td colspan="8" width="80%">
				<div class="predefinido">Direccion:</div> 
				<c:if test="${not empty factura.persona.direccion}">
					${factura.persona.direccion.domicilioCompleto}
				</c:if>
				<c:if test="${empty factura.persona.direccion}">
					&nbsp; 
				</c:if>	
			</td>
			<td colspan="4" width="20%"><div class="predefinido">Cuit:</div> ${factura.cuit}</td>
		</tr>
		<tr>
			<td colspan="8" width="80%">
				<div class="predefinido">Consumidor Final</div>
			</td>
			<td colspan="4" width="20%" style="text-align: right;">
				 &nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="8" width="80%">
				Tipo Pago:<c:out value="${factura.formaPago.descripcion}"></c:out>
			</td>
			<td colspan="4" width="20%">
				&nbsp;
			</td>
		</tr>
	</table>
	<table width="100%" bgcolor="#ffffff" border="0">	
		<tr>
			<td colspan="6">
				<table align="center" width="85%" bgcolor="#ffffff" border="0" cellpading=3 cellspacing=0>
					<tr>	
						<td width=50>Cod.</td>
						<td width=50>Cant.</td>
						<td width=640>Detalle</td>
						<td width=85>Precio</td>
						<td width=85>Importe</td>
					</tr>
					<c:set var="i" value="${0}"/>
				<c:forEach items="${factura.facturaItems}"  var="item">
					<tr>
						<td>${item.codigo}</td>
						<td>${item.cantidad}</td>
						<td>${item.facturaItemTipo.descripcion}</td>
						<td>${item.precio}0</td>
						<td>${item.precioTotal}0</td>
					</tr>
					<c:set var="i" value="${i+1}"/>
				</c:forEach>
				<c:forEach var="a" begin="${i}" end="10">
				</c:forEach>
		        </table>
			</td>
		</tr>
		<tr>
			<td colspan="4"  width="85%"></td> 
			<td colspan="2" width="15%"style="text-align: left;">Total:${factura.monto}0</td>
		</tr>
	</table>
	<table width="100%" bgcolor="#ffffff" border="0">
		<tr>
			<td colspan="10"></td>
			<td colspan="1"style="text-align: right;">CAI - 48074191610878</td>
			<td colspan="1"></td>
		</tr>
	</table>

</div>
<!-- 
    <span class="receipt-label">${factura.persona.carrerasActiva}</span>
              
      CAI - 43414117441037
      Fecha de Vto. 09/10/2018
      NºR. ${factura.nro}
  </div>
 -->

</body>
</html>