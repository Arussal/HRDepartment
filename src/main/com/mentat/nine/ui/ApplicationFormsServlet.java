package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;

/**
 * Servlet implementation class ApplicationFormsServlet
 */
@WebServlet("/applicationsServlet")
public class ApplicationFormsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private List<ApplicationForm> apps;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationFormsServlet() {
        super();
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
		try {
			apps = getApps();
		} catch (PersistException e) {
			e.printStackTrace();
		}
		request.setAttribute("apps", apps);
		request.getRequestDispatcher("applications.jsp").forward(request, response);
	}
	
	private List<ApplicationForm> getApps() throws PersistException{
		DAOFactory factory = DAOFactory.getFactory();
		ApplicationFormDAO appDao = factory.getApplicationFormDAO();
		List<ApplicationForm> applications = appDao.getAllApplicationForms();
		return applications;
	}
}
