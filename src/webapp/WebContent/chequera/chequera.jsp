<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="cuotas.dataSource.repository.hibernate.FactoryDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<%@ include file="/administracion/abm_headers.jsp"%> 
<title>Chequeras</title>
</head>
<body>
<div class="recuadro">
	<%@ include file="/menu.jsp"%>
	<div id="chequeraPersonas"  align="left">	
		<%@ include file="chequeraPersonas.jsp"%>
	</div>
</div>
</body>
</html>