package main.com.mentat.nine.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.exceptions.NoSuitableCandidateException;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * Servlet implementation class ApplicationFormControllerServlet
 */
@WebServlet("/appControllerServlet")
public class ApplicationFormControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(ApplicationFormControllerServlet.class);
	
	private ApplicationFormDAO appDao;
	
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationFormControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
		appDao = daoFactory.getApplicationFormDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			log.error("doGet error");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			log.error("doPost error");
		}
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, PersistException {

		request.setCharacterEncoding("UTF8");
		
		int action = checkAction(request);
		if (1 == action) {
			createNewApp(request, response);
		} else if (2 == action) {
			deleteApp(request, response);
		} else if (3 == action) {
			editApp(request, response);
		} else {
			findNewCandidate(request, response);
		}
		
	}
	

	private int checkAction(HttpServletRequest request){
		if ((request.getParameter("createApp") != null) || (request.getParameter("newApp") != null)) {
			return 1;
		} else if (request.getParameter("deleteApp") != null) {
			return 2;
		} else if ((request.getParameter("editApp") != null) || (request.getParameter("edit") != null)) {
			return 3;
		}
		return 0;
	}

	
	private void createNewApp(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {

		if (request.getParameter("createApp") != null) {
			setDateFields(request);
			forward("new_application.jsp", request, response);
		}

		ApplicationForm appForm = getDataFromForm(request, response);
		appDao.createApplicationForm(appForm);
		
		forward("applicationBaseServlet", request, response);
		
	}

	
	private ApplicationForm getDataFromForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, PersistException {
		
		ApplicationForm appForm = null;
		boolean emptyFields = isEmptyFields(request);

		if (!emptyFields) {
			String age = request.getParameter("age");
			String education = request.getParameter("education");
			String requirements = request.getParameter("requirements");
			String post = request.getParameter("post");
			String salary = request.getParameter("salary");
			String workExpirience = request.getParameter("expirience");
			
			//delete '[' and ']' symbols from requirements to avoid double symbols
			String formattedRequirements = requirements;
			if (requirements.startsWith("[")) {
				formattedRequirements = requirements.substring(1, requirements.length());
				requirements = formattedRequirements;
			} 
			if (requirements.endsWith("]")) {
				formattedRequirements = requirements.substring(requirements.length());
			}
			
			//get data from dataFields
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String day = request.getParameter("day");
			boolean correctDate = isCorrectInputDateFields(month, day);
			if (!correctDate) {
				request.setAttribute("wrongDate", "wrongData");
				forward("error.jsp", request, response);
			}
			String date = year + "-" + month + "-" + day;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = null;
			try {
				parsedDate = dateFormat.parse(date);
			} catch (ParseException e) {
				log.error("can't parse date from Form");
			}
			
			//check fields with numbers
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("age", age);
			intData.put("salary", salary);
			intData.put("workExpirience", workExpirience);
			
			boolean wrongFields = isWrongDataFields(intData, request);
			
			if (!wrongFields) {
				HRDepartment hrDep = new HRDepartment();
				
				int parsedAge = Integer.parseInt(age);
				int parsedSalary = Integer.parseInt(salary);
				int parsedWorkExpirience = Integer.parseInt(workExpirience);
				Set<String> parsedRequirements = new HashSet<String>(Arrays.asList(formattedRequirements.split(";")));
				appForm = hrDep.formApplicationForm(parsedAge, education, 
						parsedRequirements, post, parsedSalary, parsedWorkExpirience, parsedDate);
			}
		} else {
			request.setAttribute("wrongFields", "wrongFields");
			forward("error.jsp", request, response);
		}
			
		return appForm;
	}
	
	//if the Days corresponding to the Months
	private boolean isCorrectInputDateFields(String month, String day) {
		String[] shortMonths = {"2", "4", "6", "9", "11"};
		List<String> shortM = Arrays.asList(shortMonths);
		if (shortM.contains(month)) {
			if (Integer.parseInt(day) == 31) {
				return false;
			}
		}
		return true;
	}
	
	//if there is no empty fields
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
	
	//if there is correct data in fields
	private boolean isWrongDataFields(Map<String, String> map, HttpServletRequest request) {
		List<String> wrongFields = new ArrayList<String>();
		for (String data : map.keySet()) {
			try {
				Integer.parseInt(map.get(data));
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
	
	private void deleteApp(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		List<Integer> idList = getSelectedAppFormsId(request, "appId");
		
		for (Integer id : idList) {
			ApplicationForm appForm = appDao.getApplicationFormById(id);
			appDao.deleteApplicationForm(appForm);
		}
		
		forward("applicationBaseServlet", request, response);
	}
	
	
	private void editApp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
						
		if (request.getParameter("editApp") != null) {
			List<Integer> idList = getSelectedAppFormsId(request, "appId");
	
			makeErrorNoOneSelectedItem(idList, request, response);
			makeErrorTooManySelectedItem(idList,request, response);
			
			ApplicationForm appForm = null;
			try {
				appForm = appDao.getApplicationFormById(idList.get(0));
			} catch (PersistException e) {
				log.error("can't get Application Form with id " + idList.get(0));
			}
			setDateFields(request);
			request.setAttribute("app", appForm);
			forward("edit_application.jsp", request, response);
		}
		
		ApplicationForm appForm = null;
		try {
			appForm = getDataFromForm(request, response);
		} catch (PersistException e) {
			log.error("can't get ApplicationForm from Form");
		}
		Integer id = Integer.parseInt(request.getParameter("id"));
		appForm.setId(id);
		try {
			appDao.updateApplicationForm(appForm);
		} catch (PersistException e) {
			log.warn("can't update ApplicationForm with id " + id);
		}
		
		forward("applicationBaseServlet", request, response);
	}
	
	

	private void findNewCandidate(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {

		List<Integer> idList = getSelectedAppFormsId(request, "appId");
		
		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
		
		ApplicationForm appForm = appDao.getApplicationFormById(idList.get(0));
		HRDepartment hrDep = new HRDepartment();
		Set<Candidate> findedCandidates = null;
		try {
			findedCandidates = hrDep.findCandidates(appForm);
		} catch (NoSuitableCandidateException e) {
			request.setAttribute("noOneCandidate", "noOneCandidate");
			forward("error.jsp", request, response);
		}
		
		request.setAttribute("candidates", findedCandidates);
		forward("new_candidate.jsp", request, response);
			
	}

	
	private void setDateFields(HttpServletRequest request) {
		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			years.add(currentYear);
		
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		for (int i = currentMonth; i < 13; i++) {
			months.add(i);
		}
		for (int i = 1; i < 32; i++) {
			days.add(i);
		}
		
		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
	}
	
	
	private List<Integer> getSelectedAppFormsId(HttpServletRequest request, String parameter) {
	
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
	
	
	private void makeErrorNoOneSelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) {
		if (0 == idList.size()) {
			request.setAttribute("nothingSelectedError", "nothingSelectedError");
			forward("error.jsp", request, response);
		}
	}
	
	
	private void makeErrorTooManySelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) {
		if (idList.size() > 1) {
			request.setAttribute("selectedCount", idList.size());
			request.setAttribute("tooManySelectedError", "tooManySelectedError");
			forward("error.jsp", request, response);
		}
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (IOException e) {
			log.error("Page - " + path + " - not found");
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
