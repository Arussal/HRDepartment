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
import java.util.LinkedHashSet;
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

import main.com.mentat.nine.dao.DepartmentDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.domain.util.LogConfig;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class EmployeeControllerServlet
 */
@WebServlet("/employeeControllerServlet")
public class EmployeeControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(CandidateControllerServlet.class);
	
	private EmployeeDAO empDao;
	private DepartmentDAO depDao;
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        empDao = daoFactory.getEmployeeDAO();
        depDao = daoFactory.getDepartmentDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			// TODO logger
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			// TODO logger
			e.printStackTrace();
		}
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		int action = checkAction(request);
		
		if (1 == action) {
			findEmployee(request, response);
		} else if (2 == action) {
			forward(WebPath.EMPLOYEE_BASE_PAGE_SERVLET, request, response);
		} else if (3 == action) {
			editEmployee(request, response);
		} else if (4 == action) {
			saveEmployeeChanges(request, response);
		} else {
			fireEmployee(request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {
		if (request.getParameter("findEmployee") != null) {
			return 1;
		} else if (request.getParameter("showAllEmployees") != null) {
			return 2;
		} else if (request.getParameter("editEmployee") != null) {
			return 3;
		} else if (request.getParameter("saveEmployeeChanges") != null) {
			return 4;
		}
		return 0;
	}
	
	
	private void findEmployee(HttpServletRequest request, HttpServletResponse response)
			throws PersistException, ServletException, IOException {

		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		String idParameter = request.getParameter("id");
		String ageParameter = request.getParameter("age");
		String postParameter = request.getParameter("post");
		String educationParameter = request.getParameter("education");
		String expirienceParameter = request.getParameter("expirience");
		String departmentParameter = request.getParameter("department");
		String salaryParameter = request.getParameter("salary");
		String hireDateParameter = request.getParameter("hireDate");
		String fireDateParameter = request.getParameter("fireDate");
		
		String ageSymbol = request.getParameter("ageComparable");
		String expirienceSymbol = request.getParameter("expirienceComparable");
		String salarySymbol = request.getParameter("salaryComparable");
		String hireDateSymbol = request.getParameter("hireDateComparable");
		String fireDateSymbol = request.getParameter("fireDateComparable");
		
		Department department = depDao.getDepartmentByName(departmentParameter);
	
		
		addToParameterMap(parameters, idParameter, "id", "=");
		addToParameterMap(parameters, ageParameter, "id", ageSymbol);
		addToParameterMap(parameters, postParameter, "post", "=");
		addToParameterMap(parameters, educationParameter, "education", "=");
		addToParameterMap(parameters, expirienceParameter, "work_expirience", expirienceSymbol);
		addToParameterMap(parameters, String.valueOf(department.getId()), "id_department", "=");
		addToParameterMap(parameters, salaryParameter, "salary", salarySymbol);
		addToParameterMap(parameters, hireDateParameter, "hireDate", hireDateSymbol);
		addToParameterMap(parameters, fireDateParameter, "fireDate", fireDateSymbol);
	
		Set<Employee> employees = empDao.getEmployees(parameters);
		request.setAttribute("empIncomeList", employees);
		forward(WebPath.EMPLOYEE_BASE_PAGE_SERVLET, request, response);
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


	private void editEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {

		List<Integer> idList = getSelectedItems(request);

		makeErrorNoOneSelectedItem(idList, request, response);
		makeErrorTooManySelectedItem(idList,request, response);
			
		List<Department> departments = depDao.getAllDepartments();
		request.setAttribute("departments", departments);
		Employee employee = empDao.getEmployeeById(idList.get(0));
		setDateFields(request);
		request.setAttribute("emp", employee);
		forward(WebPath.HR_EDIT_EMPLOYEE_JSP, request, response); 
	}
	
	
	public void saveEmployeeChanges(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		Employee nonUpdatedEmployee = empDao.getEmployeeById(id);
		Employee updatedEmployee = getDataFromForm(request, response);
		
		updatedEmployee.setId(id);
		updatedEmployee.setName(nonUpdatedEmployee.getName());
		empDao.updateEmployee(updatedEmployee);
				
		forward(WebPath.EMPLOYEE_BASE_PAGE_SERVLET, request, response);
	}
	
	
	private List<Integer> getSelectedItems(ServletRequest request){
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals("empId")){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		return idList;
	}
	
	
	private Employee getDataFromForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {
				
		Employee employee = new Employee();
		boolean emptyFields = isEmptyFields(request);
		
		if (!emptyFields) {
			String age = request.getParameter("age");
			String education = request.getParameter("education");
			String post = request.getParameter("post");
			String department = request.getParameter("department");
			String salary = request.getParameter("salary");
			String workExpirience = request.getParameter("expirience");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
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
			
			//get Hire Date from date Fields
			String yearHire = request.getParameter("yearHire");
			String monthHire = request.getParameter("monthHire");
			String dayHire = request.getParameter("dayHire");
			boolean correctHireDate = isCorrectInputDateFields(monthHire, dayHire);
			if (!correctHireDate) {
				request.setAttribute("wrongDate", "wrongData");
				forward(WebPath.ERROR_JSP, request, response);
			}
			String hireDate = yearHire + "-" + monthHire + "-" + dayHire;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedHireDate = null;
			try {
				parsedHireDate = dateFormat.parse(hireDate);
			} catch (ParseException e) {
				log.error("can't parse date from Form");
			}
			
			//get Fire Date from date Fields
			String year = request.getParameter("yearFire");
			String month = request.getParameter("monthFire");
			String day = request.getParameter("dayFire");
			boolean correctFireDate = isCorrectInputDateFields(month, day);
			if (!correctFireDate) {
				request.setAttribute("wrongDate", "wrongData");
				forward(WebPath.ERROR_JSP, request, response);
			}
			String fireDate = year + "-" + month + "-" + day;
			Date parsedFireDate = null;
			try {
				parsedFireDate = dateFormat.parse(fireDate);
			} catch (ParseException e) {
				log.error("can't parse date from Form");
			}
			
			
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("age", age);
			intData.put("salary", salary);
			intData.put("workExpirience", workExpirience);
			
			boolean wrongData = isWrongDataFields(intData, request);
			
			if (!wrongData) {
			
				int parsedAge = Integer.parseInt(age);
				int parsedSalary = Integer.parseInt(salary);
				int parsedWorkExpirience = Integer.parseInt(workExpirience);
				
				Department parsedDepartment = null;
				List<Department> departments = depDao.getAllDepartments();
				for (Department dep : departments) {
					if (dep.getName().equals(department)) {
						parsedDepartment = dep;
					}
				}

				Set<String> parsedSkills = new HashSet<String>(Arrays.asList(skills.split(";")));
				employee.setAge(parsedAge);
				employee.setDepartment(parsedDepartment);
				employee.setEducation(education);
				employee.setEmail(email);
				employee.setFireDate(parsedFireDate);
				employee.setHireDate(parsedHireDate);
				employee.setPhone(phone);
				employee.setPost(post);
				employee.setSalary(parsedSalary);
				employee.setWorkExpirience(parsedWorkExpirience);
				employee.setSkills(parsedSkills);
			
			} else {
				request.setAttribute("wrongFields", "wrongFields");
				forward(WebPath.ERROR_JSP, request, response);
			}
		} else {
			
			request.setAttribute("emptyFields", "emptyFields");
			forward(WebPath.ERROR_JSP, request, response);
		}
		
		return employee;
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
			for (String emptyParameter : emptyParameters) {
				if (!emptyParameter.endsWith("Fire")) {
					request.setAttribute("emptyFields", emptyParameters);
					return true;
				}
			}
		}
		return false;
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
	
	
	private boolean isWrongDataFields(Map<String, String> map, HttpServletRequest request) {
		List<String> wrongFields = new ArrayList<String>();
		for (String data : map.keySet()) {
			if (data.equals("fireDate") || data.equals("hireDate")) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					dateFormat.parse(map.get(data));
				} catch (Exception e) {
					wrongFields.add(data);
				}
			} else {
				try {
					Integer.parseInt(map.get(data));
				} catch (NumberFormatException e){
					wrongFields.add(data);
				}
			}
		}
		if (wrongFields.size() > 0) {
			request.setAttribute("wrongFields", wrongFields);
			return true;
		}
		return false;
	}
	

	private void fireEmployee(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, PersistException {
		
		if (request.getParameter("fireEmployee") != null) {
			List<Integer> idList = getSelectedItems(request);
			makeErrorNoOneSelectedItem(idList, request, response);
			Set<Employee> empList = getSelectedEmployees(idList);
			setDateFields(request);
			request.setAttribute("empList", empList);
			forward(WebPath.HR_FIRE_EMPLOYEE_JSP, request, response);
		}
		
		List<Integer> idList = getSelectedItems(request);
		makeErrorNoOneSelectedItem(idList, request, response);
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
		Date fireDate = null;
		try {
			fireDate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO logger
			e.printStackTrace();
		}
		Set<Employee> empList = getSelectedEmployees(idList);
		for (Employee employee : empList) {
			employee.setFireDate(fireDate);
			empDao.updateEmployee(employee);
		}
		forward(WebPath.EMPLOYEE_BASE_PAGE_SERVLET, request, response);
		
	}
	
	
	private Set<Employee> getSelectedEmployees(List<Integer> idList) 
			throws PersistException {
		Set<Employee> empList = new HashSet<Employee>();
		for (int i = 0; i < idList.size(); i++) {
			Employee employee = empDao.getEmployeeById(idList.get(i));
			empList.add(employee);
		}
		return empList;
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
		
		Set<String> yearSet = new LinkedHashSet<String>();
		Set<String> monthSet = new LinkedHashSet<String>();
		Set<String> daySet = new LinkedHashSet<String>();
		
		yearSet.add("");
		monthSet.add("");
		daySet.add("");
		
		for (Integer year : years) {
			yearSet.add(year.toString());
		}
		for (Integer month : months) {
			monthSet.add(month.toString());
		}
		for (Integer day : days) {
			daySet.add(day.toString());
		}
		
		request.setAttribute("years", yearSet);
		request.setAttribute("months", monthSet);
		request.setAttribute("days", daySet);
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
