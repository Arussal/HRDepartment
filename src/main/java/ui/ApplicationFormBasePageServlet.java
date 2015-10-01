package ui;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ApplicationFormDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.ApplicationForm;
import ui.util.WebPath;

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
        daoFactory.setLogPath(properties);
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
