package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Servlet implementation class ServletFich
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFich() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String respuesta = "";
        String page = "";

        //Obtener el formato, la accion y los datos del formulario
        String formatoFichero = request.getParameter("opciones");
        String accion = request.getParameter("accion");
        String dato1 = request.getParameter("dato1");
        String dato2 = request.getParameter("dato2");
        String dato3 = request.getParameter("dato3");
        String dato4 = request.getParameter("dato4");
        String dato5 = request.getParameter("dato5");
        String dato6 = request.getParameter("dato6");

        if ("escritura".equals(accion) && (dato1.isBlank() || dato2.isBlank() || dato3.isBlank() || dato4.isBlank() || dato5.isBlank() || dato6.isBlank())) {
            respuesta = "(*) Todos los campos son obligatorios";
            page = "TratamientoFich.jsp"; 
        } else {
            // Procesar según el tipo de fichero seleccionado
            switch (formatoFichero) {
            	case "RDF":
                	// Parte de Lucas
                	break;
                
            	case "XLS":
                	// Parte de Gonzalo
                	break;
                
            	case "CSV":
                	// Parte de Alberto
                	break;
                
            	case "JSON":
                	// Parte de Serafin
                	break;
                
            	case "XML":
                    String xmlFilePath = "C:/Users/fraferal/Downloads/mixmlLeer.xml";

                    if ("lectura".equals(accion)) {
                        try {
                            leerXML(xmlFilePath);
                            respuesta = "Archivo XML leído correctamente.";
                        } catch (Exception e) {
                            respuesta = "Error al leer el archivo XML: " + e.getMessage();
                            request.setAttribute("errorLeerArchivoXML", true);
                            page = "Error.jsp";
                        }
                    } else if ("escritura".equals(accion)) {
                        try {
                            // Nombres y valores de los elementos
                            String[] nombresElementos = {
                                "CODIGO", "TITULO_DEL_CURSO",
                                "DESCRIPCION_OBJETIVOS", "REQUISITOS",
                                "FAMILIA", "DURACION_HORAS"
                            };
                            String[] valoresElementos = {
                                dato1, dato2, dato3,
                                dato4, dato5, dato6
                            };

                            //Para dar un ID "aleatorio" a los nuevos introducidos
                            String nuevoId = String.valueOf(System.currentTimeMillis() % 1000);
                            escribirXML(xmlFilePath, nombresElementos, valoresElementos, nuevoId);
                            respuesta = "Datos añadidos al archivo XML correctamente.";
                        } catch (Exception e) {
                            respuesta = "Error al actualizar el archivo XML: " + e.getMessage();
                        }
                    }
                    page = "TratamientoFich.jsp";
                    break;
            }
        }
        // Enviar la respuesta, para que segun lo seleccionado, haga lo que se quiera
        request.setAttribute("errorMensaje", respuesta);
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    //PARTE DE ALBERTO
    
    //PARTE DE SERAFIN
    
    //PARTE DE GONZALO
    
    //PARTE DE LUCAS
    
    //PARTE DE ALEJANDRO
    //Para la lectura
    private void leerXML(String xmlFilePath) {
        try {
            // Ubicación del archivo XML
            File fXmlFile = new File("C:/Users/fraferal/Downloads/mixmlLeer.xml");

            // Crear la instancia del documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Para conocer cual es el nodo padre, no hace falta pero es informacion adicional
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            // Buscar todos los elementos <row>
            NodeList nList = doc.getElementsByTagName("row");
            System.out.println("----------------------------");

            // Recorrer cada elemento <row>
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String id = eElement.getAttribute("_id");
                    // Imprimir el valor del atributo _id como "Current Element"
                    System.out.println("\033[1m\nElemento (ID): " + id + "\033[0m");

                    // Extraer el valor de los elementos dentro de <row>
                    String codigo = eElement.getElementsByTagName("CODIGO").item(0).getTextContent();
                    String tituloCurso = eElement.getElementsByTagName("TITULO_DEL_CURSO").item(0).getTextContent();
                    String descripcionObjetivos = eElement.getElementsByTagName("DESCRIPCION_OBJETIVOS").item(0).getTextContent();
                    String requisitos = eElement.getElementsByTagName("REQUISITOS").item(0).getTextContent(); 
                    String familia = eElement.getElementsByTagName("FAMILIA").item(0).getTextContent(); 
                    String duracionHoras = eElement.getElementsByTagName("DURACION_HORAS").item(0).getTextContent(); 

                    //Para mostrar la informacion
                    System.out.println("Codigo : " + codigo);
                    System.out.println("Titulo del curso : " + tituloCurso);
                    System.out.println("Descripcion-Objetivos : " + descripcionObjetivos);
                    System.out.println("REQUISITOS : " + requisitos);
                    System.out.println("FAMILIA : " + familia);
                    System.out.println("DURACION_HORAS : " + duracionHoras);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Para la escritura
    private void escribirXML(String rutaArchivoXML, String[] nombresElementos, String[] valoresElementos, String id) throws Exception {
        if (nombresElementos.length != valoresElementos.length) {
            throw new IllegalArgumentException("Los nombres de los elementos y los valores deben tener la misma longitud.");
        }

        Document documento;
        File archivo = new File(rutaArchivoXML);

        try {
            DocumentBuilderFactory fabricaDocumentos = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructorDocumento = fabricaDocumentos.newDocumentBuilder();

            if (archivo.exists()) {
                // Si el archivo existe, cargar su contenido
                documento = constructorDocumento.parse(archivo);
                documento.getDocumentElement().normalize();
            } else {
                throw new FileNotFoundException("El archivo XML no existe en la ruta especificada: " + rutaArchivoXML);
            }

            // Obtener el nodo raíz del documento
            Element elementoRaiz = (Element) documento.getDocumentElement();

            // Crear un nuevo nodo <row>
            Element nuevoRow = documento.createElement("row");
            nuevoRow.setAttribute("_id", id); // Establecer el atributo _id

            // Agregar elementos hijos al nuevo nodo <row>
            for (int i = 0; i < nombresElementos.length; i++) {
                Element elemento = documento.createElement(nombresElementos[i]);
                elemento.appendChild(documento.createTextNode(valoresElementos[i]));
                nuevoRow.appendChild(elemento);
            }

            // Agregar el nuevo nodo <row> al nodo raíz <data>
            elementoRaiz.appendChild(nuevoRow);

            // Guardar el documento actualizado en el archivo
            Transformer transformador = TransformerFactory.newInstance().newTransformer();
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");
            transformador.setOutputProperty(OutputKeys.METHOD, "xml");
            transformador.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //Sirve para que se guarde de linea por linea, sin esto sale todo en una linea(es mas facil de leerlo)
            transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformador.transform(new DOMSource(documento), new StreamResult(new FileOutputStream(archivo)));

            System.out.println("Archivo XML actualizado exitosamente en: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el archivo XML", e);
        }
    }
}