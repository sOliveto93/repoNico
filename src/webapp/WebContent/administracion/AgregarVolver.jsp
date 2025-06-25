<script type="text/javascript">
	$(document).ready(function() {
			$("#agregar").button();
			$("#volver").button();
	});
</script>

<table name="botones" id="botones">
	<tr>
		<td>
			<span align="left">
				<button id="agregar" onclick="javascript:agregar();">Agregar<img src="images/add.png" style="" /></button>
			</span>
		</td>
		<td>
			<form action="./index.jsp" method="post" name="button">
				<span align="left">
					<button id="volver" name="operacion">Volver</button>
				</span>
			</form>
		</td>
	</tr>
</table>
