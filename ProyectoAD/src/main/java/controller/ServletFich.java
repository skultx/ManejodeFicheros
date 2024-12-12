package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.VCARD;
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

import java.awt.Window.Type;
import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String respuesta = "";
		String page = "";

		// Obtener el formato, la accion y los datos del formulario
		String formatoFichero = request.getParameter("opciones");
		String accion = request.getParameter("accion");
		String dato1 = request.getParameter("dato1");
		String dato2 = request.getParameter("dato2");
		String dato3 = request.getParameter("dato3");
		String dato4 = request.getParameter("dato4");
		String dato5 = request.getParameter("dato5");
		String dato6 = request.getParameter("dato6");

		if ("escritura".equals(accion) && (dato1.isBlank() || dato2.isBlank() || dato3.isBlank() || dato4.isBlank()
				|| dato5.isBlank() || dato6.isBlank())) {
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
				String rutaAbsolutaXLS = "C:\\Users\\delcrego\\git\\ManejodeFicheros\\ProyectoAD\\src\\main\\webapp\\eventos-deportivos-diciembre-2024.xls";

				if ("lectura".equals(accion)) {
					try {
						List<String[]> datosXLS = leerSimilXLS(rutaAbsolutaXLS);
						request.setAttribute("datosXLS", datosXLS);
						page = "AccesoDatosA.jsp";
					} catch (FileNotFoundException e) {
						request.setAttribute("errorMensaje", "El archivo XLS no se encuentra: " + e.getMessage());
						page = "Error.jsp";
					} catch (IOException e) {
						request.setAttribute("errorMensaje", "Error al leer el archivo XLS: " + e.getMessage());
						page = "Error.jsp";
					}
				} else if ("escritura".equals(accion)) {
					try {
						String[] valoresFila = { dato1, dato2, dato3, dato4, dato5, dato6 };
						escribirSimilXLS(rutaAbsolutaXLS, valoresFila);
						request.setAttribute("mensaje", "Datos escritos correctamente.");
						page = "TratamientoFich.jsp";
					} catch (IOException e) {
						request.setAttribute("errorMensaje", "Error al escribir en el archivo XLS: " + e.getMessage());
						page = "Error.jsp";
					}
				}
				break;

			case "CSV":
				// Parte de Alberto
				String csvFilePath = getServletContext().getRealPath("/micsv.csv");
				if ("lectura".equals(accion)) {
					leerCSV(request, response, csvFilePath);
				} else if ("escritura".equals(accion)) {
					insertarCSV(request, response, csvFilePath);
				}
				break;

			case "JSON":
				// Parte de Serafin
                String jsonFilePath = getServletContext().getRealPath("/instalaciones.json");
                if ("lectura".equals(accion)) {
                    try {
                        lecturaJSON(jsonFilePath, request);
                        page = "AccesoDatosA.jsp";
                    } catch (Exception e) {
                        request.setAttribute("errorMensaje", "Error al leer el archivo JSON: " + e.getMessage());
                        page = "Error.jsp";
                    }
                } else if ("escritura".equals(accion)) {
                    try {
                        escrituraJSON(jsonFilePath, dato1, dato2, dato3, dato4, dato5, dato6, request);
                        request.setAttribute("mensaje", "Datos escritos correctamente.");
                        page = "TratamientoFich.jsp";
                    } catch (Exception e) {
                        request.setAttribute("errorMensaje", "Error al escribir en el archivo JSON: " + e.getMessage());
                        page = "Error.jsp";
                    }
                }
                break;

			case "XML":
				String xmlFilePath = getServletContext().getRealPath("/mixmlLeer.xml");

				if ("lectura".equals(accion)) {
					try {
						List<Map<String, String>> listaDatos = leerDatosXML(xmlFilePath);
						request.setAttribute("listaDatos", listaDatos);
						page = "AccesoDatosA.jsp";
					} catch (Exception e) {
						page = "Error.jsp";
					}
				} else if ("escritura".equals(accion)) {
					try {
						String[] nombresElementos = { "CODIGO", "TITULO_DEL_CURSO", "DESCRIPCION_OBJETIVOS",
								"REQUISITOS", "FAMILIA", "DURACION_HORAS" };
						String[] valoresElementos = { dato1, dato2, dato3, dato4, dato5, dato6 };
						String nuevoId = String.valueOf(System.currentTimeMillis() % 1000);

						escribirXML(xmlFilePath, nombresElementos, valoresElementos, nuevoId);
						page = "TratamientoFich.jsp"; // Redirige al formulario
					} catch (Exception e) {
						page = "Error.jsp";
					}
				}
				break;
			}
		}
		// Enviar la respuesta, para que segun lo seleccionado, haga lo que se quiera
		request.setAttribute("errorMensaje", respuesta);
		RequestDispatcher dispatcher = request.getRequestDispatcher(page);
		dispatcher.forward(request, response);
	}

	// PARTE DE ALBERTO

	// Metodo leer CSV
	private void leerCSV(HttpServletRequest request, HttpServletResponse response, String filePath)
			throws ServletException, IOException {
		List<String[]> data = new ArrayList<>();
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
				.withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build()) {
			try {
				data = reader.readAll();
			} catch (IOException | CsvException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("data", data);
		request.getRequestDispatcher("/AccesoDatosA.jsp").forward(request, response);
	}

	// Metodo insertar CSV
	private void insertarCSV(HttpServletRequest request, HttpServletResponse response, String filePath)
			throws ServletException, IOException {
		// Obtener los datos del formulario
		String[] nuevosDatos = new String[6];
		for (int i = 0; i < 6; i++) {
			nuevosDatos[i] = request.getParameter("dato" + (i + 1));
		}

		// Verificar si los datos son válidos (puedes agregar más validaciones aquí)
		boolean datosVálidos = true;
		for (String dato : nuevosDatos) {
			if (dato == null || dato.trim().isEmpty()) {
				datosVálidos = false;
				break;
			}
		}

		// Si los datos son válidos, agregar al archivo CSV
		if (datosVálidos) {
			try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true), ';', CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
				writer.writeNext(nuevosDatos); // Escribir la nueva fila en el archivo
				// Redirigir o reenviar con éxito
				request.setAttribute("mensaje", "Datos añadidos correctamente.");
			} catch (IOException e) {
				request.setAttribute("errorMensaje", "Error al guardar los datos.");
			}
		} else {
			request.setAttribute("errorMensaje", "Todos los campos son obligatorios.");
		}

		// Reenviar a la página para mostrar el mensaje
		request.getRequestDispatcher("/TratamientoFich.jsp").forward(request, response);
	}

	// PARTE DE SERAFIN
	private void lecturaJSON(String jsonFilePath, HttpServletRequest request) throws IOException {
	    File jsonFile = new File(jsonFilePath);

	    if (!jsonFile.exists()) {
	        request.setAttribute("error", "El archivo JSON no existe.");
	        return;
	    }

	    try (FileReader fileReader = new FileReader(jsonFile)) {
	        Gson gson = new Gson();

	        // Leer el archivo como un objeto que contiene un array en "data"
	        Map<String, List<Map<String, String>>> jsonData = gson.fromJson(fileReader, Map.class);

	        // Extraer la lista de datos desde la clave "data"
	        List<Map<String, String>> dataList = jsonData.get("data");

	        // Pasar la lista como atributo a la JSP
	        request.setAttribute("datosJSON", dataList);
	    } catch (IOException e) {
	        throw new IOException("Error al leer el archivo JSON: " + e.getMessage());
	    }
	}

	public void escrituraJSON(String jsonFilePath, String dato1, String dato2, String dato3, String dato4, String dato5, String dato6, HttpServletRequest request) throws IOException {
	    File jsonFile = new File(jsonFilePath);

	    // Inicializar JSONArray para almacenar la información
	    JSONArray jsonArray = new JSONArray();

	    // Si el archivo JSON existe, leer los datos existentes
	    if (jsonFile.exists()) {
	        try (FileReader fileReader = new FileReader(jsonFile)) {
	            // Leer y parsear el archivo JSON existente
	            char[] buffer = new char[(int) jsonFile.length()];
	            fileReader.read(buffer);
	            String jsonContent = new String(buffer);
	            jsonArray = new JSONArray(jsonContent); // Parsear el JSON contenido
	        } catch (Exception e) {
	            // Agregar más información en el mensaje de error
	            throw new IOException("Error al leer los datos existentes en JSON: " + e.getMessage(), e);
	        }
	    }

	    // Crear una nueva entrada para los datos
	    JSONObject newEntry = new JSONObject();
	    newEntry.put("dato1", dato1);
	    newEntry.put("dato2", dato2);
	    newEntry.put("dato3", dato3);
	    newEntry.put("dato4", dato4);
	    newEntry.put("dato5", dato5);
	    newEntry.put("dato6", dato6);

	    // Agregar la nueva entrada al JSONArray
	    jsonArray.put(newEntry);

	    // Escribir los datos de nuevo en el archivo JSON
	    try (FileWriter fileWriter = new FileWriter(jsonFile)) {
	        // Escribir el JSONArray actualizado en el archivo
	        fileWriter.write(jsonArray.toString());
	        fileWriter.flush();
	    } catch (IOException e) {
	        // Agregar más información en el mensaje de error
	        throw new IOException("Error al escribir en el archivo JSON: " + e.getMessage(), e);
	    }
	}


	// PARTE DE GONZALO
	// Lectura de CSV con UTF-8
	private List<String[]> leerSimilXLS(String rutaArchivo) throws IOException {
		List<String[]> datos = new ArrayList<>();
		File archivo = new File(rutaArchivo);

		if (!archivo.exists()) {
			throw new FileNotFoundException("El archivo no existe en la ruta especificada: " + rutaArchivo);
		}

		// Usar InputStreamReader con UTF-8 para leer correctamente los caracteres
		try (BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] fila = linea.split(";"); // Separar por punto y coma, adaptando a CSV
				datos.add(fila);
			}
		}

		return datos;
	}

	// Escritura en archivo CSV con UTF-8
	private void escribirSimilXLS(String rutaArchivo, String[] valoresFila) throws IOException {
		File archivo = new File(rutaArchivo);

		// Crear archivo si no existe
		if (!archivo.exists()) {
			archivo.createNewFile();
		}

		try (BufferedWriter escritor = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(archivo, true), "UTF-8"))) {
			StringBuilder linea = new StringBuilder();
			for (int i = 0; i < valoresFila.length; i++) {
				linea.append(valoresFila[i]);
				if (i < valoresFila.length - 1) {
					linea.append(";"); // Separar con punto y coma
				}
			}
			escritor.write(linea.toString());
			escritor.newLine(); // Nueva línea para el próximo registro
		}
	}

	// PARTE DE LUCAS
	private void escribirRDF() {
		Scanner scanner = new Scanner(System.in);

		// Pedir los 6 datos
		System.out.print("Introduce el nombre: ");
		String name = scanner.nextLine();

		System.out.print("Introduce la edad: ");
		String age = scanner.nextLine();

		System.out.print("Introduce el correo electrónico: ");
		String email = scanner.nextLine();

		System.out.print("Introduce la URL única del recurso: ");
		String url = scanner.nextLine();

		System.out.print("Introduce el nombre de alguien que conozca: ");
		String friendName = scanner.nextLine();

		System.out.print("Introduce la URL única del amigo: ");
		String friendUrl = scanner.nextLine();

		// Pedir la ubicación del archivo RDF
		System.out.println("Introduce la ubicación del archivo RDF:");
		String rdfFilePath = scanner.nextLine();

		try {
			// Cargar o crear un modelo RDF
			Model model = ModelFactory.createDefaultModel();
			File rdfFile = new File(rdfFilePath);
			if (rdfFile.exists()) {
				model.read(new FileInputStream(rdfFile), null);
			} else {
				// Añadir prefijos al modelo si el archivo no existe
				model.setNsPrefix("vcard", VCARD.getURI());
				model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
			}

			// Crear las descripciones
			Resource person = model.createResource(url).addProperty(VCARD.FN, name)
					.addProperty(model.createProperty("http://xmlns.com/foaf/0.1/age"), age)
					.addProperty(VCARD.EMAIL, email);

			Resource friend = model.createResource(friendUrl).addProperty(VCARD.FN, friendName);

			person.addProperty(model.createProperty("http://xmlns.com/foaf/0.1/knows"), friend);

			// Guardar los cambios en el archivo RDF con el encabezado XML
			try (FileOutputStream out = new FileOutputStream(rdfFile)) {
				// Escribe manualmente la declaración XML antes del modelo RDF
				out.write("<?xml version=\"1.0\"?>\n".getBytes());
				model.write(out, "RDF/XML");
			}

			System.out.println("Datos añadidos correctamente al archivo RDF.");
		} catch (IOException e) {
			System.err.println("Error al leer o escribir el archivo RDF: " + e.getMessage());
		}
	}

	private void leerRDF() {
		// Ruta del archivo RDF
		System.out.println("Introduce la ubicación del archivo RDF:");
		String rdfFilePath = new java.util.Scanner(System.in).nextLine();

		File rdfFile = new File(rdfFilePath);

		// Verificar si el archivo existe
		if (!rdfFile.exists()) {
			System.err.println("El archivo RDF no existe: " + rdfFilePath);
			return;
		}

		try {
			// Crear un modelo RDF
			Model model = ModelFactory.createDefaultModel();
			// Leer el archivo RDF
			model.read(new FileInputStream(rdfFile), null);

			// Iterar por todos los triples y mostrarlos
			StmtIterator iter = model.listStatements();
			while (iter.hasNext()) {
				Statement stmt = iter.nextStatement();
				String subject = stmt.getSubject().toString();
				String predicate = stmt.getPredicate().toString();
				String object = stmt.getObject().toString();

				System.out.println("Subject: " + subject);
				System.out.println("Predicate: " + predicate);
				System.out.println("Object: " + object);
				System.out.println("----------------------------------------------------");
			}

		} catch (IOException e) {
			System.err.println("Error al leer el archivo RDF: " + e.getMessage());
		}
	}

	// PARTE DE ALEJANDRO
	// Para la lectura
	private List<Map<String, String>> leerDatosXML(String xmlFilePath) throws Exception {
		List<Map<String, String>> listaDatos = new ArrayList<>();

		File fXmlFile = new File(xmlFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("row");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				Map<String, String> datos = new HashMap<>();
				datos.put("id", eElement.getAttribute("_id"));
				datos.put("codigo", eElement.getElementsByTagName("CODIGO").item(0).getTextContent());
				datos.put("tituloCurso", eElement.getElementsByTagName("TITULO_DEL_CURSO").item(0).getTextContent());
				datos.put("descripcionObjetivos",
						eElement.getElementsByTagName("DESCRIPCION_OBJETIVOS").item(0).getTextContent());
				datos.put("requisitos", eElement.getElementsByTagName("REQUISITOS").item(0).getTextContent());
				datos.put("familia", eElement.getElementsByTagName("FAMILIA").item(0).getTextContent());
				datos.put("duracionHoras", eElement.getElementsByTagName("DURACION_HORAS").item(0).getTextContent());

				listaDatos.add(datos);
			}
		}
		return listaDatos;
	}

	// Para la escritura
	private void escribirXML(String rutaArchivoXML, String[] nombresElementos, String[] valoresElementos, String id)
			throws Exception {
		if (nombresElementos.length != valoresElementos.length) {
			throw new IllegalArgumentException(
					"Los nombres de los elementos y los valores deben tener la misma longitud.");
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
			// Sirve para que se guarde de linea por linea, sin esto sale todo en una
			// linea(es mas facil de leerlo)
			transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			transformador.transform(new DOMSource(documento), new StreamResult(new FileOutputStream(archivo)));

			System.out.println("Archivo XML actualizado exitosamente en: " + archivo.getAbsolutePath());
		} catch (Exception e) {
			throw new RuntimeException("Error al actualizar el archivo XML", e);
		}
	}
}