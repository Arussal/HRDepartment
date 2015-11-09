package ui;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dao.CVFormApplicantDAO;
import dao.SkillApplicantDAO;
import dao.exceptions.PersistException;
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
	private SkillApplicantDAO skillAppDao;
	private Properties properties;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicantCVControllerServlet() throws ServletException {
        super();
    }

    
    private void setSessionAttributes(HttpSession session) {
    	this.cvApplicantDao = (CVFormApplicantDAO) session.getAttribute("cvAplcntDao");
    	this.skillAppDao = (SkillApplicantDAO) session.getAttribute("skillAppDao");
    	this.properties = (Properties) session.getAttribute("properties");
    }
      

	@RequestMapping(value = "applicant/main", method = RequestMethod.GET)
	private ModelAndView enter(@ModelAttribute("applicant") Applicant applicant, HttpSession session)
			throws ServletException {
		
		if (cvApplicantDao == null) {
			setSessionAttributes(session);
		}
		
		ModelAndView modelView = null;

		Applicant foundedApplicant = (Applicant) session.getAttribute("applicant");
		if (foundedApplicant != null) {
			List<CVFormApplicant> cvList = cvApplicantDao.getCVFormByName(foundedApplicant.getName());
			modelView = new ModelAndView(WebPath.getApplicantMainPage());
			modelView.addObject("cvList", cvList);
		} else {
			modelView = new ModelAndView(WebPath.getApplicantLoginPage());
		}
		modelView.addObject("applicant", foundedApplicant);
		return modelView;
	}
	
        
    @RequestMapping("applicant/cvform/{id}/view")
    private ModelAndView getCVViewPage(@PathVariable("id") int id){
    	CVFormApplicant applicantCV = cvApplicantDao.getCVFormById(id);
    	ModelAndView modelView = new ModelAndView(WebPath.getApplicantViewCVPage());
    	modelView.addObject("cv", applicantCV);
    	return modelView;
    }
    
    
    @RequestMapping(value = "applicant/cvform/create", method=RequestMethod.GET)
	private ModelAndView getCreateCVFormPage(){
    	List<SkillApplicantCV> applicantSkills = new ArrayList<SkillApplicantCV>();
    	for(int i = 0; i < 10; i++) {
    		SkillApplicantCV skill = new SkillApplicantCV();
    		skill.setName("Skill " + i);
    		applicantSkills.add(skill);
    	}
    	CVFormApplicant applicantCV = new CVFormApplicant();
    	applicantCV.setSkills(applicantSkills);
    	ModelAndView modelView = new ModelAndView(WebPath.getApplicantCreateCVPage());
    	modelView.addObject("applicantCV", applicantCV);
    	modelView.addObject("applicantSkills", applicantSkills);
    	return modelView;
    }
    
    
    @RequestMapping(value = "applicant/cvform/create", method=RequestMethod.POST)
	private String createNewCV(@ModelAttribute ("appilcantCV") CVFormApplicant cvform, 
			BindingResult result, HttpSession session) {
		
    	Applicant applicant = (Applicant) session.getAttribute("applicant");
    	String surname = applicant.getSurname();
    	String name = applicant.getName();
    	String lastName = applicant.getLastName();
    	String phone = applicant.getPhone();
    	String email = applicant.getEmail();
    	String education = applicant.getEducation();
    	Date birthday = applicant.getBirthDay();
    	cvform.setSurname(surname);
    	cvform.setName(name);
    	cvform.setLastName(lastName);
    	cvform.setBirthday(birthday);
    	cvform.setPhone(phone);
    	cvform.setEmail(email);
    	cvform.setEducation(education);
		cvform.setSendStatus("Not sent");
		List<SkillApplicantCV> skills = new ArrayList<SkillApplicantCV>();
		for (int i = 0; i < cvform.getSkills().size(); i++) {
			if (!cvform.getSkills().get(i).getName().equals("Skill " + i)) {
				SkillApplicantCV skill = cvform.getSkills().get(i);
				skill.setCvApplicant(cvform);
				skills.add(skill);
			}
		}
		cvform.setSkills(skills);
		cvApplicantDao.createCVForm(cvform);
		
		return "redirect:/" + WebPath.APPLICANT_MAIN_HTML;
	}
	
    
    @RequestMapping(value = "applicant/cvform/{id}/edit", method = RequestMethod.GET)
	private ModelAndView editCV(@PathVariable("id") int id) { 
		
    	CVFormApplicant applicantCV = cvApplicantDao.getCVFormById(id);
    	List<SkillApplicantCV> skills = applicantCV.getSkills();
    	if (skills.size() < 10) {
    		for(int i = skills.size(); i < 10; i++) {
    			SkillApplicantCV skill = new SkillApplicantCV();
    			skill.setName("Skill " + i);
    			skills.add(i, skill);
    		}
    	}
    	applicantCV.setSkills(skills);
		ModelAndView modelView = new ModelAndView(WebPath.getApplicantEditCVPage());
		modelView.addObject("applicantCV", applicantCV);
		return modelView;
	}


    @RequestMapping(value = "applicant/cvform/edit", method = RequestMethod.POST)
	private String saveChangesCV(@ModelAttribute ("applicantCV") CVFormApplicant applicantCV)
			throws ServletException {
    	
    	applicantCV.setSendStatus("Not sent");
    	List<SkillApplicantCV> persistedSkills = skillAppDao.getSkillsByApplicantCV(applicantCV);
    	for (SkillApplicantCV skill : persistedSkills) {
    		skillAppDao.deleteSkill(skill);
    	}
    	List<SkillApplicantCV> inputedSkills = applicantCV.getSkills();
		for (int i = inputedSkills.size()-1; i >= 0; i--) {
			SkillApplicantCV skill = inputedSkills.get(i);
			if (!skill.getName().equals("Skill " + i)) {
				skill.setCvApplicant(applicantCV);
			} else {
				inputedSkills.remove(skill);
			}
		}
		
		applicantCV.setSkills(inputedSkills);
		try {
			cvApplicantDao.updateCVForm(applicantCV);
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		return "redirect:/" + WebPath.APPLICANT_MAIN_HTML;	
	}


	@RequestMapping(value = "applicant/cvform/{id}/send", method = RequestMethod.GET)
	private String sendCV(@PathVariable ("id") int id) throws ServletException {

		HRDepartment hr = new HRDepartment(properties);
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
		
		return "redirect:/" + WebPath.APPLICANT_MAIN_HTML;
	}

	
	@RequestMapping (value = "applicant/cvform/{id}/delete", method = RequestMethod.GET)
	private String deleteCV(@PathVariable ("id") int id) throws ServletException {
		
		CVFormApplicant cv = cvApplicantDao.getCVFormById(id);
		try {
			cvApplicantDao.deleteCVForm(cv);
		} catch (PersistException e) {
			throw new ServletException();
		}

		return "redirect:/" + WebPath.APPLICANT_MAIN_HTML;	
	}
}
