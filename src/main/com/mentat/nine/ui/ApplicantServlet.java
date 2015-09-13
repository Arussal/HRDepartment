package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.ApplicantDAO;
import main.com.mentat.nine.dao.CVFormApplicantDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Applicant;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.util.LogConfig;
import main.com.mentat.nine.ui.util.*;

/**
 * Servlet implementation class ApplicantServlet
 */
@WebServlet("/applicantServlet")
public class ApplicantServlet extends HttpServlet {
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(HRManagerServlet.class);
	
	private static final long serialVersionUID = 1L;
	private ApplicantDAO aplcntDao;
	private CVFormApplicantDAO cvAplcntDao;
       
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicantServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        aplcntDao = daoFactory.getApplicantDAO();
        cvAplcntDao = daoFactory.getCVFormApplicantDAO();
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
		request.setCharacterEncoding("UTF8");
		
		int action = checkAction(request); 
		if (1 == action) {
			enter(request, response);
		} else if (2 == action) {
			changePassword(request, response);
		} else if (3 == action) {
			goToDeletePage(request, response);
		} else if (4 == action) {
			deleteApplicant(request, response);
		} else if (5 == action) {
			goToRegistrationForm(request, response);
		} else if (6 == action) {
			registrate(request, response);
		} else {
			goToMainPage(request, response);
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
		
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				log.error("applicant with login " + login + " not found");
				request.setAttribute("userNotFound", "userNotFound");
				request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		

		String password = request.getParameter("password");
		if (applicant.getPassword().equals(password)) {
			HttpSession currentSession = request.getSession(true);
			currentSession.setAttribute("applicant", applicant);
			goToMainPage(request, response);
		} else {
			request.setAttribute("passwordNotFound", "passwordNotFound");
			request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	

	private void changePassword(HttpServletRequest request,	HttpServletResponse response) 
			throws ServletException, IOException {
		
		String login = request.getParameter("login");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String repeatePassword = request.getParameter("repeatePassword");
		
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				log.error("applicant with login " + login + " not found");
				request.setAttribute("userNotFound", "userNotFound");
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		if (oldPassword.equals(applicant.getPassword())) {
			boolean registrationConditions = isCorrectRegistrationData(login, newPassword, repeatePassword, request);
			if (registrationConditions) {
				applicant.setPassword(newPassword);
				try {
					aplcntDao.updateApplicant(applicant);
					request.setAttribute("successChangePassword", "successChangePassword");
					forward(WebPath.APPLICANT_SUCCESS_JSP, request, response);
				} catch (PersistException e) {
					request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
					forward(WebPath.ERROR_JSP, request, response);
				}
			} else {
				request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
			request.setAttribute("passwordNotFound", "passwordNotFound");
			forward(WebPath.ERROR_JSP, request, response);
		}
		
	}

	
	private void registrate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
						
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String confirmedPassword = request.getParameter("confirmPassword");
		String firstName = request.getParameter("firstName");
		String surname = request.getParameter("surname");
		String secondName = request.getParameter("secondName");
		
		boolean registrationConditions = isCorrectRegistrationData(login, password, confirmedPassword, request);
		
		if (registrationConditions) {
			Applicant applicant = new Applicant();
			applicant.setLogin(login);
			applicant.setPassword(password);
			applicant.setName(surname + " " + firstName + " " + secondName);
			try {
				applicant = aplcntDao.createApplicant(applicant);
			} catch (PersistException e) {
				request.setAttribute("notSuccessApplicantCreateOperation", "notSuccessApplicantCreateOperation");
				forward(WebPath.ERROR_JSP, request, response);	
			}
			request.setAttribute("successApplicantRegistration", "successApplicantRegistration");
			forward(WebPath.APPLICANT_SUCCESS_JSP, request, response);
		} else {
			request.setAttribute("notSuccessApplicantRegistration", "notSuccessApplicantRegistration");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}	
	
	
	private void goToMainPage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		Applicant applicant = (Applicant) session.getAttribute("applicant");
		try {
			List<CVForm> cvList = cvAplcntDao.getCVFormByName(applicant.getName());
			request.setAttribute("cvList", cvList);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		forward(WebPath.APPLICANT_MAIN_JSP, request, response);
	}
	
	
	private void goToRegistrationForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
			forward(WebPath.APPLICANT_REGISTRATE_JSP, request, response);
	}

	
	private void goToDeletePage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String login = request.getParameter("login");
		
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				log.error("applicant with login " + login + " not found");
				request.setAttribute("userNotFound", "userNotFound");
				request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		String password = request.getParameter("password");
		if (applicant.getPassword().equals(password)) {
			request.setAttribute("applicant", applicant);
			forward(WebPath.APPLICANT_DELETE_JSP, request, response);
		} else {
			request.setAttribute("passwordNotFound", "passwordNotFound");
			request.setAttribute("notSuccessApplicantLoginOperation", "notSuccessApplicantLoginOperation");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void deleteApplicant(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String applicantLogin = request.getParameter("applicantLogin");

		try {
			Applicant applicant = aplcntDao.getApplicantByLogin(applicantLogin);
			aplcntDao.deleteApplicant(applicant);
			request.setAttribute("successApplicantDeleteOperation", "successApplicantDeleteOperation");
			forward(WebPath.APPLICANT_SUCCESS_JSP, request, response);
		} catch (PersistException e) {
			request.setAttribute("notSuccessApplicantDeleteOperation", "notSuccessApplicantDeleteOperation");
			forward(WebPath.ERROR_JSP, request, response);	
		}
	}
	
	private boolean isCorrectRegistrationData(String login, String password,
			String confirmedPassword, HttpServletRequest request) {
		
		boolean condition = true;
		
		if (request.getParameter("changePassword") == null) {
			List<Applicant> applicants = null;
			try {
				applicants = aplcntDao.getAllApplicants();
			} catch (PersistException e1) {
				
			}

			for (Applicant searchedApplicant : applicants) {
				if (searchedApplicant.getLogin().equalsIgnoreCase(login)) {
					request.setAttribute("existUserError", "existUserError");
					condition = false;
				}
			}
			
			if (request.getParameter("firstName").equals("")) {
				request.setAttribute("emptyNameFields", "emptyNameFields");
				condition = false;
			}
			if (request.getParameter("surname").equals("")) {
				request.setAttribute("emptyNameFields", "emptyNameFields");
				condition = false;
			}
			if (request.getParameter("secondName").equals("")) {
				request.setAttribute("emptyNameFields", "emptyNameFields");
				condition = false;
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
	

	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
