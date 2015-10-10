package ui;

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
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CandidateDAO;
import dao.DepartmentDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.Candidate;
import domain.Department;
import domain.HRDepartment;
import ui.util.WebAttributes;
import ui.util.WebPath;

/**
 * Servlet implementation class CandidateControllerServlet
 */
@WebServlet("/candidateControllerServlet")
public class CandidateControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CandidateDAO candDao;
	private DepartmentDAO depDao;
	private Properties properties;
	private DAOFactory daoFactory;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public CandidateControllerServlet() {
        super();
        daoFactory = DAOFactory.getFactory();
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
		request.setCharacterEncoding("UTF8");
		
		HttpSession session = request.getSession(false);
        Properties properties = (Properties) session.getAttribute("properties");
        this.properties = properties;
        daoFactory.setLogPath(properties);
		candDao = daoFactory.getCandidateDAO();
	    depDao = daoFactory.getDepartmentDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		int action = checkAction(request);
		
		if (1 == action) {
			deleteCandidate(request, response);
		} else if (2 == action) {
			findCandidate(request, response);
		} else if (3 == action) {
			forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
		} else if (4 == action) {
			goToHireEmployeePage(request, response);
		} else if (5 == action) {
			hireEmployee(request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {
		
		if (request.getParameter("deleteCandidate") != null) {
			return 1;
		} else if (request.getParameter("findCandidate") != null) {
			return 2;
		} else if (request.getParameter("showAllCandidates") != null) {
			return 3;
		} else if (request.getParameter("hireEmployee") != null) {
			return 4;
		} else if (request.getParameter("hireSubmitEmployee") != null) {
			return 5;
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
		
		addToParameterMap(parameters, idParameter, "id", "=", "integer");
		addToParameterMap(parameters, ageParameter, "age", ageSymbol, "integer");
		addToParameterMap(parameters, postParameter, "post", "=", "string");
		addToParameterMap(parameters, educationParameter, "education", "=", "string");
		addToParameterMap(parameters, expirienceParameter, "workExpirience", expirienceSymbol, "integer");
	
		Set<Candidate> candList = null;
		try {
			candList = candDao.getCandidates(parameters);
		} catch (PersistException e) {
			throw new ServletException();
		}
		if (null == candList) {
			candList = new HashSet<Candidate>();
		}
		request.setAttribute("candIncomeList", candList);
		forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
		
	}
	
	
	private void addToParameterMap(Map <String, List<String>> map, String parameter, 
			String field, String symbol, String type) {
		
		if (!parameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (parameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				queryParameters.add(type);
				map.put(field, queryParameters);
			} else {
				queryParameters.add(parameter);
				queryParameters.add(convertCondition(symbol));
				queryParameters.add(type);
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
			throws ServletException, IOException {
		
		List<Integer> idList = selectedItems(request);
			
		makeErrorNoOneSelectedItem(idList, request, response);
		
		try {
			for (Integer id : idList) {
				Candidate cand = candDao.getCandidateById(id);
				candDao.deleteCandidate(cand);
			}
		} catch (PersistException pe) {
			throw new ServletException();
		}
		
		forward(WebPath.CANDIDATE_BASE_PAGE_SERVLET, request, response);
	}
	
	
	private void goToHireEmployeePage (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Integer> idList = selectedItems(request);
			
		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
			
		Candidate cand = candDao.getCandidateById(idList.get(0));		
		List<Department> departments = depDao.getAllDepartments();
		setDateFields(request);
		request.setAttribute("candidate", cand);
		request.setAttribute("departments", departments);
		forward(WebPath.HR_HIRE_EMPLOYEE_JSP, request, response);
		
	}
	
	
	private void hireEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		
		HRDepartment hrDep = new HRDepartment(properties);
		boolean emptyFields = isEmptyFields(request);
		
		if (!emptyFields) {
			
			String salary = request.getParameter("salaryInput");
			String department = request.getParameter("department");
			
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("salary", salary);
			
			boolean wrongData = isWrongDataFields(intData, request);
			
			if (!wrongData) {
			
				String year = request.getParameter("year");
				String month = request.getParameter("month");
				String day = request.getParameter("day");
				boolean correctDate = checkCorrectInputDateFields(month, day);
				if (!correctDate) {
					WebAttributes.loadAttribute(request,  WebAttributes.WRONG_DATA);
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
				int parsedSalary = Integer.parseInt(salary);
				
				List<Department> departments = depDao.getAllDepartments();			
				Department parsedDepartment = null;
				for (Department dpmnt : departments) {
					if (dpmnt.getName().equals(department)) {
						parsedDepartment = dpmnt;
					}
				}
			
				String  candidateID = request.getParameter("candidateID");
				Candidate cand = candDao.getCandidateById(Integer.parseInt(candidateID));								
				try {
					hrDep.hireEmployee(cand, parsedSalary, cand.getPost(), 
							parsedDate, parsedDepartment);
				} catch (PersistException e) {
					throw new ServletException();
				}
								
				try {
					candDao.deleteCandidate(cand);
				} catch (PersistException pe) {
					throw new ServletException();
				}
				
				forward(WebPath.EMPLOYEE_BASE_PAGE_SERVLET, request, response);
			} else {
				WebAttributes.loadAttribute(request,  WebAttributes.WRONG_DATA);
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			WebAttributes.loadAttribute(request, WebAttributes.WRONG_DATA);
			forward(WebPath.ERROR_JSP, request, response);
		}	
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
