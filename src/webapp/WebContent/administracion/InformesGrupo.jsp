<%@ taglib uri="/WEB-INF/veltag.tld" prefix="vel"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<fieldset><legend>Informes</legend>
	<form>
	 	<input type="radio" name="informe" onclick="javascript:dialogReporteGrupoMonto('reporteGrupoConMonto','12');">Cuotas por Grupo con Monto<br><br>		
	 	<input type="radio" name="informe" onclick="javascript:dialogReporteGrupoMonto('reporteGrupoSinMonto','12');">Cuotas por Grupo sin Monto<br><br>
	 	<input type="radio" name="informe" onclick="javascript:dialogReporteGrupoMonto('reporteGrupoPorCarrera','3');">Grupo por Carrera<br><br>
	 	<input type="radio" name="informe" onclick="javascript:alert('Alumnos Carrera sin Grupo');">Alumnos Carrera sin Grupo
	</form>
</fieldset>
</body>
</html>