<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>

<title>Facturas</title>
</head>
<body onload="doOnLoad();" onKeyDown="javascript:Verificar();">
<div class="recuadro">
<%@ include file="/menu.jsp"%>
	<input type="hidden" name="cartel" value="${msg}" id="cartel" />
		<form name="fac" id="fac" action="FacturaServlet" method="post">
		<c:set var="itemsFactura" scope="request" value="${factura.facturaItems}"/>
			<table id='cabeceraFactura' border="1">
				<tr>
					<td>
						<!-- cambiar esto para que traiga los datos de mapeo_tipo_doc -->
						<label for="tipoDocumento">Tipo Documento:</label>
						<select name="tipoDocumento" id="tipoDocumento">
							<option value="">Tipo Documento</option>
							<option value="DN">DNI</option>
							<option value="PA">PA</option>
							<option value="CI">CI</option>
							<option value="LE">LE</option>
							<option value="LC">LC</option>
							<option value="CU">CUIT</option>
						</select>
						<label for="numeroDocumento">Nro Documento:</label>
						<input name='numeroDocumento' id='numeroDocumento' onblur="javascript:traerDatosPersona(this.value);" value='${factura.persona.documento.numero}' size="10">
					</td>
					<td>
						<label for="numeroFactura">Nro Factura:</label>
						<input name='numeroFactura' id='numeroFactura' value='${factura.nro}' readonly="readonly">
						<a href="javascript:modificarNumeroFactura();"><img src="images/pencil.png" width="20px" title="Modificar Carrera"/></a>
					</td>
					<td colspan="2">
						<label for="fechaDePago">Fecha:</label>
						<input name='fechaDePago' id='fechaDePago' value='${factura.fechaPago}' readonly="readonly">
					</td>
				</tr>
				<tr>
					<td>
						<label for="descripcionBasica">Persona:</label>					
						<input name='descripcionBasica' id='descripcionBasica' onblur="javascript:verificarPersona();" value='${factura.persona.nombreCompleto}' size="75">
					</td>
					<td align="left">
						T. Pago:
						<select name="tipoPago" id="tipoPago">
						<c:forEach items="${tiposPago}"  var="tipoPago">
							<option value="${tipoPago.id}"
									<c:if test="${factura.tipoPago.id==tipoPago.id}">
										selected="selected"
									</c:if>
									>${tipoPago.descripcion}</option>
						</c:forEach>
						</select>
					</td>
					<td>
						O. Pago
						<select name="origenPagoS" id="origenPagoS">
						<c:forEach items="${origenesPago}"  var="origenPago">
							<c:if test="${origenPago.descripcion == 'ventanilla'}">
								<option value="${origenPago.id}">${origenPago.descripcion}</option>
							</c:if>
						</c:forEach>
						</select>
					</td>
					<td>
						Sucursal:
						<select name='idSucursalCobro' id='idSucursalCobro'>
							<c:forEach items="${sucursales}"  var="sucursal">
								<option value="${sucursal.codigo}"
								<c:if test="${factura.sucursalCobro.codigo==sucursal.codigo}">
									selected="selected"
								</c:if>
								>${sucursal.descri}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="descripcionExtendida">Descripcion</label>
						<input type="hidden" name="numeros_grupos" value="${grupo_numeros}" id="numeros_grupos">
						<input name='descripcionExtendida' id='descripcionExtendida'
							<c:if test="${factura.descripcionExtendida==''}">
								value='${factura.persona.carrerasActiva}'
							</c:if>
							<c:if test="${factura.descripcionExtendida!=''}">
								value='${factura.descripcionExtendida}'
							</c:if>
						 size="72">
						<input name='idPersona' id='idPersona' value='${factura.persona.id}' type="hidden">
						
					</td>
					<td>
						<label for="tipoFactura">T.Factura</label>
						<select name="tipoFactura" id="tipoFactura">
							<c:forEach items="${tiposFactura}"  var="tipoFactura">
								<option value="${tipoFactura.id}"
									<c:if test="${tipoFactura.id == factura.tipoFactura.id}">
										selected="selected"
									</c:if>
								>${tipoFactura.descripcion}</option>
							</c:forEach>
						</select>
					</td>
					<td colspan="2">
						<label for="formaDePago">F.Pago</label>
						<select name='formaDePago' id='formaDePago'>
							<c:forEach items="${formasPago}"  var="formaPago">
								<option value="${formaPago.id}"
									<c:if test="${formaPago.id == factura.formaPago.id}">
										selected="selected"
									</c:if>
								>${formaPago.descripcion}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<label>Razon Social</label>
						<input type="text" id="razonSocial" name="razonSocial"  value='${factura.razonSocial}' readonly>
					</td>
					<td colspan="3">
						<label>ResponsableIva</label>
						<select name="responsableIva" id="responsableIva">
							<c:if test="${factura.responsableIva==''}"></c:if>
							<option value="Consumidor Final"              							<c:if test="${factura.responsableIva=='Consumidor Final'}">selected</c:if>                                       >Consumidor Final</option>
							<option value="IVA Responsable Inscripto"     							<c:if test="${factura.responsableIva=='IVA Responsable Inscripto'}">selected</c:if>	                             >IVA Responsable Inscripto</option>
							<option value="IVA Responsable no Inscripto"  							<c:if test="${factura.responsableIva=='IVA Responsable no Inscripto'}">selected</c:if>				             >IVA Responsable no Inscripto</option>
							<option value="IVA no Responsable"			  							<c:if test="${factura.responsableIva=='IVA no Responsable'}">selected</c:if> 				                     >IVA no Responsable</option>
							<option value="IVA Exento"			 							<c:if test="${factura.responsableIva=='IVA Exento'}">selected</c:if>				                         >IVA Exento</option>
							<option value="Responsable Monotributo"		  							<c:if test="${factura.responsableIva=='Responsable Monotributo'}">selected</c:if>				                 >Responsable Monotributo</option>
							<option value="no Categorizado"		  							<c:if test="${factura.responsableIva=='no Categorizado'}">selected</c:if>				                 >no Categorizado</option>
							<option value="Proveedor del Exterior"		  							<c:if test="${factura.responsableIva=='Proveedor del Exterior'}">selected</c:if>				                 >Proveedor del Exterior</option>
							<option value="Cliente del Exterior"		  							<c:if test="${factura.responsableIva=='Cliente del Exterior'}">selected</c:if>				                     >Cliente del Exterior</option>
							<option value="IVA Liberado Ley N 19.640"	  							<c:if test="${factura.responsableIva=='IVA Liberado Ley N 19.640'}">selected</c:if>				             >IVA Liberado  Ley N 19640</option>
							<option value="IVA Responsable Inscripto  Agente de Percepcion"	<c:if test="${factura.responsableIva=='IVA Responsable Inscripto  Agente de Percepci&oacute;n'}">selected</c:if>	 >IVA Responsable Inscripto  Agente de Percepci&oacute;n</option>
							<option value="Pequeno Contribuyente Eventual"		            <c:if test="${factura.responsableIva=='Pequeno Contribuyente Eventual'}">selected</c:if>				   	 >Pequeno Contribuyente Eventual</option>
							<option value="Monotributista Social"						 			<c:if test="${factura.responsableIva=='Monotributista Social'}">selected</c:if> 				  				 >Monotributista Social</option>
							<option value="Pequeno Contribuyente Eventual Social"			<c:if test="${factura.responsableIva=='Pequeno Contribuyente Eventual Social'}">selected</c:if>			 >Pequeno Contribuyente Eventual Social</option>

						</select>
						<!--  <input type="text" id="cuit"  name="cuit" value='${factura.cuit}' readonly> -->
					</td>
				</tr>
			</table>
			<table id='items'>
				<tr>
					<td>R.</td>
					<td>Concepto</td>
					<td>Descripcion</td>
					<td>Pr.Unitario</td>
					<td>Cantidad</td>
					<td>Rubro</td>	
					<td>Codigo</td>
				</tr>
				<tr>
					<td><input name="codigos" id="codigos" size="4"  onblur="javascript:traerDatosRubro(this.value,'${facturaItems}');" disabled="disabled"></td>
					<td><input name="conceptoRubro" id="conceptoRubro"  value="" disabled="disabled" readonly></td> <!-- onClick="javascript:mostrarRubros();" -->
					<td><input name="descripcionItem" id="descripcionItem" value="" disabled="disabled" readonly></td> 
					<td><input name="precio" id="precio" value="" disabled="disabled" readonly></td>
					<td><input name="cantidadItem" id="cantidadItem" value="" disabled="disabled" onblur="javasctipt:verificarLimiteItems();"></td>
					<td><input name="rubroCodigo" id="rubroCodigo" value="" disabled="disabled" readonly></td>
					<td><input name="codigoTiposRubroCobro" id="codigoTiposRubroCobro" value="" disabled="disabled" readonly></td>
					<td><input name="idTiposRubroCobro" id="idTiposRubroCobro" disabled="disabled" value="" type="hidden"></td>
					<td><input name="idRubroCobro" id="idRubroCobro" value="" type="hidden" ></td>		
				</tr>
				<c:set var="subTotal" value="0"/>		
				<c:set var="puedeImprimir" value="0"/>
				<c:forEach items="${facturaItems}" var="item">
					<input name="idaux" id="idaux" value="si" type="hidden" >
					<tr>
						<td>${item.codigo}</td>
						<td>
							<c:if test="${item.facturaItemTipo.rubroCobro.codigo==9}">
							 	${item.descripcion}
							</c:if>
							<c:if test="${item.facturaItemTipo.rubroCobro.codigo!=9}">
								${item.facturaItemTipo.descripcion}
							</c:if>
						</td>
						<td>${item.facturaItemTipo.rubroCobro.descripcion}</td>
						<td>${item.precio}</td>
						<td>${item.cantidad}</td>
						<td>${item.facturaItemTipo.rubroCobro.codigo}</td>
						<td>${item.facturaItemTipo.codigo}</td>
						<c:set var="subTotal" value="${subTotal + (item.precio*item.cantidad)}"/>	
						<c:set var="puedeImprimir" value="1"/>
					</tr>
				</c:forEach>
				
			</table>
			<div align="right">Total: <c:out value="${subTotal}"></c:out></div>
			<input name="operacion" id="operacion" value="" type="hidden"  >
			<input name="persistir" id="persistir" value="" type="hidden" >
			<input name="NumeroCuotasDeudaPersona" id="NumeroCuotasDeudaPersona" value="" type="hidden" >
		</form>
	<hr></hr>
	<button id="agregarItem" name="agregarItem" onclick="javascript:activarAgregarItem()">Agregar Item</button>
	<button id="guardarItem" name="guardarItem"  style="visibility:hidden">Guardar Item</button>
	
	<button id="guardarFactura" name="guardarFactura" 
		<c:if test="${puedeImprimir==0}">
			disabled="disabled"
		</c:if>  
	>Imprimir Factura</button>
	
	<form action="FacturaServlet?operacion=listar&limpiarSession=1" method="post" name="button">
		<button id="volver" name="operacion">Volver</button>
	</form>
	<div id="dialog-vista" title="Busqueda de un Concepto">
		<table>
			<tr>
				<td>Codigo</td>
				<td>Descripcion</td>
			</tr>
			<c:forEach items="${rubrosCobro}"  var="rubroCobro">
			<tr>
				<td><a href="javascript:traerDatosRubroCobroItemTipos(${rubroCobro.id},'${facturaItems}');">${rubroCobro.codigo}</a></td> 
				<td>${rubroCobro.descripcion}</td>
			</tr>
			</c:forEach>
		</table>
		<table>
			<thead>
				<th>Codigo</th>
				<th>Descripcion</th>
			</thead>
			<tbody id="datos_tiposRubro">
			</tbody>
		</table>
	</div>
	<div id="dialog-numeroFacturaCambiar" title="Modificar numero factura">
		Nro Factura: <input name='numeroFacturaNuevo' id='numeroFacturaNuevo' value='' type="text" onblur="verificarNumeroFactura();">
		<input name='numeroFacturaOriginal' id='numeroFacturaOriginal' value='' type="hidden">
	</div>
</div>
<div  id="itemsFacturadiv" style="display: none">

<select id="itemsFactura">
<!-- codigo id_cbr_tipo-->
	<c:forEach items="${ItemsPrueba}"  var="item">
		<option id="${item.codigo}">${item.cantidad}</option>
	</c:forEach>
</select>
</div>
</body> 
<script language="JavaScript">
if($('#cartel').val()!=""){
	window.alert($('#cartel').val());	
}
var a_fields = {
		'codigos': {'l': 'Codigo', 'r': true,'mx':250},
		'cantidadItem':{'l':'Cantidad Item', 'r': true,'mx':250},
		'conceptoRubro':{'l':'Concepto', 'r': true,'mx':250},
		
},
o_config = {
		'to_disable' : ['Aceptar'],	'alert' : 1
};

var v = new validator('fac', a_fields, o_config);
var personaNueva = false;
var precio = 0;
var maximoCuotas;
	$(function(){
		var tab = $('#tabs').tabs();
		//dialogoEdicion(500,990);
		dialogoVista(500,990,"dialog-vista");
		$("#volver")
			.button()
			.click(function(){
				var resp = confirm("Desea salir? Perdera la factura.");
				if(resp == false){
					event.preventDefault();
				}
		});
		$('#tipoFactura').change(function(event){
			if($('#tipoFactura').val()==2){			
				$('#precio').removeAttr('readonly');
			}else{
				$('#precio').prop( "disabled", true );
			}		
		});
		$("#guardarItem")
			.button()
			.click(function(){
				if (v.exec()){
					verificarLimiteItems();
					document.fac.operacion.value = "alta";
					document.fac.persistir.value = "false";
					//console.log("Cuit: "+document.fac.cuit.value + " Razon Social: "+document.fac.razonSocial.value);
					maximoCuotas = 0;
					document.fac.submit();
				}else{
					event.preventdefault();
				}
		});
		
		$("#guardarFactura")
			.button()
			.click(function(event){
				if($("#conceptoRubro").val()!="" || $("#idaux").val()=="si"){
					document.fac.operacion.value = "alta";
					document.fac.persistir.value = "true";
					maximoCuotas = 0;
					document.fac.submit();
				}else{					
					alert("Agregue un codigo valido");
					event.preventDefault();
				}
		});
		$("#agregarItem").button();

		$("#dialog-numeroFacturaCambiar").dialog({
			autoOpen: false,
			height:200,
			width:350,
			modal: true,
			buttons: {
			'Cambiar': function() {
				numeroFactura  = $("#numeroFacturaNuevo").val();
				$("#numeroFactura").val(numeroFactura );
				$.post('FacturaServlet?operacion=cambiaNumeroFactura&nuevoNumeroFactura='+numeroFactura,'', function(data){
					if(data.status == "OK"){
						alert(data.lista[0].msj);
					};
				});
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
	});
	
	function verificarPersona(){
		var nombrePersona,numeroDocumento,tipoDocumento;
		nombrePersona = $("#descripcionBasica").val();
		numeroDocumento = $("#numeroDocumento").val();
		tipoDocumento = $("#tipoDocumento").val();
		if(personaNueva == true){
				$.post('PersonaServlet?operacion=altaPersonaFactura&nombrePersona='+nombrePersona+"&numeroDocumento="+numeroDocumento+"&tipoDocumento="+tipoDocumento,'', function(data){
					if(data.status == "OK"){
						mostrarDatosPersona(data.lista[0]);
					};
				});
		}
	}
	function traerDatosPersona(numeroDocumento){
		var tipoDocumento = document.getElementById("tipoDocumento").value;
		numeroDocumento = trim(numeroDocumento);
		document.getElementById("numeroDocumento").value=numeroDocumento;
		if (numeroDocumento != ''){
			$.post('FacturaServlet?operacion=verPersonaJson&numeroDocumento='+numeroDocumento+"&tipoDocumento="+tipoDocumento,'', function(data){
				if(data.status == "OK"){
					var persona = data.lista[0];
					if(persona.id){
						mostrarDatosPersona(persona);
					}else{
						personaNueva = true;
					}
				};
			});
		}
	}
	function traerDatosRubroCobroItemTipos(id,facturaItems){
		$.post('FacturaServlet?operacion=verFacturaItemTiposJson&id='+id,'', function(data){
				mostrarDatosRubroCobroItemTipos(data,facturaItems);
		});
	}
	function mostrarDatosRubroCobroItemTipos(facturaItemTipos,facturaItems){
		var string= "";
		for(i = 0; i < facturaItemTipos.length; i++) {
			var rubro = new String(facturaItemTipos[i].rubro);
			var itemTipo = new String(facturaItemTipos[i].codigo);
	
			if(itemTipo.length==1)
				itemTipo = "0" + itemTipo;
			if(rubro.length==1)
				rubro = "0" + rubro;
			var codigo = rubro + itemTipo;
			
			if(!facturaItems){
				facturaItems = null;
			}
			
			string = string +
			'<tr>'+
				'<td><a href="javascript:traerDatosRubro(\''+codigo+'\','+'\''+facturaItems+'\');">'+facturaItemTipos[i].codigo+'</a></td>'+
				'<td>'+facturaItemTipos[i].descripcion+'</td>'+
			'</tr>';	
		}
		$('#datos_tiposRubro').html(string);
	}
	
function traerDatosRubro(codigos,facturaItems){
	console.log("codigos: ",codigos);
	if(codigos){
		//PARA EVITAR QUE UN CODIGO DE 4 DIGITOS O MAS LE AGREGUE UN 0-PRUEBA 05-04-19
		console.log(codigos.charAt(0) != '0' ," && ",codigos.length<4);
		if(codigos.charAt(0) != '0' && codigos.length<4){
			codigos = '0'+codigos;
		}
		console.log("Codigos: ",codigos);
		var itemTipo = codigos.substring(2,4);
		console.log("ItemTipo: ", itemTipo);
		//
		$.post('FacturaServlet?operacion=verRubroJson&codigos='+codigos,'', function(data){
			if(data.status == "OK"){
				console.log("itemTipo: ",itemTipo, " cod: ",codigos);
				
				if(itemTipo)
					mostrarDatosRubro(data.lista[0],itemTipo,facturaItems);
				else
					mostrarDatosRubro(data.lista[0],null,facturaItems);
			};
		});
	}else{
		clearCamposItem();
	}
}
// Estaba aca mirando para poder pagar items de mas de 3 digitos.
function mostrarDatosRubro(rubro,itemTipo,facturaItems){
	var cuotasLength ="";
	var tiposRubroCobro = rubro.tiposRubroCobro;
	var codigoRubro = rubro.codigo;
	var rubroCodigoItemCodigo;
	var codigo;
	var existe = 0;
	var cargoPrecio = 0;		
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
	
	for(var i = 0; i < tiposRubroCobro.length; i++) {
		if(tiposRubroCobro[i].estado != "Inactivo" && (tiposRubroCobro[i].codigo == codigo)){
			console.log("tiposRubroCobro[i].codigo: ",tiposRubroCobro[i].codigo," - codigo: ",codigo, " - Estado: ",tiposRubroCobro[i].estado);
			$('#codigos').val(rubroCodigoItemCodigo);
			$('#conceptoRubro').val(tiposRubroCobro[i].descripcion); // si el codigo es 9 tambien se tiene que poder modificar esta parte
			$('#codigoTiposRubroCobro').val(tiposRubroCobro[i].codigo);
			$('#idTiposRubroCobro').val(tiposRubroCobro[i].id);
			$('#rubroCodigo').val(rubro.codigo);
			$('#idRubroCobro').val(rubro.id);
			$('#cantidadItem').val("1");
			$('#cantidadItem').focus();
			var cargoPrecio = 0;
			//si el valor es 0 no lo cargo si es 1 si!
			var grupos = tiposRubroCobro[i].grupos;	
			
			//console.log("Grupos.Length: ",grupos.length);
			console.log("rubro.codigo: ",rubro.codigo);
			if(rubro.codigo==8 ){
				if($('#tipoFactura').val()==2){			
					$('#precio').removeAttr('readonly');
				}
			}
			if(rubro.codigo==9 && itemTipo==99){
				$('#precio').removeAttr('readonly');
				$('#cuit').removeAttr('readonly');
				$('#razonSocial').removeAttr('readonly');
				$('#descripcionItem').removeAttr('readonly');
				$('#descripcionExtendida').val("");
				$('#conceptoRubro').removeAttr('readonly');
				$('#conceptoRubro').removeAttr("disabled");
				$('#conceptoRubro').val();
				
			}
			//corregido por Bety, para que use el arreglo que se guardo separado por comas en #numeros_grupos
			arreglo = $('#numeros_grupos').val().split(',');	
			for(var j = 0; j < grupos.length; j++){
				var grupo = grupos[j];
				if(arreglo.indexOf(grupo.id+"")!=-1){
					console.log("operacion: traerCuotasPersona (ajax) "+grupo.id);
					$.post('FacturaServlet?operacion=traerCuotasPersona&idGrupo='+grupo.id+"&idPersona="+$('#idPersona').val(),'', function(data){
						if(data.status == "OK" && data.lista.length>0){
							var pagosList = data.lista;							
							var cuotasPorPagar=0;
							var precio = 0;
							var monto_control = 0;
							for(var x = 0; x < pagosList.length; x++){
								pagoAux =pagosList[x];							
								if (pagoAux.pagada == false && (monto_control ==0 || monto_control == pagoAux.monto1 + pagoAux.monto2)){
									cuotasPorPagar++;
									precio = pagoAux.monto1 + pagoAux.monto2;
									monto_control = pagoAux.monto1 + pagoAux.monto2;
									console.log("carga el precio: ",precio);
								}
							}
							maximoCuotas = cuotasPorPagar;
			
							console.log("MaximoCuotas: ",maximoCuotas);
							console.log("NumeroFactura: ",numeroFactura);
							
							var numeroFactura = $("#numeroFactura").val();
							if(numeroFactura){
								console.log("operacion : ConsultarFacturaItems");
								$.post('FacturaServlet?operacion=ConsultarFacturaItems','', function(itemsFacturaAux){
									if(itemsFacturaAux!=null && itemsFacturaAux.status == "OK"){
										itemsFactura = itemsFacturaAux.lista;
										var itemsLength = itemsFacturaAux.lista.length;
										for(i=0; i <= itemsLength ; i++){
											var codigo = $("#codigos").val();
											codigo = codigo.replace("0", "");
											if(maximoCuotas <= 0){
												limpiarCuota();
											}
										}
									} 
								});
							}				
							$('#precio').val(precio);								
							cargoPrecio = 1;
						}else{
							if(data.lista.length <= 0){								
								limpiarCuota();
							}
						}
					});
				}
			}//end for
			console.log("CargoPrecio: ",cargoPrecio);
			console.log("tiposRubroCobro[i].precio",tiposRubroCobro[i].precio);
			if(cargoPrecio == 0){
				$('#precio').val(tiposRubroCobro[i].precio);
				
			}
			
			existe = 1;
			$('#dialog-vista').dialog('close');
		}//fin if abajo del for
		
	}
	/*if(maximoCuotas <= 0){
		alert("Ya no contiene cuotas a pagar");
		limpiarItemCargado();
	}*/
	if(existe==0){
		clearCamposItem();
		alert("El Concepto/Codigo no existe");
		$('#codigos').focus();
	}
	
}

function mostrarDescRubroAbierto(desc){
	return desc;
}
function limpiarCuota(){
	alert($("#descripcionBasica").val() + " no adeuda cuotas de " + $("#conceptoRubro").val());
	limpiarItemCargado();
}
	function limpiarItemCargado(){
		$('#codigos').val("");
		$('#conceptoRubro').val("");
		$('#codigoTiposRubroCobro').val("");
		$('#idTiposRubroCobro').val("");
		$('#rubroCodigo').val("");
		$('#idRubroCobro').val("");
		$('#cantidadItem').val("");
		$('#precio').val("");
	}
	function verificarLimiteItems(){
		if(maximoCuotas!=0){
			console.log(maximoCuotas);		
			cantidadItem = $("#cantidadItem").val();
			if(cantidadItem > maximoCuotas){
				$("#cantidadItem").val(maximoCuotas);
				$("#cantidadItem").focus();
			}/*else if(!isEmpty(maximoCuotas)){
				Console.log("MaximoCuotas: ", maximoCuotas);
				maximoCuotas = maximoCuotas - cantidadItem;
				Console.log("MaximoCuotas-cantidadItem: ", maximoCuotas);
			}*/
		}
	}
	function mostrarDatosPersona(persona){
		personaNueva = false;
		if(persona.nombreCompleto==""){
			personaNueva = true;
		}
		var nombreCarrera;
		var inscripciones = persona.inscripciones;
		var cuotas = persona.cuotas;
		for(i=0 ; i < inscripciones.length ; i++){
			var inscripcion = inscripciones[i];
			if(inscripcion.estadoCarrera.valor == "Activo"){			
				nombreCarrera = inscripcion.nombreCarrera;
			}
		}
		//corregido de Bety para que inserte una sola vez cada valor, y separados por coma.
		var num = [];
		for(i=0 ; i < cuotas.length ; i++){
			var cuota = cuotas[i];
			if(num.indexOf(cuota.grupo.id)==-1){
				num.push(cuota.grupo.id);
			}
		}
		tipoDocumento = persona.tipoDocumento;
		$('#numeros_grupos').val(num.flat());
		$('#descripcionExtendida').val(persona.carrerasActiva);
		$('#descripcionBasica').val(persona.nombreCompleto);
		$("#tipoDocumento option[value="+tipoDocumento +"]").attr('selected', 'selected');
		$('#idPersona').val(persona.id);
	}
	function activarAgregarItem(){		
		if(document.getElementById("descripcionBasica").value!=""){
			$('#codigos').removeAttr("disabled");
			$('#conceptoRubro').removeAttr("disabled");
			$('#descripcionItem').removeAttr("disabled");
			$('#precio').removeAttr("disabled");
			$('#cantidadItem').removeAttr("disabled");
			$('#rubroCodigo').removeAttr("disabled");
			$('#codigoTiposRubroCobro').removeAttr("disabled");
			$('#idTiposRubroCobro').removeAttr("disabled");
		
			$('#guardarItem').attr("style","visibility:visible");
			$('#agregarItem').attr("style","visibility:hidden");
		}else{
			alert("Agregue primero el nombre de la persona");
		}
	}
	var myCalendar;
	function doOnLoad() {
		personaNueva = false;
		myCalendar = new dhtmlXCalendarObject(["fechaDePago"]);
		myCalendar.loadUserLanguage("esp");
		myCalendar.setDateFormat("%d-%m-%Y");
		var currentTime = new Date();
		var fecha =	currentTime.getDate() + "-0" +(currentTime.getMonth()+1) + "-"+currentTime.getFullYear();
		$('#fechaDePago').val(fecha);
	}
	function clearCamposItem(){
		$('#codigos').val("");
		$('#conceptoRubro').val("");
		$('#precio').val("");
		$('#codigoTiposRubroCobro').val("");
		$('#idTiposRubroCobro').val("");
		$('#rubroCodigo').val("");
		$('#idRubroCobro').val("");
		$('#cantidadItem').val("");
	}
	function mostrarRubros(){
		$('#dialog-vista').dialog('open');
	}
	function verificarNumeroFactura(){
		var numeroFactura, numeroFacturaOriginal;
		numeroFactura = $("#numeroFacturaNuevo").val();
		numeroFacturaOriginal = $("#numeroFacturaOriginal").val();
		if(numeroFactura < numeroFacturaOriginal){
			alert("El numero de factura ya existe");
			event.preventdefault();
		}
	}
	function modificarNumeroFactura(){
		var numeroFactura = $("#numeroFactura").val();
		if(!isEmpty(numeroFactura)){
			$("#numeroFacturaNuevo").val(numeroFactura);
			$("#numeroFacturaOriginal").val(numeroFactura);
			// ********************************************************
				
			// ********************************************************
			$('#dialog-numeroFacturaCambiar').dialog('open');
		}
	}
//FIN PRUEBA
</script>
</html>