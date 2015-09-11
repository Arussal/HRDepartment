package main.com.mentat.nine.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import main.com.mentat.nine.domain.Applicant;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.CVForm;
import main.com.mentat.nine.domain.HRDepartment;

/**
 * Servlet implementation class ApplicantCVControllerServlet
 */
@WebServlet("/applicantCVControllerServlet")
public class ApplicantCVControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicantCVControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		performTask(request, response);
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int action = checkActiion(request);
		
		if (1 == action) {
			forward("createCV.jsp", request, response);
		} else if (2 == action) {
			createNewCV(request, response);
		}
	}
	

	private int checkActiion(HttpServletRequest request) {
		if (request.getParameter("createCV") != null) {
			return 1;
		} else if (request.getParameter("confirmCreateCV") != null) {
			return 2;
		} else if (request.getParameter("editCV") != null) {
			return 3;
		} else if (request.getParameter("deleteCV") != null) {
			return 4;
		}
		return 0;
	}
	
	
	private void createNewCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		CVForm cv = getDataFromForm(request, response);
		
	}

	
	private CVForm getDataFromForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		CVForm newCV = null;
		boolean emptyFields = isEmptyFields(request);

		if (!emptyFields) {
			String age = request.getParameter("age");
			String education = request.getParameter("education");
			String email = request.getParameter("education");
			String phone = request.getParameter("education");
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
				formattedSkills = skills.substring(skills.length());
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
				Set<String> parsedSkills = new HashSet<String>(Arrays.asList(formattedSkills.split(";")));

				HttpSession session = request.getSession();
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
			}
		} else {
			request.setAttribute("wrongFields", "wrongFields");
			forward("error.jsp", request, response);
		}
			
		return newCV; 
		
	}

	
	private boolean isEmptyFields(HttpServletRequest request) {
		
		Map<String, String[]> parameters = request.getParameterMap();
		List<String> emptyParameters = new ArrayList<String>();
		for (String key : parameters.keySet()) {
			String val = request.getParameter(key);
			if (val.equals("")) {
				emptyParameters.add(key);
			}
		}
		if (emptyParameters.size() > 0) {
			request.setAttribute("emptyFields", emptyParameters);
			return true;
		}
		return false;
	}

	
	private boolean isWrongDataFields(Map<String, String> map, HttpServletRequest request) {
		
		List<String> wrongFields = new ArrayList<String>();
		for (String data : map.keySet()) {
			try {
				int value = Integer.parseInt(map.get(data));
				if (value < 0) {
					wrongFields.add(data);	
				}
			} catch (NumberFormatException e){
				wrongFields.add(data);
			}
		}
		
		if (wrongFields.size() > 0) {
			request.setAttribute("wrongFields", wrongFields);
			return true;
		}
		return false;
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
