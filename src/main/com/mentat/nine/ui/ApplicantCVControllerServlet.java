package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.CVFormApplicantDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Applicant;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.util.LogConfig;
import main.com.mentat.nine.ui.util.*;

/**
 * Servlet implementation class ApplicantCVControllerServlet
 */
@WebServlet("/applicantCVControllerServlet")
public class ApplicantCVControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(ApplicantCVControllerServlet.class);
    
	private CVFormApplicantDAO cvApplicantDao;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicantCVControllerServlet() {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        cvApplicantDao = daoFactory.getCVFormApplicantDAO();
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
		
		CVForm cv = getDataFromForm(request, response);
		cv.setSendStatus("Not sent");
		
		try {
			cvApplicantDao.createCVForm(cv);
		} catch (PersistException e) {
			log.error("can't create new CVForm");
			throw new ServletException();
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);
	}
	

	private void editCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException { 

		WebAttributes.loadAttribute(request,  WebAttributes.APPLICANT_BASE_PAGE_ATTRIBUTE);
		
		List<Integer> idList = getSelectedCVFormsId(request, "cvId");
		
		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
		
		CVForm cv = null;
		try {
			cv = cvApplicantDao.getCVFormById(idList.get(0));
		} catch (PersistException e) {
			log.error("can't get CVForm Form with id " + idList.get(0));
		}

		request.setAttribute("cv", cv);
		forward(WebPath.APPLICANT_EDIT_CV_JSP, request, response);
		
	}


	private void saveChangesCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		CVForm cvForm = getDataFromForm(request, response);

		Integer id = Integer.parseInt(request.getParameter("id"));
		cvForm.setId(id);

		try {
			cvApplicantDao.updateCVForm(cvForm);
		} catch (PersistException e) {
			log.warn("can't update CVForm with id " + id);
			throw new ServletException();
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);		
	}


	private void sendCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Integer> idList = getSelectedCVFormsId(request, "cvId");
		
		HRDepartment hr = new HRDepartment();
		for (Integer id : idList) {
			CVForm cv = null;
			try {
				cv = cvApplicantDao.getCVFormById(id);
			} catch (PersistException e) {
				log.error("can't get CV with id " + id);
				throw new ServletException();
			}
			cv.setSendStatus("Sent");
			try {
				hr.addCVForm(cv);
			} catch (PersistException e) {
				log.error("can't send CV to HRDepartment");
				throw new ServletException();
			}
			try {
				cvApplicantDao.updateCVForm(cv);
			} catch (PersistException e) {
				log.error("can't update CV status");
				throw new ServletException();
			}
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);

	}

	
	private void deleteCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Integer> idList = getSelectedCVFormsId(request, "cvId");
		
		for (Integer id : idList) {
			try {
				CVForm cv = cvApplicantDao.getCVFormById(id);
				cvApplicantDao.deleteCVForm(cv);
			} catch (PersistException e) {
				log.error("can't delete CVForm with id " + id);
				throw new ServletException();
			}
		}
		
		forward(WebPath.APPLICANT_BASE_PAGE_SERVLET, request, response);
	}
	

	private CVForm getDataFromForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		CVForm newCV = new CVForm();
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
				Set<String> parsedSkills = new HashSet<String>(Arrays.asList(skills.split(",")));

				HttpSession session = request.getSession(false);
				Applicant applicant = (Applicant)session.getAttribute("applicant"); 
				newCV.setName(applicant.getName());
				newCV.setAge(parsedAge);
				newCV.setEducation(education);
				newCV.setEmail(email);
				newCV.setPhone(phone);
				newCV.setPost(post);
				newCV.setSkills(parsedSkills);
				newCV.setAdditionalInfo(addInfo);
				newCV.setWorkExpirience(parsedWorkExpirience);
				newCV.setDesiredSalary(parsedSalary);
				
			} else {
				WebAttributes.loadAttribute(request,  WebAttributes.APPLICANT_EDIT_CV_ATTRIBUTE);
				WebAttributes.loadAttribute(request,  WebAttributes.WRONG_DATA);
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			WebAttributes.loadAttribute(request,  WebAttributes.APPLICANT_EDIT_CV_ATTRIBUTE);
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
