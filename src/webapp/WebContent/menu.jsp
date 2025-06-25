<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<html>
<head>
<title>Menu</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ include file="/administracion/abm_headers.jsp"%>
<link type="text/css" href="css/menu.css" rel="stylesheet" />

<script type="text/javascript" src="js/menu.js"></script>
	<style type="text/css">
	{ margin:15; padding:15;}

	div#menu {
	    margin:0px auto;
    	width:100%;
	}

	</style>
</head>
<body  >
<div class="menu-principal">
<div id="menu" style="align:center;" >
	<ul class="menu">
		<c:if test="${empty usuario}">
			<li><a href="javascript:login();"><span><img src="images/login.png" height="32px" />Login</span></a></li>
		</c:if>
		<c:set var="DRIYC" value="DRIYC"></c:set>
		<c:set var="DEP" value="DEP"></c:set>
		
		<c:set var="DEPADMIN" value="DEP_ADMIN"></c:set> <!-- se usa para los perfiles de dep_admin y drc_admin --> 
		<c:set var="DRCADMIN" value="DRC_ADMIN"></c:set>
		
		<c:set var="CONSULTA" value="consulta"></c:set>
		<c:set var="AUDITORIA" value="auditoria"></c:set>
		<c:set var="ADM" value="admin"></c:set>
		<c:set var="USR" value="${usuario.usuario}"></c:set>
		<c:set var="PADE" value="tiana"></c:set>
		<c:set var="FORASTIER" value="pyro"></c:set>

		<c:if test="${(usuario.tipo == ADM) || (usuario.tipo == AUDITORIA)}">
			<li>
				<a href="CarreraServlet?operacion=listar"><span><img src="images/university.png" height="32px" />Carreras</span></a>
			</li>
		</c:if>  
		<c:if test="${(usuario.tipo == DEP) ||(usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN) || (usuario.tipo == ADM) || (usuario.tipo == DRIYC) || (usuario.tipo == AUDITORIA) ||  (usuario.tipo == CONSULTA)}">
				<li><a href="GrupoServlet?operacion=listar"><span><img src="images/group.png" height="32px" />Grupos</span></a></li>
				
				<c:if test="${(usuario.tipo == ADM) || (usuario.tipo == DEP) || (usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN) || (usuario.tipo == DRIYC) ||  (usuario.tipo == CONSULTA)}">
					<li><a href="PersonaServlet?operacion=listar&limpiarBusqueda=1"><span><img src="images/people.png" height="32px" />Personas</span></a>
						<c:if test="${(usuario.tipo == DEP) || (usuario.tipo == DRIYC) ||  (usuario.tipo == CONSULTA)}">
							<div>
								<ul>
									<li><a href="ChequeraServlet?operacion=filtroPersonasConChequera"><span></span>Buscar Personas con Chequeras</a></li>			
								</ul>
							</div>	
						</c:if>
						<c:if test="${(usuario.tipo == ADM)}">
							<div>
								<ul>
									<li><a href="PersonaServlet?operacion=mantenimiento_persona"><span>Agregar</span></a></li>			
									<li><a href="PersonaServlet?operacion=filtrar&adeudaCuotas=on&sancionarPersonas=true&operacionDos=filtrarMora&vieneDelServlet=no"><span>Sancionar alumnos</span></a></li>
								</ul>
							</div>
						</c:if>
					</li>
				</c:if>
				
				<c:if test="${(usuario.tipo == ADM) || (usuario.tipo == AUDITORIA) || (usuario.tipo == DEP) || (usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN) ||  (usuario.tipo == DRIYC)}"> <!--    -->
					<li><a href="FacturaServlet?operacion=listar&limpiarSession=1"><span><img src="images/invoice.png" height="32px" />Facturas</span></a>
						<div>
							<c:if test="${(usuario.tipo == ADM) || (usuario.tipo == AUDITORIA)}">
								<c:if test="${usuario.tipo == ADM}">
									<ul>
										<li><a href="FacturaServlet?id=&operacion=actualizar"><span>Nueva Factura</span></a></li>
										<li><a href="PartidaServlet?operacion=listar"><span>Partidas</span></a></li>
									</ul>
								</c:if>
							<ul>
								<li><a href="FacturaItemTipoServlet?operacion=listar"><span>Factura Item Tipo</span></a></li>
							</ul>
							<ul>
								<li><a href="FacturaRubroCobroServlet?operacion=listar"><span>Factura Rubro Cobro</span></a></li>
							</ul>
							<ul>
								<li><a href="TipoPagoServlet?operacion=listar"><span>Factura Tipo Pago</span></a></li>
							</ul>
							</c:if>
							<!--  
								<ul>
									<li><a href="FacturaServlet?operacion=modificarNroFacturas"><span>Factura Modif Numero</span></a></li>
								</ul>
							-->
						</div>
					</li>
					<c:if test="${(usuario.tipo == ADM) || (usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN)}">
						<li><a href="#"><span><img src="images/database-import.png" height="32px" />Datos Externos</span></a>
						 	<div>
								<ul>
									<li><a href="InterfaceServlet?operacion=importar_alumnos"><span>Importar Alumnos de Otros Sistemas</span></a></li>
									<c:if test="${usuario.tipo == ADM}">	
										<li><a href="InterfaceServlet?operacion=pago_facil"><span>Importar datos de Pago Facil</span></a></li>
										<li><a href="CarrerasDepCuotasServlet?operacion=listar"><span>Configuracion Carreras DEP/DRC</span></a></li>
									</c:if>
									
								</ul>
							</div>
						</li>
					</c:if>
				</c:if>
		</c:if>
		<c:if test="${(usuario.tipo == CONSULTA)  || (usuario.tipo == DEP)  || (usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN) || (usuario.tipo == ADM)|| (usuario.tipo == DRIYC)  || (usuario.tipo == AUDITORIA)}">	
			<li><a href="#"><span><img src="images/reports.png" height="32px" />Informes</span></a>
				<div>
					<ul>
						<li><a href="PersonaServlet?operacion=informePago"><span>Pagos</span></a></li>
						<li><a href="CarreraServlet?operacion=informesCarrera"><span>Carrera</span></a></li>
						<c:if test="${(usuario.tipo == DEP)  || (usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN) || (usuario.tipo == ADM) || (usuario.tipo == DRIYC)  || (usuario.tipo == AUDITORIA)}">
							<li><a href="FacturaServlet?operacion=informesFactura"><span>Facturas</span></a></li>	
						</c:if>
						<%-- <c:if test="${(usuario.tipo == ADM)  || (usuario.tipo == AUDITORIA)}">
							<li><a href="CarreraServlet?operacion=informesCarrera"><span>Carrera</span></a></li>
						</c:if> --%>
					</ul>
				</div>	
			</li>
			<c:if test="${(usuario.tipo == DEP)|| (usuario.tipo == DRIYC)}">
				<!-- <li><a href="GrupoServlet?operacion=listar"><span><img src="images/group.png" height="32px" />Grupos</span></a></li> -->
				<li><a href="FacturaItemTipoServlet?operacion=listar"><span><img src="images/invoice.png" height="32px" />Item Tipo </span></a></li>
			</c:if>	
			<c:if test="${(usuario.tipo == DEPADMIN)|| (usuario.tipo == DRCADMIN)}">
				<li><a href="CarrerasDepCuotasServlet?operacion=listar"><span><img src="images/reports.png" height="32px" />Configuracion Carreras 
																															<c:if test="${(usuario.tipo == DEPADMIN)}">DEP</c:if>
																															<c:if test="${(usuario.tipo == DRCADMIN)}">DRC</c:if>
				</span></a></li>
			</c:if>	
					
<%-- 			<c:if test="${(usuario.tipo == CONSULTA)}">
				<li><a href="GrupoServlet?operacion=listar"><span><img src="images/group.png" height="32px" />Grupos</span></a></li>
			<li><a href="ChequeraServlet?operacion=filtroPersonasConChequera"><span><img src="images/people.png" height="32px" />Personas con Chequeras</span></a></li>
			<!-- 	<li><a href="PersonaServlet?operacion=generaPagoPersona"><span><img src="images/signoPeso2.png" height="32px"/>Generar Pago</span></a></li>		 -->
			</c:if> --%>
			<li><a href="UsuarioServlet?operacion=logout"><span><img src="images/logout.png" height="32px" />Salir</span></a></li>
		</c:if>
	</ul>
</div>
<input type="hidden" id="usuarioAux" name="usuarioAux" value="${USR}">
</div>
<p>&nbsp;</p>
<div id="copyright"><a href="http://apycom.com/"></a></div>
</body>
</html>
