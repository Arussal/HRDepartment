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
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.exceptions.NoSuitableCandidateException;
import main.com.mentat.nine.ui.util.WebAttributes;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class ApplicationFormControllerServlet
 */
@WebServlet("/appControllerServlet")
public class ApplicationFormControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    	
	private ApplicationFormDAO appDao;
	private DAOFactory daoFactory;
	private String logPath;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationFormControllerServlet() {
        super();
        daoFactory = DAOFactory.getFactory();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
			performTask(request, response);
	}

	/**
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
        String logPath = (String) session.getAttribute("logPath");
        this.logPath = logPath;
        daoFactory.setLogPath(logPath);
        appDao = daoFactory.getApplicationFormDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		request.setCharacterEncoding("UTF8");
		
		int action = checkAction(request);
		if (1 == action) {
			goToNewAppFormPage(request, response);
		} else if (2 == action) {
			createNewApp(request, response);
		} else if (3 == action) {
			deleteApp(request, response);
		} else if (4 == action) {
			goToEditAppForm(request, response);
		} else if (5 == action) {
			editApp(request, response);
		} else {
			findNewCandidate(request, response);
		}
		
	}
	

	private int checkAction(HttpServletRequest request){
		if (request.getParameter("createApp") != null) {
			return 1;
		}  else if (request.getParameter("confirmCreateApp") != null) {
			return 2;
		} else if (request.getParameter("deleteApp") != null) {
			return 3;
		} else if (request.getParameter("editApp") != null) {
			return 4;
		} else if (request.getParameter("confirmEditApp") != null) {
			return 5;
		}
		return 0;
	}

	
	private void goToNewAppFormPage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		setDateFields(request);
		forward(WebPath.HR_NEW_APPLICATION_FORM_JSP, request, response);
	}
	
	
	private void createNewApp(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {			

		ApplicationForm appForm = getDataFromForm(request, response);
		try {
			appDao.createApplicationForm(appForm);
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		forward(WebPath.APPLICATION_BASE_PAGE_SERVLET, request, response);
		
	}

	
	private ApplicationForm getDataFromForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
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
				formattedRequirements = requirements.substring(0, requirements.length()-1);
				requirements = formattedRequirements;
			}
			
			//get data from dataFields
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String day = request.getParameter("day");
			boolean correctDate = isCorrectInputDateFields(month, day);
			if (!correctDate) {
				request.setAttribute("wrongDate", "wrongData");
				forward(WebPath.ERROR_JSP, request, response);
			}
			String date = year + "-" + month + "-" + day;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = null;
			try {
				parsedDate = dateFormat.parse(date);
			} catch (ParseException e) {
				throw new ServletException();
			}
			
			//check fields with numbers
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("age", age);
			intData.put("salary", salary);
			intData.put("workExpirience", workExpirience);
			
			boolean wrongFields = isWrongDataFields(intData, request);
			
			if (!wrongFields) {
				HRDepartment hrDep = new HRDepartment(logPath);
				
				int parsedAge = Integer.parseInt(age);
				int parsedSalary = Integer.parseInt(salary);
				int parsedWorkExpirience = Integer.parseInt(workExpirience);
				Set<String> parsedRequirements = new HashSet<String>(Arrays.asList(formattedRequirements.split(";")));
				appForm = hrDep.formApplicationForm(parsedAge, education, 
						parsedRequirements, post, parsedSalary, parsedWorkExpirience, parsedDate);
			} else {
				WebAttributes.loadAttribute(request,  WebAttributes.WRONG_DATA);
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_DATA);
			forward(WebPath.ERROR_JSP, request, response);
		}
			
		return appForm;
	}
	
	
	private void deleteApp(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Integer> idList = getSelectedAppFormsId(request, "appId");
		
		try {
			for (Integer id : idList) {
				ApplicationForm appForm = appDao.getApplicationFormById(id);
				appDao.deleteApplicationForm(appForm);
			}
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		forward(WebPath.APPLICATION_BASE_PAGE_SERVLET, request, response);
	}
	
	
	private void goToEditAppForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
	
		List<Integer> idList = getSelectedAppFormsId(request, "appId");
		
		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
		
		ApplicationForm appForm = null;
		try {
			appForm = appDao.getApplicationFormById(idList.get(0));
		} catch (PersistException e) {
			throw new ServletException();
		}
		setDateFields(request);
		request.setAttribute("app", appForm);
		forward(WebPath.HR_EDIT_APPLICATION_FORM_JSP, request, response);
	}
	
	
	private void editApp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ApplicationForm appForm = getDataFromForm(request, response);

		Integer id = Integer.parseInt(request.getParameter("id"));
		appForm.setId(id);
		try {
			appDao.updateApplicationForm(appForm);
		} catch (PersistException e) {
			throw new ServletException();
		}
		
		forward(WebPath.APPLICATION_BASE_PAGE_SERVLET, request, response);
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
		List<String> emptyFieldsList = new ArrayList<String>();
		for (String key : parameters.keySet()) {
			String val = request.getParameter(key);
			if (val.equals("")) {
				emptyFieldsList.add(key);
			}
		}
		if (emptyFieldsList.size() > 0) {
			WebAttributes.loadAttribute(request, WebAttributes.EMPTY_FIELDS_LIST);
			request.setAttribute("emptyFieldsList", emptyFieldsList);
			return true;
		}
		return false;
	}
	
	//if there is correct data in fields
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
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_DATA_FIELDS_LIST);
			request.setAttribute("wrongFieldsList", wrongFieldsList);
			return true;
		}
		return false;
	}
	

	private void findNewCandidate(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		List<Integer> idList = getSelectedAppFormsId(request, "appId");
		
		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
		
		ApplicationForm appForm;
		try {
			appForm = appDao.getApplicationFormById(idList.get(0));
		} catch (PersistException e1) {
			throw new ServletException();
		}
		HRDepartment hrDep = new HRDepartment(logPath);
		Set<Candidate> findedCandidates = null;
		try {
			findedCandidates = hrDep.findCandidates(appForm);
		} catch (NoSuitableCandidateException e) {
			WebAttributes.loadAttribute(request, WebAttributes.NO_NEW_CANDIDATE);
			forward(WebPath.ERROR_JSP, request, response);
		} catch (PersistException pe) {
			throw new ServletException();
		}
		
		request.setAttribute("candidates", findedCandidates);
		forward(WebPath.HR_NEW_CANDIDATE_JSP, request, response);
			
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
