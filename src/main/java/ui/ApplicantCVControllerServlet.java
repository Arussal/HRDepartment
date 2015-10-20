package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dao.CVFormApplicantDAO;
import dao.SkillApplicantDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.Applicant;
import domain.CVFormApplicant;
import domain.HRDepartment;
import domain.SkillApplicantCV;
import ui.util.*;

/**
 * Servlet implementation class ApplicantCVControllerServlet
 */
@Controller
public class ApplicantCVControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private CVFormApplicantDAO cvApplicantDao;
	private DAOFactory daoFactory;
	private Properties properties;
	private SkillApplicantDAO skillDao;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicantCVControllerServlet() throws ServletException {
        super();
        daoFactory = DAOFactory.getFactory();
    }


    @RequestMapping("applicantCVControllerServlet")
	private ModelAndView performTask(HttpSession session) throws ServletException {
		
		Properties properties = (Properties) session.getAttribute("properties");
		loadProperties(properties);
        cvApplicantDao = daoFactory.getCVFormApplicantDAO();
        skillDao = daoFactory.getSkillApplicantDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    	    
		//request.setCharacterEncoding("UTF-8");
	    return new ModelAndView(WebPath.getApplicantMainPage());
    }
    

    public void loadProperties(Properties properties) throws ServletException {
    	daoFactory.setLogPath(properties);
        try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
    }
    
    
    @RequestMapping("applicantCVDispatcher")
    public String dispatchRequest(@RequestParam Map<String, String> params) {
    	
    	if (params.get("createCV") != null) {
    		return "redirect:/" + WebPath.APPLICANT_CREATE_CV_HTML;
    	} else if (params.get("editCV") != null) {
    		return "redirect:/" + WebPath.APPLICANT_EDIT_CV_HTML;
    	} else if (params.get("deleteCV") != null) {
    		return "redirect:/" + WebPath.APPLICANT_DELETE_CV_HTML;
    	} else {
    		return "redirect:/" + WebPath.APPLICANT_SEND_CV_HTML;
    	}
    }
    
    
    @RequestMapping(value = "applicant/cvform/create.html", method=RequestMethod.GET)
	private ModelAndView getCreateCVFormPage(){
    	return new ModelAndView(WebPath.getApplicantCreateCVPage());
    }
    
    
    @RequestMapping(value = "applicant/cvform/create.html", method=RequestMethod.POST)
	private ModelAndView createNewCV(@ModelAttribute ("cvform") CVFormApplicant cvform) {
		
		cvform.setSendStatus("Not sent");
		CVFormApplicant cvApplicant = cvApplicantDao.createCVForm(cvform);
		
		for (SkillApplicantCV oneSkill : skills) {
			oneSkill.setCvApplicant(cvApplicant);
		}
		cvApplicant.setSkills(skills);
		try {
			cvApplicantDao.updateCVForm(cvApplicant);
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);
	}
	
    
    @RequestMapping(value = "applicant/cvform/edit.html", method = RequestMethod.GET)
	private ModelAndView editCV(@RequestParam ("cvId") int[] idList) { 
		
    	ModelAndView modelView = null;
    	modelView = makeErrorNoOneSelectedItem(idList);
    	if (modelView != null) {
    		return modelView;
    	}
		modelView = makeErrorTooManySelectedItem(idList);
		if (modelView != null) {
    		return modelView;
    	}
		CVFormApplicant cv = cvApplicantDao.getCVFormById(idList[0]);

		modelView = new ModelAndView(WebPath.getApplicantEditCVPage());
		modelView.addObject("cv", cv);
		return modelView;
	}


    @RequestMapping(value = "applicant/cvform/edit.html", method = RequestMethod.POST)
	private void saveChangesCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		CVFormApplicant cvApplicant = getDataFromForm(request, response);
		Integer id = Integer.parseInt(request.getParameter("id"));
		cvApplicant.setId(id);
		cvApplicant.setSendStatus("Not Sent");
		
		List<SkillApplicantCV> applicantSkills = skillDao.getSkillsByApplicantCV(cvApplicant);
		for (SkillApplicantCV oneApplicantSkill : applicantSkills) {
			try {
				skillDao.deleteSkill(oneApplicantSkill);
			} catch (PersistException e) {
				throw new ServletException();
			}
		}

		applicantSkills = getSkillFromForm(request);
		for (SkillApplicantCV oneSkill : applicantSkills) {
			oneSkill.setCvApplicant(cvApplicant);
		}
		
		cvApplicant.setSkills(applicantSkills);
		
		try {
			cvApplicantDao.updateCVForm(cvApplicant);
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);		
	}


	@RequestMapping("")
	private void sendCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Integer> idList = getSelectedCVFormsId(request, "cvId");
		
		HRDepartment hr = new HRDepartment(properties);
		for (Integer id : idList) {
			CVFormApplicant cv = cvApplicantDao.getCVFormById(id);
			cv.setSendStatus("Sent");
			try {
				hr.addCVForm(cv);
			} catch (PersistException e) {
				throw new ServletException();
			}
			try {
				cvApplicantDao.updateCVForm(cv);
			} catch (PersistException p){
				throw new ServletException();
			}
			
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);

	}

	
	@RequestMapping (value = "appilcant/cvform/delete.html", method = RequestMethod.POST)
	private ModelAndView deleteCV(@RequestParam ("cvId") int[] idList) throws ServletException {
		
		ModelAndView modelView = makeErrorNoOneSelectedItem(idList);
    	if (modelView != null) {
    		return modelView;
    	}
		
		for (Integer id : idList) {
			CVFormApplicant cv = cvApplicantDao.getCVFormById(id);
			try {
				cvApplicantDao.deleteCVForm(cv);
			} catch (PersistException e) {
				throw new ServletException();
			}
		}
		
		return new ModelAndView(WebPath.getApplicantMainPage());
	}
	
		
	private List<SkillApplicantCV> getSkillFromForm(HttpServletRequest request){
		
		String skills = request.getParameter("skills");
		//delete '[' and ']' symbols from skills to avoid double symbols
		String formattedSkills = skills;
		if (skills.startsWith("[")) {
			formattedSkills = skills.substring(1, skills.length());
			skills = formattedSkills;
		} 
		if (skills.endsWith("]")) {
			formattedSkills = skills.substring(0, skills.length()-1);
			skills = formattedSkills;
		}
		
		String[] skillsArray = skills.split(", ");
		List<SkillApplicantCV> applicantSkills = new ArrayList<SkillApplicantCV>();
		for (int i = 0; i < skillsArray.length; i++) {
			SkillApplicantCV oneSkillApplicant = new SkillApplicantCV();
			oneSkillApplicant.setSkill(skillsArray[i]);
			SkillApplicantCV createdSkill = (SkillApplicantCV) skillDao.createSkill(oneSkillApplicant);
			applicantSkills.add(createdSkill);
		}
		
		return applicantSkills;
	}

	
	private ModelAndView makeErrorNoOneSelectedItem(int[] idList) {
		if (0 == idList.length) {
			ModelAndView modelView = new ModelAndView(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.NO_ONE_ITEM_SELECTED);
			return modelView;
		}
		return null;
	}
	
	
	private ModelAndView makeErrorTooManySelectedItem(int[] idList) {
		if (idList.length > 1) {
			ModelAndView modelView = new ModelAndView(WebPath.getErrorPage());
			WebAttributes.loadAttribute(modelView, WebAttributes.TOO_MANY_ITEMS_SELECTED);
			return modelView;
		}
		return null;
	}
}
