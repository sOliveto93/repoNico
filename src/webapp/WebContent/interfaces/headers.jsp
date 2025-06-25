<link type="text/css" href="css/dhtmlxcalendar.css" rel="stylesheet"></link>
<link type="text/css" href="css/skins/dhtmlxcalendar_dhx_skyblue.css" rel="stylesheet"></link>
<link type="text/css" href="css/jquery-ui-1.8.2.custom.css" rel="stylesheet" />
<link type="text/css" href="css/mio.css" rel="stylesheet" />
<link type="text/css" href="css/base.css" rel="stylesheet" >
 
<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="js/jquery.fixheadertable.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="js/dhtmlxcalendar.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter_filter.js"></script>
<script type="text/javascript" src="js/validador.js"></script>
<script type="text/javascript" src="js/jquery.maskedinput-1.3.js"></script>


<script type="text/javascript">
$.ajaxSetup({
	cache: true,
	dataType: 'json',
	error: function(xhr, status, error) {
		alert("error, Comuniquese con el Administrador."+error);
	},
	timeout: 60000, // Timeout of 60 seconds
	type: 'POST'
});
</script>