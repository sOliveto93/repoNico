<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Grupo-Informes</title>
</head>
<body>
<script language="javascript">
	$(function(){
		dialogoGenerico(250,330,"dialog-reporteCuotasPorGrupoConMonto","formGrupoConMonto");		
	});
	function dialogReporteGrupoMonto(radioButton){
		var id = radioButton.id ;
		if(id == 'reporteGrupoConMonto' || id == 'reporteGrupoSinMonto'){
			$('#grupoTr').css('display','');
			$('#cohorteTr').css('display','');
			$('#nroDocumentoTr').css('display','none');
			$('#dialog-reporteCuotasPorGrupoConMonto').dialog( "option", "height", 200 );
		}else if(id == 'reporteGrupoPorCarrera'){
			$('#grupoTr').css('display','');
			$('#cohorteTr').css('display','none');
			$('#nroDocumentoTr').css('display','none');
			$('#dialog-reporteCuotasPorGrupoConMonto').dialog( "option", "height", 170 );
		}
		$('#operacion').val(id);
		$("#formGrupoConMonto input[name=operacion]").val(id);		
		$('#dialog-reporteCuotasPorGrupoConMonto').dialog('open');
	}
</script>
<fieldset><legend>Informes</legend>
	<form>
	 	<!-- <input type="radio" name="informe" id="reporteGrupoConMonto" onclick="javascript:dialogReporteGrupoMonto(this);">Cuotas por Grupo con Monto<br><br>		
	 	<input type="radio" name="informe" id="reporteGrupoSinMonto" onclick="javascript:dialogReporteGrupoMonto(this);">Cuotas por Grupo sin Monto<br><br> -->
	 	<input type="radio" name="informe" id="reporteGrupoPorCarrera" onclick="javascript:dialogReporteGrupoMonto(this);">Alumnos Activos por Carrera por Grupo<br><br>
	 	<!--  <input type="radio" name="informe" onclick="javascript:alert('Alumnos Carrera sin Grupo');">Alumnos Carrera sin Grupo-->
	</form>
</fieldset>
<div id="dialog-reporteCuotasPorGrupoConMonto" title="Grupo">
	<form id="formGrupoConMonto" name="formGrupoConMonto" action="ReporteServlet" method="post">
		<table>
		<tr id="grupoTr">
			<td >
				Grupo:
			</td>
			<td>
				<input type="hidden" id="idGrupoInforme" name="idGrupoInforme" />
				<input id="grupoInforme" name="grupoInforme" onclick="javascript:windowGrupo(functionAceptar);"/>
				<img src="images/lupa.gif" onclick="javascript:windowGrupo(functionAceptar);" height="25" width="25"/><br>
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
		<input type="hidden" name="operacion" id="operacion" value="">
	</form>
</div>
</body>
</html>