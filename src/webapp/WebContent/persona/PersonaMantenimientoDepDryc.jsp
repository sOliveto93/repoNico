<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript"> 
var idAux, idPersonaAux;
$(function(){
	
	$("#personaFacturas").fixheadertable({ 
		caption   : 'Facturas Persona',
        colratio  : [100,100,150,350 , 150],
        width     : 890,
        zebra     : true
	});
	console.log("********************formatoCuotasTabla********************")
	$("#cuotasTabla").fixheadertable({ 
		caption   : 'Cuotas Persona',
        colratio  : [20,100, 200, 50, 50, 50, 50,100 ,100, 100,100,50,200,50,50,30],
        width     : 1330,
        zebra     : true
	});

	myCalendar = new dhtmlXCalendarObject(["fechaPgo"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
	
	
	$("#dialog-cuota").dialog({
		autoOpen: false,
		height:520,
		width:440,
		modal: true,
		buttons: {
			'Aceptar': function(){
				if(validaPago()){
					asignarPagoJson();
				}else{
					alert("Debe cargar ambos campos ( Fecha - Monto Pago ) o ambos vacios");
				}
			},
			'Cancel': function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
	$("#dialogEliminar").dialog({
		autoOpen: false,
		height:180,
		width:300,
		modal: true,
		buttons: {
			'Aceptar': function(){				
				if(isEmpty($("#comentarioDelete").val())==false){
					eliminar(idAux,idPersonaAux,$("#comentarioDelete").val()); 
				}else{
					alert("Debe escribir algo para el registro de la baja...");
					event.preventDefault();
					$("#comentarioDelete").focus();
				}
				$(this).dialog('close');
			},
			'Cancel': function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
});
function validaPago(){
	var respuesta = true;
	var montoPgo = $("#montoPgo").val();
	var fecha = $("#fechaPgo").val();
	// si el origen de pago es todo pago se debe verificar que tenga el codigo de todo pago.
	
	if((isEmpty(fecha) && !isEmpty(montoPgo)) || (!isEmpty(fecha) && isEmpty(montoPgo))){
		    respuesta = false;
	}
	return respuesta;
}
/* function registrarDeleteSiNo(id,idPersona){
	if(confirm("Desea dejar registrada la baja?")==true){
		idAux=id;
		idPersonaAux=idPersona;
		$("#dialogEliminar").dialog('open');	
	}else{
		eliminar(id,idPersona,"");
	}
}
function eliminar(id,idPersona,comentarioDelete){
	if(seguroEliminar("Cuota")){
		$.post('PersonaServlet?operacion=borrarCuotaPersona&id='+id+"&idPersona="+idPersona+"&comentarioDelete="+comentarioDelete,'', function(data){
			if(data.status == "OK"){
				cargaCuotasPersona(data.lista[0].cuotas);
			}
		});
	}
	
} */
function openDialogCuota(monto1,monto2,vtoMonto,fecha,tipo,idCuota,exceptuarMora){	
	$("#monto1").val(monto1);
	$("#monto2").val(monto2);
	$("#vtoMonto").val(vtoMonto);
	$("#idCuota").val(idCuota);
	$("#idPagoTipo").val(tipo);

	console.log("exceptuarMora: ",exceptuarMora);
	if(exceptuarMora == false){
		$("#ExceptuarCuota").removeAttr('checked');	
	}else if(exceptuarMora == true){
		$("#ExceptuarCuota").attr('checked', 'checked');
	}
	
	//prueba para la fecha
	var currentTime = new Date();
	var fecha =	currentTime.getDate() + "-0" +(currentTime.getMonth()+1) + "-"+currentTime.getFullYear();
	$('#fechaPagoCarga').val(fecha);
	 //fin de prueba
	
	$("#dialog-cuota").dialog('open');
	document.edicionCuotaPago.operacion.value = "actualizaCuotaPersona";
}

function buscaCuotasPersona(idPersona){
	document.edicionCuotaPago.idPersona.value = idPersona;
	document.edicionCuotaPago.operacion.value = "cuotasPersonaJson";
	$.post('PersonaServlet',$("#edicionCuotaPago").serialize(), function(data){
		if(data.status == "OK"){
			cargaCuotasPersona(data.lista[0].cuotas);
		};
	});
}

function cargaCuotasPersona(cuotasPersona){
	var string="";
	var entro = false;
	for(i=0;i<cuotasPersona.length ;i++){
		if(cuotasPersona[i].estado.valor != 0){
			while(entro==false){
				entro = true;
			}
			string = string + filaJson(cuotasPersona[i]);
		}
	}
	$('#cuotas').html(string);
}
function asignarPagoJson(){	
	$.post('PersonaServlet',$("#edicionCuotaPago").serialize(), function(data){
		if(data.status == "OK"){
			actualizarFila(data.lista[0]);
		};
	});
}
function actualizarFilas(pagos){
	console.log("actualizarFILAS: " + pagos.length);
	for(var i=0;i<=pagos.length;i++){
		actualizarFila(pagos[i]);
	}
}

function actualizarFila(pago){
	
	if(pago.estado.activado == true){
		
		var newRow = filaJson(pago);		
		//$('#'+pago.id).add(newRow);
		$('#'+pago.id).replaceWith(newRow);
	}
	$("#dialog-cuota").dialog('close');
}

function filaJson(pago){

	var montoPago = "0";
	var pagado = "No";
	var fechaPago = "";
	var monto1 = pago.monto1;
	var monto2 = pago.monto2;
	var total = Number(monto1) + Number(monto2);
	if(pago.fechaPgo){
		montoPago = pago.monPgo;
		pagado ='Pagado'; 
	}

	var observaciones="";
	if(pago.observaciones){
		observaciones = pago.observaciones;
	}
	if(pago.fechaPgo){
		fechaPago = pago.fechaPgo;
	}
	var string = 
	'<tr id="'+pago.id+'">'+
		'<td class="ui-widget-content">'+pago.numero+'</td>'+
		'<td class="ui-widget-content">'+pago.fecha+'</td>'+
		'<td class="ui-widget-content">'+pago.grupo.codigo+' - '+pago.grupo.descripcion+'</td>'+
		'<td class="ui-widget-content">'+total+'</td>'+
		'<td class="ui-widget-content">'+pago.vtoMonto +'</td>'+
		'<td class="ui-widget-content">'+pago.grupo.vencimientosDias+'</td>'+
		'<td class="ui-widget-content">'+pago.grupo.segundoVencimientosDias+'</td>'+
		'<td class="ui-widget-content">'+observaciones+'</td>'+
		'<td class="ui-widget-content">'+pagado+'</td>'+
		'<td class="ui-widget-content">'+montoPago+'</td>'+
		'<td class="ui-widget-content">'+fechaPago+'</td>'+
		'<td class="ui-widget-content">'+pago.tipo+'</td>'+
		'<td class="ui-widget-content">'+pago.detallePago+'</td>';
		
	//Si esta pago!
	if((pago.estado.valor == "Incierto") || (!isEmpty(pago.fechaPgo))){
		string = string + '<td align="center" class="ui-widget-content"><img src="images/pencilGris.png" width="15px" title="Cuota Paga"/></td> <td align="center"><img src="images/deleteGris.png" width="15px" title="Cuota Paga" /></td>';
		string = string + '<td class="ui-widget-content"><img src="images/pesosDeleteGris.png" width="15px" title="Operaciion Deshabilitada" /></td>';
		//Si No esta pago
	}else if((pago.estado.activado) && (isEmpty(pago.fechaPgo))){
		string = string + '<td align="center" class="ui-widget-content"><a href=""><img src="images/pencilGris.png" width="15px" title="Modificar Cuota"/></a></td> <td align="center"><img src="images/deleteGris.png" width="15px" title="Eliminar Cuota" /></td>';
		string = string + '<td class="ui-widget-content"><img src="images/pesosDeleteGris.png" width="15px" title="Operaciion Deshabilitada" /></td>';
	}
	
	string = string +'</tr>';
	return string;

}
function addDate(dateObject, numDays){
	dateObject.setDate(dateObject.getDate() + numDays);
	return dateObject.toLocaleDateString();
}


</script>

</head>
<body  onload="doOnLoad('${operacion}','${persona.documento.tipo.abreviacion}','${usuario.tipo}');">
<div class="recuadro">
	<%@ include file="/menu.jsp"%>
	<ul id="navigation">
		<li class="pencil"><a href="javascript:buscaDatosPersona();" title="Datos Persona"></a></li>
		<li class="signoPeso"><a href="javascript:cuotasPersona(${persona.id});" title="Cuotas Persona"></a></li>
		<li class="factura"><a href="javascript:facturasPersona();" title="Facturas Persona"></a></li>
	</ul>
	<div id="persona" align="left" style="display: none;">
		
	</div>
	<div id="datosPersona"  align="left" style="display: none;">
		<%@  include file="PersonaDatosAcademicosyPersonalesDepDryc.jsp"  %>
	</div>
 	<div id="cuotasPersona"  align="left" style="display: none;">
		<%@ include file="PersonaCuotasDepDryc.jsp" %>
	</div>
	<div id="facturasPersona"  align="left" style="display: none;">
		<%@ include file="PersonaFacturasDepDryc.jsp" %>
	</div>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    

<table id="botones">
	<tr>		
		<td>
			<button name="volver" id="volver">Volver</button>
		</td>
	</tr>
</table>

</div>

</body>

<script language="javascript">
var usuarioGlobalTipoUsuario;
$(function() {
	 $('#navigation a').stop().animate({'marginLeft':'-25px'},1500);
	 
	 $('#navigation > li').hover(
	  function () {
	   $('a',$(this)).stop().animate({'marginLeft':'-2px'},200);
	  },
	  function () {
	   $('a',$(this)).stop().animate({'marginLeft':'-25px'},200);
	  }
	 );
	 $("#imprimir")
		.button()
		.click(function(){
			//alert("Hola");
			var idPersona = document.edicionCuotaPago.idPersona.value;
			buscaCarreraPersona(idPersona);
	});
	 $("#cupones")
		.button()
		.click(function(){
			//alert("Hola");
			var idPersona = document.edicionCuotaPago.idPersona.value;
			buscaCarreraPersonaCupones(idPersona);
	});
});

function buscaDatosPersona(){
	$("#cuotasPersona").hide();
	$("#facturasPersona").hide();
	$("#datosPersona").show('slow');
	buscaCarreras();
}
function cuotasPersona(idPersona){
	$("#datosPersona").hide();
	$("#facturasPersona").hide();
	$("#cuotasPersona").show('slow');
	buscaCuotasPersona(idPersona);
}
function facturasPersona(idPersona){
	$("#datosPersona").hide();
	$("#cuotasPersona").hide();
	$("#facturasPersona").show('slow');
	//buscaCuotasPersona(idPersona);
}
function doOnLoad(operacion,tipoDocumento,usuario){
	if(operacion=='mantenimiento_persona'){
		$("#cuotasPersona").hide();
		$("#facturasPersona").hide();
		$("#datosPersona").show("slow");
	}else if(operacion=='cuotasPersona'){
		$("#datosPersona").hide();
		$("#facturasPersona").hide();
		$("#cuotasPersona").show("slow");
	}else if(operacion=='facturasPersona'){
		$("#datosPersona").hide();
		$("#facturasPersona").hide();
		$("#cuotasPersona").show("slow");
	}else{
		alert("Error en la Operacion  -"+operacion);
	}
	usuarioGlobalTipoUsuario = usuario;
	myCalendar = new dhtmlXCalendarObject(["fechaIngreso"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
}
</script>
</html>