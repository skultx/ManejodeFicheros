<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<%
    List<String[]> listaCSV = (List<String[]>) request.getAttribute("lectura");
%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <h1>Datos</h1>

	<!--//PARTE DE ALBERTO-->
    
    <!--//PARTE DE SERAFIN-->
    
    <!--//PARTE DE GONZALO-->
    
    <!--//PARTE DE LUCAS-->
	
	<!-- Mostrar los datos del XML, Parte Alejandro. SUPUESTAMENTE SI SELECCIONAIS OTRO TIPO NO DEBERIA VERSE ESTA -->
    <%
        // Recuperar la lista de datos del request
        List<Map<String, String>> listaDatos = (List<Map<String, String>>) request.getAttribute("listaDatos");

        if (listaDatos != null && !listaDatos.isEmpty()) {
    %>
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
                    // Iterar sobre la lista de datos y construir las filas
                    for (Map<String, String> fila : listaDatos) {
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
    <%
        } else {
    %>
        <p>No hay datos disponibles para mostrar.</p>
    <%
        }
    %>
    
</body>
</html>