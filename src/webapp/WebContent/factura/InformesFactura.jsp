<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
var itemsDepDryc = null;
$(function($){
	   $("#fechaFacturaDesde").mask("99-99-9999");
	   $("#fechaFacturaHasta").mask("99-99-9999");
	   $("#buscar")
			.button()
			.click(function(event){				
				if(document.getElementById("operacion").value!=''){
					if(document.getElementById("numeroFactura").value == "" && 
							document.getElementById("numeroDocumento").value == "" && 
							document.getElementById("fechaFacturaDesde").value == ""){ 
						alert("Seleccione algun valor en los filtros para armar el reporte de factura");
						event.preventDefault();
					}else if(document.getElementById("numeroDocumento").value != "" && document.getElementById("tipoDocumento").value == ""){
						alert("Si busca por Nro. Documento es necesario el Tipo de Documento");
						event.preventDefault();
					}
				}
		});
	   
	   /*$("#codigos").keypress(function(e) {
	    	var usu = $("#usuarioAux").val();
	    	var code = e.charCode;
	    	console.log("usuario: " + usu);
	    	console.log("code: ",code);	
	        if((code<=55 || code >=57) && (usu=="dep")){
	        	var codigo = $("#codigos").val();
	        	var codigoLength = codigo.length;
	        	console.log("CodigoLength: "+ codigoLength);
	        	if(codigoLength<1){
	        		e.preventDefault();	
	        	}
	        }
	    });*/
});
/*($.inArray(e.which, range(55, 57)) == -1)
$("#codigos").keypress(function(event) {
	  var usu = $("#usuarioAux").val();
	  var code = event.charCode;
	  alert(code);
	  if((code<=55 || code >=57) && (usu=="DEP")){
		  event.preventDefault();
	  }
});
*/
function range(start, end) {
    var range = [];

    for(var i = start; i <= end; i++) {
        range.push(i);
    }
    console.log("range: "+range);
    return range;
}
		
function traerDatosRubro(codigos){
	if(codigos!=""){		
		if(codigos.charAt(0) != '0'){
			codigos = '0'+codigos;
		}
		//verificar los items que estan cargados en la var itemsDepDryc
		console.log("Codigo: ",codigos);
		//console.log("Tamaño2: ",itemsDepDryc.lista[0].length);
//		$.post('FacturaServlet?operacion=verRubroJson&codigos='+codigos,'', function(data){
//		});
		var itemTipo = codigos.substring(2,4);
		if(codigos){
			$.post('FacturaServlet?operacion=verRubroJson&codigos='+codigos,'', function(data){				
				if(data.status == "OK"){
					if(itemTipo){
						mostrarDatosRubro(data.lista[0],itemTipo);
					}else{
						mostrarDatosRubro(data.lista[0],null);
					}
				};
			});
		}
	}
}
function mostrarDatosRubro(rubro,itemTipo){
	var tiposRubroCobro = rubro.tiposRubroCobro;
	var codigoRubro = rubro.codigo;
	
	var rubroCodigoItemCodigo;
	var codigo;
	var existe = 0;
	if(itemTipo){
		codigo = itemTipo;
	}else{
		codigo =  document.getElementById('codigos').value.substring(2,4);
	}

	rubroCodigoItemCodigo = codigoRubro + codigo;
	
	// Si tiene 0 en el primer caracter lo trunco para compararlo.
	if(codigo.charAt(0) == '0'){
		codigo = codigo.substring(1,2);
	}
	$("#id_item").val("");
	$("#iditem").val("");
	for(var i = 0; i < tiposRubroCobro.length; i++) {
		if(tiposRubroCobro[i].codigo == codigo ){
			$("#id_item").val(tiposRubroCobro[i].descripcion);
			$("#iditem").val(tiposRubroCobro[i].id);
						
			existe = 1;
		}
	}
	if(existe==0){
		clearCamposItem();
		alert("El Concepto/Codigo no existe");
		$('#codigos').focus();
	}
}
function cambiarOperacionReporte(operacion,usuario){
	if(operacion=="facturaPorItem"){
		if(usuario == "DEP"){
			$("#idrubro").attr("disabled",true);
		}
		$('#codigos').removeAttr("disabled");		
	}else if(operacion=="reporteRubroItem"){
		//$("#formaPago").attr("disabled",true);
	}else{
		$('#codigos').val("");
		$('#id_item').val("");
		$('#iditem').val("");
		$('#codigos').attr("disabled",true);
		$("#idrubro").removeAttr("disabled");
	}
	$("#operacion").val(operacion);
}
function doOnLoad(){
	
	myCalendar = new dhtmlXCalendarObject(["fechaFacturaDesde","fechaFacturaHasta"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
}

</script>
<title>Informes</title>
</head>
<body onload="doOnLoad();">
<div class="recuadro">
	<%@ include file="/menu.jsp"%>
	<div style="margin:0px auto;text-align:center;">
		<fieldset><legend>Filtro Facturas</legend>
			<form action="ReporteServlet" name="filtro" id="filtro" target="_blank">
				<table id="filtroFacturas">
				  <tr>
				    <td>Numero Factura:</td><td>
				      <input id="numeroFactura" name="numeroFactura" value=""></input>
					</td>
					<td colspan=2>Tipo de Pago:
						<select name="tipoPago" id="tipoPago" 
								<c:if test="${usuario.tipo!='admin'}">
									disabled="disabled"
								</c:if>
						>
						<c:forEach items="${tiposPago}"  var="tipoPago">
							<option value="${tipoPago.id}"
								<c:if test="${factura.tipoPago.id==tipoPago.id}">
									selected="selected"
								</c:if>
								>${tipoPago.descripcion}</option>
						</c:forEach>
						</select>
						F. Pago: 
						<select name="formaPago" id="formaPago">
							<option value="0" selected="selected">TODOS</option>
						<c:forEach items="${formasPago}"  var="formaPago">
							<option value="${formaPago.id}"
								>${formaPago.descripcion}</option>
						</c:forEach>
						</select>
					</td>
				</tr><tr>
					<td>
						Fecha desde:</td><td><input id="fechaFacturaDesde" name="fechaFacturaDesde" value="">
					</td><td>
				      	Hasta:</td><td><input id="fechaFacturaHasta" name="fechaFacturaHasta" value="" onfocus="javaScript:copiarValor('fechaFacturaDesde','fechaFacturaHasta');">
					</td>
				</tr>
				<tr>
					<td>Tipo Documento</td><td>
						<select name="tipoDocumento" id="tipoDocumento" >
							<option value="" selected="selected">Seleccione Tipo Documento</option> 
								<c:forEach items="${tiposDocumento}" var="tipoDocumento">
									<option value="${tipoDocumento}">${tipoDocumento}</option>
								</c:forEach>
						</select>
					</td>
					<td>Numero Documento:</td><td>
						<input id="numeroDocumento" name="numeroDocumento" value="" onblur="javaScript:validarCamposDocumento();"></input>
					</td>
				</tr>
				<tr>
					<td>Código Item:</td><td>
						<input name="codigos" id="codigos" size="4"  onblur="javascript:traerDatosRubro(this.value);" disabled>
					</td>
					<td colspan=2><input type="hidden" name="iditem" id="iditem" size=45 ><input name="item" id="id_item" size=45 disabled></td>
				</tr>				
				<tr>
					<td>Rubro</td>
					<td> <!--  -->
					<select name="idrubro" id="idrubro">
						<option value="">
							Seleccione Rubro...
						</option>
						<c:forEach items="${rubrosCobro}"  var="rubroCobro">
							<c:if test="${rubroCobro.estado.activado}">
								<c:if test="${rubroCobro.codigo==9}">
									<option value="${rubroCobro.id}">
										${rubroCobro.codigo} - Item Abierto
									</option>
								</c:if>
								<c:if test="${rubroCobro.codigo!=9}">
									<option value="${rubroCobro.id}">
										${rubroCobro.codigo} -  ${rubroCobro.descripcion}
									</option>
								</c:if>
							</c:if>
						</c:forEach>
					</select>
					</td>
				</tr>
				<tr>
					<td>Facturas Anuladas:</td><td>
						<input type="checkbox" name="facturasAnuladas" id="facturasAnuladas">
					</td>
					<td colspan=2>
				</tr>
				<tr>
					<td>Informe Por Item</td><td><input type="radio" id="tipoInforme" name="tipoInforme" value="facturaPorItem" onclick="javaScript:cambiarOperacionReporte(this.value,'${usuario.tipo}');"></td>
					<td colspan=2></td>
				</tr>
				<tr>					
					<td>Informe Factura</td><td><input type="radio" id="tipoInforme" name="tipoInforme" value="reporteFactura" onclick="javaScript:cambiarOperacionReporte(this.value,'${usuario.tipo}');" checked></td>
					<td colspan=2></td>				
				</tr>
				<tr>
					<td>Informe por Partida</td><td><input type="radio" id="tipoInforme" name="tipoInforme" value="reporteRubroItem" onclick="javaScript:cambiarOperacionReporte(this.value,'${usuario.tipo}');"></td>
					<td colspan=2></td>
				</tr>
				<tr>
					<td>Resumido:</td><td>
						<input type="checkbox" name="resumido" id="resumido">
					</td>
					<td colspan=2>
				</tr>
				<tr>	
				    <td align="right" colspan=4><button id="buscar">Buscar</button></td>
				  </tr>
				</table>
				<input type="hidden" id="operacion" name="operacion" value="reporteFactura"></br>		
								
			</form>
		</fieldset>
	</div>	
</div>
</body>
</html>