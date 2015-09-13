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
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.DepartmentDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.util.LogConfig;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class CandidateControllerServlet
 */
@WebServlet("/candidateControllerServlet")
public class CandidateControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(CandidateControllerServlet.class);
	
	private CandidateDAO candDao;
	private DepartmentDAO depDao;
	private EmployeeDAO empDao;
       
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public CandidateControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        candDao = daoFactory.getCandidateDAO();
        depDao = daoFactory.getDepartmentDAO();
        empDao = daoFactory.getEmployeeDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			log.error("doGet error");
		}
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			deleteCandidate(request, response);
		} else if (2 == action) {
			findCandidate(request, response);
		} else if (3 == action) {
			forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
		} else {
			hireEmployee(request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {
		
		if (request.getParameter("deleteCandidate") != null) {
			return 1;
		} 
		if (request.getParameter("findCandidate") != null) {
			return 2;
		}
		if (request.getParameter("showAllCandidates") != null) {
			return 3;
		}
		return 0;
	}
	

	private void findCandidate(HttpServletRequest request, 	HttpServletResponse response) 
			throws ServletException, IOException {
		
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		String idParameter = request.getParameter("id");
		String ageParameter = request.getParameter("age");
		String postParameter = request.getParameter("post");
		String educationParameter = request.getParameter("education");
		String expirienceParameter = request.getParameter("expirience");
		
		String ageSymbol = request.getParameter("ageComparable");
		String expirienceSymbol = request.getParameter("expirienceComparable");
		
		addToParameterMap(parameters, idParameter, "id", "=");
		addToParameterMap(parameters, ageParameter, "age", ageSymbol);
		addToParameterMap(parameters, postParameter, "post", "=");
		addToParameterMap(parameters, educationParameter, "education", "=");
		addToParameterMap(parameters, expirienceParameter, "work_expirience", expirienceSymbol);
	
		Set<Candidate> candList = null;
		try {
			candList = candDao.getCandidates(parameters);
		} catch (PersistException e) {
			log.warn("no one Candidate with different query parameters found");
		}
		if (null == candList) {
			candList = new HashSet<Candidate>();
		}
		request.setAttribute("candIncomeList", candList);
		forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
		
	}
	
	
	private void addToParameterMap(Map <String, List<String>> map, String parameter, String field, String symbol) {
		
		if (!parameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (parameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				map.put(field, queryParameters);
			} else {
				queryParameters.add(parameter);
				queryParameters.add(convertCondition(symbol));
				map.put(field, queryParameters);
			}
		}
	}

	
	private String convertCondition(String condition) {
		String conditionOperator = "";
		if (condition.equals("меньше или равно")) {
			conditionOperator = "<=";
		} else if (condition.equals("меньше")) {
			conditionOperator = "<";
		} else if (condition.equals("больше или равно")) {
			conditionOperator = ">=";
		} else if (condition.equals("больше")) {
			conditionOperator = ">";
		} else {
			conditionOperator = "=";
		}
		return conditionOperator;
	}
	
	
	private void deleteCandidate(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, NumberFormatException, ServletException, IOException {
		
		List<Integer> idList = selectedItems(request);
			
		makeErrorNoOneSelectedItem(idList, request, response);
		
		for (Integer id : idList) {
			Candidate cand = candDao.getCandidateById(id);
			candDao.deleteCandidate(cand);
		}
		
		forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
	}
		
	
	private void hireEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		List<Department> departments = depDao.getAllDepartments();
		
		if (request.getParameter("hireCandidate") != null) {
			List<Integer> idList = selectedItems(request);
			HRDepartment hrDep = new HRDepartment();
			
			makeErrorNoOneSelectedItem(idList, request, response);
			makeErrorTooManySelectedItem(idList,request, response);
			
			Candidate cand = candDao.getCandidateById(idList.get(0));
			
			boolean isEmptyFields = checkEmptyFields(request);
			
			String salary = request.getParameter("salaryInput");
			String department = request.getParameter("department");
			
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("salary", salary);
			
			boolean isWrongData = checkWrongDataFields(intData, request);
			
			if (isEmptyFields | isWrongData) {
				request.setAttribute("wrongData", "wrongData");
				forward(WebPath.ERROR_JSP, request, response);
			}
			
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String day = request.getParameter("day");
			boolean correctDate = checkCorrectInputDateFields(month, day);
			if (!correctDate) {
				request.setAttribute("wrongData", "wrongData");
				forward(WebPath.ERROR_JSP, request, response);
			}
			String date = year + "-" + month + "-" + day;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = null;
			try {
				parsedDate = dateFormat.parse(date);
			} catch (ParseException e) {
				// TODO logger
				e.printStackTrace();
			}
			int parsedSalary = Integer.parseInt(salary);
			
			Department parsedDepartment = null;
			for (Department dpmnt : departments) {
				if (dpmnt.getName().equals(department)) {
					parsedDepartment = dpmnt;
				}
			}
			
			Employee employee = hrDep.hireEmployee(cand, parsedSalary, cand.getPost(), 
					parsedDate, parsedDepartment);
			empDao.createEmployee(employee);
			candDao.deleteCandidate(cand);
			forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
		}
		
		setDateFields(request);
		request.setAttribute("departments", departments);
		forward(WebPath.HR_HIRE_EMPLOYEE_JSP, request, response);
		
	}


	private boolean checkCorrectInputDateFields(String month, String day) {
		String[] shortMonths = {"2", "4", "6", "9", "11"};
		List<String> shortM = Arrays.asList(shortMonths);
		if (shortM.contains(month)) {
			if (Integer.parseInt(day) == 31) {
				return false;
			}
		}
		return true;
	}

	private boolean checkEmptyFields(HttpServletRequest request) {
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

	
	private boolean checkWrongDataFields(Map<String, String> map, HttpServletRequest request) {
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
	
	
	private List<Integer> selectedItems(ServletRequest request){
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals("candId")){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		return idList;
	}
	
	
	private void setDateFields(HttpServletRequest request) {
		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();
		List<Integer> days = new ArrayList<Integer>();
		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 1950; i < currentYear + 1; i++) {
			years.add(i);
		}
		for (int i = 1; i < 13; i++) {
			months.add(i);
		}
		for (int i = 1; i < 32; i++) {
			days.add(i);
		}
		
		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
	}
	
	
	private void makeErrorNoOneSelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if (0 == idList.size()) {
			request.setAttribute("nothingSelectedError", "nothingSelectedError");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	private void makeErrorTooManySelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if (idList.size() > 1) {
			request.setAttribute("selectedCount", idList.size());
			request.setAttribute("tooManySelectedError", "tooManySelectedError");
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
