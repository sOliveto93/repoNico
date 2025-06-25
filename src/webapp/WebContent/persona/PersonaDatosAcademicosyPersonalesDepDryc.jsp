<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="PersonaServlet" method="post"  name="formModificarInscripcion" id="formModificarInscripcion">
	<input type="hidden" id="idCarrera" name="idCarrera" value="">
	<input type="hidden" id="idPersona" name="idPersona" value="">
	<input type="hidden" id="operacion" name="operacion" value="">	
	<input type="hidden" id="idInscripcion" name="idInscripcion" value="">	
	<input type="hidden" id="operacionAnterior" name="operacionAnterior" value="">
</form>
<form action="PersonaServlet" method="post" name="edicion" id="edicion" >
	<fieldset><legend>Datos Personales</legend>
		<table id='datosPersonales'>
			<tr>
				<td>Apellido:<input name='apellido' id='apellido' value='${persona.apellido}' size="45" disabled="disabled"></td>				
				<td>Nombre:<input name='nombre' id='nombre' value='${persona.nombre}' size="45" disabled="disabled"></td>
			</tr>
			<tr>
				<td><a href="#"><img src="images/help.png" width="24px" title="Si la persona con el tipo y número de documento que se indica ya existe no carga una nueva."/></a>Tipo Documento:
					<select name='tipoDocumento' id='tipoDocumento' disabled="disabled">
						<option value='0'>Tipo Documento</option>
						<c:forEach items="${tiposDocumento}"  var="tipoDocumento">
							<c:choose>
								<c:when test="${tipoDocumento == persona.documento.tipo.abreviacion}">
									<option value="${tipoDocumento}" selected="selected">${tipoDocumento}</option>
	      						</c:when>
	      						<c:otherwise>
	      							<option value="${tipoDocumento}">${tipoDocumento}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td>Numero Documento: <input name='numeroDocumento' id='numeroDocumento' value='${persona.documento.numero}' disabled="disabled"></td>
				<td>Telefono:<input name='telefono' id='telefono' value='${persona.telefono}' disabled="disabled"></td>
			</tr>
			<tr>
				<td>Mail: <input name='mail' id='mail' value='${persona.mail}' disabled="disabled" size="80">
					<c:if test="${persona.password == null}">
					<a href="javascript:openDialogMail();"><img src="images/pencil.png" width="24px" title="Modificar Mail"></a>
					</c:if>
				</td>			
				<td>Habilitado TODO PAGO: 
					<c:if test="${persona.password != null}">
						<img src="images/dialog-ok-apply-2.png" alt="" width="24px"/>
					</c:if>
					<c:if test="${persona.password == null}">
						<img src="images/red-cross.png" alt="" width="24px"/>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					Legajo Grado: <input name='mail' id='mail' value='${persona.legajoGrado}' disabled="disabled" size="15">  - Legajo Posgrado: <input name='mail' id='mail' value='${persona.legajoPosgrado}' disabled="disabled" size="15">  - Identificador DEP: <input name='mail' id='mail' value='${persona.iddep}' disabled="disabled" size="15" >    
				</td>
			</tr>
		</table>
	</fieldset>	
	<fieldset><legend>Domicilio</legend>
		<table>
			<tr>
				<td>Provincia:<input name='provincia' id='provincia' value='${persona.direccion.pcia}' disabled="disabled"></td>
				<td>Localidad:<input name='localidad' id='localidad' value='${persona.direccion.localidad}' disabled="disabled">
				Codigo Postal:<input name='codigoPostal' id='codigoPostal' value='${persona.direccion.cp}' size="9" disabled="disabled"></td>
			</tr>
			<tr>
				<td>Calle:<input name='calle' id='calle' value='${persona.direccion.calle}' size="50" disabled="disabled"></td>
				<td>Numero:<input name='numeroCalle' id='numeroCalle' value='${persona.direccion.numero}' size="5" disabled="disabled">
					Depto:<input name='depto' id='depto' value='${persona.direccion.depto}'size="3" disabled="disabled">
					Piso:<input name='piso' id='piso' value='${persona.direccion.piso}' size="2" disabled="disabled"></td>
			</tr>
		</table>
	</fieldset>
	<fieldset><legend>Datos Academicos</legend>
		<table id='datosAcademicos'>
			<tr> 	
				<td colspan="1">
					<a href="#">
						<img src="images/help.png" width="24px" title="Corresponde a la fecha de ingreso del Alumno a la UNLa, si no se indica nada toma la fecha del dia de la carga."/>
					</a>
					Fecha de Ingreso:
					<input name='fechaIngreso' id='fechaIngreso' value='${persona.fechaIngreso}' disabled="disabled">
				</td>
				
			</tr>
		</table>
		<fieldset><legend>Carreras</legend>
			<table id='carrerasPersona'>
				<tr>
					<td>
						<ul id='carreraPersona'>
							<c:forEach items="${persona.inscripciones}" var="inscripcion">
								<li>
									<c:out value="${inscripcion.carrera.nombreCarrera}"></c:out>  						
									<c:choose>
										<c:when test="${inscripcion.estado eq 65}">
											(Activo-Regular)
										</c:when>
										<c:when test="${inscripcion.estado eq 69}">
											(Egresado)
										</c:when>
										<c:when test="${inscripcion.estado eq 80}">
											(Pasivo)
										</c:when>
										<c:when test="${inscripcion.estado eq 78}">
											(Abandono)
										</c:when>
										<c:when test="${inscripcion.estado eq 73}">
											(Ingresante)
										</c:when>
										<c:otherwise>
											(No tiene estado)
										</c:otherwise> 
									</c:choose>	
									 <img src="images/deleteGris.png" width="15px" title="Eliminar Carrera" />
								</li>
							</c:forEach>
						</ul>
					</td>
					<td>
						<select id='carreras' name='carreras' size='10' ondblclick="javascript:modificarInscripcion('agregarInscripcion','${persona.id}',this[selectedIndex].value,'','${operacion}');">
							<c:forEach items="${carreras}"  var="carrera">	
								<c:if test="${carrera.estado.activado==true}">
									<option value="${carrera.id}">${carrera.codigo} - ${carrera.nombreCarrera}</option>
								</c:if>
							</c:forEach>								
						</select>
					</td>
				</tr>
			</table>
		</fieldset>	
	</fieldset>
	<input type="hidden" id="operacion" name="operacion" value="">
	<input type="hidden" id="operacionAnterior" name="operacionAnterior" value="${operacion}">
	<input type="hidden" id="id" name="id" value='${persona.id}'>
</form>
<div id="dialogFormBeca" title="Beca">
	<form name="edicionBeca" id="edicionBeca">
		Descripcion: <input type="text" name="descripcion" id="descripcion">
	</form>
</div>

<div id="dialogFormMail" title="Mail">
	<form name="edicionMail" id="edicionMail">
		Mail: <input type="text" name="mailEdit" id="mailEdit"  value='${persona.mail}' size=30>
		<input type="hidden" name="pass" id="pass" value="${persona.password}">
		<input type="hidden" name="operacionMail" id="operacionMail" value="actualizarMail">
	</form>
	<p>El email de la persona solo podra ser modificado si el usuario no ha gestionado aun su usuario de Todo Pago
	</p>
	
</div>
<div id="dialogDescuento" title="Beca">
	<form name="descuentoAcademico" id="descuentoAcademico">
		<table id="descuentoAcademicoT" name="decuentoAcademicoT">
			<tr>
				<td>
					Carrera:
				</td>
				<td>
					<select id="carreraDescuento" name="carreraDescuento" onchange="javascript:buscaDescuento();">
						<c:forEach items="${persona.inscripciones}" var="inscripcion">
							<option value='${inscripcion.carrera.id}'> ${inscripcion.carrera.nombreCarrera}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					Tipo de Beca:
				</td>
				<td>
					<select id="becaS" name="becaS">
						<c:forEach items="${becas}" var="becaAux">
							<!--  elegir el tipo de decuento que tenga cargado la persona en la inscripcion -->
							<option value='${becaAux.id}'> ${becaAux.descripcion}</option>
						</c:forEach>
					</select>
					<a href="javascript:openDialogBeca('altaBecaJson','');"><img src="images/add.png" width="15px" title="Agregar Beca"/></a>
					<a href="javascript:openDialogBeca('modificarBecaJson','true');"><img src="images/pencil.png" width="15px" title="Modificar Beca"/></a>
				</td>
			</tr>
			<tr>
				<td>
					Descuento:
				</td>
				<td>
					<input type="text" name="descuentoPorcentaje" id="descuentoPorcentaje">
				</td>
			</tr>
			<tr>
				<td>
					Eliminar:
				</td>
				<td>
					<input type="checkbox" name="removePersonaBeca" id="removePersonaBeca">
				</td>
			</tr>
		</table>
	</form>
</div> 
<input type="hidden" name="cartel" value="${msgError}" id="cartel">
<script type="text/javascript">
var operacionBeca;
if($('#cartel').val()!=""){
	window.alert($('#cartel').val());
}
$(function(){
	$("#beca")
		.button()
		.click(function(e){
			$('#descuentoAcademico')[0].reset(); 
			buscaDescuento();
			e.preventDefault();
		});
	$("#descripcion").keypress(function( event ){
		  if ( event.keyCode == 13 ) {
				event.preventDefault();
				var descripcion = $("#descripcion").val(); 
				if(!isEmpty(descripcion)){
					operacionesBeca(descripcion);
					$("#dialogFormBeca").dialog('close');
				}
				
		  }
	  });
	$("#volver")
		.button()
		.click(function(){
			document.edicion.operacion.value = 'listar';
			document.getElementById("edicion").submit();
		});
	$("#guardar")
		.button()
		.click(function(){
			guardar();
		});
		
	
	$("#dialogFormBeca").dialog({
		autoOpen: false,
		height:250,
		width:500,
		modal: true,
		buttons: {
			'Grabar': function(event) {
				controlaDescuento();
				var descripcion = $("#descripcion").val(); 
				if(!isEmpty(descripcion)){
					operacionesBeca(descripcion);
					$("#dialogFormBeca").dialog('close');
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
	
	$("#dialogFormMail").dialog({
		autoOpen: false,
		height:200,
		width:500,
		modal: true,
		buttons: {
			'Grabar': function(event) {
				var mail = $("#mailEdit").val(); 
				var pass = $("#pass").val();
				
				if(isEmpty(pass)){
					if(!isEmpty(mail)){
						operacionesMail(mail);
						$("#dialogFormMail").dialog('close');
					}
				}else{
				Alert("El usuario tiene generada la contraseña en Pago Electronico, no puede cambiar la cuenta de mail");
				}	
			},
			Cancel: function(){
				$(this).dialog('close');
			}
		},
		close: function(){
		}
	});
	
	$("#dialogDescuento").dialog({
		autoOpen: false,
		height:250,
		width:500,
		modal: true,
		buttons: {
			'Grabar': function(event) {
				var idCarrera = $("#carreraDescuento").val();
				//En el form edicion el input "id" es el id de la persona.
				var idPersona = $("#id").val();
				var descuento = $("#descuentoPorcentaje").val();
				var idBeca = $("#becaS option:selected").val();
				
				//console.log("idPersona:",idPersona," -Descuento: ",descuento," -IdBeca:",idBeca);
				var controlDescuento = controlaDescuento();
				if((idCarrera != '' & descuento!= '' & (idBeca!= '' || idBeca!= 'undefined')) &controlDescuento){
					if($("#removePersonaBeca").is( ":checked" )==false){
						$.post('PersonaServlet?operacion=cargarDescuentoAcademico&descuentoPorcentaje='+descuento+'&carreraDescuento='+idCarrera+'&idPersona='+idPersona+'&idBeca='+idBeca,'', function(data){
							persona = data.lista[0];
							$("#becasPersona option").remove();
							cargarPersonaBecas(persona);
						});
					}else{ 
						console.log("idpersona: ",idPersona,"idcarrera: ", idCarrera,"ExisteBeca: ",existeBeca );
						$.post('PersonaServlet?operacion=buscaDescuento&idCarrera='+idCarrera+'&idPersona='+idPersona,'', function(data){
							console.log("Status: ",data.status,data.lista.idCarrera);
							var inscripcion = data.lista[0];
							console.log("inscripcion.personasBeca: ", inscripcion.personasBeca.id);
							if(inscripcion.personasBeca!=""){
								console.log("deleteando por la vida");
								$.post('PersonaServlet?operacion=eliminarDescuentoAcademico&carreraDescuento='+idCarrera+'&idPersona='+idPersona+'&idBeca='+idBeca,'', function(data){
									persona = data.lista[0];
									$("#becasPersona option").remove();
									cargarPersonaBecas(persona);
								});
							}else{
								alert("No existe la beca que intenta borrar");
							}
						});
					}
					$("#dialogDescuento").dialog('close');
				}else{
					event.preventDefault();
					if(controlDescuento){
						alert("Son requeridos los tres campos");	
					}
				}
			},
			Cancel: function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			$(this).dialog('close');
			//allFields.val('').removeClass('ui-state-error');
		}
	});
	
	//acata
	$("#tipoDocumento").change(function( event ){
		var tipoDocumento = $("#tipoDocumento option:selected").val();
		var numeroDocumento = 	$("#numeroDocumento").val();
		$("#numeroDocumento").val(controlaNumeroDocumento(tipoDocumento,numeroDocumento));
	});
	$("#numeroDocumento").blur(function() {
		var numeroDocumento = 	$("#numeroDocumento").val();
		var tipoDocumento = $("#tipoDocumento option:selected").val();
		if(tipoDocumento!="PA"){
			$("#numeroDocumento").val(numeroDocumento.replace(/[^0-9]/g, ''));
		}else{
			$("#numeroDocumento").val(numeroDocumento.replace(/[^0-9a-zA-Z]/g, ''));
		}
		
	});
	
	$("#descuentoPorcentaje").keydown(function( event ){
		  if ( event.which == 13 ) {
			   event.preventDefault();
			  }
	  });
});
function controlaNumeroDocumento(tipoDocumento,numeroDocumento){
	console.log(numeroDocumento);
	if(tipoDocumento!="PA"){
		numeroDocumento= numeroDocumento.replace(/[^0-9]/g, '');
	}
	//console.log(numeroDocumento);
	return numeroDocumento;
}

function existeBeca(idPersona,idCarrera ){
	var resp=null;
	$.post('PersonaServlet?operacion=buscaDescuento&idCarrera='+idCarrera+'&idPersona='+idPersona,'', function(data){
		console.log("Status: ",data.status,data.lista.idCarrera);
		var inscripcion = data.lista[0];
		console.log("inscripcion.personasBeca: ", inscripcion.personasBeca.id);
		if(inscripcion.personasBeca!=""){
			resp = true;
		}
	});
	return resp;
}

function controlaDescuento(){
	console.log("controla el descuento");
	var respuesta = true;
	descuento = $("#descuentoPorcentaje").val();
	if((descuento < 0) || (descuento > 100)){
		alert("valores permitidos de 1 a 100");
		$("#descuentoPorcentaje").select();
		respuesta = false;
	}
	return respuesta;
}
function cargarPersonaBecas(persona){
	console.log("entro cargarPersonaBecas");
	var string="<option value='0'>Sin Beca</option>";
	var i;
	var inscripciones = persona.inscripciones;	
	console.log("inscripciones.length",inscripciones.length);
	for(i=0;i<inscripciones.length;i++){
		var inscripcion = inscripciones[i];
		personasBeca = "";
		if(inscripcion.personasBeca != ""){
			personasBeca=inscripcion.personasBeca;
			console.log("personasBeca: ",personasBeca.id);
			string = string + '<option value="'+personasBeca.id+'">'+inscripcion.nombreCarrera + " - " + inscripcion.personasBeca.beca.descripcion +" - " +  inscripcion.personasBeca.descuentoBeca+"%" +'</option>';
		}
	}
	$('#becasPersona').html(string); 
}
function openDialogBeca(operacion,cargarDescripcion){
	operacionBeca= operacion;
	console.log("openDialogBeca");
	var idBeca = $("#becaS option:selected").val();
	$("#descripcion").val('');
	$('#dialogFormBeca').dialog('option', 'title', 'Crear Beca');
	if(cargarDescripcion == "true" && idBeca !=1 ){
		$('#dialogFormBeca').dialog('option', 'title', 'Modificar Beca');
		$("#descripcion").val($("#becaS option:selected").text());
		var beca = $("#becaS option:selected").val();
		$("#beca").val(beca);
	}
	console.log("opening...");
	$('#dialogFormBeca').dialog('open');
}
function openDialogMail(){
	$('#dialogFormMail').dialog('option', 'title', 'Actualizar Mail');
	$('#dialogFormMail').dialog('open');
}

function operacionesMail(){
	var mailEdit =  $("#mailEdit").val();
	if(!isEmpty(mailEdit)){
		mailEdit = mailEdit.replace(/^\s+/g,'').replace(/\s+$/g,'').replace(/\s/g,'');
	}
	var operacion = $("#operacionMail").val();
	var idPersona = $("#id").val();
	$.post('PersonaServlet?operacion='+operacion+'&mail='+mailEdit+'&idPersona='+idPersona,'', function(data){
		persona = data.lista[0];
		//console.log("length: "+data.lista.length + "- isEmpty: " + isEmpty(persona.msj));
		if(!isEmpty(persona.msj)){
			console.log(persona.msj);
			alert(persona.msj);
		}else{
			$("#mail").val(persona.mail);
		}
		
	});
}
function operacionesBeca(descripcion){
	var beca =  $("#becaS option:selected").val();
	console.log("OperacionBeca: ",operacionBeca);
	$.post('BecaServlet?operacion='+operacionBeca+'&descripcion='+descripcion+'&idBeca='+beca,'', function(data){
		beca = data.lista[0];
		if ($("#becaS option[value="+beca.id+"]").length == 0){
			$('#becaS').append($('<option>', {
			    value:beca.id,
			    text: beca.descripcion,
			    selected: true
			}));
		}else if($("#becaS option[value="+beca.id+"]").text() != beca.descripcion){
			$("#becaS option[value="+beca.id+"]").text(beca.descripcion);
		}else{
			$("#becaS option[value=" + beca.id +"]").attr("selected","selected");
		}
	});
}
function buscaDescuentoPorInscripcion(idInscripcion){
	console.log("buscaDescuentoPorInscripcion(",idInscripcion,")");
	if(idInscripcion && idInscripcion!=0){
		$.post('PersonaServlet?operacion=buscaDescuentoPorInscripcion&idInscripcion='+idInscripcion,'', function(data){
			var inscripcion = data.lista[0];
			var personaBeca = inscripcion.personasBeca;
			console.log("personaBeca: ",personaBeca.id,"-",personaBeca.descuentoBeca);
			if(personaBeca != ""){

				$("#carreraDescuento option[value=" + inscripcion.idCarrera+"]").attr("selected","selected") ;	
				$("#descuentoPorcentaje").val(personaBeca.descuentoBeca);
				$("#becaS option[value=" + personaBeca.beca.id +"]").attr("selected","selected") ;	
			}
			$('#dialogDescuento').dialog('open');
		});	
	}else{
		if(idInscripcion!=0){
			alert("Error, el alumno no esta inscripto a ninguna carrera.");
		}
	}
}
function buscaDescuento(){
	//En el form edicion el input "id" es el id de la persona.
	var idPersona = $("#id").val();
	var idCarrera = $("#carreraDescuento").val();
	if(idCarrera){
		$.post('PersonaServlet?operacion=buscaDescuento&idCarrera='+idCarrera+'&idPersona='+idPersona,'', function(data){
			var inscripcion = data.lista[0];
			var personaBeca = inscripcion.personasBeca;
			console.log("personaBeca: ",personaBeca.id,"-descuentoBeca: ",personaBeca.descuentoBeca);
			if(personaBeca != ""){
				console.log("laconchadetumadre");
				$("#descuentoPorcentaje").val(personaBeca.descuentoBeca);
				console.log(personaBeca.beca.id);
				$("#becaS option[value=" + personaBeca.beca.id +"]").attr("selected","selected") ;	
			}else{
				//$('#descuentoAcademico')[0].reset(); 
				$("#descuentoPorcentaje").val('');
			}
			$('#dialogDescuento').dialog('open');
		});	
	}else{
		alert("Error, el alumno no esta inscripto a ninguna carrera.");
	}
}
function guardar(){
	if((document.edicion.piso.value=="" || !isNaN(document.edicion.piso.value))&& 
				(document.edicion.numeroDocumento.value!="" && document.edicion.nombre.value!="" && document.edicion.apellido.value!="")){
		
		if(document.getElementById("id").value != ''){
			document.edicion.operacion.value = 'actualizar';
		}else{
			document.edicion.operacion.value = 'alta';
		}
		document.getElementById("edicion").submit();
	}else{
		alert("El nombre, apellido y numero de documento son obligatorios, el piso debe ser numerico");
		event.preventDefault();
	}
}
function doOnLoad(idPersona){
	if(idPersona){
		$('#carreras').attr('disabled','');
	}
	myCalendar = new dhtmlXCalendarObject(["fechaIngreso"]);
	myCalendar.loadUserLanguage("esp");
	myCalendar.setDateFormat("%d-%m-%Y");
	document.getElementById("apellido").focus();
}
// Para agregar y eliminar Inscripciones
function modificarInscripcion(operacion,idPersona,idCarrera,idInscripcion,operacionAnterior){
	var cantidadItemsBeca = $("#becaS").size();
	//console.log("cantidadItemsBeca: "+cantidadItemsBeca);
	//me quede aca tebngo que comprar el size > 1 si es mayor pasa porq tiene beca sino no tiene beca
	if(cantidadItemsBeca>1){
		//$.post('PersonaServlet?operacion=buscaDescuento&idCarrera='+idCarrera+'&idPersona='+idPersona,'', function(data){
			//var inscripcion = data.lista[0];
			//var personaBeca = inscripcion.personasBeca;
			//if(inscripcion.personasBeca != ""){
			alert("Beca existente, No se puede borrar la inscripcion");
		//});
	}else{
			document.formModificarInscripcion.operacion.value = operacion;
			document.formModificarInscripcion.idPersona.value= idPersona;
			document.formModificarInscripcion.idCarrera.value= idCarrera;
			document.formModificarInscripcion.idInscripcion.value= idInscripcion;
			document.formModificarInscripcion.operacionAnterior.value= operacionAnterior;
			document.formModificarInscripcion.submit();			
		}
	
}
function buscaCarreras(){
	document.edicionCuotaPago.operacion.value = "carrerasJson";
	$.post('CarreraServlet',$("#edicionCuotaPago").serialize(), function(data){
		if(data.status == "OK"){
			cargaCarreras(data);
		};
	});	
}
function cargaCarreras(carreras){
	var string;
	for(i=0;i<carreras.length ;i++){
		string = string +
		'<option value="'+carreras[i].id+'">'+carreras[i].nombreCarrera+'</option>';
	}
	$('#carreras').html(string); 
}
</script>
</html>