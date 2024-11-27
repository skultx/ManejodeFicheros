<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>TRATAMIENTO FICHEROS</h2>
	<form action="ServletFich" method="post">
		<table>
			<tr>
				<!-- Informacion izquierda-->
				<td>
					<table>
						<tr>
							<td><label for="opciones">Formato del fichero:</label></td>
						</tr>
						<tr>
							<td><select id="opciones" name="opciones">
									<option value="RDF">RDF</option>
									<option value="XLS">XLS</option>
									<option value="CSV">CSV</option>
									<option value="JSON">JSON</option>
									<option value="XML">XML</option>
							</select></td>
						</tr>

						<tr>
							<td><label>¿Qué quiere hacer con el fichero?</label></td>
						</tr>
						<tr>
							<td><input type="radio" name="accion" value="lectura"
								id="lectura" checked> <label for="lectura">Lectura</label>
							</td>
						</tr>
						<tr>
							<td><input type="radio" name="accion" value="escritura"
								id="escritura"> <label for="escritura">Escritura</label>
							</td>
						</tr>
					</table>
				</td>

				<!-- Informacion derecha-->
				<td>
					<table>
						<tr>
							<td><label for="dato1">DATO1 : </label></td>
							<td><input type="text" name="dato1" id="dato1" ></td>
						</tr>
						<tr>
							<td><label for="dato2">DATO2 : </label></td>
							<td><input type="text" name="dato2" id="dato2" ></td>
						</tr>
						<tr>
							<td><label for="dato3">DATO3 : </label></td>
							<td><input type="text" name="dato3" id="dato3" ></td>
						</tr>
						<tr>
							<td><label for="dato4">DATO4 : </label></td>
							<td><input type="text" name="dato4" id="dato4" ></td>
						</tr>
						<tr>
							<td><label for="dato5">DATO5 : </label></td>
							<td><textarea name="datos" id="datos" ></textarea></td>
						</tr>
						<tr>
							<td><label for="dato6">DATO6 : </label></td>
							<td><input type="text" name="dato6" id="dato6" ></td>
						</tr>
						<tr>
							<td colspan="2"><span style="color: red;"> <%=request.getAttribute("errorMensaje") != null ? request.getAttribute("errorMensaje") : ""%>
							</span></td>

						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>
		<button type="submit" name="boton" value="enviar">Enviar</button>
	</form>
	
	<h5>HOLA!!</h5>

</body>
</html>