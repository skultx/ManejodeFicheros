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
	<table border="1">
		<%if(request.getAttribute("extension").equals(".csv")){ %>
	        <% for (String[] fila : listaCSV) { %>
	            <tr>
	                <% for (String celda : fila) { %>
	                    <td><%= celda %></td>
	                <% } %>
	            </tr>
	        <% } %>
        <% } %>
    </table>
</body>
</html>