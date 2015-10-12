package ui;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ui.util.WebPath;

/**
 * Servlet implementation class MainServlet
 */
@Controller

public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @RequestMapping("/")
	private ModelAndView performTask(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException {
		HttpSession session = request.getSession(true);
		WebPath.loadPathValues(session);
		
		String dbPathProperty = request.getServletContext().getInitParameter("dbproperties");
		String logPath = request.getServletContext().getInitParameter("log4jproperties");

		Properties properties = new Properties();
		try {
			InputStream dbStream = request.getServletContext().getResourceAsStream(dbPathProperty);
			InputStream logStream = request.getServletContext().getResourceAsStream(logPath);
			try {
				
				properties.load(dbStream);
				properties.load(logStream);
			} finally {
				dbStream.close();
				logStream.close();
			}
		} catch (IOException e) { 	 	
			throw new ServletException();
		}
	      
		session.setAttribute("properties", properties);
		
		ModelAndView model = new ModelAndView(WebPath.getMainPage());
		
		return model;
	}
	
}
