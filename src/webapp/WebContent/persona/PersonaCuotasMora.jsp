<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
</head>
<body onload="doOnLoad('${adeudaCuotas}');">
	<div class="recuadro">
		<%@ include file="/menu.jsp"%>
		<%@ include file="/persona/PersonaFiltro.jsp" %>
		<c:set var="personas" value="${personas}"/>
		<!-- 
		<input name="paginaNumero" id="paginaNumero" value="${paginaNumero}" type="hidden">
		<c:if test="${not empty personas}">
			 <div align="left">
				<a href="javascript:paginaAnterior();">
					Prev page
				</a>
			 </div>

			<div align="right">
				<a href="javascript:paginaSiguiente()">
					Next page
				</a>
			</div>
		</c:if>
		 -->
		 <!-- deberia hacerlo con DIV -->
		<table id="tablaPersonasMora" class="fixheadertable">
			<thead>
				<tr>
					<th><input type="checkbox" name="aux" onclick="selectAll()" id="aux"></th>
					<th>Apellido-Nombre</th>
					<th>Documento</th>
					<th>Carrera Tipo</th>
				</tr>
			</thead>
			<c:set var="i" value="0"/>
			<tbody id="personasMoraTbody">
				<c:forEach items="${personas}" var="persona">
					<tr>
						<td>
						<c:set var="i" value="${i+1}"/>
							<input type="checkbox" name="checkPersona" id="checkPersona${i}" onchange="javaScript:agregarPersonaList('${persona.carreraAdeudada}',this);" class='check' value="${persona.carreraAdeudada}" >
						</td>
						<td>${persona.nombreCompleto}</td>
						<td>${persona.documento.tipo.abreviacion} - ${persona.documento.numero}</td>
						<td>${persona.carreraAdeudada}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="buttons" >
		<a href="javascript:excepturSancionar('exceptuarPersonas');" class="positive">
			<img src="images/dialog-ok-apply-2.png" alt=""/>
			Exceptuar
		</a>
	
		<a href="javascript:excepturSancionar('sancionarPersonas');" class="negative">
			<img src="images/red-cross.png" alt=""/>
			Sancionar        
		</a>
	</div>
	</div>
	<form action="PersonaServlet"  name="exceptuarSancionarF" id="exceptuarSancionarF">
		<input type="hidden" value="" name="operacion" id="operacion">
		<input type="hidden" value="" name="liPersonas" id="liPersonas">
	</form>
</body>
<script  type="text/javascript">
var total = ${i}
var listaPersonas = [];
$(function(){
	$('#salto').val("/persona/PersonaCuotasMora.jsp");
	//$("#carreraTd").toggle();
	$('#tablaPersonasMora').fixheadertable({
	        colratio    : [30,350, 200],
	        height      : 265,
	        width       : 602,
	        zebra       : true
	   });
	//evito que se ejecute el evento.
	$("#NoHagoNadaSoyUnaMentira")
		.button()
		.click(function(event){
		 	event.preventDefault();			 	
		}
	); 
	/*
	$('#filtrosSancionarAlumnos').fixheadertable({
		caption   : '',
		height      : 50, 
	    width       : 250
	    });
	*/
});
function selectAll(){		
		if (!$('#aux').is(':checked')){			
			$('.check').attr('checked', false);			
		}else{
			$('.check').attr('checked', true);
		}
		for(i=1; i<=total ;i++){
			if($('#checkPersona'+i).is(':checked')){
				agregarPersonaList($('#checkPersona'+i).val(),null);
			}
		}
}
function excepturSancionar(operacion){
	document.exceptuarSancionarF.operacion.value = operacion;
	var length = listaPersonas.length;
	salida = "";
	for(i=0; i<length ;i++){
		salida = salida + listaPersonas[i]			
			
	}
	$("#liPersonas").val(salida);
	$("#exceptuarSancionarF").submit();
}
function agregarPersonaList(legajoCarreraTipo, check){	
	if(listaPersonas.length==0){
		listaPersonas.push(legajoCarreraTipo);
	}else{
		if(existeEn(listaPersonas,legajoCarreraTipo)==true){
			var i,length;
			length = listaPersonas.length;
			for(i=0; i<length ;i++){
				if(listaPersonas[i]==legajoCarreraTipo){
					listaPersonas.splice(i,1);
				}
			}
		}else{
			listaPersonas.push(legajoCarreraTipo);
		}
	}
}
function existeEn(lista,id){
	var length = lista.length;
	var i;
	var respuesta=false;
	for(i=0; i<length ;i++){
		if(lista[i]==id){			
			respuesta = true;
			break;
		}
	}
	return respuesta;
}
function doOnLoad(adeudaValue){
	/*
	alert("adeudaValue : "+adeudaValue);
	$("#operacion").before("<br><input name='adeudaCuotasCheck' id='adeudaCuotasCheck' type='checkbox'>Adeuda");
	$("#adeudaCuotasCheck").before("<input name='adeuda' id='adeuda' type='hidden' value=''>");
	$("#adeuda").before("<input name='vieneDelServlet' id='vieneDelServlet' type='hidden' value='true'>");
	$("#adeudaCuotasCheck").attr('onblur', 'traerPersonasMora();');
	createElement("filtro","input","hidden","operacionDos","filtrarMora");
	if(adeudaValue == "on"){
		$("#adeudaCuotasCheck").attr('checked', 'checked');
	}
	*/
	$("#saltarVerificacion").val("saltarVerificacion");
	$("#operacionDos").val("filtrarMora");
}
function traerPersonasMora(){
	value = $("#adeudaCuotasCheck").val();
	if(document.getElementById("adeudaCuotasCheck").checked == true){
		$("#adeuda").val("on");	
	}else{
		$("#adeuda").val("off");
	}
	$("#filtro").submit();
}
function paginaSiguiente(){
	var paginaNumero =$("#paginaNumero").val();
	paginaNumero = new Number(paginaNumero) + new Number(1);
	if(paginaNumero>0){
		$.post('PersonaServlet?operacion=moraPersonasJson&paginaNumero='+paginaNumero,'', function(data){
			mostrarPersonasMora(data);
		});
	}
	$("#paginaNumero").val(paginaNumero);
}
function paginaAnterior(){
	var paginaNumero = $("#paginaNumero").val();
	if(paginaNumero>0){
		paginaNumero = new Number(paginaNumero) - new Number(1);
		$.post('PersonaServlet?operacion=moraPersonasJson&paginaNumero='+paginaNumero,'', function(data){
			mostrarPersonasMora(data);
		});
	}
	$("#paginaNumero").val(paginaNumero);
}
function mostrarPersonasMora(datos){
	var string="";
	var personas = datos.personas;
	var numeroPagina = datos.numeroPagina;
	for(i=0;i<personas.length;i++){
		string = string +
	 	'<tr>'+
		'<td><input type="checkbox" name=""></td>'+
		'<td>'+personas[i].nombreCompleto+'</td>'+
		'<td>'+personas[i].tipoDocumento +" - "+ personas[i].numeroDocumento+'</td>'+
	'</tr>';
	}
	$('#personasMoraTbody').html(string);
}

</script>
</html>