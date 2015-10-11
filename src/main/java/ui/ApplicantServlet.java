package ui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import dao.ApplicantDAO;
import dao.CVFormApplicantDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.Applicant;
import domain.CVFormApplicant;
import ui.util.*;

/**
 * Servlet implementation class ApplicantServlet
 */

@Controller
public class ApplicantServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private DAOFactory daoFactory;
	private ApplicantDAO aplcntDao;
	private CVFormApplicantDAO cvAplcntDao;
       
	
    public ApplicantServlet() {
        super();
        daoFactory = DAOFactory.getFactory();
    }

    @RequestMapping("/applicantServlet")
	private void performTask(@RequestParam Map<String, String> params, Model model, HttpSession session) {
		
        Properties properties = (Properties) session.getAttribute("properties");
        daoFactory.setLogPath(properties);
		aplcntDao = daoFactory.getApplicantDAO();
		cvAplcntDao = daoFactory.getCVFormApplicantDAO();
		
        try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
        
		//request.setCharacterEncoding("UTF8");
		
		int action = checkAction(params); 
		if (1 == action) {
			enter(params.get("login"), params.get("login"), model, session);
		} else if (2 == action) {
			changePassword(params, model);
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

		
	private int checkAction(Map<String, String> params) {
		
		if (params.get("enter") != null) {
			return 1;
		} else if (params.get("changePassword") != null) {
			return 2;
		} else if (params.get("delete") != null) {
			return 3;
		} else if (params.get("confirmDelete") != null) {
			return 4;
		} else if (params.get("registration") != null) {
			return 5;	
		} else if (params.get("completeRegistration") != null) {
			return 6;
		}
		return 0;
	}


	private String enter(String login, String password, Model model, HttpSession session) {
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				WebAttributes.loadAttribute(model, WebAttributes.USER_NOT_FOUND);
				WebAttributes.loadAttribute(model, WebAttributes.INVALID_APPLICANT_LOGIN);
				return "redirect:/" + WebPath.ERROR_JSP;
			}
		}
		if (applicant != null) {
			if (applicant.getPassword().equals(password)) {
				//session.setAttribute("applicant", applicant);
				return "redirect:/" + WebPath.ERROR_JSP;
			} else {
				WebAttributes.loadAttribute(model, WebAttributes.WRONG_PASSWORD);
				WebAttributes.loadAttribute(model, WebAttributes.INVALID_APPLICANT_LOGIN);
				return "redirect:/" + WebPath.APPLICANT_MAIN_JSP;
			}
		}
		return "redirect:/";
	}
	

	private String changePassword(Map <String, String> params, Model model) {
		
		String login = params.get("login");
		String oldPassword = params.get("oldPassword");
		String newPassword = params.get("newPassword");
		String repeatePassword = params.get("repeatePassword");
		
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				WebAttributes.loadAttribute(model, WebAttributes.USER_NOT_FOUND);
				return "redirect:/" + WebPath.ERROR_JSP;
			}
		}
		
		if (oldPassword.equals(applicant.getPassword())) {
			boolean registrationConditions = isCorrectRegistrationData(login, newPassword, 
					repeatePassword, params);
			if (registrationConditions) {
				applicant.setPassword(newPassword);
				try {
					aplcntDao.updateApplicant(applicant);
				} catch (PersistException e) {
					WebAttributes.loadAttribute(model, WebAttributes.INVALID_CHANGE_PASSWORD);
					return "redirect:/" + WebPath.ERROR_JSP;
				}
				WebAttributes.loadAttribute(model, WebAttributes.SUCCESS_CHANGE_PASSWORD);
				return "redirect:/" + WebPath.APPLICANT_SUCCESS_JSP;
			} else {
				WebAttributes.loadAttribute(model, WebAttributes.INVALID_CHANGE_PASSWORD);
				return "redirect:/" + WebPath.ERROR_JSP;
			}
		} else {
			WebAttributes.loadAttribute(model, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(model, WebAttributes.INVALID_APPLICANT_LOGIN);
			return "redirect:/" + WebPath.ERROR_JSP;
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
		
		boolean registrationConditions = isCorrectRegistrationData(login, password, confirmedPassword, params);
		
		if (registrationConditions) {
			Applicant applicant = new Applicant();
			applicant.setLogin(login);
			applicant.setPassword(password);
			applicant.setName(surname + " " + firstName + " " + secondName);
			try {
				applicant = aplcntDao.createApplicant(applicant);
			} catch (PersistException e) {
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_REGISTRATION);
				forward(WebPath.ERROR_JSP, request, response);	
			}
			WebAttributes.loadAttribute(request, WebAttributes.SUCCESS_REGISTRATION);		
			forward(WebPath.APPLICANT_SUCCESS_JSP, request, response);
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_REGISTRATION);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}	
	
	
	@RequestMapping(WebPath.APPLICANT_MAIN_JSP)
	private String goToMainPage(HttpSession session, Model model) 
			throws ServletException, IOException {
		
		Applicant applicant = (Applicant) session.getAttribute("applicant");
		if (applicant != null) {
				List<CVFormApplicant> cvList = cvAplcntDao.getCVFormByName(applicant.getName());
				model.addAttribute("cvList", cvList);

			return "redirect:/" + WebPath.APPLICANT_MAIN_JSP;
		} else {
			return "redirect:/" + WebPath.APPLICANT_LOGIN_JSP;
		}
	}
	
	
	private String goToRegistrationForm() {
			return "redirect:/" + WebPath.APPLICANT_REGISTRATE_JSP;
	}

	
	private void goToDeletePage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String login = request.getParameter("login");
		
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				WebAttributes.loadAttribute(request, WebAttributes.USER_NOT_FOUND);
				WebAttributes.loadAttribute(request, WebAttributes.INVALID_APPLICANT_LOGIN);
				forward(WebPath.ERROR_JSP, request, response);
			}
		}
		
		String password = request.getParameter("password");
		if (applicant.getPassword().equals(password)) {
			request.setAttribute("applicant", applicant);
			forward(WebPath.APPLICANT_DELETE_JSP, request, response);
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_APPLICANT_LOGIN);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void deleteApplicant(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String applicantLogin = request.getParameter("applicantLogin");

		try {
			Applicant applicant = aplcntDao.getApplicantByLogin(applicantLogin);
			aplcntDao.deleteApplicant(applicant);
			WebAttributes.loadAttribute(request, WebAttributes.SUCCESS_DELETE);
			forward(WebPath.APPLICANT_SUCCESS_JSP, request, response);
		} catch (PersistException e) {
			WebAttributes.loadAttribute(request, WebAttributes.INVALID_DELETE);
			forward(WebPath.ERROR_JSP, request, response);	
		}
	}
	
	private boolean isCorrectRegistrationData(String login, String password,
			String confirmedPassword, Map <String, String> params) {
		
		boolean condition = true;
		
		if (params.get("changePassword") == null) {
			List<Applicant> applicants = null;
			try {
				applicants = aplcntDao.getAllApplicants();
			} catch (PersistException e1) {
				
			}

			for (Applicant searchedApplicant : applicants) {
				if (searchedApplicant.getLogin().equalsIgnoreCase(login)) {
					WebAttributes.loadAttribute(request, WebAttributes.USER_ALREADY_EXIST_ERROR);
					condition = false;
				}
			}
			
			if (request.getParameter("firstName").equals("")) {
				WebAttributes.loadAttribute(request, WebAttributes.EMPTY_NAME_FIELDS);
				condition = false;
			}
			if (request.getParameter("surname").equals("")) {
				WebAttributes.loadAttribute(request, WebAttributes.EMPTY_NAME_FIELDS);
				condition = false;
			}
			if (request.getParameter("secondName").equals("")) {
				WebAttributes.loadAttribute(request, WebAttributes.EMPTY_NAME_FIELDS);
				condition = false;
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
	

	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (IOException e) {
			throw new ServletException();
		}
	}
}
