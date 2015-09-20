package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class ApplicationFormsServlet
 */
@WebServlet("/applicationBaseServlet")
public class ApplicationFormBasePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ApplicationFormDAO appDao;
	private List<ApplicationForm> apps;

	private DAOFactory daoFactory;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationFormBasePageServlet() {
        super();
        daoFactory = DAOFactory.getFactory();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		performTask(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		performTask(request, response);
	}

	private void performTask(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        Properties properties = (Properties) session.getAttribute("properties");
        String logPath = (String) session.getAttribute("logPath");
        daoFactory.setLogPath(logPath);
        appDao = daoFactory.getApplicationFormDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		try {
			apps = getApps();
		} catch (PersistException e) {
			e.printStackTrace();
		}
		request.setAttribute("apps", apps);
		request.getRequestDispatcher(WebPath.HR_APPLICAION_FORMS_JSP).forward(request, response);
	}
	
	private List<ApplicationForm> getApps() throws PersistException{
		return appDao.getAllApplicationForms();
	}
}
