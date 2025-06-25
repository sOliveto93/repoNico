<link type="text/css" href="<%=request.getContextPath()%>/css/dhtmlxcalendar.css" rel="stylesheet"></link>
<link type="text/css" href="<%=request.getContextPath()%>/css/skins/dhtmlxcalendar_dhx_skyblue.css" rel="stylesheet"></link>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui-1.8.2.custom.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath()%>/css/mio.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath()%>/css/menu.efecto.slide.css" rel="stylesheet"/>
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery.modal.css" rel="stylesheet"/>
<!-- produce error al ejecutarlo -->
<!--
<link type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui.css" rel="stylesheet" >
<link type="text/css" href="<%=request.getContextPath()%>/css/base.css" rel="stylesheet" >
<link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui.js"></script> 
-->

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dhtmlxcalendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.tablesorter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.tablesorter_filter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.maskMoney.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validador.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.maskedinput-1.3.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/spin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.easyModal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.confirm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jony-printer.js"></script>
<script type="text/javascript">
var spiner; 
$.ajaxSetup({
	cache: true,
	dataType: 'json',
	error: function(xhr, status, error) {
		alert("error, Comuniquese con el Administrador."+error);
	},
	timeout: 60000, // Timeout of 60 seconds
	type: 'POST'
});
function seguroEliminar(tipo){
	if(confirm('Esta seguro de que desea eliminar  '+tipo+' ?')){
		return true;
	}
	return false;
}
function funcionActualizar(nombreFormulario){
	console.log("FuncionActualizar abm_headers: Nombre Formulario: ",nombreFormulario);
	$("#actualizar")
	.button()
	.click(function(){
		document.getElementById(nombreFormulario).submit();
	});
}
function dialogoEdicion(alto,ancho){
	$("#dialog-form").dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Grabar': function() {
				if (v.exec()){
					document.edicion.submit();
				}
			},
			Cancel: function(){
				limpiarForm('edicion');
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
}
function dialogoGenerico(alto,ancho,nombreDialog,formulario){
	$("#"+nombreDialog).dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Generar': function() {
					document.getElementById(formulario).submit();
			},
			Cancel: function(){
				limpiarForm(formulario);
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
}
function dialogoVista(alto,ancho,nombre){
	$("#"+nombre).dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			Cancel: function(){
				limpiarForm('vista');
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
}
function CabeceraFijaTabla(nombreTabla){
	$('#'+nombreTabla).fixheadertable(
			{
				theme : 'ui',
				height : 300,
				//width : 500,
				zebra : true,
				sortable : true,
				sortedColId : 0,
				sortType : [ 'integer', 'string', 'string', 'string','string', 'date' ],
				dateFormat : 'm/d/Y'
			});
	
}
function limpiarForm(formulario){
	$('#'+formulario).each (function(){
		  this.reset();
	});
}
//borra todos los espacios en blanco que tenga el String
function trim (myString)
{
	return myString.replace(/^\s+/g,'').replace(/\s+$/g,'').replace(/\s/g,'');
}

function submitForm(form){
	document.getElementById(form).submit();
}
function bloquearTecla(nombreObj,numeroAscii){
	$("#"+nombreObj).keypress(function(event) {
		  if ( event.which == numeroAscii) {
		     event.preventDefault();
		   }
	});
}
//BLOQUEA EL F5 
function Verificar(){
	var tecla=window.event.keyCode;
	if (tecla==116) {
		event.keyCode=0;
		event.returnValue=false;
	}
}
function removeBackgroundColor(rowId) {
	$("#"+rowId).closest('tr').children('td,th').css('background-color','#FFFFCC');
}
function setBackgroundColor(rowId,color){
	$("#"+rowId).children('td, th').css('background-color',color);
}
function openDialog(nombreDialog){
	$('#'+nombreDialog).dialog('open');
}
/**
 * Para usar las funciones loopingLoaderPlay/Stop
 * se debe incluir spin.js 
 * se le pasa el obj donde se va a situar el spiner ( tag DIV )
 */
function loopingLoaderPlay(obj){
	spiner = new Spinner().spin(obj);
}	
function loopingLoaderStop(){
	spiner.stop();
}
function isEmpty(val){ 
	if ( val==null || val == "" || val=="undefined"){
		return true; 
	}
	return false;
}
function copiarValor(campoValor,campoCopia){
	campoValor = $('#'+campoValor).val();
	$('#'+campoCopia).val(campoValor);
			
}
/*
 * Form: Nombre del form donde queremos insertar.
 * ElementString: tipo de objeto que queremos, input, botton...
 * TypeElement: hidden, text, radio, lo que corresponda al objeto que vamos a insertar
 * IdName: nombre y id del objeto.
 * Value: valor que va a tener el objeto.
 */
function createElement(form,elementString,typeElement,idName,value){
	var element = document.createElement(elementString);
	element.type = typeElement;
	element.id = idName;
	element.name = idName;
	$("#"+form).append(element);
	//$("#"+form).appendChild(element);
}
</script>