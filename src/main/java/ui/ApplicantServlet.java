package ui;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	private ModelAndView performTask(@RequestParam Map<String, String> params, Model model, HttpSession session) 
			throws ServletException {
		
        Properties properties = (Properties) session.getAttribute("properties");
        daoFactory.setLogPath(properties);
		aplcntDao = daoFactory.getApplicantDAO();
		cvAplcntDao = daoFactory.getCVFormApplicantDAO();
		
        try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
        
        ModelAndView modelView = null;
		//request.setCharacterEncoding("UTF8");
		
		int action = checkAction(params); 
		if (1 == action) {
			Applicant applicant = enter(params, model);
			if (applicant != null) {
				//goToMainPage(params, model, session);
				modelView = new ModelAndView(WebPath.getApplicantMainPage());
			} else {
				modelView = new ModelAndView("error");
			}
		} else if (2 == action) {
			changePassword(params, model);
		} else if (3 == action) {
			goToDeletePage(params.get("login"), params.get("password"), model);
		} else if (4 == action) {
			deleteApplicant(params.get("applicantLogin"), model);
		} else if (5 == action) {
			goToRegistrationForm();
		} else if (6 == action) {
			registrate(params, model);
		} else {
		
		}
		
		return modelView;
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


	private Applicant enter(Map<String, String> params, Model model) {
		
		Applicant applicant = null;
		String login = params.get("login");
		String password = params.get("password");
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				WebAttributes.loadAttribute(model, WebAttributes.USER_NOT_FOUND);
				WebAttributes.loadAttribute(model, WebAttributes.INVALID_APPLICANT_LOGIN);
				return null;
			}
		}
		if (applicant != null) {
			if (applicant.getPassword().equals(password)) {
				return applicant;
			} else {
				WebAttributes.loadAttribute(model, WebAttributes.WRONG_PASSWORD);
				WebAttributes.loadAttribute(model, WebAttributes.INVALID_APPLICANT_LOGIN);
				return null;
			}
		}
		return applicant;
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
					repeatePassword, params, model);
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

	
	private String registrate(Map <String, String> params, Model model) {
						
		String login = params.get("login");
		String password = params.get("password");
		String confirmedPassword = params.get("confirmPassword");
		String firstName = params.get("firstName");
		String surname = params.get("surname");
		String secondName = params.get("secondName");
		
		boolean registrationConditions = isCorrectRegistrationData(login, password, 
				confirmedPassword, params, model);
		
		if (registrationConditions) {
			Applicant applicant = new Applicant();
			applicant.setLogin(login);
			applicant.setPassword(password);
			applicant.setName(surname + " " + firstName + " " + secondName);
			try {
				applicant = aplcntDao.createApplicant(applicant);
			} catch (PersistException e) {
				WebAttributes.loadAttribute(model, WebAttributes.INVALID_REGISTRATION);
				return "redirect:/" + WebPath.ERROR_JSP;	
			}
			WebAttributes.loadAttribute(model, WebAttributes.SUCCESS_REGISTRATION);		
			return "redirect:/" + WebPath.APPLICANT_SUCCESS_JSP;
		} else {
			WebAttributes.loadAttribute(model, WebAttributes.INVALID_REGISTRATION);
			return "redirect:/" + WebPath.ERROR_JSP;
		}
	}	
	
	
	@RequestMapping("/applicant")
	private ModelAndView goToMainPage(@RequestParam Map<String, String> params, Model model, HttpSession session) {
		
		Applicant applicant = (Applicant) session.getAttribute("applicant");
		ModelAndView modelView = null;
		if (applicant != null) {
			List<CVFormApplicant> cvList = cvAplcntDao.getCVFormByName(applicant.getName());
			model.addAttribute("cvList", cvList);
			modelView = new ModelAndView(WebPath.getApplicantMainPage());
		} else {
			modelView = new ModelAndView(WebPath.getApplicantLoginPage());
		}
		return modelView;
	}
	
	
	private String goToRegistrationForm() {
			return "redirect:/" + WebPath.APPLICANT_REGISTRATE_JSP;
	}

	
	private String goToDeletePage(String login, String password, Model model) {
		
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
		
		if (applicant.getPassword().equals(password)) {
			model.addAttribute("applicant", applicant);
			return "redirect:/" + WebPath.APPLICANT_DELETE_JSP;
		} else {
			WebAttributes.loadAttribute(model, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(model, WebAttributes.INVALID_APPLICANT_LOGIN);
			return "redirect:/" + WebPath.ERROR_JSP;
		}
	}
	
	
	private String deleteApplicant(String applicantLogin, Model model) {
		
		try {
			Applicant applicant = aplcntDao.getApplicantByLogin(applicantLogin);
			aplcntDao.deleteApplicant(applicant);
			WebAttributes.loadAttribute(model, WebAttributes.SUCCESS_DELETE);
			return "redirect:/" + WebPath.APPLICANT_SUCCESS_JSP;
		} catch (PersistException e) {
			WebAttributes.loadAttribute(model, WebAttributes.INVALID_DELETE);
			return "redirect:/" + WebPath.ERROR_JSP;	
		}
	}
	
	private boolean isCorrectRegistrationData(String login, String password,
			String confirmedPassword, Map <String, String> params, Model model) {
		
		boolean condition = true;
		
		if (params.get("changePassword") == null) {
			List<Applicant> applicants = null;
			try {
				applicants = aplcntDao.getAllApplicants();
			} catch (PersistException e1) {
				
			}

			for (Applicant searchedApplicant : applicants) {
				if (searchedApplicant.getLogin().equalsIgnoreCase(login)) {
					WebAttributes.loadAttribute(model, WebAttributes.USER_ALREADY_EXIST_ERROR);
					condition = false;
				}
			}
			
			if (params.get("firstName").equals("")) {
				WebAttributes.loadAttribute(model, WebAttributes.EMPTY_NAME_FIELDS);
				condition = false;
			}
			if (params.get("surname").equals("")) {
				WebAttributes.loadAttribute(model, WebAttributes.EMPTY_NAME_FIELDS);
				condition = false;
			}
			if (params.get("secondName").equals("")) {
				WebAttributes.loadAttribute(model, WebAttributes.EMPTY_NAME_FIELDS);
				condition = false;
			}
				
		}
		
		if (login.equals("") || password.equals("")) {
			WebAttributes.loadAttribute(model, WebAttributes.EMPTY_LOGIN_FIELDS);
			condition = false;
		} 
		if (login.length() < 6 || login.length() > 10) {
			WebAttributes.loadAttribute(model, WebAttributes.LOGIN_LENGTH_ERROR);
			condition = false;
		} 
		if (login.contains(" ")) {	
			WebAttributes.loadAttribute(model, WebAttributes.LOGIN_SPACE_ERROR);
			condition = false;
		}
		if (password.contains(" ")) {	
			WebAttributes.loadAttribute(model, WebAttributes.PASSWORD_SPACE_ERROR);
			condition = false;
		}
		if (password.length() < 8 || password.length() > 14) {
			WebAttributes.loadAttribute(model, WebAttributes.PASSWORD_LENGTH_ERROR);
			condition = false;
		}
		if (!password.equals(confirmedPassword)) {
			WebAttributes.loadAttribute(model, WebAttributes.DIFFERENT_PASSWORDS_ERROR);
			condition = false;
		}
		return condition;
	}
}
