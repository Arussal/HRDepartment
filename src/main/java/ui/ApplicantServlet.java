package ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dao.ApplicantDAO;
import dao.CVFormApplicantDAO;
import dao.SkillApplicantDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.util.DAOFactory;
import domain.Applicant;
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
	private SkillApplicantDAO skillAppDao;
	
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
    
    @InitBinder
    private void initBinder(WebDataBinder binder) {
    	binder.registerCustomEditor(String.class, "login", new LoginCheckEditor());
    	binder.registerCustomEditor(Date.class, "birthDay", new DateCheckEditor());
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
		skillAppDao = daoFactory.getSkillApplicantDAO();
		session.setAttribute("cvAplcntDao", cvAplcntDao);
		session.setAttribute("skillAppDao", skillAppDao);
		return new ModelAndView(WebPath.getApplicantLoginPage());
	}	


	
    @RequestMapping(value = "applicant/login", method = RequestMethod.GET)
    private ModelAndView getLoginPage(){
    	return new ModelAndView(WebPath.getApplicantLoginPage());
    }
    
    
    @RequestMapping(value = "applicant/login", method = RequestMethod.POST)
    private Object login(@RequestParam("login") String login, 
    		@RequestParam("password") String password, HttpSession session) {
    	Applicant foundedApplicant = null;
    	ModelAndView modelView = null;
    	if (login != null) {
			foundedApplicant = aplcntDao.getApplicantByLogin(login);
			if (null == foundedApplicant) {
				modelView = new ModelAndView(WebPath.getErrorPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
				WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
			}
    		if (foundedApplicant.getPassword().equals(password)) {
				session.setAttribute("applicant", foundedApplicant);
				return "redirect:/" + WebPath.APPLICANT_MAIN_HTML;
    		} else {
    			modelView = new ModelAndView(WebPath.getErrorPage());
				WebAttributes.loadAttribute(modelView, WebAttributes.WRONG_PASSWORD);
				WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
    		}
    	} else {
    		modelView = new ModelAndView(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
			WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
    	}
		return modelView;
    }

	@RequestMapping(value = "applicant/change-password", method = RequestMethod.GET)
	private ModelAndView goToChangePasswordPage() {
		return new ModelAndView(WebPath.getApplicantChangePasswordPage());
	}
	
	
	@RequestMapping(value = "applicant/change-password", method = RequestMethod.POST)
	private ModelAndView changePassword(@RequestParam Map <String, String> params) 
			throws ServletException {
		
		ModelAndView modelView = new ModelAndView();
		
		String login = params.get("login");
		String oldPassword = params.get("oldPassword");
		String newPassword = params.get("newPassword");
		String repeatePassword = params.get("repeatePassword");
		
		Applicant applicant = null;
		applicant = aplcntDao.getApplicantByLogin(login);
		if (null == applicant) {
			modelView = new ModelAndView(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
			return modelView;
		}
		
		if (oldPassword.equals(applicant.getPassword())) {
			
			
				applicant.setPassword(newPassword);
				aplcntDao.updateApplicant(applicant);
				modelView.setViewName(WebPath.getApplicantCompleteChangePasswordPage());
			
		} else {
			modelView.setViewName(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.WRONG_PASSWORD);
			WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
		}
		return modelView;
	}

	
    @RequestMapping(value = "applicant/registrate", method = RequestMethod.GET)
    private ModelAndView getRegistrationPage() {
    	return new ModelAndView(WebPath.getApplicantRegistratePage());
	}
    
	
    @RequestMapping(value = "applicant/registrate", method=RequestMethod.POST)
	private ModelAndView registrateApplicant(@Valid @ModelAttribute("applicant") Applicant applicant, 
			BindingResult result, @RequestParam("confirmPassword") String confirmPassword ) {
						
		ModelAndView modelView = new ModelAndView();
		Applicant persistedApplicant = aplcntDao.getApplicantByLogin(applicant.getLogin());
		
		if (result.hasErrors()) {
			modelView.setViewName(WebPath.getApplicantRegistratePage());
		} else if (applicant.getLogin() == null) {
			modelView.setViewName(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.USER_ALREADY_EXIST_ERROR);
		} else if (!applicant.getPassword().equals(confirmPassword)) {
			modelView.setViewName(WebPath.getApplicantRegistratePage());
		} else if (persistedApplicant != null) {
			modelView.setViewName(WebPath.getApplicantRegistratePage());
		} else {
			applicant = aplcntDao.createApplicant(applicant);
			modelView.setViewName(WebPath.getApplicantCompleteRegistrationPage());
		}
		return modelView;
	}	
	
	
    @RequestMapping(value = "/applicant/{id}/edit", method = RequestMethod.GET)
    private ModelAndView updateApplicantInfo(@PathVariable("id") int id) {
    	Applicant applicant = aplcntDao.getApplicantByID(id);
    	ModelAndView modelView = new ModelAndView(WebPath.getApplicantEditPage());
    	modelView.addObject(applicant);
    	return modelView;
    }
    
    
    @RequestMapping(value = "/applicant/edit", method = RequestMethod.POST)
    private ModelAndView updateApplicantInfo(@ModelAttribute("applicant") Applicant applicant) {
		aplcntDao.updateApplicant(applicant);
    	return new ModelAndView(WebPath.getApplicantMainPage());
    }
    
    
	@RequestMapping(value = "applicant/delete", method = RequestMethod.GET)
	private ModelAndView getDeletePage() {
		return new ModelAndView(WebPath.getApplicantDeletePage());
	}
	
	
	@RequestMapping(value = "applicant/delete", method = RequestMethod.POST)
	private ModelAndView deleteApplicant(@ModelAttribute("applicant") Applicant applicant, 
			HttpSession session) {
		
		Applicant foundedApplicant = null;
		ModelAndView modelView = null;
		if (applicant.getLogin() != null) {
			foundedApplicant = aplcntDao.getApplicantByLogin(applicant.getLogin());
			if (null == foundedApplicant) {
					modelView = new ModelAndView(WebPath.getErrorPage());
					WebAttributes.loadAttribute(modelView, WebAttributes.USER_NOT_FOUND);
					WebAttributes.loadAttribute(modelView, WebAttributes.INVALID_APPLICANT_LOGIN);
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
	
	
	@RequestMapping("applicant/success-delete")
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
	
		
	private String getFooterMessage(){
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return "<hr/><h2>" + year + ". All rights are reserved</h2>";
	}
}
