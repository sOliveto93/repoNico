var idGrupoActual;
$(function(){
	bloquearTecla("cohorteFiltro",13);
	bloquearTecla("numeroDeDocumentoFiltro",13);
	$("#NoHagoNadaSoyUnaMentira")
		.button()
		.click(function(event){
			 	event.preventDefault();			 	
			}
		); 
	
	$("#modalLoader").easyModal({
		top: 100,
		autoOpen: false,
		overlayOpacity: 0.3,
		overlayColor: "#333",
		overlayClose: false,
		closeOnEscape: false
	});
	$('#filtrosChequeraPersonas').fixheadertable({
		caption   : 'Filtro Personas',
		height      : 50, 
	    width       : 830
	    });

	$('#tablaPersonas').fixheadertable({
	    caption   : 'Personas',
	    colratio    : [50, 480,150,100,50], 
	    height      : 280, 
	    width       : 830, 
	    zebra       : true
	});
	$('#parametrosChequeraPersonas').fixheadertable({
		caption   : 'Selecci�n de meses',
		height      : 30, 
	    width       : 830
	    });
	$('#parametrosChequeraPersonas2').fixheadertable({
		caption   : 'Meses a Exceptuar mora',
		height      : 30, 
	    width       : 830
	    });
	$('#parametrosChequeraPersonas3').fixheadertable({
		caption   : 'Tipo de cuota',
		height      : 30, 
	    width       : 830
	    });
	$("#imprimirChequerasGrupo").button().click(function(){
		$("#imprimir").val("si");
		$("#forzado").val("no");
		document.personasChequera.submit();
	});
	$("#generarChequerasGrupo").button().click(function(){
		$("#imprimir").val("no");
		$("#forzado").val("no");
		document.personasChequera.submit();
	});
	$("#regenerarChequerasGrupo").button().click(function(event){
		if(confirm("Esta seguro de que sea regenerar las chequeras ya generadas cambiando las cuotas ya cargadas en las personas")){
			$("#imprimir").val("no");
			$("#forzado").val("si");			
			document.personasChequera.submit();
		}else{
			event.preventDefault();
		}
	});
	$("#numeroDeDocumentoFiltro").change(function(){
		filtroChequeraPersona();
	});
	$("#cohorteFiltro").change(function(){
		filtroChequeraPersona();	
	});
	$('input[name=estadoFiltro]:radio').change(function(){
		filtroChequeraPersona();	
	});
});

function filtroChequeraPersona(){
	
	$.post('ChequeraServlet?operacion=buscaPersonasPorCarrera&idGrupo='+idGrupoActual+'&numeroDeDocumentoFiltro='+$("#numeroDeDocumentoFiltro").val()+'&cohorteFiltro='+$("#cohorteFiltro").val()+'&estado='+$('input[name=estadoFiltro]:radio:checked').val(),'', function(data){
		mostrarPersonasChequera(data);
	});
}

function buscaPersonasGrupo(idGrupo){
	$("#modalLoader").trigger('openModal');
	loopingLoaderPlay(document.getElementById('progressBar'));
	$("#personasChequera").hide();
	$("#grupoChequera").val(idGrupo);
	$("#numeroDeDocumentoFiltro").val("");
	$("#cohorteFiltro").val("");
	$("#id_meses input[type='checkbox']").attr("disabled",true);
	$("#id_filtrar_meses:first").attr('checked', true);
	$("#personasChequera").hide();
	$("#grupoChequera").val(idGrupo);

	$.ajaxSetup({
		error: function(xhr, status, error) {
			alert("error, Comuniquese con el Administrador."+error);
			loopingLoaderStop();
			$("#modalLoader").trigger('closeModal');
		}
	});

	$.post('ChequeraServlet?operacion=buscaPersonasPorCarrera&idGrupo='+idGrupo+'&estado=A','', function(data){
		idGrupoActual = idGrupo;
		mostrarPersonasChequera(data);
	});
}

function mostrarPersonasChequera(data){
	var personas = data.personas;
	//Comentado por BETY, pregunta a Juan porque lo agrego.
	//var anioCuotas = data.anioCuota;
	var string="";
	var carrerasPersona="";
	if(personas.length > 0){
		for(i=0;i<personas.length;i++){
			//alert(personas[i].estadoPersona.activado);
			if(personas[i].estadoPersona.valor == "Activo"){
				string = string + 
					"<tr>"+
						"<td><input type='checkbox' value='"+personas[i].id+"' name='idPersonaCheck' id='idPersonaCheck' checked='checked' class='check'></td>"+
						"<td>"+personas[i].nombreCompleto+"</td>"+
						"<td>"+personas[i].tipoDocumento+"</td>"+
						"<td>"+personas[i].numeroDocumento+"</td>"+
						"<td>"+personas[i].fondoBeca1+"</td>"+
					"</tr>";
			}
		}
	}else{
		string = string +"<tr><td>Sin Resultados</td></tr>";
	}
	//$("#anioCuota").value(anioCuotas);
	$("#personasChequera").show('slow');
	$("#personasChequeraBody").html(string);
	loopingLoaderStop();
	$("#modalLoader").trigger('closeModal');
}

function selectAll(){	
	if (!$('#aux').is(':checked')){			
		$('.check').attr('checked', false);			
	}else{
		$('.check').attr('checked', true);
	}
	
}
function cambiarFiltro(radioButton){
	if(radioButton.value == "si"){
		$( '#id_meses input[type="checkbox"]' ).attr("disabled",true);			
	}else{
		$( '#id_meses input[type="checkbox"]' ).removeAttr( "disabled" );
	}
}
function cambiarFiltro2(radioButton){
	if(radioButton.value == "si"){
		$( '#id_meses2 input[type="checkbox"]' ).attr("disabled",true);			
	}else{
		$( '#id_meses2 input[type="checkbox"]' ).removeAttr( "disabled" );
	}
}
function cambiarFiltro3(radioButton){
	if(radioButton.value == "si"){
		$( '#id_tipos input[type="checkbox"]' ).attr("disabled",true);			
	}else{
		$( '#id_tipos input[type="checkbox"]' ).removeAttr( "disabled" );
	}
}