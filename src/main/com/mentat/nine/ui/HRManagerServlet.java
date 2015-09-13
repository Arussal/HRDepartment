package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.List;

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
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class HRManagerServlet
 */
@WebServlet("/hrManagerServlet")
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		int action = checkAction(request);
		if (1 == action) {
			enter(request, response);
		} else if (2 == action) {
			changePassword(request, response);
		} else if (3 == action) {
			deleteManager(request, response);
		} else {
			registrate(request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {

		if (request.getParameter("enter") != null) {
			return 1;
		} else if (request.getParameter("changePassword") != null) {
			return 2;
		}
		return 0;
	}


	private void enter(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String login = request.getParameter("login");
		
		Manager manager = null;
		try {
			manager = mngrDao.getManagerByLogin(login);
		} catch (PersistException e) {
			if (null == manager) {
				log.error("manager with login " + login + " not found");
				request.setAttribute("userNotFound", "userNotFound");
				request.setAttribute("notSuccessManagerLoginOperation", "notSuccessManagerLoginOperation");
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		String password = request.getParameter("password");
		if (manager.getPassword().equals(password)) {
			forward(WebPath.MANAGER_MAIN_JSP, request, response);
		} else {
			request.setAttribute("passwordNotFound", "passwordNotFound");
			request.setAttribute("notSuccessManagerLoginOperation", "notSuccessManagerLoginOperation");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void registrate(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		 
		if (request.getParameter("registration")!= null) {
			forward(WebPath.MANAGER_REGISTRATE_JSP, request, response);
		}
				
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String confirmedPassword = request.getParameter("confirmPassword");
		
		boolean registrationConditions = isCorrectRegistrationData(login, password, confirmedPassword, request);
		
		if (registrationConditions) {
			Manager manager = new Manager();
			manager.setLogin(login);
			manager.setPassword(password);
			try {
				mngrDao.createManager(manager);
			} catch (PersistException e) {
				request.setAttribute("notSuccessManagerCreateOperation", "notSuccessManagerCreateOperation");
				request.setAttribute("notSuccessManagerRegistration", "notSuccessManagerRegistration");
				forward(WebPath.ERROR_JSP, request, response);	
			}
			request.setAttribute("successManagerRegistration", "successManagerRegistration");
			forward(WebPath.MANAGER_SUCCESS_JSP, request, response);
		} else {
			request.setAttribute("notSuccessManagerRegistration", "notSuccessManagerRegistration");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}


	private boolean isCorrectRegistrationData(String login, String password,
			String confirmedPassword, HttpServletRequest request) {
		
		boolean condition = true;
		
		if (request.getParameter("changePassword") == null) {
			List<Manager> managers = null;
			try {
				managers = mngrDao.getAllManagers();
				for (Manager searchedManager : managers) {
					if (searchedManager.getLogin().equalsIgnoreCase(login)) {
						request.setAttribute("existUserError", "existUserError");
						condition = false;
					}
				}
			} catch (PersistException e1) {
				condition = true;
			}

		}
		
		if (login.equals("") || password.equals("")) {
			request.setAttribute("emptyLoginFields", "emptyLoginFields");
			condition = false;
		} 
		if (login.length() < 6 || login.length() > 10) {
			request.setAttribute("incorrectLogin", "incorrectLogin");
			condition = false;
		} 
		if (password.contains(" ")) {	
			request.setAttribute("passwordSpaceError", "passwordSpaceError");
			condition = false;
		}
		if (login.contains(" ")) {	
			request.setAttribute("loginSpaceError", "loginSpaceError");
			condition = false;
		}
		if (password.length() < 8 || password.length() > 14) {
			request.setAttribute("incorrectPassword", "incorrectPassword");
			condition = false;
		}
		if (!password.equals(confirmedPassword)) {
			request.setAttribute("notEqualsPassword", "notEqualsPassword");
			condition = false;
		}
		return condition;
	}

	private void changePassword(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {
	
		String login = request.getParameter("login");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String repeatePassword = request.getParameter("repeatePassword");
		
		Manager manager = null;
		try {
			manager = mngrDao.getManagerByLogin(login);
		} catch (PersistException e) {
			if (null == manager) {
				log.error("manager with login " + login + " not found");
				request.setAttribute("managerNotFound", "managerNotFound");
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		if (oldPassword.equals(manager.getPassword())) {
			boolean registrationConditions = isCorrectRegistrationData(login, newPassword, repeatePassword, request);
			if (registrationConditions) {
				manager.setPassword(newPassword);
				mngrDao.updateManager(manager);
				request.setAttribute("successChangePassword", "successChangePassword");
				forward(WebPath.MANAGER_SUCCESS_JSP, request, response);
			} else {
				request.setAttribute("notSuccessManagerOperation", "notSuccessManagerOperation");
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			request.setAttribute("notSuccessManagerOperation", "notSuccessManagerOperation");
			request.setAttribute("passwordNotFound", "passwordNotFound");
			forward(WebPath.ERROR_JSP, request, response);
		}
		
	}

	
	private void deleteManager(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			
	if (request.getParameter("delete") != null) {
		String login = request.getParameter("login");
		
		Manager manager = null;
		try {
			manager = mngrDao.getManagerByLogin(login);
		} catch (PersistException e) {
			if (null == manager) {
				log.error("manager with login " + login + " not found");
				request.setAttribute("userNotFound", "userNotFound");
				request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		String password = request.getParameter("password");
		if (manager.getPassword().equals(password)) {
			request.setAttribute("manager", manager);
			forward(WebPath.MANAGER_DELETE_JSP, request, response);
		} else {
			request.setAttribute("passwordNotFound", "passwordNotFound");
			request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	Manager manager = (Manager)request.getAttribute("manager");
	try {
		mngrDao.deleteManager(manager);
		request.setAttribute("successManagerDeleteOperation", "successManagerDeleteOperation");
		forward(WebPath.MANAGER_SUCCESS_JSP, request, response);
	} catch (PersistException e) {
		request.setAttribute("notSuccessManagerDeleteOperation", "notSuccessManagerDeleteOperation");
		forward(WebPath.ERROR_JSP, request, response);
	}
	
}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
