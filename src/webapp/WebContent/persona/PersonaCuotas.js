var idAux, idPersonaAux;
$(function(){
	
	//formatoTablaCutas();
	//formatoTablaFactura();
	myCalendar = new dhtmlXCalendarObject(["fechaPgo"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
	
	
	$("#dialog-cuota").dialog({
		autoOpen: false,
		height:450,
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
/*
function validaPago(){
	var respuesta = true;
	var montoPgo = $("#montoPgo").val();
	var fecha = $("#fechaPgo").val();
	
	if((isEmpty(fecha) && !isEmpty(montoPgo)) || (!isEmpty(fecha) && isEmpty(montoPgo))){
		    respuesta = false;
	}
	return respuesta;
}
function registrarDeleteSiNo(id,idPersona){
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
	
}
function formatoTablaFactura(){
	$("#personaFacturas").fixheadertable({ 
		caption   : 'Facturas Persona',
        colratio  : [50,350, 50, 150],
        width     : 615,
        zebra     : true
	});
}
function formatoTablaCutas(){
	$("#cuotasTabla").fixheadertable({ 
		caption   : 'Cuotas Persona',
        colratio  : [20,100,400, 50, 50, 50, 50,200 ,100, 100,200,50,700,50,50,50],
        width     : 2300,
        zebra     : true
	});
}

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
		//var currentTime = new Date();
		//var fecha =	currentTime.getDate() + "-0" +(currentTime.getMonth()+1) + "-"+currentTime.getFullYear();
		//$('#fechaPgo').val(fecha);
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
	var tipoPago="";
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
		'<td class="ui-widget-content">'+origenPago+'</td>';
	
		console.log("origenPago: ",origenPago);
	//Si esta pago!
	if((pago.estado.valor == "Incierto") || (!isEmpty(pago.fechaPgo))){
		string = string + '<td align="center" class="ui-widget-content"><img src="images/pencilGris.png" width="15px" title="Cuota Paga"/></td> <td align="center"><img src="images/deleteGris.png" width="15px" title="Cuota Paga" /></td>';
		string = string + '<td class="ui-widget-content"><a href="javascript:anulaPagoPersona('+pago.id+')" title="Anular Pago Cuota"><img src="images/pesosDelete.png" width="15px" title="Anular Pago Cuota" /></a></td>';
		//Si No esta pago
	}else if((pago.estado.activado) && (isEmpty(pago.fechaPgo))){
		string = string + '<td align="center" class="ui-widget-content"><a href="javascript:openDialogCuota('+pago.monto1+','+pago.monto2+','+pago.vtoMonto+','+pago.fecha+',\''+pago.tipo+'\','+pago.id+','+pago.exceptuarMora+');"><img src="images/pencil.png" width="15px" title="Modificar Cuota"/></a></td> <td align="center"><a href="javascript:registrarDeleteSiNo('+pago.id+','+pago.idPersona+')" title="Eliminar Cuota"><img src="images/delete.png" width="15px" title="Eliminar Cuota" /></a></td>';
		string = string + '<td class="ui-widget-content"><img src="images/pesosDeleteGris.png" width="15px" title="Anular Pago Cuota" /></td>';
	}
	
	string = string +'</tr>';
	return string;

}
function addDate(dateObject, numDays){
	dateObject.setDate(dateObject.getDate() + numDays);
	return dateObject.toLocaleDateString();
}*/