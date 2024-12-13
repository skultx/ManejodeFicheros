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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.VCARD;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

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
		try {
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
					if ("lectura".equals(accion)) {
						leerRDF(getServletContext().getRealPath("/rdf.rdf"), request);
						request.getRequestDispatcher("/AccesoDatosA.jsp").forward(request, response);
					} else if ("escritura".equals(accion)) {
						escribirRDF(dato1, dato2, dato3, dato4, dato5, dato6,
								getServletContext().getRealPath("rdf.rdf"));
						leerRDF(getServletContext().getRealPath("/rdf.rdf"), request);
						request.getRequestDispatcher("/TratamientoFich.jsp").forward(request, response);
					}

					break;

				case "XLS":
					// Parte de Gonzalo
					String rutaAbsolutaXLS = getServletContext().getRealPath("/eventos-deportivos-diciembre-2024.xls");

					if ("lectura".equals(accion)) {
						try {
							List<String[]> datosXLS = leerXLS(rutaAbsolutaXLS);
							request.setAttribute("datosXLS", datosXLS);
							page = "AccesoDatosA.jsp";
						} catch (Exception e) {
							request.setAttribute("errorMensaje", "Error al procesar el archivo XLS: " + e.getMessage());
							page = "Error.jsp";
						}
					} else if ("escritura".equals(accion)) {
						try {
							String[] valoresFila = { dato1, dato2, dato3, dato4, dato5, dato6 };
							escribirXLS(rutaAbsolutaXLS, valoresFila);
							request.setAttribute("mensaje", "Datos escritos correctamente.");
							page = "TratamientoFich.jsp";
						} catch (Exception e) {
							request.setAttribute("errorMensaje", "Error al procesar el archivo XLS: " + e.getMessage());
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
							escrituraJSON(jsonFilePath, dato1, dato2, dato3, dato4, dato5, dato6);
							request.setAttribute("mensaje", "Datos escritos correctamente.");
							page = "TratamientoFich.jsp";
						} catch (Exception e) {
							request.setAttribute("errorMensaje",
									"Error al escribir en el archivo JSON: " + e.getMessage());
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
		} catch (Exception e) {
			// Redirigir a la página de error
			request.setAttribute("javax.servlet.error.message", e.getMessage());
			request.setAttribute("javax.servlet.error.exception", e);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
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

	public void escrituraJSON(String jsonFilePath, String descripcion, String tipo, String horario, String direccion,
			String articleId, String modalidad) throws IOException {
		File jsonFile = new File(jsonFilePath);

		JSONArray jsonArray = new JSONArray();

		// Leer datos existentes si el archivo existe
		if (jsonFile.exists()) {
			try (FileReader fileReader = new FileReader(jsonFile)) {
				char[] buffer = new char[(int) jsonFile.length()];
				fileReader.read(buffer);
				String jsonContent = new String(buffer);
				jsonArray = new JSONObject(jsonContent).getJSONArray("data");
			} catch (Exception e) {
				throw new IOException("Error al leer los datos existentes en JSON: " + e.getMessage(), e);
			}
		}

		// Crear nuevo objeto para añadir
		JSONObject newEntry = new JSONObject();
		newEntry.put("descripcion", descripcion);
		newEntry.put("tipo", tipo);
		newEntry.put("horario", horario);
		newEntry.put("direccion", direccion);
		newEntry.put("articleId", articleId);
		newEntry.put("modalidad", modalidad);

		// Agregar el nuevo objeto al array
		jsonArray.put(newEntry);

		// Reescribir el archivo
		try (FileWriter fileWriter = new FileWriter(jsonFile)) {
			JSONObject finalData = new JSONObject();
			finalData.put("data", jsonArray);
			fileWriter.write(finalData.toString(4)); // Formateo de JSON para legibilidad
		} catch (IOException e) {
			throw new IOException("Error al escribir en el archivo JSON: " + e.getMessage(), e);
		}
	}

	// PARTE DE GONZALO
	private List<String[]> leerXLS(String rutaArchivo) throws IOException {
		List<String[]> datos = new ArrayList<>();
		File archivo = new File(rutaArchivo);

		if (!archivo.exists()) {
			throw new FileNotFoundException("El archivo no existe: " + rutaArchivo);
		}

		try (FileInputStream fis = new FileInputStream(archivo); Workbook workbook = obtenerWorkbook(archivo)) {

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				List<String> fila = new ArrayList<>();
				for (org.apache.poi.ss.usermodel.Cell cell : row) {
					fila.add(obtenerCelda(cell));
				}
				datos.add(fila.toArray(new String[0]));
			}
		}
		return datos;
	}

	private String obtenerCelda(org.apache.poi.ss.usermodel.Cell cell) {
		switch (cell.getCellType()) {
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case STRING:
			return cell.getStringCellValue();
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}
	}

	private void escribirXLS(String rutaArchivo, String[] valoresFila) throws IOException {
		File archivo = new File(rutaArchivo);

		try (FileInputStream fis = archivo.exists() ? new FileInputStream(archivo) : null;
				Workbook workbook = archivo.exists() ? obtenerWorkbook(archivo) : new XSSFWorkbook()) {

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet("Datos");
			if (sheet == null) {
				sheet = workbook.createSheet("Datos");
			}

			int rowNum = sheet.getPhysicalNumberOfRows();
			Row fila = sheet.createRow(rowNum);

			for (int i = 0; i < valoresFila.length; i++) {
				org.apache.poi.ss.usermodel.Cell cell = fila.createCell(i);
				cell.setCellValue(valoresFila[i]);
			}

			try (FileOutputStream fos = new FileOutputStream(archivo)) {
				workbook.write(fos);
			}
		}
	}

	private Workbook obtenerWorkbook(File archivo) throws IOException {
		try (FileInputStream fis = new FileInputStream(archivo)) {
			if (archivo.getName().endsWith(".xls")) {
				return new HSSFWorkbook(fis);
			} else if (archivo.getName().endsWith(".xlsx")) {
				return new XSSFWorkbook(fis);
			} else {
				throw new IOException("Formato no soportado.");
			}
		}
	}

	// PARTE DE LUCAS
	public void escribirRDF(String name, String age, String email, String url, String friendName, String friendUrl,
			String rdfFilePath) {
		try {
			// Cargar o crear un modelo RDF
			Model model = ModelFactory.createDefaultModel();
			File rdfFile = new File(rdfFilePath);
			model.read(new FileInputStream(rdfFile), null);

			// Crear las descripciones
			Resource person = model.createResource(url).addProperty(VCARD.FN, name)
					.addProperty(model.createProperty("http://xmlns.com/foaf/0.1/age"), age)
					.addProperty(VCARD.EMAIL, email);

			Resource friend = model.createResource(friendUrl).addProperty(VCARD.FN, friendName);

			person.addProperty(model.createProperty("http://xmlns.com/foaf/0.1/knows"), friend);

			// Guardar los cambios en el archivo RDF
			try (FileOutputStream out = new FileOutputStream(rdfFile)) {
				model.write(out, "RDF/XML");
			}
		} catch (IOException e) {
			System.err.println("Error al leer o escribir el archivo RDF: " + e.getMessage());
		}
	}

	private void leerRDF(String rdfFilePath, HttpServletRequest request) {
		List<String> subjects = new ArrayList<>();
		List<String> predicates = new ArrayList<>();
		List<String> objects = new ArrayList<>();

		File rdfFile = new File(rdfFilePath);

		// Verificar si el archivo existe
		if (!rdfFile.exists()) {
			System.err.println("El archivo RDF no existe: " + rdfFilePath);
			request.setAttribute("rdfData", Arrays.asList(subjects, predicates, objects));
			return;
		}

		try {
			// Crear un modelo RDF
			Model model = ModelFactory.createDefaultModel();
			// Leer el archivo RDF
			model.read(new FileInputStream(rdfFile), null);

			// Iterar por todos los triples y almacenarlos en las listas
			StmtIterator iter = model.listStatements();
			while (iter.hasNext()) {
				Statement stmt = iter.nextStatement();
				String subject = stmt.getSubject().toString();
				String predicate = stmt.getPredicate().toString();
				String object = stmt.getObject().toString();

				subjects.add(subject);
				predicates.add(predicate);
				objects.add(object);
			}

		} catch (IOException e) {
			System.err.println("Error al leer el archivo RDF: " + e.getMessage());
		}

		request.setAttribute("rdfData", Arrays.asList(subjects, predicates, objects));
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