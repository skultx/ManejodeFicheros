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

// Para XLS
List<String[]> datosXLS = (List<String[]>) request.getAttribute("datosXLS");

//Para JSON
List<Map<String, String>> datosJSON = (List<Map<String, String>>) request.getAttribute("datosJSON");

//Para RDF
List<List<String>> rdfData = (List<List<String>>) request.getAttribute("rdfData");
%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <h1>Datos del Fichero</h1>

    <!-- Verificar si se seleccionó CSV -->
    <% if ("CSV".equals(formatoFichero)) { %>
        <h2>Datos CSV</h2>
        <% if (dataCSV != null && !dataCSV.isEmpty()) { %>
            <table border="1">
                <tr>
                    <th>Dato 1</th>
                    <th>Dato 2</th>
                    <th>Dato 3</th>
                    <th>Dato 4</th>
                    <th>Dato 5</th>
                    <th>Dato 6</th>
                </tr>
                <% for (String[] fila : dataCSV) { %>
                    <tr>
                        <% for (String celda : fila) { %>
                            <td><%= celda %></td>
                        <% } %>
                    </tr>
                <% } %>
            </table>
        <% } else { %>
            <p>No hay datos disponibles para mostrar.</p>
        <% } %>

    <% } else if ("XML".equals(formatoFichero)) { %>
        <h2>Datos XML</h2>
        <% if (listaDatosXML != null && !listaDatosXML.isEmpty()) { %>
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
                <% for (Map<String, String> fila : listaDatosXML) { %>
                    <tr>
                        <td><%= fila.get("id") %></td>
                        <td><%= fila.get("codigo") %></td>
                        <td><%= fila.get("tituloCurso") %></td>
                        <td><%= fila.get("descripcionObjetivos") %></td>
                        <td><%= fila.get("requisitos") %></td>
                        <td><%= fila.get("familia") %></td>
                        <td><%= fila.get("duracionHoras") %></td>
                    </tr>
                <% } %>
            </table>
        <% } else { %>
            <p>No hay datos disponibles para mostrar.</p>
        <% } %>

    <% } else if ("XLS".equals(formatoFichero) && datosXLS != null && !datosXLS.isEmpty()) { %>
        <h2>Datos XLS</h2>
        <table border="1">
            <thead>
                <tr>
                    <% for (int i = 0; i < datosXLS.get(0).length; i++) { %>
                        <th>Columna <%= (i + 1) %></th>
                    <% } %>
                </tr>
            </thead>
            <tbody>
                <% for (String[] fila : datosXLS) { %>
                    <tr>
                        <% for (String celda : fila) { %>
                            <td><%= celda %></td>
                        <% } %>
                    </tr>
                <% } %>
            </tbody>
        </table>


    <% } else if ("JSON".equals(formatoFichero)) { %>
        <h2>Datos JSON</h2>
        <table border="1">
            <thead>
                <tr>
                    <% for (String key : datosJSON.get(0).keySet()) { %>
                        <th><%= key %></th>
                    <% } %>
                </tr>
            </thead>
            <tbody>
                <% for (Map<String, String> fila : datosJSON) { %>
                    <tr>
                        <% for (String key : fila.keySet()) { %>
                            <td><%= fila.get(key) %></td>
                        <% } %>
                    </tr>
                <% } %>
            </tbody>
        </table>
		<%
        } else if ("RDF".equals(formatoFichero)) {
    %>
    <h2>Datos RDF</h2>
    <%
        if (rdfData != null && !rdfData.isEmpty()) {
            List<String> subjects = rdfData.get(0);
            List<String> predicates = rdfData.get(1);
            List<String> objects = rdfData.get(2);
    %>
    <table border="1">
        <tr>
            <th>Subject</th>
            <th>Predicate</th>
            <th>Object</th>
        </tr>
        <%
            for (int i = 0; i < subjects.size(); i++) {
        %>
        <tr>
            <td><%= subjects.get(i) %></td>
            <td><%= predicates.get(i) %></td>
            <td><%= objects.get(i) %></td>
        </tr>
        <%
            }
        %>
    </table>
    <%} %>
    <% } else { %>
        <p>No hay datos disponibles o el formato seleccionado no es válido.</p>
    <% } %>

    <!-- Botón de Volver -->
    <form action="TratamientoFich.jsp" method="post">
        <input type="submit" value="Volver" name="volver" />
    </form>
</body>
</html>
