package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.mentat.nine.dao.ManagerDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Manager;
import main.com.mentat.nine.ui.util.WebAttributes;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class HRManagerServlet
 */
@WebServlet("/hrManagerServlet")
public class HRManagerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ManagerDAO mngrDao;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public HRManagerServlet() throws ServletException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        try {
			mngrDao = daoFactory.getManagerDAO();
		} catch (PersistException e) {
			throw new ServletException();
		}
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
		
		request.setCharacterEncoding("UTF-8");
		
		int action = checkAction(request);
		if (1 == action) {
			enter(request, response);
		} else if (2 == action) {
			changePassword(request, response);
		} else if (3 == action) {
			deleteManager(request, response);
		} else if (4 == action) {
			confirmDeleteManager(request, response);
		} else if (5 == action) {
			forward(WebPath.MANAGER_REGISTRATE_JSP, request, response);
		} else if (6 == action) {
			registrate(request, response);
		} else {
			forward(WebPath.MANAGER_LOGIN_JSP, request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {

		if (request.getParameter("enter") != null) {
			return 1;
		} else if (request.getParameter("changePassword") != null) {
			return 2;
		} else if (request.getParameter("delete") != null) {
			return 3;
		} else if (request.getParameter("confirmDelete") != null) {
			return 4;
		} else if (request.getParameter("registration") != null) {
			return 5;
		} else if (request.getParameter("completeRegistration") != null) {
			return 6;
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
				WebAttributes.loadAttribute(request, WebAttributes.USER_NOT_FOUND);
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_LOGIN);
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		String password = request.getParameter("password");
		if (manager.getPassword().equals(password)) {
			forward(WebPath.MANAGER_MAIN_JSP, request, response);
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_LOGIN);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void registrate(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		 				
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
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_REGISTRATION);
				forward(WebPath.ERROR_JSP, request, response);	
			}
			WebAttributes.loadAttribute(request, WebAttributes.SUCCESS_MANAGER_REGISTRATION);
			forward(WebPath.MANAGER_SUCCESS_JSP, request, response);
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_REGISTRATION);
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
						WebAttributes.loadAttribute(request, WebAttributes.USER_ALREADY_EXIST_ERROR);
						condition = false;
					}
				}
			} catch (PersistException e1) {
				condition = true;
			}

		}
		
		if (login.equals("") || password.equals("")) {
			WebAttributes.loadAttribute(request, WebAttributes.EMPTY_LOGIN_FIELDS);
			condition = false;
		} 
		if (login.length() < 6 || login.length() > 10) {
			WebAttributes.loadAttribute(request, WebAttributes.LOGIN_LENGTH_ERROR);
			condition = false;
		} 
		if (login.contains(" ")) {	
			WebAttributes.loadAttribute(request, WebAttributes.LOGIN_SPACE_ERROR);
			condition = false;
		}
		if (password.contains(" ")) {	
			WebAttributes.loadAttribute(request, WebAttributes.PASSWORD_SPACE_ERROR);
			condition = false;
		}
		if (password.length() < 8 || password.length() > 14) {
			WebAttributes.loadAttribute(request, WebAttributes.PASSWORD_LENGTH_ERROR);
			condition = false;
		}
		if (!password.equals(confirmedPassword)) {
			WebAttributes.loadAttribute(request, WebAttributes.DIFFERENT_PASSWORDS_ERROR);
			condition = false;
		}
		return condition;
	}

	private void changePassword(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
	
		String login = request.getParameter("login");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String repeatePassword = request.getParameter("repeatePassword");
		
		Manager manager = null;
		try {
			manager = mngrDao.getManagerByLogin(login);
		} catch (PersistException e) {
			if (null == manager) {
				WebAttributes.loadAttribute(request, WebAttributes.USER_NOT_FOUND);
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		if (oldPassword.equals(manager.getPassword())) {
			boolean registrationConditions = isCorrectRegistrationData(login, newPassword, repeatePassword, request);
			if (registrationConditions) {
				manager.setPassword(newPassword);
				try {
					mngrDao.updateManager(manager);
				} catch (PersistException e) {
					throw new ServletException();
				}
				WebAttributes.loadAttribute(request, WebAttributes.SUCCESS_MANAGER_CHANGE_PASSWORD);
				forward(WebPath.MANAGER_SUCCESS_JSP, request, response);
			} else {
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_CHANGE_PASSWORD);
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_CHANGE_PASSWORD);
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_PASSWORD);
			forward(WebPath.ERROR_JSP, request, response);
		}
		
	}

	
	private void deleteManager(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			
		String login = request.getParameter("login");
		
		Manager manager = null;
		try {
			manager = mngrDao.getManagerByLogin(login);
		} catch (PersistException e) {
			if (null == manager) {
				WebAttributes.loadAttribute(request, WebAttributes.USER_NOT_FOUND);
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_LOGIN);
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		String password = request.getParameter("password");
		if (manager.getPassword().equals(password)) {
			request.setAttribute("manager", manager);
			forward(WebPath.MANAGER_DELETE_JSP, request, response);
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_LOGIN);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void confirmDeleteManager(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String managerLogin = request.getParameter("login");
			try {
				Manager manager = mngrDao.getManagerByLogin(managerLogin);
				mngrDao.deleteManager(manager);
				WebAttributes.loadAttribute(request, WebAttributes.SUCCESS_MANAGER_DELETE);
				forward(WebPath.MANAGER_SUCCESS_JSP, request, response);
			} catch (PersistException e) {
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_MANAGER_DELETE);
				forward(WebPath.ERROR_JSP, request, response);
			}
	}
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
