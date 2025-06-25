<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Login</title>
<%@ include file="/administracion/abm_headers.jsp"%>
<script  type="text/javascript">
$(function(){
	$("#dialog-login").dialog({
		autoOpen: false,
		height:260,
		width:300,
		modal: true,
		buttons: {
			'Acceder': function() {
				document.usuarioLogin.submit();
			},
			Cancel: function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			allFields.val('').removeClass('ui-state-error');
		}
	});
});
function login(){
	$('#dialog-login').dialog('open');
	$('#usuario').focus();
}
function acceder(e) {
	  tecla = (document.all) ? e.keyCode : e.which;
	  if (tecla==13) document.usuarioLogin.submit();
}
</script>
</head>
<body>
<div id="dialog-login" name="Login">
	<div style="margin-top: 1em;">
		<form action="UsuarioServlet" id="usuarioLogin" name="usuarioLogin" method="post">
			<table id="myTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><div align="center">Ingrese sus Datos</div></td>
				</tr>
				<tr></tr>
				<tr>
					<td>Usuario:</td>
				</tr>
				<tr>
					<td><input type="text" value="" name="usuario" id="usuario"></td>
				</tr>
				<tr>
					<td>Password:</td>
				</tr>
				<tr>
					<td><input type="password" value="" name="password" id="password" onkeypress="acceder(event)" ></td>
					<input type="hidden" name="operacion" value="login">
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>