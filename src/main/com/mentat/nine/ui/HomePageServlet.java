package main.com.mentat.nine.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/")
public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomePageServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);
	}

	private void performTask(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		WebPath.loadPathValues(session);
		
		String dbPathProperty = getServletContext().getInitParameter("dbproperties");
		System.out.println(dbPathProperty);		//////
		String logPath = getServletContext().getInitParameter("log4jproperties");
		System.out.println(logPath);		//////
		Properties properties = new Properties();
		try {
			InputStream dbStream = getServletContext().getResourceAsStream(dbPathProperty);
			try {
				properties.load(dbStream);
			} finally {
				dbStream.close();
			}
		} catch (IOException e) {
			throw new ServletException();
		}
		session.setAttribute("properties", properties);
		session.setAttribute("logPath", logPath);
		request.getRequestDispatcher(WebPath.HOME_PAGE_JSP).forward(request, response);
	}
	
}
