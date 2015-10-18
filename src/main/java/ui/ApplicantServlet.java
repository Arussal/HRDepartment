package ui;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
    public ApplicantServlet() throws ServletException {
        super();
        daoFactory = DAOFactory.getFactory(); 
    }

    
    public void loadProperties(Properties properties) throws ServletException {
    	 daoFactory.setLogPath(properties);
         try {
 			DAOFactory.loadConnectProperties(properties);
 		} catch (NoSuitableDBPropertiesException e) {
 			throw new ServletException();
 		}
    }
    
    
    @ModelAttribute
    private void addFooter(Model model) {
    	String footer = getFooterMessage();
    	model.addAttribute("footer", footer);
    }
    
    
    @RequestMapping("applicantServlet")
	private ModelAndView performTask(@RequestParam Map<String, String> params, HttpSession session) 
			throws ServletException {
    	
		Properties properties = (Properties) session.getAttribute("properties");
		loadProperties(properties);
		aplcntDao = daoFactory.getApplicantDAO();
		cvAplcntDao = daoFactory.getCVFormApplicantDAO();
		return new ModelAndView(WebPath.getApplicantLoginPage());
		//request.setCharacterEncoding("UTF8");
	}	


	
    @RequestMapping("applicant/login.html")
    private ModelAndView login(){
    	return new ModelAndView(WebPath.getApplicantLoginPage());
    }


	@RequestMapping("applicant/main.html")
	private ModelAndView enter(@ModelAttribute("applicant") Applicant applicant, HttpSession session)
			throws ServletException {
		
		ModelAndView modelView = null;

		Applicant foundedApplicant = (Applicant) session.getAttribute("applicant");
		if (foundedApplicant != null) {
			List<CVFormApplicant> cvList = cvAplcntDao.getCVFormByName(foundedApplicant.getName());
			modelView = new ModelAndView(WebPath.getApplicantMainPage());
			modelView.addObject("cvList", cvList);
			return modelView;
		}
		
		if (applicant.getLogin() != null) {
			try {
				foundedApplicant = aplcntDao.getApplicantByLogin(applicant.getLogin());
			} catch (PersistException e) {
				if (null == foundedApplicant) {
					modelView = new ModelAndView(WebPath.getErrorPage());
					WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
					WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
				}
			}
			if (foundedApplicant.getPassword().equals(applicant.getPassword())) {
				session.setAttribute("applicant", foundedApplicant);
				List<CVFormApplicant> cvList = cvAplcntDao.getCVFormByName(foundedApplicant.getName());
				modelView = new ModelAndView(WebPath.getApplicantMainPage());
				modelView.addObject("cvList", cvList);
			} else {
				modelView = new ModelAndView(WebPath.getErrorPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.WRONG_PASSWORD);
				WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
			}
		} else {
			modelView = new ModelAndView();
			modelView.clear();
			modelView.setViewName("applicant/login");
		}
		
		return modelView;
	}
	
	
	@RequestMapping("applicant/change-password.html")
	private ModelAndView goToChangePasswordPage() {
		return new ModelAndView(WebPath.getApplicantChangePasswordPage());
	}
	
	
	@RequestMapping("applicant/complete-change-password.html")
	private ModelAndView changePassword(@RequestParam Map <String, String> params) 
			throws ServletException {
		
		ModelAndView modelView = new ModelAndView();
		
		String login = params.get("login");
		String oldPassword = params.get("oldPassword");
		String newPassword = params.get("newPassword");
		String repeatePassword = params.get("repeatePassword");
		
		Applicant applicant = null;
		try {
			applicant = aplcntDao.getApplicantByLogin(login);
		} catch (PersistException e) {
			if (null == applicant) {
				modelView = new ModelAndView(WebPath.getErrorPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
				return modelView;
			}
		}
		
		if (oldPassword.equals(applicant.getPassword())) {
			boolean registrationConditions = isCorrectChangePasswordData(newPassword, repeatePassword, modelView);
			
			if (registrationConditions) {
				applicant.setPassword(newPassword);
				try {
					aplcntDao.updateApplicant(applicant);
				} catch (PersistException e) {
					modelView.setViewName(WebPath.getErrorPage());
					WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_CHANGE_PASSWORD);
				}
				modelView.setViewName(WebPath.getApplicantCompleteChangePasswordPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.SUCCESS_CHANGE_PASSWORD);
			} else {
				modelView.setViewName(WebPath.getErrorPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_CHANGE_PASSWORD);
			}
		} else {
			modelView.setViewName(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
		}
		return modelView;
	}

	
    @RequestMapping("applicant/registrate.html")
    private ModelAndView getRegistrationPage() {
    	return new ModelAndView(WebPath.getApplicantRegistratePage());
	}
    
	
    @RequestMapping(value="applicant/complete-registration.html", method=RequestMethod.POST)
	private ModelAndView registrateApplicant(@ModelAttribute("applicant") Applicant applicant, 
			@RequestParam("confirmPassword") String confirmPassword) {
						
		ModelAndView modelView = new ModelAndView();
		
		boolean registrationConditions = isCorrectRegistrationData(applicant.getLogin(), 
				applicant.getPassword(), applicant.getSurname(), applicant.getName(), 
				applicant.getLastName(), confirmPassword, modelView);
		
		if (registrationConditions) {
			applicant = aplcntDao.createApplicant(applicant);
			modelView.setViewName(WebPath.getApplicantCompleteRegistrationPage());
		} else {
			modelView.setViewName(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_REGISTRATION);
		}
		return modelView;
	}	
	
	
	@RequestMapping("applicant/delete.html")
	private ModelAndView getDeletePage() {
		return new ModelAndView(WebPath.getApplicantDeletePage());
	}
	
	
	@RequestMapping("applicant/confirm-delete.html")
	private ModelAndView deleteApplicant(@ModelAttribute("applicant") Applicant applicant, 
			HttpSession session) {
		
		Applicant foundedApplicant = null;
		ModelAndView modelView = null;
		if (applicant.getLogin() != null) {
			try {
				foundedApplicant = aplcntDao.getApplicantByLogin(applicant.getLogin());
			} catch (PersistException e) {
				if (null == foundedApplicant) {
					modelView = new ModelAndView(WebPath.getErrorPage());
					WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
					WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
				}
			}
			if (foundedApplicant.getPassword().equals(applicant.getPassword())) {
				session.setAttribute("applicantToDelete", foundedApplicant);
				modelView = new ModelAndView(WebPath.getApplicantConfirmDeletePage());
			} else {
				modelView = new ModelAndView(WebPath.getErrorPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.WRONG_PASSWORD);
				WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
			}
		}
		return modelView;
	}
	
	
	@RequestMapping("applicant/complete-delete.html")
	private ModelAndView confirmDeleteApplicant(@RequestParam Map<String, String> params, 
			HttpSession session) {
		
		Applicant applicant = (Applicant) session.getAttribute("applicantToDelete");
		if (applicant != null) {
			if (params.get("apply") != null) {
				aplcntDao.deleteApplicant(applicant);
				return new ModelAndView(WebPath.getApplicantCompleteDeletePage());
			} else {
				return new ModelAndView(WebPath.getApplicantMainPage());
			}
		} else {
			return new ModelAndView(WebPath.getApplicantMainPage());
		}
	}
	
	
	private boolean isCorrectChangePasswordData(String newPassword, String confirmPassword, 
			ModelAndView modelView) {
		
		if (newPassword.equals("")) {
			WebAttributes.loadAttribute(modelView, WebAttributes.EMPTY_LOGIN_FIELDS);
			return false;
		} 
		if (newPassword.length() < 8 || newPassword.length() > 14) {
			WebAttributes.loadAttribute(modelView, WebAttributes.LOGIN_LENGTH_ERROR);
			return false;
		} 
		if (!newPassword.equals(confirmPassword)) {
			return false;
		}
		return true;
	}
	
	
	private boolean isCorrectRegistrationData(String login, String password, String surname, 
			String name, String lastName, String confirmedPassword, ModelAndView modelView) {
		
		boolean condition = true;
		
		List<Applicant> applicants = null;

		applicants = aplcntDao.getAllApplicants();


		for (Applicant searchedApplicant : applicants) {
			if (searchedApplicant.getLogin().equalsIgnoreCase(login)) {
				WebAttributes.loadAttribute(modelView, WebAttributes.USER_ALREADY_EXIST_ERROR);
				condition = false;
			}
		}
		
		if (surname.equals("")) {
			WebAttributes.loadAttribute(modelView, WebAttributes.EMPTY_NAME_FIELDS);
			condition = false;
		}
		if (name.equals("")) {
			WebAttributes.loadAttribute(modelView, WebAttributes.EMPTY_NAME_FIELDS);
			condition = false;
		}
		if (lastName.equals("")) {
			WebAttributes.loadAttribute(modelView, WebAttributes.EMPTY_NAME_FIELDS);
			condition = false;
		}
		
		if (login.equals("") || password.equals("")) {
			WebAttributes.loadAttribute(modelView, WebAttributes.EMPTY_LOGIN_FIELDS);
			condition = false;
		} 
		if (login.length() < 6 || login.length() > 10) {
			WebAttributes.loadAttribute(modelView, WebAttributes.LOGIN_LENGTH_ERROR);
			condition = false;
		} 
		if (login.contains(" ")) {	
			WebAttributes.loadAttribute(modelView, WebAttributes.LOGIN_SPACE_ERROR);
			condition = false;
		}
		if (password.contains(" ")) {	
			WebAttributes.loadAttribute(modelView, WebAttributes.PASSWORD_SPACE_ERROR);
			condition = false;
		}
		if (password.length() < 8 || password.length() > 14) {
			WebAttributes.loadAttribute(modelView, WebAttributes.PASSWORD_LENGTH_ERROR);
			condition = false;
		}
		if (!password.equals(confirmedPassword)) {
			WebAttributes.loadAttribute(modelView, WebAttributes.DIFFERENT_PASSWORDS_ERROR);
			condition = false;
		}
		return condition;
	}
	
	private String getFooterMessage(){
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return "<hr/><h2>" + year + ". All rights are reserved</h2>";
	}
}
