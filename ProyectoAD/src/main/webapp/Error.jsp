<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f8f9fa;
            color: #dc3545;
        }
        .container {
            margin-top: 50px;
        }
        .error-title {
            font-size: 24px;
            font-weight: bold;
        }
        .error-details {
            margin-top: 20px;
            font-size: 18px;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-title">¡Ha ocurrido un error!</div>
        <div class="error-details">
            Tipo de error: <%= request.getAttribute("javax.servlet.error.message") %><br>
            Código de error: <%= request.getAttribute("javax.servlet.error.status_code") %><br>
            Página de origen: <%= request.getAttribute("javax.servlet.error.request_uri") %><br>
            Detalles adicionales: <%= request.getAttribute("javax.servlet.error.exception") %>
        </div>
    </div>
</body>
</html>
