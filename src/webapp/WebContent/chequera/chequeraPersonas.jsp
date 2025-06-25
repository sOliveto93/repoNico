<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>

<script type="text/javascript" src="chequera/chequereaPersona.js"></script>
<form action="ChequeraServlet" method="post" name="personasChequera" id="personasChequera" style="display: none">
	<div id="filtrosChequeraPersonas">	
		Cohorte: <input type="text" name="cohorteFiltro" id="cohorteFiltro" value="">
		Numero Documento: <input type="text" name="numeroDeDocumentoFiltro" id="numeroDeDocumentoFiltro" value="">
		<button id="NoHagoNadaSoyUnaMentira">Buscar</button><br>
		Estado: 
		<input type="radio" name="estadoFiltro" value="A" id="estadoFiltro" checked>Activo
		<input type="radio" name="estadoFiltro" value="E" id="estadoFiltro">Egresado
		<input type="radio" name="estadoFiltro" value="P" id="estadoFiltro">Pasivo
		<input type="radio" name="estadoFiltro" value="I" id="estadoFiltro">Ingresante
		<input type="radio" name="estadoFiltro" value="N" id="estadoFiltro">Abandono
		<input type="radio" name="estadoFiltro" value="T" id="estadoFiltro">Todos
	</div>
	<table id="tablaPersonas">
		<thead>
			<tr>
				<th><input type="checkbox" name="aux" onclick="selectAll()" id="aux" checked='checked'></th>
				<th>Nombre</th>
				<th>Tipo. Doc.</th>
				<th>Documento</th>
				<th>Categoria</th>
			</tr>
		</thead>
		<tbody id="personasChequeraBody">
		</tbody>
	</table>
	<input type="hidden" name="operacion" id="operacion" value="generarChequerasGrupo">
	<input type="hidden" name="grupoChequera" id="grupoChequera" value="">
	<input type="hidden" name="anioCuota" id="anioCuota" value="">
	<input type="hidden" name="forzado" id="forzado" value="">
	<input type="hidden" name="imprimir" id="imprimir" value="">
	<div id="parametrosChequeraPersonas">
		Meses:
		<input type="radio" name="filtrar_meses" value="si" id="id_filtrar_meses" checked onClick="javascript:cambiarFiltro(this);">Todos
		<input type="radio" name="filtrar_meses" value="no" id="id_no_filtrar_meses" onClick="javascript:cambiarFiltro(this);">Filtrar:
		<span id="id_meses">  
		En <input type="checkbox" name="mes" value="0" id="id_mes" disabled>.
		Feb <input type="checkbox" name="mes" value="1" id="id_mes"  disabled>. 
		Mar <input type="checkbox" name="mes" value="2" id="id_mes"  disabled>. 
		Abr <input type="checkbox" name="mes" value="3" id="id_mes"  disabled>. 
		May <input type="checkbox" name="mes" value="4" id="id_mes"  disabled>. 
		Jun <input type="checkbox" name="mes" value="5" id="id_mes"  disabled>.
		Jul <input type="checkbox" name="mes" value="6" id="id_mes"  disabled>.
		Agos <input type="checkbox" name="mes" value="7" id="id_mes"  disabled>. 
		Sep <input type="checkbox" name="mes" value="8" id="id_mes"  disabled>.
		Oct <input type="checkbox" name="mes" value="9" id="id_mes"  disabled>.
		Nov <input type="checkbox" name="mes" value="10" id="id_mes"  disabled>.
		Dic <input type="checkbox" name="mes" value="11" id="id_mes"  disabled>.
		</span>  
	</div>
	<div id="parametrosChequeraPersonas2">
		Meses:
		<input type="radio" name="filtrar_meses2" value="si" id="id_filtrar_meses2" checked onClick="javascript:cambiarFiltro2(this);">Todos
		<input type="radio" name="filtrar_meses2" value="no" id="id_no_filtrar_meses2" onClick="javascript:cambiarFiltro2(this);">Filtrar:
		<span id="id_meses2">  
		En <input type="checkbox" name="mes2" value="0" id="id_mes2" disabled>.
		Feb <input type="checkbox" name="mes2" value="1" id="id_mes2"  disabled>. 
		Mar <input type="checkbox" name="mes2" value="2" id="id_mes2"  disabled>. 
		Abr <input type="checkbox" name="mes2" value="3" id="id_mes2"  disabled>. 
		May <input type="checkbox" name="mes2" value="4" id="id_mes2"  disabled>. 
		Jun <input type="checkbox" name="mes2" value="5" id="id_mes2"  disabled>.
		Jul <input type="checkbox" name="mes2" value="6" id="id_mes2"  disabled>.
		Agos <input type="checkbox" name="mes2" value="7" id="id_mes2"  disabled>. 
		Sep <input type="checkbox" name="mes2" value="8" id="id_mes2"  disabled>.
		Oct <input type="checkbox" name="mes2" value="9" id="id_mes2"  disabled>.
		Nov <input type="checkbox" name="mes2" value="10" id="id_mes2"  disabled>.
		Dic <input type="checkbox" name="mes2" value="11" id="id_mes2"  disabled>.
		</span>  
	</div>
	<div id="parametrosChequeraPersonas3">
		Tipo Cuota:
		<input type="radio" name="filtrar_tipos" value="si" id="id_filtrar_tipos" checked onClick="javascript:cambiarFiltro3(this);">Todos
		<input type="radio" name="filtrar_tipos" value="no" id="id_no_filtrar_tipos" onClick="javascript:cambiarFiltro3(this);">Filtrar:
		<span id="id_tipos">  
		E: <input type="checkbox" name="tipo" value="E" id="id_tipo" disabled>.
		M: <input type="checkbox" name="tipo" value="M" id="id_tipo"  disabled>. 		
		</span>  
	</div>
	<br><button id="imprimirChequerasGrupo" name="imprimirChequerasGrupo">Imprimir Chequeras</button>
	 <button id="generarChequerasGrupo" name="generarChequerasGrupo">Generar Chequeras</button>
	 <c:if test="${usuario.tipo == ADM}">
	 	<button id="regenerarChequerasGrupo" name="regenerarChequerasGrupo">Forzar Regeneración de Chequeras</button>
	 </c:if>
</form>