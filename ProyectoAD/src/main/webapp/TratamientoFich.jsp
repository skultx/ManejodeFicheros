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
    <form action="procesarFichero.jsp" method="post">
        <table>
            <tr>
                <!-- Tabla de la izquierda -->
                <td>
                    <table>
                        <!-- Selección del formato del fichero -->
                        <tr>
                            <td>
                                <label for="opciones">Formato del fichero:</label>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <select id="opciones" name="opciones">
                                    <option value="RDF">RDF</option>
                                    <option value="XLS">XLS</option>
                                    <option value="CSV">CSV</option>
                                    <option value="JSON">JSON</option>
                                    <option value="XML">XML</option>
                                </select>
                            </td>
                        </tr>

                        <!-- Opciones de lectura o escritura -->
                        <tr>
                            <td>
                                <label>¿Qué quiere hacer con el fichero?</label>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="radio" name="accion" value="lectura" id="lectura" checked>
                                <label for="lectura">Lectura</label>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="radio" name="accion" value="escritura" id="escritura">
                                <label for="escritura">Escritura</label>
                            </td>
                        </tr>
                    </table>
                </td>

                <!-- Tabla de la derecha -->
                <td>
                    <table>
                        <!-- Campos de texto -->
                        <tr>
                            <td><label for="dato1">DATO1:</label></td>
                            <td><input type="text" name="dato1" id="dato1" required></td>
                        </tr>
                        <tr>
                            <td><label for="dato2">DATO2:</label></td>
                            <td><input type="text" name="dato2" id="dato2" required></td>
                        </tr>
                        <tr>
                            <td><label for="dato3">DATO3:</label></td>
                            <td><input type="text" name="dato3" id="dato3" required></td>
                        </tr>
                        <tr>
                            <td><label for="dato4">DATO4:</label></td>
                            <td><input type="text" name="dato4" id="dato4" required></td>
                        </tr>
                        <tr>
                            <td><label for="datos">DATOS:</label></td>
                            <td><textarea name="datos" id="datos" required></textarea></td>
                        </tr>
                        <tr>
                            <td><label for="dato6">DATO6:</label></td>
                            <td><input type="text" name="dato6" id="dato6" required></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <br>
        <!-- Mensaje de error, Hay que configurarlo en el servlet
        <span>(*) Los campos no pueden estar vacíos</span>
        <br><br>-->

        <!-- Botón de envío -->
        <button type="submit">Enviar</button>
    </form>
</body>
</html>