package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

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
		
		HttpSession sesion;
		String respuesta = "";
		String page = "";
		
		String dato1 = request.getParameter("dato1");
		String dato2 = request.getParameter("dato2");
		String dato3 = request.getParameter("dato3");
		String dato4 = request.getParameter("dato4");
		String dato5 = request.getParameter("dato5");
		String dato6 = request.getParameter("dato6");


		
		switch (request.getParameter("boton")){
		case "enviar": {
			
			if (dato1.isBlank() || dato2.isBlank() || dato3.isBlank() || dato4.isBlank() || dato5.isBlank() || dato6.isBlank()) {
	
				
				respuesta = "(*) El nombre y el codigo pin son obligatorios";
				page = "TratamientoFich.jsp";
			} else {
				sesion = request.getSession(true);
				page = "AccesoDatosA.jsp";
			}
			
			break;
		}
		}
		
		
		request.setAttribute("errorMensaje", respuesta);
		RequestDispatcher dispatcher = request.getRequestDispatcher("TratamientoFich.jsp");
		dispatcher.forward(request, response);

		
	}

}
