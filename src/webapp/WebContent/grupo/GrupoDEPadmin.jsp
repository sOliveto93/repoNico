<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<title>Grupo</title>
<%@ include file="/administracion/abm_headers.jsp"%>
<script type="text/javascript">
var operacionPartida;
var idFilaAnterior;
	$(function(){
		 $('#tablaGrupo').fixheadertable({
	         caption   : 'Grupos',
	         colratio    : [25,420, 425,125,34,24,24,24,24,24], 
	         height      : 300, 
	         width       : 1180, 
	         zebra       : true
	    });

		$("#preCarga").button({});

		var tab = $('#tabs').tabs();
		dialogGrupos(530,530,functionAceptar);
// 		$("#agregar")
// 			.button()
// 			.click(function(){
// 				modificar('','','','','','','','','','','','','','','','','','alta','','');
// 		});
		$("#agregarGrupoCuota")	
		.button()
		.click(function(){	
			document.grupoCuotas.submit();
		});
		funcionActualizar('grupoCuotasF');
		
		$("#preCarga").hide();
		
		$("#dialog-form2").dialog({
			autoOpen: false,
			height:600,
			width:1050,
			modal: true,
			buttons: {
				'Grabar': function(event) {
					if (v.exec()){
						if($("#conceptoUnoMonto").val()!= "0" && $("#diasPrimerVencimiento").val() != "0" && $("#diasSegundoVencimiento").val() != "0"){
							document.edicion.submit();
						}else{
							alert("El monto 1 y los días de vencimento son obligatorios y no pueden ser 0");
							event.preventDefault();
						}
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
// 		$("#dialogFormPartida").dialog({
// 			autoOpen: false,
// 			height:250,
// 			width:500,
// 			modal: true,
// 			buttons: {
// 				'Grabar': function(event) {
// 					var descripcion = $("#descripcion").val();
// 					operacionesGrupoPartida(descripcion);
// 					$("#dialogFormPartida").dialog('close');
// 				},
// 				Cancel: function(){
// 					$(this).dialog('close');
// 				}
// 			},
// 			close: function(){
// 				allFields.val('').removeClass('ui-state-error');
// 			}
// 		});
	});
	
// 	function operacionesGrupoPartida(descripcion){
// 		var idPartida = $("#idPartida").val();
// 		$.post('GrupoServlet?operacion='+operacionPartida+'&descripcion='+descripcion+'&idPartida='+idPartida,'', function(data){
// 			partida = data.lista[0];
// 			if ($("#partida option[value="+partida.id+"]").length == 0){
// 				$('#partida').append($('<option>', {
// 				    value:partida.id,
// 				    text: partida.descripcion,
// 				    selected: true
// 				}));
// 			}else if($("#partida option[value="+partida.id+"]").text() != partida.descripcion){
// 				$("#partida option[value="+partida.id+"]").text(partida.descripcion);
// 			}else{
// 				$("#partida option[text=" + partida.id +"]").attr("selected","selected") ;
// 			}
// 		});
// 	}
// 	function openDialogPartida(operacion,cargarDescripcion){
// 		operacionPartida = operacion;
// 		if(cargarDescripcion == "true"){
// 			$("#descripcion").val($("#partida option:selected").text());
// 			var idPartida = $("#partida").val();
// 			$("#idPartida").val(idPartida);
// 		}
// 		$('#dialogFormPartida').dialog('open');
// 	}
	function modificar(codigo,descripcionGrupo,carreraGrupo,nombreCarrera,anioCuotas,conceptoNumeroUno,
			tipoInforme,conceptoNumeroDos,diasPrimerVencimiento,diasSegundoVencimiento,vencimientoMonto,
			conceptoUnoMonto,conceptoDosMonto,ctrlPago,cobraMora,id,cantidadCuotas,operacion,partida,tipo,tipoMora){ //,tramiteIngreso,tramiteGrado,tramitePosgrado 
		$('#id').val(id);
		
		$('#codigo').val(codigo);
		$('#CodigoOriginal').val(codigo);
		
		$('#operacion1').val(operacion);
		$('#descripcionGrupo').val(descripcionGrupo);
		$("#carreraGrupo option[value="+carreraGrupo+"]").attr('selected', 'selected');
		$("#anioCuotas option[value=" + anioCuotas+ "]").attr('selected', 'selected');
		$("#tipo option[value=" + tipo + "]").attr('selected', 'selected');
		$("#tipoMora option[value=" + tipoMora + "]").attr('selected', 'selected');
		if(ctrlPago == 'S')
			$("#ctrlPago").attr('checked', 'checked');
		/*
		if(tramiteIngreso == 'S')
			$("#tramite_ingreso").attr('checked', 'checked');
		if(tramiteGrado == 'S')
			$("#tramite_grado").attr('checked', 'checked');
		if(tramitePosgrado == 'S')
			$("#tramite_posgrado").attr('checked', 'checked');
		*/
		$('#nombreCarrera').val(nombreCarrera);
		
		$('#tipoInforme').val(tipoInforme);
		
		$('#conceptoNumeroUno').val(conceptoNumeroUno);
		$('#conceptoNumeroDos').val(conceptoNumeroDos);

		$('#diasPrimerVencimiento').val(diasPrimerVencimiento);
		$('#diasSegundoVencimiento').val(diasSegundoVencimiento);		

		$('#vencimientoMonto').val(vencimientoMonto);

		$('#conceptoUnoMonto').val(conceptoUnoMonto);
		$('#conceptoDosMonto').val(conceptoDosMonto);	
		$('#cantidadCuotas').val(cantidadCuotas);
		if(cobraMora == 'S'){
			$("#cobraMora").attr('checked', 'checked');
		}
		$("#partida option[value=" + partida + "]").attr('selected', 'selected');
		$('#dialog-form2').dialog('open');
	}
	
// 	function eliminar(id){
// 		if(seguroEliminar('Grupo')){
// 			document.edicion.id.value = id;
// 			document.edicion.operacion.value = 'borrar';
// 			document.edicion.submit();
// 		}
// 	}
	//GRUPOCUOTA INICIO
	function open(idGrupo,operacion){
		if(idFilaAnterior && idGrupo != idFilaAnterior){
			removeBackgroundColor(idFilaAnterior);
		}
		idFilaAnterior = idGrupo;
		setBackgroundColor(idGrupo,'#666666');
		$.post('GrupoCuotasServlet?operacion=ver_GrupoCuotas&idGrupo='+idGrupo,'', function(data){
				if(data.status == "OK"){
					$("#tabs").removeAttr("style");
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
			montoDos = grupoCuota.montoDos;
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
		return string=	'<tr id='+id+'> '+
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
					'<td><a href="javascript:limpiarCampo('+id+');"><img src="images/delete.png" width="15px" title="Eliminar Carrera" /></a></td>'+
				'<tr> ';
		//alert(string);
	}
	function limpiarCampo(id){
		var nombreMes = id + "-mes";
		var tipo = id + "-tipo";
		var montoUno = id + "-montoUno";
		var montoDos = id + "-montoDos";
		$("#"+nombreMes+" option[value=]").attr('selected', 'selected');
		$("#"+tipo+" option[value=]").attr('selected', 'selected');
		$("#"+montoUno).val("");
		$("#"+montoDos).val("");
	}
	function tablaGruposCuotas(funcionFila,grupoCuotas,operacion){
		var string ="";
		for(i = 0; i < grupoCuotas.length; i++) {
			string = string + funcionFila(grupoCuotas[i],null);
		}
		//ESTO  VA EN LA CREACION DE LOS CAMPOS VACIOS.
		if(operacion == 'modificarGrupoCuota'){
			for(i2 = (i+1);i2 < 24;i2++){
				string = string + funcionFila(null,i2);
			}
		}
		return string;
	}
	function mostrarGrupoCuotas(datos,operacion){
		var option= "" ;
		var tiposCuota = "";
		var string ="";
		$("#personasChequera").hide();
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
				actualizar.style.visibility="hidden";
				string = string + tablaGruposCuotas(filaGrupoCuotaLectura,datos[0].grupoCuotas,operacion);
			}
		}
		grupoCoutas.style.visibility="visible";
		$('#datos_grupoCuotas').html(string);
		var i=0;
		for(i=12;i<=24;i++){
			$('#N'+i).hide(); 
		}
		if(operacion == 'modificarGrupoCuota'){
			$("#idGrupo").val(datos[0].id);
			$("#montoUnoHidden").val(datos[0].conceptoUnoMonto);
			$("#montoDosHidden").val(datos[0].conceptoDosMonto);
			$("#verMas").show('slow');
			$("#preCarga").show();
			$("#preCarga").fadeTo('0',1);
		}else{
			$("#verMas").hide();
			$("#preCarga").hide();
		}
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
	function functionAceptar(){
		$("#idGrupoInforme").val($("#gruposDisponibles option:selected").val()) ;
		$("#grupoInforme").val($("#gruposDisponibles option:selected").text()) ;
	}
	function mostrarMasMenos(){
		var i = 0;
		var mostrar= $("#mostrarValue").val();
		if (mostrar == ">>"){
			for(i=12;i<=24;i++){
				$('#N'+i).show();
			}
			$("#verMas").hide();
			$("#verMenos").show();
			$("#mostrarValue").val("<<");
		}else if(mostrar == "<<"){
			for(i=12;i<=24;i++){
				$('#N'+i).hide();
			}
			$("#verMenos").hide();
			$("#verMas").show();
			$("#mostrarValue").val(">>");
		}
	}
	function onLoad(idGrupo){
		if(idGrupo){
			setBackgroundColor(idGrupo,'#666666');
			idFilaAnterior=idGrupo;
		}
	}
	
	function calcularMora(){
		if($("#cobraMora").attr('checked')){
			$("#vencimientoMonto").val((parseInt($("#conceptoUnoMonto").val())+parseInt($("#conceptoDosMonto").val()))*2/100);
		}else{
			$("#vencimientoMonto").val(0);
		}
	}
	function buscarGrupoPorCodigo(codigo){
		var lengthJson;
		var limite;
		var id = $("#id").val(); 
		
		$.post('GrupoServlet?operacion=buscarGrupoPorCodigo&codigo='+codigo,'', function(grupos){
			var grupos = grupos.lista;
			var lengthJson = grupos.length;
			var idJson = grupos[0].id;
			var idModificar = document.edicion.id.value;
			//Si el id es vacio estaria en el Alta del grupo. 
			//el resultado esperado del length seria  0 de otro forma el codigo ya esta siendo usado
			if(idModificar){
				limite = 1;
			}else{
				limite = 0;
			}
			if(lengthJson >= limite && idJson != idModificar ){
				alert("El codigo " + codigo + " ya existe");
				$("#codigo").val($('#CodigoOriginal').val());
				$("#codigo").focus();
			}
			
		});
	
	}
</script>
</head>
<body onload="javascript:onLoad(${idGrupoAnterior});">
<div class="recuadro">
<%@ include file="/menu.jsp"%>
<input type="hidden" name="cartel" value="${msgError}" id="cartel">


<table id="tablaGrupo" class="fixheadertable">
	<thead>
		<tr>
			<th><img src="images/hash.png" width="25px" title="Codigo" /></th>
			<th>Descripcion Grupo</th>
			<th>Nombre Carrera</th>
			<th>Partida</th>
			<th>Cont. Pago</th>
			<th><img src="images/pencil.png" width="24px" title="Modificar Grupo"/></th>
			<th><img src="images/check.png" width="24px" title="Generar Chequera"/></th>
			<th><img src="images/signoPesoEditar.png" width="24px" title="Modificar Cuotas"/></th>
			<th><img src="images/signoPesoView.png" width="24px" title="Vista de Cuotas"/></th>
			<th><img src="images/deleteGris.png" width="24px" title="Eliminar Carrera" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${grupos}"  var="grupo">
			<c:if test="${grupo.estado.activado==true}">
			<tr id="${grupo.id}">
				<td>${grupo.codigo}</td>
				<td>${grupo.descripcion}</td>
				<td>${grupo.carrera.nombreCarrera}</td>
				<td>${grupo.partida.descripcion}</td>
				<td>${grupo.ctrlPago}</td>
				<c:if test="${(usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN)}">
					<td><a href="javascript:modificar('${grupo.codigo}','${grupo.descripcion}','${grupo.carrera.id}','','${grupo.anioCuota}','${grupo.concepto1}','${grupo.tipoInforme }','${grupo.concepto2 }','${grupo.vtoDias }','${grupo.vtoPlus}','${grupo.vtoMonto}','${grupo.conceptoUnoMonto}','${grupo.conceptoDosMonto}','${grupo.ctrlPago}','${grupo.cobraMora}','${grupo.id}','${grupo.cantidadCuotas}','actualizar','${grupo.partida.id}','${grupo.facturaItemTipo.id}','${grupo.facturaItemTipoMora.id}');"><img src="images/pencil.png" width="24px" title="Modificar Grupo"/></a></td>
					<td><a href="javascript:buscaPersonasGrupo('${grupo.id}');"><img src="images/check.png" width="24px" title="Generar Chequeras"/></a></td>
					<td><a href="javascript:open(${grupo.id},'modificarGrupoCuota');"><img src="images/signoPesoEditar.png" width="24px" title="Modificar Cuotas"/></a></td>
					<td><a href="javascript:open(${grupo.id},'');"><img src="images/signoPesoView.png" width="24x" title="Vista de Cuotas"/></a></td>
					<td><img src="images/deleteGris.png" width="24px" title="Eliminar Grupo" /></td>
				</c:if>
			</tr>
			</c:if>
		</c:forEach>
	</tbody>
</table>
<div id="modalLoader">
	<div id="progressBar" align="center"></div>
</div>
<c:if test="${(usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN)}">
<%-- 	<%@ include file="/administracion/AgregarVolver.jsp"%> --%>
	<%@ include file="/chequera/chequeraPersonas.jsp"%>
</c:if>
<div id="tabs" style="display: none">
	<ul>
		<li><a href="#tabs-1" id="idtab1">Cuotas</a></li>
		
	</ul>
	<div id="tabs-1">
		<%@ include file="/grupo/GrupoEdicion.jsp"%>
		<div id="verMas" style="display: none;margin-left: 43%;">
			<a href="javascript:mostrarMasMenos();"><img src="images/flechaDerechaVerde.png" width="25px" title="Mostrar Filas" /></a>
		</div>
		<div id="verMenos" style="display: none;margin-left:43%;" >
			<a href="javascript:mostrarMasMenos();"><img src="images/flechaIzquierdaVerde.png" width="25px" title="Esconder Filas" /></a>
		</div>
		<div class="div-table">
	 		<div class="div-table-row">
  	  			<div class="div-table-col">
					<button id="actualizar" style="visibility: hidden">Guardar</button>
				</div>
				<div class="div-table-col">
					<input type="hidden" name="mostrarValue" id="mostrarValue" value=">>">
				</div>
				<div class="div-table-col">
					<div align="left" style="margin-left: 150px"><a href="javascript:ActualizarCuotas();" id="preCarga">
					PreCargar de cuotas<img src="images/signoPeso2.png" width="18px"></a></div>
				</div>
			</div>
		</div>
	</div>
	
</div>
<div id="chequeraPersonas" align="left" style="display: none">	
	<%@ include file="/chequera/chequeraPersonas.jsp"%>
</div>

	<div id="dialog-form2" title="Edición de los datos comunes del Grupo">
		<form action="GrupoServlet" method="post" name="edicion" id="edicion">
			<table>
				<tr>
					<td colspan="2" align="right">Codigo</td>
					<td colspan="3">
						<input name="codigo" id="codigo" size="7" maxlength="7" onblur="javascript:buscarGrupoPorCodigo(this.value);" disabled="disabled">
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">Descripcion Grupo</td>
					<td colspan="3">
						<input name="descripcionGrupo" id="descripcionGrupo" value="" size="100" maxlength="200" disabled="disabled">
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">Carrera</td>
					<td colspan="3">
						<!--  <input name="carreraGrupo" id="carreraGrupo" value="" size="7" maxlength="7">-->
						<select name="carreraGrupo" id="carreraGrupo" disabled="disabled">
							<c:forEach items="${carreras}"  var="carrera">
								<c:if test="${carrera.estado.activado==true}">
									<option value="${carrera.id}"  >${carrera.codigo} - ${carrera.nombreCarrera}</option>
								</c:if>
							</c:forEach>
						</select>
					</td >
				</tr>
			</table>
			<table width="721" border="0">
				<tr>
					<td align="right" >A&ntilde;o de las cuotas </td>
					<td>
						<select name="anioCuotas" id="anioCuotas" disabled="disabled">
							<c:forEach items="${anios}"  var="anio">
								<option value="${anio}">${anio}</option>
							</c:forEach>
						</select>
					</td>
					<td colspan="2">
						Partida <select name="partida" id="partida" disabled="disabled">
							<c:forEach items="${partidas}"  var="partida">
								<option value="${partida.id}">${partida.descripcion}</option>
							</c:forEach>
						</select>
						<!-- 
						<a href="javascript:openDialogPartida('altaPartidaGrupoJson','');"><img src="images/add.png" width="15px" title="Agregar Partida"/></a>
						<a href="javascript:openDialogPartida('modificarPartidaGrupoJson','true');"><img src="images/pencil.png" width="15px" title="Modificar Partida"/></a>
						
						 -->
					</td>			
				</tr>
				<tr>
					<td align="right">Tipo:</td>
					<td colspan="2">
					<select name="tipo" id="tipo" disabled="disabled">
						<option value="">Tipos</option>
						<c:forEach items="${facturaItemTipos}"  var="facturaItemTipo">
							<c:if test="${facturaItemTipo.estado.activado}">
								<option value="${facturaItemTipo.id}">${facturaItemTipo.rubroCobro.codigo}<c:if test="${facturaItemTipo.codigo  < 10}">0</c:if>${facturaItemTipo.codigo} - ${facturaItemTipo.descripcion}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>
				</tr>
				<tr>
					<td align="right">Tipo Mora:</td>
					<td colspan="2">
					<select name="tipoMora" id="tipoMora" disabled="disabled">
						<option value="">Tipos mora</option>
						<c:forEach items="${facturaItemTipos}"  var="facturaItemTipo">
							<c:if test="${facturaItemTipo.estado.activado}">
								<c:if test="${fn:contains(facturaItemTipo.descripcion, 'Mora')}">
									<option value="${facturaItemTipo.id}"> ${facturaItemTipo.rubroCobro.codigo}<c:if test="${facturaItemTipo.codigo  < 10}">0</c:if>${facturaItemTipo.codigo} - ${facturaItemTipo.descripcion}</option>
								</c:if>
							</c:if>
						</c:forEach>
					</select>
					</td>
				</tr>
				<tr>
					<td align="right">Concepto Nº 1</td>
					<td>
						<input name="conceptoNumeroUno" id="conceptoNumeroUno" size="20" maxlength="20" disabled="disabled">
					</td>
					<td align="right">Monto Concepto Nº 1</td>
					<td>
						<input name="conceptoUnoMonto" id="conceptoUnoMonto" value="" size="10" maxlength="10" onchange="javascript:calcularMora();">
					</td>
				</tr>
				<tr>
					<td align="right">Concepto Nº 2</td>
					<td>
						<input name="conceptoNumeroDos" id="conceptoNumeroDos" value="" size="20" maxlength="20" disabled="disabled">
					</td>				
					<td align="right">Monto Concepto N° 2</td>
					<td>
						<input name="conceptoDosMonto" id="conceptoDosMonto" value="" size="10" maxlength="10" onchange="javascript:calcularMora();">
					</td>
				</tr>
				<tr>
					<td align="right">Días 1° Vencimiento:</td>
					<td>
						<input name="diasPrimerVencimiento" id="diasPrimerVencimiento" value="" size="10" maxlength="10" disabled="disabled">
					</td>
					<td align="right"><a href="#"><img src="images/help.png" width="24px" title="Corresponde al monto que se cobrara luego del primer vencimiento, se suma al total para el calculo del monto. Se calcula como el 2% del total del grupo, si corresponde el cobro de mora."/></a>Monto Punitorio Vencimiento</td>
					<td>
						<input name="vencimientoMonto" id="vencimientoMonto" value="" size="10" maxlength="10" disabled="disabled">
					</td>
				</tr>
				<tr>
					<td align="right"><a href="#"><img src="images/help.png" width="24px" title="Cantidad de dias para el calculo del segundo vencimiento. Todas las chequeras tienen que tener segundovencimiento, pero algunas no cobran mora.."/></a>Dias 2° Vencimiento:</td>
					<td>
						<input name="diasSegundoVencimiento" id="diasSegundoVencimiento" value="" size="10" maxlength="10" disabled="disabled">
					</td>
					<td>
						Cantidad de Cuotas
					</td>
					<td>
						<input type="text" id="cantidadCuotas" name="cantidadCuotas" >
						<input name="vencimientoDosMonto" id="vencimientoDosMonto" value="" size="10" maxlength="10" type="hidden">
					</td>			
					
				</tr>
				<tr>
					<td>
						Control de Pago
					</td>
					<td>
						<input type="checkbox" id="ctrlPago" name="ctrlPago" >
					</td>
					<td>
						Cobra mora
					</td>
					<td>
						<input type="checkbox" id="cobraMora" name="cobraMora" onclick="javascript:calcularMora();">
					</td>
				</tr>
			</table>
			<input type="hidden" id="id" name="id" >
	
			<input type="hidden" id="CodigoOriginal" name="CodigoOriginal" value="">
			<input type="hidden" id="operacion1" name="operacion">
		</form>
	</div>

</div>
<%@ include file="/grupo/GrupoLista.jsp"%>
</body>
<script language="JavaScript">
if($('#cartel').val()!=""){
	window.alert($('#cartel').val());
}
 
var a_fields = {                                                                  
		'descripcionGrupo':{'l': 'Descripcion Grupo', 'r': true,'mx':240},
		'carreraGrupo':{'l': 'Nombre Carrera', 'r': true,'mx':240,'f':'integer'},		
		'conceptoNumeroUno':{'l': 'año Cuotas', 'r': true,'mx':240},
		'conceptoNumeroUno':{'l': 'Concepto N° 1', 'r': true,'mx':240},
		'conceptoNumeroDos':{'l': 'Concepto N° 2', 'r': false,'mx':240},
		'diasPrimerVencimiento':{'l': 'Dias Monto Nº 1', 'r': true,'mx':20,'f':'integer'},
		'vencimientoMonto':{'l': 'Monto Concepto N° 1', 'r': true,'mx':250,'f':'real'},
		'diasSegundoVencimiento':{'l': 'Dias Monto Nº 2', 'r': false,'mx':20,'f':'integer'},
		'vencimientoDosMonto':{'l': 'Monto Concepto N° 2', 'r': false,'mx':250,'f':'real'}
},
o_config = {
'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>