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
	private void performTask(@RequestParam Map<String, String> params, HttpSession session) 
			throws ServletException, IOException {
		
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
    }

    public void loadProperties(Properties properties) throws ServletException {
    	daoFactory.setLogPath(properties);
        try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
    }
    
    
    @RequestMapping(value = "applicant/cvform/create.html", method=RequestMethod.GET)
	private ModelAndView getCreateCVFormPage(){
    	return new ModelAndView(WebPath.getApplicantCreateCVPage());
    }
    
    @RequestMapping(value = "applicant/cvform/create.html", method=RequestMethod.POST)
	private ModelAndView createNewCV(@ModelAttribute ("cvform") CVFormApplicant cvform) 
			throws ServletException, IOException {
		
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
	

	private void editCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException { 
		
		List<Integer> idList = getSelectedCVFormsId(request, "cvId");
		
		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
		
		CVFormApplicant cv = cvApplicantDao.getCVFormById(idList.get(0));

		request.setAttribute("cv", cv);
		forward(WebPath.APPLICANT_EDIT_CV_JSP, request, response);
		
	}


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

	
	private void deleteCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Integer> idList = getSelectedCVFormsId(request, "cvId");
		
		for (Integer id : idList) {
			CVFormApplicant cv = cvApplicantDao.getCVFormById(id);
			try {
				cvApplicantDao.deleteCVForm(cv);
			} catch (PersistException e) {
				throw new ServletException();
			}
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);
	}
	

	private CVFormApplicant getDataFromForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		CVFormApplicant newCV = new CVFormApplicant();
		boolean emptyFields = isEmptyFields(request);

		if (!emptyFields) {
			String age = request.getParameter("age");
			String education = request.getParameter("education");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String post = request.getParameter("post");
			String workExpirience = request.getParameter("expirience");
			String salary = request.getParameter("desiredSalary");
			String addInfo = request.getParameter("addInfo");
		
			
			//check fields with numbers
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("age", age);
			intData.put("salary", salary);
			intData.put("workExpirience", workExpirience);
			
			boolean wrongFields = isWrongDataFields(intData, request);
			
			if (!wrongFields) {
					
				int parsedAge = Integer.parseInt(age);
				int parsedSalary = Integer.parseInt(salary);
				int parsedWorkExpirience = Integer.parseInt(workExpirience);
				
				HttpSession session = request.getSession(false);
				Applicant applicant = (Applicant)session.getAttribute("applicant"); 
				newCV.setName(applicant.getName());
				newCV.setAge(parsedAge);
				newCV.setEducation(education);
				newCV.setEmail(email);
				newCV.setPhone(phone);
				newCV.setPost(post);
				newCV.setAdditionalInfo(addInfo);
				newCV.setWorkExpirience(parsedWorkExpirience);
				newCV.setDesiredSalary(parsedSalary);
				
			} else {
				WebAttributes.loadAttribute(request,  WebAttributes.WRONG_DATA);
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_DATA);
			forward(WebPath.ERROR_JSP, request, response);
		}
		
		return newCV; 
		
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


	private List<Integer> getSelectedCVFormsId(HttpServletRequest request,
			String parameter) {
		
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals(parameter)){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		return idList;
	}

	
	private boolean isEmptyFields(HttpServletRequest request) {
		
		Map<String, String[]> parameters = request.getParameterMap();
		List<String> emptyFieldsList = new ArrayList<String>();
		for (String key : parameters.keySet()) {
			String val = request.getParameter(key);
			if (val.equals("")) {
				emptyFieldsList.add(key);
			}
		}
		if (emptyFieldsList.size() > 0) {
			WebAttributes.loadAttribute(request,  WebAttributes.EMPTY_FIELDS_LIST);
			request.setAttribute("emptyFieldsList", emptyFieldsList);
			return true;
		}
		return false;
	}

	
	private boolean isWrongDataFields(Map<String, String> map, HttpServletRequest request) {
		
		List<String> wrongFieldsList = new ArrayList<String>();
		for (String data : map.keySet()) {
			try {
				int value = Integer.parseInt(map.get(data));
				if (value < 0) {
					wrongFieldsList.add(data);	
				}
			} catch (NumberFormatException e){
				wrongFieldsList.add(data);
			}
		}
		
		if (wrongFieldsList.size() > 0) {
			WebAttributes.loadAttribute(request,  WebAttributes.WRONG_DATA_FIELDS_LIST);
			request.setAttribute("wrongFieldsList", wrongFieldsList);
			return true;
		}
		return false;
	}
	
	
	private void makeErrorNoOneSelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if (0 == idList.size()) {
			WebAttributes.loadAttribute(request, WebAttributes.NO_ONE_ITEM_SELECTED);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void makeErrorTooManySelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if (idList.size() > 1) {
			WebAttributes.loadAttribute(request, WebAttributes.TOO_MANY_ITEMS_SELECTED);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
			request.getRequestDispatcher(path).forward(request, response);
	}
}
