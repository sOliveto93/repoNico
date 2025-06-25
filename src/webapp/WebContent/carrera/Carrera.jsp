 <%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>

<script type="text/javascript">
	$(function(){
		dialogoEdicion(380,600);
		$("#agregar").click(function(){
				modificar('','','','','','','','alta');
			});
		
	});
	$(document).ready(function() {
	    $('#tablaCarreras').fixheadertable({
	         caption   : 'Carreras',
	         colratio    : [26, 350, 350, 80, 80,80,80,23, 23], 
	         height      : 200, 
	         width       : 1120, 
	         zebra       : true
	         
	    });
	});
	function modificar(id,nombre,titulo,vigenciaDesde,vigenciaHasta,codigo,tipo,operacion){
		$('#nombreCarrera').val(nombre);
		$('#nombreTitulo').val(titulo);
		$('#codigo').val(codigo);
		$('#vigenciaCarreraDesde').val(vigenciaDesde);
		$('#vigenciaCarreraHasta').val(vigenciaHasta);
		$("input[name=tipo][value=" + tipo + "]").attr('checked', 'checked');
		$('#id').val(id);
		$('#operacion').val(operacion);
		$('#dialog-form').dialog('open');
	}
	function eliminar(id){
		if(seguroEliminar("Carrera")){
			document.edicion.id.value = id;
			document.edicion.operacion.value = 'borrar';
			document.edicion.submit();
		}
	}
	var myCalendar;
	function doOnLoad() {
		myCalendar = new dhtmlXCalendarObject(["vigenciaCarreraDesde","vigenciaCarreraHasta"]);
		myCalendar.loadUserLanguage("esp");
		myCalendar.setDateFormat("%d-%m-%Y");
	}
	function ComparaDosFecha(fechaHasta){
		var fechaDesde =$("#vigenciaCarreraDesde").val();
		var fechaHasta = fechaHasta;
		if (fechaHasta!="" && (fechaDesde>fechaHasta)){
			$("#vigenciaCarreraDesde").val(fechaHasta);
			$("#vigenciaCarreraHasta").val(fechaDesde);
		}
	}
	function validarCodigoCarrera(){
		codigo = $("#codigo").val();
		id = $("#id").val();
		
		if(codigo && (codigo!=id)){
			$.post('CarreraServlet?operacion=carreraJson&codigo='+codigo,'', function(data){
				var lengthJson = data.lista.length;
				var carrerasJson = data.lista;
				if(lengthJson > 0){
					alert("El codigo " + carrerasJson[0].codigo + " ya existe");
					$("#codigo").focus();
				}
			});
		}else if(!codigo){
			alert("El codigo no puede ser vacio");
			$("#codigo").focus();
		}
	}
</script>
<title>Carreras</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body onload="doOnLoad();">
<div class="recuadro">

<%@ include file="/menu.jsp"%>

<vel:velocity strictaccess="true">
#set($msgError = $scopetool.getRequestScope("msgError"))
<input type="hidden" name="cartel" value="$!{msgError}" id="cartel">
</vel:velocity>

<table id="tablaCarreras" class="fixheadertable">
	<thead>
		<tr>
			<th ><img src="images/hash.png" width="24px" title="Posicion en tabla" /></th>
			<th>Carrera</th>
			<th>Titulo</th>
			<th>Vigencia<br>Desde</th>
			<th>Vigencia<br> Hasta</th>
			<th>Código</th>
			<th>Tipo</th>
			<th><img src="images/pencil.png" width="24px" title="Modificar Carrera"/></th>
			<th><img src="images/delete.png" width="24px" title="Eliminar Carrera" /></th>
		</tr>
	</thead>
	<tbody>
		<c:set var="carreraNumero" value="0"/>
		<c:forEach items="${carreras}"  var="carrera">
			<c:if test="${carrera.estado.activado}">
				<c:set var="carreraNumero" value="${carreraNumero + 1}"/>
				<tr>
					<td>${carreraNumero}</td>
					<td>${carrera.nombreCarrera}</td>
					<td>${carrera.nombreTitulo}</td>
					<td>${carrera.vigencia.desde}</td>
					<td>${carrera.vigencia.hasta}</td>
					<td>${carrera.codigo}</td>
					<td>${carrera.tipo}</td>
					<c:if test="${usuario.tipo == AUDITORIA}">
						<td><a href="javascript:alert('operacion deshabilitada');"><img src="images/pencilGris.png" width="20px" title="Modificar Carrera"/></a></td>
						<td><a href="javascript:alert('operacion deshabilitada');" title="Eliminar Carrera"><img src="images/deleteGris.png" width="24px" title="Eliminar Carrera" /></a></td>
					</c:if>
					<c:if test="${usuario.tipo == ADM}">
						<td><a href="javascript:modificar('${carrera.id}','${carrera.nombreCarrera}','${carrera.nombreTitulo}','${carrera.vigencia.desde}','${carrera.vigencia.hasta}','${carrera.codigo}','${carrera.tipo}','actualizar');"><img src="images/pencil.png" width="20px" title="Modificar Carrera"/></a></td>
						<td><a href="javascript:eliminar('${carrera.id}')" title="Eliminar Carrera"><img src="images/delete.png" width="24px" title="Eliminar Carrera" /></a></td>
					</c:if>
				</tr>
		 	</c:if> 
		</c:forEach>	
	</tbody>
</table>
<c:if test="${usuario.tipo == ADM}">
	<%@ include file="/administracion/AgregarVolver.jsp"%>
</c:if>
<div id="dialog-form" title="Carrera">
	<form action="CarreraServlet" method="post" name="edicion" id="edicion">
		<table  border="0">
			<tr>
				<td colspan=2><label for="nombreCarrera">Nombre Carrera : </label></td>
				<td colspan=2><input name="nombreCarrera" type="text" id="nombreCarrera" value="" size="50" maxlength="250"></td>
			</tr>
			<tr>
				<td colspan=2><label for="nombreTitulo">Nombre Titulo:</label></td>
				<td colspan=2><input name="nombreTitulo" type="text" id="nombreTitulo" value="" size="50" maxlength="250"></td>
			</tr>
			<tr>
				<td colspan=2><label for="nombreTitulo">Código Carrera:</label></td>
				<td colspan=2><input name="codigo" type="text" id="codigo" value="" size="4" maxlength="10" onblur="javaScript:validarCodigoCarrera();"></td>
				
			</tr><tr>
				<td colspan=4>
				<label for="nombreTipo">Tipo Carrera:</label></br>			
				Becas <input name="tipo" type="radio" id="tipo" value="becas"> - 
				Dep <input name="tipo" type="radio" id="tipo" value="dep"> -
				DRIyC <input name="tipo" type="radio" id="tipo" value="driyc"> -			
				Grado <input name="tipo" type="radio" id="tipo" value="grado"> - 
				Posgrado <input name="tipo" type="radio" id="tipo" value="posgrado"> -
				Teatro <input name="tipo" type="radio" id="tipo" value="teatro"></td>
			</tr>
			<tr>
				<td colspan="4"><p><b>Vigencia Carrera:</b></p></td>
			</tr>
			<tr>
				<td><label for="vigenciaCarreraDesde">Desde:</label></td>
				<td><input name="vigenciaCarreraDesde" type="text" id="vigenciaCarreraDesde" value="" size="10" maxlength="10"></td>
			
				<td align="right"><label for="vigenciaCarreraHasta">Hasta:</label></td>
				<td><input onblur="javaScript:ComparaDosFecha(this.value);" name="vigenciaCarreraHasta" type="text" class="dhxform_textarea" id="vigenciaCarreraHasta" size="10" maxlength="10">	</td>
			</tr>
		</table>
		<input type="hidden" name="operacion" id="operacion" value="">
		<input type="hidden" name="id" id="id" value="">
	</form>
</div>
</div>
</body>
<script language="JavaScript">
if($('#cartel').val()!=""){
	window.alert($('#cartel').val());
}

var a_fields = {
		'nombreCarrera': {'l': 'Nombre Carrera', 'r': true,'mx':250},
		'nombreTitulo': {'l': 'Nombre Titulo', 'r': false,'mx':250},
		'codigo': {'l': 'Código', 'r': true,'mx':10, 'f':'integer'},
		'vigenciaCarreraDesde': {'l': 'Vigencia Carrera Desde', 'r': false,'f':'date'},
		'vigenciaCarreraHasta': {'l': 'Vigencia Carrera Hasta',  'r': false,'f':'date'}
},
o_config = {
		'to_disable' : ['Aceptar'],	'alert' : 1
};
var v = new validator('edicion', a_fields, o_config);
</script>
</html>
 