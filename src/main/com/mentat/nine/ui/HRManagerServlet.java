package main.com.mentat.nine.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.ManagerDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Manager;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * Servlet implementation class HRManagerServlet
 */
@WebServlet("/hrManager")
public class HRManagerServlet extends HttpServlet {
	
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(HRManagerServlet.class);
	
	private static final long serialVersionUID = 1L;
	private ManagerDAO mngrDao;
       
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public HRManagerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        mngrDao = daoFactory.getManagerDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response) throws PersistException, ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("enconding UTF-8 not set");
		}
		int action = checkAction(request);
		if (1 == action) {
			enter(request, response);
		} else {
			registrate(request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {

		if (request.getParameter("enter") != null) {
			return 1;
		} 
		return 0;
	}


	private void enter(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		String login = request.getParameter("login");
		Manager manager = mngrDao.getManagerByLogin(login);
		if (null != manager) { 
			String password = request.getParameter("password");
			if (manager.getPassword().equals(password)) {
				forward("hrdepartment.jsp", request, response);
			} else {
				request.setAttribute("passwordNotFound", "passwordNotFound");
				forward("error.jsp", request, response);
			}
		} else {
			request.setAttribute("managerNotFound", "managerNotFound");
			forward("error.jsp", request, response);
		}
	}
	
	
	private void registrate(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {
		 
		if (request.getParameter("registration")!= null) {
			forward("manager_registration.jsp", request, response);
		}
		
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String confirmedPassword = request.getParameter("confirmPassword");
		if (login.equals("") || password.equals("")) {
			request.setAttribute("emptyLoginFields", "emptyLoginFields");
			forward("error.jsp", request, response);
		} else if (login.length() < 6 || login.length() > 10) {
			request.setAttribute("incorrectLogin", "incorrectLogin");
			forward("error.jsp", request, response);
		} else if (password.length() < 8 || password.length() > 14) {
			request.setAttribute("incorrectPassword", "incorrectPassword");
			forward("error.jsp", request, response);
		} else if (!password.equals(confirmedPassword)) {
			request.setAttribute("notEqualsPassword", "notEqualsPassword");
			forward("error.jsp", request, response);
		}
		
		Manager manager = new Manager();
		manager.setLogin(login);
		manager.setPassword(password);
		mngrDao.createManager(manager);
		forward("manager_success_registration.jsp", request, response);
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
