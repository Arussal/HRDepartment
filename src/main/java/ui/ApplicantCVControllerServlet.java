package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CVFormApplicantDAO;
import dao.SkillDAO;
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
@WebServlet("/applicantCVControllerServlet")
public class ApplicantCVControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private CVFormApplicantDAO cvApplicantDao;
	private DAOFactory daoFactory;
	private Properties properties;
	private SkillDAO skillDao;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicantCVControllerServlet() throws ServletException {
        super();
        daoFactory = DAOFactory.getFactory();
    }

	/**
	 * @throws IOException 
	 * @throws ServletException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		performTask(request, response);

	}

	/**
	 * @throws IOException 
	 * @throws ServletException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		performTask(request, response);
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
        Properties properties = (Properties) session.getAttribute("properties");
        this.properties = properties;
        daoFactory.setLogPath(properties);
        cvApplicantDao = daoFactory.getCVFormApplicantDAO();
        skillDao = daoFactory.getSkillDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		request.setCharacterEncoding("UTF-8");
			
		int action = checkActiion(request);
		if (1 == action) {
			forward(WebPath.APPLICANT_CREATE_CV_JSP, request, response);
		} else if (2 == action) {
			createNewCV(request, response);
		} else if (3 == action) {
			editCV(request, response);
		} else if (4 == action) {
			saveChangesCV(request, response); 
		} else if (5 == action) {
			deleteCV(request, response);
		} else if (6 == action) {
			sendCV(request, response);
		} else {
			forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);
		}
	}


	private int checkActiion(HttpServletRequest request) {
		if (request.getParameter("createCV") != null) {
			return 1;
		} else if (request.getParameter("confirmCreateCV") != null) {
			return 2;
		} else if (request.getParameter("editCV") != null) {
			return 3;
		} else if (request.getParameter("saveChanges") != null) {
			return 4;
		} else if (request.getParameter("deleteCV") != null) {
			return 5;
		} else if (request.getParameter("sendCV") != null) {
			return 6;
		}
		return 0;
	}
	
	
	private void createNewCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		CVFormApplicant cv = getDataFromForm(request, response);
		cv.setSendStatus("Not sent");
		cvApplicantDao.createCVForm(cv);
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
		
		CVFormApplicant cvForm = getDataFromForm(request, response);
		cvForm.setSendStatus("Not Sent");

		Integer id = Integer.parseInt(request.getParameter("id"));
		cvForm.setId(id);

		try {
			cvApplicantDao.updateCVForm(cvForm);
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
			hr.addCVForm(cv);
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
			String skills = request.getParameter("skills");
			String workExpirience = request.getParameter("expirience");
			String salary = request.getParameter("desiredSalary");
			String addInfo = request.getParameter("addInfo");
			
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
				String[] skillsArray = skills.split(", ");
				Set<SkillApplicantCV> applicantSkills = new HashSet<SkillApplicantCV>();
				for (int i = 0; i < skillsArray.length; i++) {
					SkillApplicantCV oneSkillApplicant = new SkillApplicantCV();
					oneSkillApplicant.setSkill(skillsArray[i]);
					SkillApplicantCV createdSkill = (SkillApplicantCV) skillDao.createSkill(oneSkillApplicant);
					applicantSkills.add(createdSkill);
				}

				HttpSession session = request.getSession(false);
				Applicant applicant = (Applicant)session.getAttribute("applicant"); 
				newCV.setName(applicant.getName());
				newCV.setAge(parsedAge);
				newCV.setEducation(education);
				newCV.setEmail(email);
				newCV.setPhone(phone);
				newCV.setPost(post);
				newCV.setSkills(applicantSkills);
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
