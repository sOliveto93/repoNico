<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<%@ include file="/administracion/abm_headers.jsp"%>
</head>
<body>
<input type="hidden" name="cartel" value="$!{msgError}" id="cartel">

<form action="GrupoCuotasServlet" method="post" name="grupoCuotasF" id="grupoCuotasF">
<div class="div-table">
	 <div class="div-table-row">
  	   <div class="div-table-col">
			 <table id="grupoCoutas" style="visibility: hidden">
				<thead>
					<tr>
						<th>M/E</th>
						<th>N&ordm;Mes</th>
						<th>Monto Concepto N&ord;1</th>
						<th>Monto Concepto N&ord;2</th>
					</tr>
				</thead>
				<tbody id="datos_grupoCuotas"></tbody>
			</table>
	   </div>
	</div>
</div>
<input type="hidden" id="idGrupo" name="idGrupo" value="">
<input type="hidden" id="montoUnoHidden" name="montoUnoHidden" value="">
<input type="hidden" id="montoDosHidden" name="montoDosHidden" value="">
</form>

</body>
<script type="text/javascript">
function ActualizarCuotas(){
	var i=0;
	for(i=1;i<=24;i++){
		$("#"+i+"-montoUno").val(verificarValor($("#montoUnoHidden").val()));
		$("#"+i+"-montoDos").val(verificarValor($("#montoDosHidden").val()));
		$("#N"+i+"-montoUno").val(verificarValor($("#montoUnoHidden").val()));
		$("#N"+i+"-montoDos").val(verificarValor($("#montoDosHidden").val()));
	}
	$("#preCarga").fadeTo('60000',0.5);
}
function verificarValor(valor){
	if(isEmpty(valor)){
		return '0';
	}else{
		return valor;
	}
}
</script>
</html>