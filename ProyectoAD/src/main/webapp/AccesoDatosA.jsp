<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<%
// Recuperar la opción de formato seleccionada
String formatoFichero = request.getParameter("opciones");

// Para CSV
List<String[]> dataCSV = (List<String[]>) request.getAttribute("data");

// Para XML
List<Map<String, String>> listaDatosXML = (List<Map<String, String>>) request.getAttribute("listaDatos");
%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Datos del Fichero</h1>

	<!-- Verificar si se seleccionó CSV -->
	<%
		if ("CSV".equals(formatoFichero) && dataCSV != null && !dataCSV.isEmpty()) {
	%>
	<h2>Datos CSV</h2>
		<table border="1">
			<tr>
				<th>Dato 1</th>
				<th>Dato 2</th>
				<th>Dato 3</th>
				<th>Dato 4</th>
				<th>Dato 5</th>
				<th>Dato 6</th>
			</tr>
		<%
		// Iterar sobre los datos CSV y construir las filas
		for (String[] fila : dataCSV) {
		%>
		<tr>
			<%
			for (String celda : fila) {
			%>
			<td><%=celda%></td>
			<%
			}
			%>
		</tr>
		<%
		}
		%>
	</table>
	
	<form action="TratamientoFich.jsp" method="post">
		<input type="submit" value="Volver" name="volver"></input>
	</form>
	<%
	} else if ("CSV".equals(formatoFichero) && (dataCSV == null || dataCSV.isEmpty())) {
	%>
	<h2>Datos CSV</h2>
	
	<p>No hay datos disponibles para mostrar.</p>
	
	<form action="TratamientoFich.jsp" method="post">
		<input type="submit" value="Volver" name="volver"></input>
	</form>
	<%
	} else if ("XML".equals(formatoFichero) && listaDatosXML != null && !listaDatosXML.isEmpty()) {
		%>
		<h2>Datos XML</h2>
		<table border="1">
		    <tr>
		        <th>ID</th>
		        <th>Código</th>
		        <th>Título del Curso</th>
		        <th>Descripción Objetivos</th>
		        <th>Requisitos</th>
		        <th>Familia</th>
		        <th>Duración (Horas)</th>
		    </tr>
		    <%
		    // Iterar sobre la lista de datos XML y construir las filas
		    for (Map<String, String> fila : listaDatosXML) {
		    %>
		    <tr>
		        <td><%= fila.get("id") %></td>
		        <td><%= fila.get("codigo") %></td>
		        <td><%= fila.get("tituloCurso") %></td>
		        <td><%= fila.get("descripcionObjetivos") %></td>
		        <td><%= fila.get("requisitos") %></td>
		        <td><%= fila.get("familia") %></td>
		        <td><%= fila.get("duracionHoras") %></td>
		    </tr>
		    <%
		    }
		    %>
		</table>
		<form action="TratamientoFich.jsp" method="post">
		    <input type="submit" value="Volver" name="volver"></input>
		</form>
		<%
		    } else if ("XML".equals(formatoFichero) && (listaDatosXML == null || listaDatosXML.isEmpty())) {
		%>
		<h2>Datos XML</h2>

		<p>No hay datos disponibles para mostrar.</p>

		<form action="TratamientoFich.jsp" method="post">
		    <input type="submit" value="Volver" name="volver"></input>
		</form>
		<%
		    } else if ("JSON".equals(formatoFichero)) {
		%>
		<!-- Parte de Serafin, podeis poner tambien la verificacion de si no hay datos para mostrar -->
		<h2>Datos JSON</h2>
		<p>Procesamiento de datos JSON pendiente de implementación.</p>
		<form action="TratamientoFich.jsp" method="post">
		    <input type="submit" value="Volver" name="volver"></input>
		</form>
		<%
		    } else if ("XLS".equals(formatoFichero)) {
		%>
		<!-- Parte de Gonzalo, podeis poner tambien la verificacion de si no hay datos para mostrar -->
		<h2>Datos XLS</h2>
		<p>Procesamiento de datos XLS pendiente de implementación.</p>
		<form action="TratamientoFich.jsp" method="post">
		    <input type="submit" value="Volver" name="volver"></input>
		</form>
		<%
		    } else if ("RDF".equals(formatoFichero)) {
		%>
		<!-- Parte de Lucas, podeis poner tambien la verificacion de si no hay datos para mostrar -->
		<h2>Datos RDF</h2>
		<p>Procesamiento de datos RDF pendiente de implementación.</p>
		<form action="TratamientoFich.jsp" method="post">
		    <input type="submit" value="Volver" name="volver"></input>
		</form>
		<%
		    } else {
		%>
		<!-- Si no hay datos o el formato seleccionado no es válido -->
		<p>No hay datos disponibles o el formato seleccionado no es válido.</p>

		<form action="TratamientoFich.jsp" method="post">
		    <input type="submit" value="Volver" name="volver"></input>
		</form>
		<%
		    }
		%>
</body>
</html>