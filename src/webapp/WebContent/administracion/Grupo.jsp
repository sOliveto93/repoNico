<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Grupo</title>
<%@ include file="/administracion/abm_headers.jsp"%>

<script type="text/javascript">
	$(function(){
			
		 $('#tablaGrupo').fixheadertable({
	         caption   : 'Grupos',
	         colratio    : [26, 400, 50, 400, 26], 
	         height      : 200, 
	         width       : 938, 
	         zebra       : true
	         
	    });
		
		var tab = $('#tabs').tabs();
		dialogoGenerico(250,330,"dialog-reporteCuotasPorGrupoConMonto","reporteGrupoConMonto");
		dialogoEdicion(500,990);
		dialogGrupos(530,530);
		$("#agregar")
			.button()
			.click(function(){
				modificar('','','','','','','','','','','alta');
		});
		
		$("#agregarGrupoCuota")
		.button()
		.click(function(){	
			document.grupoCuotas.submit();
		});
		
		funcionActualizar('grupo');
		//CabeceraFijaTabla('tablaGrupo');
	});
	function modificar(id,descripcionGrupo,carreraGrupo,nombreCarrera,anioCuotas,conceptoNumeroUno,
								tipoInforme,conceptoNumeroDos,diasPrimerVencimiento,vencimientoMonto,operacion){
		$('#id').val(id);
		$('#descripcionGrupo').val(descripcionGrupo);
		$('#carreraGrupo').val(carreraGrupo);
		$('#nombreCarrera').val(nombreCarrera);
		$('#anioCuotas').val(anioCuotas);
		$('#conceptoNumeroUno').val(conceptoNumeroUno);
		$('#tipoInforme').val(tipoInforme);
		$('#conceptoNumeroDos').val(conceptoNumeroDos);
		$('#diasPrimerVencimiento').val(diasPrimerVencimiento);
		$('#vencimientoMonto').val(vencimientoMonto);
		$('#operacion').val(operacion);
		$('#dialog-form').dialog('open');
	}
	
	function eliminar(id){
		if(seguroEliminar('Grupo')){
			document.grupo.id.value = id;
			document.grupo.operacion.value = 'borrar';
			document.grupo.submit();
		}
	}
	
	//GRUPOCUOTA INICIO
	function open(idGrupo,operacion){
		$.post('GrupoCuotasServlet?operacion=ver_GrupoCuotas&idGrupo='+idGrupo,'', function(data){
				if(data.status == "OK"){
					mostrarGrupoCuotas(data.lista,operacion);
			};
		});
	}
	
	function filaGrupoCuotaLectura(grupoCuota){
		return '<tr> '+
				'<td>' + grupoCuota.tipo + '</td> '+
			 	'<td>' + grupoCuota.mes.numero + '</td> '+
				'<td>' + grupoCuota.montoUno + '</td> '+
				'<td>' + grupoCuota.montoDos + '</td> ' +
				'<tr> ';
	}
	
	function filaGrupoCuotaModificar(grupoCuota,posicion){
		var optionMes="";
		var optionTipo="";
		var j = 0;
		var tipo,mes,id, montoUno,montoDos ; 
		tipo = mes = id = montoUno = montoDos = "";
		//si el grupo cuota ya existe lo crea con los valores que tiene
		//de otra forma los crea con un ID temportal
		if(grupoCuota != null){
			tipo = grupoCuota.tipo;
			mes = grupoCuota.mes.numero;
			id = grupoCuota.id;
			montoUno = grupoCuota.montoUno;		
			montoDos = grupoCuota.montoDos ;
		}else{
			id = 'N'+posicion;
		}
		<vel:velocity strictaccess="true">
		#set($tiposCuota = $scopetool.getRequestScope("tiposCuota"));	
			#foreach($tipoCuota in $tiposCuota)
				if( '$tipoCuota.letra' == tipo){
					optionTipo = optionTipo+'<option value="$tipoCuota.id" selected>$tipoCuota.letra</option>';
				}else{
					optionTipo = optionTipo+'<option value="$tipoCuota.id">$tipoCuota.letra</option>';
				}
			#end
		</vel:velocity>
		for(j=1;j<=12;j++){
			optionMes = optionMes + '<option value="' + j + '" ' ;
			if(j == mes){
				optionMes = optionMes +'selected' ;		 
			}
			optionMes = optionMes + ' >' + j + '</option>';
		}
		return	'<tr> '+
					'<td>' + 
						'<select name="'+id+'-tipo" id="'+id+'-tipo">'+
							'<option value="">Tipo</option>' +						
							optionTipo +
						'</select>' +
					'</td>' +
					'<td>' +  
						'<select name="'+id+'-mes" id="'+id+'-mes" >'+
							'<option value="">Mes</option>' +
							optionMes +
						'</select>' +
					 '</td> '+
					'<td><input type="text"  name="'+id+'-montoUno" id="'+id+'-montoUno" value="'+montoUno+'"/></td> '+
					'<td><input type="text"  name="'+id+'-montoDos" id="'+id+'-montoDos" value="'+montoDos+'"/></td> '+
				'<tr> ';
	}
	
	function tablaGruposCuotas(funcionFila,grupoCuotas,operacion){
		var string ="";
		for(i = 0; i < grupoCuotas.length; i++) {
			string = string + funcionFila(grupoCuotas[i],null);
		}
		//ESTO  VA EN LA CREACION DE LOS CAMPOS VACIOS.
		if(operacion == 'modificarGrupoCuota'){
			for(i2 = i;i2 < 24;i2++){
				string = string + funcionFila(null,i2);
			}
		}
		return string;
	}
	
	function mostrarGrupoCuotas(datos,operacion){
		var option= "" ;
		var tiposCuota = "";
		var string ="";
		if(datos[0].grupoCuotas.length==0){
			if(operacion == 'modificarGrupoCuota'){
				string = modificarGrupoCuota(filaGrupoCuotaModificar,datos[0],operacion);
				actualizar.style.visibility="visible";
			}else{
				actualizar.style.visibility="hidden";
				string = '<tr><td colspan="4">No existen cuotas.</td></tr>';
			}
		}else{
			string = '<tr> '+
						'<td td colspan="4" align="center"> ' + datos[0].descripcion +' </td> '+
					 '</tr> ';
			if(operacion == 'modificarGrupoCuota'){
				string = string + modificarGrupoCuota(filaGrupoCuotaModificar,datos[0],operacion);
			}else{
				string = string + tablaGruposCuotas(filaGrupoCuotaLectura,datos[0].grupoCuotas,operacion);
			}	
		}
		grupoCoutas.style.visibility="visible";
		$('#datos_grupoCuotas').html(string);
	}
	function modificarGrupoCuota(funcion,grupo,operacion){
		var string="";
		string = string + tablaGruposCuotas(funcion,grupo.grupoCuotas,operacion);
		string = string	+ '<td colspan="4" align ="left"><input type="hidden" value="'+grupo.id+'" name="idGrupo" id="idGrupo">'
						+ '<input type="hidden" value="actualizar" id="operacion" name="operacion"></td>';
		actualizar.style.visibility="visible";
		return string;
	}
	function tablaCuotaNueva(){
		for(i = i;i < 12;i++){
			string = string + funcionFila(null,I);
		}
		return string;
	}
	function dialogReporteGrupoMonto(operacion,buscaPor){
		if(buscaPor == '12'){
			$('#grupoTr').css('display','');
			$('#cohorteTr').css('display','');
			$('#nroDocumentoTr').css('display','none');
			$('#dialog-reporteCuotasPorGrupoConMonto').dialog( "option", "height", 200 );
		}else if (buscaPor == '13'){
			$('#grupoTr').css('display','');
			$('#cohorteTr').css('display','none');
			$('#nroDocumentoTr').css('display','');
		}else if(buscaPor == '3'){
			$('#grupoTr').css('display','');
			$('#cohorteTr').css('display','none');
			$('#nroDocumentoTr').css('display','none');
			$('#dialog-reporteCuotasPorGrupoConMonto').dialog( "option", "height", 170 );
			//$('#dialog-reporteCuotasPorGrupoConMonto').dialog( "option", "width", 330);
		}
		document.reporteGrupoConMonto.operacion.value = operacion;
		$('#dialog-reporteCuotasPorGrupoConMonto').dialog('open');
	}
function dialogGrupos(alto,ancho){
	$("#dialog-grupos").dialog({
		autoOpen: false,
		height:alto,
		width:ancho,
		modal: true,
		buttons: {
			'Aceptar': function() {
				document.reporteGrupoConMonto.grupo.value = document.windowGrupos.grupo.value;
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
}
function windowGrupo(){
	$('#dialog-grupos').dialog('open');
}
	
</script>

</head>
<body>
<div class="recuadro">
<%@ include file="/menu.jsp"%>

<vel:velocity strictaccess="true">
#set($msgError = $scopetool.getRequestScope("msgError"))
<input type="hidden" name="cartel" value="$!{msgError}" id="cartel">
</vel:velocity>

<table id="tablaGrupo" class="">
	<thead>
		<tr>
			<th ><img src="images/hash.png" width="24px" title="Codigo" /></th>
			<th >Descripcion Grupo</th>
			<th >carrera</th>
			<th >Nombre Carrera</th>
			<th ><img src="images/delete.png" width="24px" title="Eliminar Carrera" /></th>
		</tr>
	</thead>
	<tbody>
	<vel:velocity strictaccess="true">
	#set($grupos = $scopetool.getRequestScope("grupos"))
	#foreach($grupo in $grupos)
		#if($grupo.estado.isActivo())
			<tr >
				<td ><a href="javascript:modificar('$!grupo.id','$!grupo.descripcion','$!grupo.carrera','$!grupo.nombreCarrera','$!grupo.anioCuota','$!grupo.concepto1','$!grupo.tipoInforme','$!grupo.concepto2','$!grupo.vtoDias','$!grupo.vtoPlus','actualizar');">$grupo.id</a></td>
				<td ><a href="javascript:open($grupo.id,'modificarGrupoCuota');">$grupo.descripcion</a></td>
				<td ><a href="javascript:open($grupo.id,'');">$!grupo.carrera</a></td>
				<td >$grupo.nombreCarrera</td>
				<td ><a href="javascript:eliminar('$grupo.id')">X</a></td>
			</tr>
		#end
	#end
	</vel:velocity>
	</tbody>
</table>


<%@ include file="AgregarVolver.jsp"%>

<div id="tabs" >
	<ul>
		<li><a href="#tabs-1" id="idtab1">Cuotas</a></li>
		<li><a href="#tabs-2" id="idtab2">Informes</a></li>
	</ul>
	<div id="tabs-1">
		<%@ include file="/administracion/GrupoCuotas.jsp"%>
		<button id="actualizar" style="visibility: hidden">Guardar</button>
	</div>
	<div id="tabs-2">
		<%@ include file="/administracion/InformesGrupo.jsp"%>
		<!--  <button id="actualizar" style="visibility: hidden">Guardar</button>  -->
	</div>
</div>

<div id="dialog-grupos" title="Grupos">
	<form name="windowGrupos" id="windowGrupos">
		<vel:velocity strictaccess="true">
			#set($grupos = $scopetool.getRequestScope("grupos"))
			<select name="gupo" id="grupo" size="20">
			<option value="">NINGUNA OPCION</option>
			#foreach($grupo in $grupos)
				#if($grupo.estado.isActivo())
					<option value="$grupo.descripcion">$grupo.descripcion</option>
				#end
			#end
			</select>
		</vel:velocity>
	</form>
</div>


<div id="dialog-reporteCuotasPorGrupoConMonto" title="Grupo">
	<form id="reporteGrupoConMonto" name="reporteGrupoConMonto" action="GrupoServlet" method="post">
		<table>
		<tr id="grupoTr">
			<td >
				Grupo:
			</td>
			<td>
				<input id="grupo" name="grupo" onclick="javascript:windowGrupo();"/>
				<img src="images/lupa.gif" onclick="javascript:windowGrupo();" height="25" width="25"/><br>
			</td>
		</tr>
		<tr id="cohorteTr">
			<td>
				Cohorte:
			</td>
			<td>
				<input type="text" name="cohorte" id="cohorte"><br>	
			</td>
		</tr>
		<tr id="nroDocumentoTr">
			<td>
				Nro.Documento:
			</td>
			<td>
				<input type="text" name="nroDocumento" id="nroDocumento">	
			</td>
		</tr>
		</table>
		<input type="hidden" name="operacion" id="operacion">
	</form>
</div>


<div id="dialog-form" title="Grupo">
	<form action="GrupoServlet" method="post" name="edicion" id="edicion">
		<table>
			<tr>
				<td colspan="2" align="right">Codigo</td>
				<td colspan="3">
					<input name="id" id="id" value="" size="7" maxlength="7">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">Descripcion Grupo</td>
				<td colspan="3">
					<input name="descripcionGrupo" id="descripcionGrupo" value="" size="100" maxlength="200">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">Carrera</td>
				<td colspan="3">
					<input name="carreraGrupo" id="carreraGrupo" value="" size="7" maxlength="7">
				</td >
			</tr>
			<tr>
				<td colspan="2" align="right">Nom. Carrera </td>
				<td colspan="3">
					<input name="nombreCarrera" id="nombreCarrera" value="" size="100" maxlength="200">
				</td>
			</tr>
		</table>
		<table width="721" border="0">
			<tr>
				<td align="right" >a&ntilde;o de las cuotas </td>
				<td colspan="2">
					<select name="anioCuotas" id="anioCuotas">
						<vel:velocity>
						#set($anios = $scopetool.getRequestScope("anios"))
						#foreach ($anio in $anios)
							<option value="$anio">$anio</option>
						#end
						</vel:velocity>
					</select>
					<input type="checkbox" id="ctrlPago" name="ctrlPago">
				</td>
				<td align="right">Concepto N&deg;1</td>
				<td>
					<input name="conceptoNumeroUno" id="conceptoNumeroUno" size="20" maxlength="20">
				</td>
			</tr>
			<tr>
				<td align="right">Tipo de informe</td>
				<td colspan="2">
					<select name="tipoInforme" id="tipoInforme">
						<option value="1">Pendiente</option>
					</select>
				</td>
				<td align="right">Concepto N&deg;2</td>
				<td>
					<input name="conceptoNumeroDos" id="conceptoNumeroDos" value="" size="20" maxlength="20">
				</td>
			</tr>
			<tr>
				<td align="right">Dias 1er vencimiento </td>
				<td colspan="2">
					<input name="diasPrimerVencimiento" id="diasPrimerVencimiento" value="" size="10" maxlength="10">
				</td>
				<td align="right">Vencimiento Monto</td>
				<td>
					<input name="vencimientoMonto" id="vencimientoMonto" value="" size="10" maxlength="10">
				</td>
			</tr>
		</table>
		<input type="hidden" id="operacion" name="operacion" value="">
	</form>
</div>

</div>
</body>
<script language="JavaScript">
if($('#cartel').val()!=""){
	window.alert($('#cartel').val());
}
 
var a_fields = {                                                                  
		'descripcionGrupo':{'l': 'Descripcion Grupo', 'r': true,'mx':240},
		'carreraGrupo':{'l': 'Nombre Carrera', 'r': true,'mx':240,'f':'integer'},	
		'nombreCarrera':{'l': 'Nombre Grupo', 'r': true,'mx':240},
		'anioCuotas':{'l': 'Nombre Carrera', 'r': true,'mx':4},
		'conceptoNumeroUno':{'l': 'año Cuotas', 'r': true,'mx':240},
		'tipoInforme':{'l': 'Tipo Informe', 'r': true,'mx':240},
		'conceptoNumeroDos':{'l': 'Concepto N°2', 'r': true,'mx':240},
		'diasPrimerVencimiento':{'l': 'Dias Primer Vencimiento', 'r': true,'mx':20,'f':'integer'},
		'vencimientoMonto':{'l': 'Vencimiento Monto', 'r': true,'mx':250,'f':'real'}
},
o_config = {
'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>