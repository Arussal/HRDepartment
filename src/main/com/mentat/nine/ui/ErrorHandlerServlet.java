package main.com.mentat.nine.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class ErrorHandlerServlet
 */
@WebServlet("/ErrorHandlerServlet")
public class ErrorHandlerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }
    private void processError(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        //customize error message
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }    
        
        String errorMessage = "";
        if (statusCode != 500) {
        	errorMessage = "Error details: <br/> Requested Uri: " + 
        requestUri + "<br /> Status Code: " + statusCode;
        } else {
        	errorMessage = "Servlet " + servletName + " has thrown an exception " +
        		throwable.getClass().getName() + ": " + throwable.getMessage(); 
        }
        request.setAttribute("error",  errorMessage);    
        request.getRequestDispatcher(WebPath.ERROR_JSP).forward(request, response);
    }
}
		
