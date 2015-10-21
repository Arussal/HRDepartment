package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dao.CVFormApplicantDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
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
	//private SkillApplicantDAO skillDao;
	
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
        //skillDao = daoFactory.getSkillApplicantDAO();
        
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
    	List<SkillApplicantCV> applicantSkills = new ArrayList<SkillApplicantCV>(10);
    	ModelAndView modelView = new ModelAndView(WebPath.getApplicantCreateCVPage());
    	modelView.addObject("applicantSkills", applicantSkills);
    	return modelView;
    }
    
    
    @RequestMapping(value = "applicant/cvform/create.html", method=RequestMethod.POST)
	private ModelAndView createNewCV(@ModelAttribute ("appilcantCV") CVFormApplicant cvform) {
		
		cvform.setSendStatus("Not sent");	
		cvApplicantDao.createCVForm(cvform);
		
//		for (SkillApplicantCV oneSkill : skills) {
//			oneSkill.setCvApplicant(cvApplicant);
//		}
//		cvApplicant.setSkills(skills);
//		try {
//			cvApplicantDao.updateCVForm(cvApplicant);
//		} catch (PersistException e) {
//			throw new ServletException();
//		}
		return new ModelAndView(WebPath.getApplicantMainPage());
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
		CVFormApplicant applicantCV = cvApplicantDao.getCVFormById(idList[0]);

		modelView = new ModelAndView(WebPath.getApplicantEditCVPage());
		modelView.addObject("applicantCV", applicantCV);
		return modelView;
	}


    @RequestMapping(value = "applicant/cvform/edit.html", method = RequestMethod.POST)
	private ModelAndView saveChangesCV(@ModelAttribute ("appilcantCV") CVFormApplicant cvApplicant)
			throws ServletException {
		
		try {
			cvApplicantDao.updateCVForm(cvApplicant);
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		return new ModelAndView(WebPath.getApplicantMainPage());		
	}


	@RequestMapping(value = "applicant/cvform/send.html", method = RequestMethod.POST)
	private ModelAndView sendCV(@RequestParam ("cvId") int[] idList) throws ServletException, IOException {
				
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
		
		return new ModelAndView(WebPath.getApplicantMainPage());

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
