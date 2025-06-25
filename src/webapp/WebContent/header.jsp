<link type="text/css" href="css/dhtmlxcalendar.css" rel="stylesheet"></link>
<link type="text/css" href="css/skins/dhtmlxcalendar_dhx_skyblue.css" rel="stylesheet"></link>
<link type="text/css" href="css/jquery-ui-1.8.2.custom.css" rel="stylesheet" /> 

<script type="text/javascript" src="js/dhtmlxcalendar.js"></script>
<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter_filter.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="js/validador.js"></script>
<script type="text/javascript" src="js/jquery.fixheadertable.min.js"></script>

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.form-defaults.js"></script>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">


<script  type="text/javascript">

$.ajaxSetup({
	cache: true,
	dataType: 'json',
	error: function(xhr, status, error) {
		alert("error, Comuniquese con el Administrador."+error);
	},
	timeout: 60000, // Timeout of 60 seconds
	type: 'POST'
});

function setTituloAbrir(tituloVentana){
	$('#dialog-form').dialog('option', 'title', tituloVentana);
	$('#dialog-form').dialog('open');
}

function funcionAgregar(nombreForm,tituloVentana){
	$("#agregar")
		.button()
		.click(function (){
			$('#dialog-form').dialog('option', 'title', tituloVentana);
			$('#dialog-form').dialog('open');
		});
}

function funcionAgregar(tituloVentana){
	funcionAgregar("#agregar",tituloVentana)
}


function funcionVolver(){
	$("#volver")
		.button()
		.click(function(){
			document.button.submit();
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
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
}

function funcionTabla(){
	$('#myTable').dataTable(
			//{	"bJQueryUI": true,	"sPaginationType": "full_numbers"}
			{
				"bPaginate":  false,
				"bLengthChange": true,
				"bFilter": true,
				"bSort": true,
				"bProcessing": true,
				"bInfo": true,
				"bAutoWidth": true,
				"bJQueryUI": true,
				"sLengthMenu": "Show _MENU_ entries",
				"sPaginationType": "full_numbers"
			}
		);
	if($('#cartel').val()!=""){
		window.alert($('#cartel').val());
	}	
	
}


function funcionExcel(){
	$(".botonExcel").click(function(event) {
		$("#datos_a_enviar").val( $("<div>").append( $("#myTable").eq(0).clone()).html());
		$("#FormularioExportacion").submit();
	});
}

function seguroEliminar(tipo){
	if(confirm('Esta seguro de que desea eliminar la '+tipo+' ?')){
		return true;
	}
	return false;
}
</script>