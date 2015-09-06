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

import main.com.mentat.nine.dao.DepartmentDAO;
import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.domain.util.LogConfig;

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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			forward("employeeBaseServlet", request, response);
		} else if (3 == action) {
			editEmployee(request, response);
		} else {
			fireEmployee(request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {
		if (request.getParameter("findEmployee") != null) {
			return 1;
		} else if (request.getParameter("showAllEmployees") != null) {
			return 2;
		} else if ((request.getParameter("editEmployee") != null) || 
				(request.getParameter("edit") != null)) {
			return 3;
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
		
		if (!idParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (idParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("id", queryParameters);
			} else {
				queryParameters.add(idParameter);
				queryParameters.add("=");
				parameters.put("id", queryParameters);
			}
		}

		if (!ageParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (ageParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("age", queryParameters);
			} else {
				queryParameters.add(ageParameter);
				queryParameters.add(convertCondition(request.getParameter("ageComparable")));
				parameters.put("age", queryParameters);
			}
		}

		if (!postParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (postParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("post", queryParameters);
			} else {
				queryParameters.add(postParameter);
				queryParameters.add(convertCondition("="));
				parameters.put("post", queryParameters);
			}
		}

		if (!educationParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (educationParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("education", queryParameters);
			} else {
				queryParameters.add(educationParameter);
				queryParameters.add(convertCondition("="));
				parameters.put("education", queryParameters);
			}
		}

		if (!expirienceParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (expirienceParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("work_expirience", queryParameters);
			} else {
				queryParameters.add(expirienceParameter);
				queryParameters.add(convertCondition(request.getParameter("expirienceComparable")));
				parameters.put("work_expirience", queryParameters);
			}
		}
		
		if (!departmentParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (departmentParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("department", queryParameters);
			} else {
				queryParameters.add(departmentParameter);
				queryParameters.add(convertCondition("="));
				parameters.put("department", queryParameters);
			}
		}
		
		if (!salaryParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (salaryParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("salary", queryParameters);
			} else {
				queryParameters.add(salaryParameter);
				queryParameters.add(convertCondition(request.getParameter("salaryComparable")));
				parameters.put("salary", queryParameters);
			}
		}
		
		if (!hireDateParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (expirienceParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("hireDate", queryParameters);
			} else {
				queryParameters.add(hireDateParameter);
				queryParameters.add(convertCondition(request.getParameter("hireDateComparable")));
				parameters.put("hireDate", queryParameters);
			}
		}
		
		if (!fireDateParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (expirienceParameter.equals("не указано")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("fireDate", queryParameters);
			} else {
				queryParameters.add(fireDateParameter);
				queryParameters.add(convertCondition(request.getParameter("fireDateComparable")));
				parameters.put("fireDate", queryParameters);
			}
		}
	
		Set<Employee> employees = empDao.getEmployees(parameters);
		request.setAttribute("empIncomeList", employees);
		forward("employeeBaseServlet", request, response);
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
		
		if (request.getParameter("editEmployee") != null) {
			List<Integer> idList = getSelectedItems(request);

			makeErrorNoOneSelectedItem(idList, request, response);
			makeErrorTooManySelectedItem(idList,request, response);
			
			List<Department> departments = depDao.getAllDepartments();
			request.setAttribute("departments", departments);
			Employee employee = empDao.getEmployeeById(idList.get(0));
			request.setAttribute("emp", employee);
			forward("edit_employee.jsp", request, response); 
		}
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		Employee nonUpdatedEmployee = empDao.getEmployeeById(id);
		Employee updatedEmployee = getDataFromForm(request, response);
		
		updatedEmployee.setId(id);
		updatedEmployee.setName(nonUpdatedEmployee.getName());
		empDao.updateEmployee(updatedEmployee);
		
		forward("applicationBaseServlet", request, response);
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
				
		boolean isEmptyFields = checkEmptyFields(request);

		String age = request.getParameter("age");
		String education = request.getParameter("education");
		String post = request.getParameter("post");
		String department = request.getParameter("department");
		String salary = request.getParameter("salary");
		String workExpirience = request.getParameter("expirience");
		String hireDate = request.getParameter("hireDate");
		String fireDate = request.getParameter("fireDate");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String skills = request.getParameter("skills");
		
		Map<String, String> intData = new HashMap<String, String>();
		intData.put("age", age);
		intData.put("salary", salary);
		intData.put("hireDate", workExpirience);
		intData.put("fireDate", workExpirience);
		intData.put("workExpirience", workExpirience);
		
		boolean isWrongData = checkWrongDataFields(intData, request);
		
		if (isEmptyFields | isWrongData) {
			request.setAttribute("wrongData", "wrongData");
			forward("error.jsp", request, response);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedHireDate = null;
		Date parsedFireDate = null;
		try {
			parsedHireDate = sdf.parse(hireDate);
			parsedFireDate = sdf.parse(fireDate);
		} catch (ParseException e) {
			log.warn("can't parse Form's date");
		}
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
		
		Employee employee = new Employee();
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
		
		return employee;
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
		if (emptyParameters.size() > 1) {
			if (request.getParameter("fireDate") != null) {
				request.setAttribute("emptyFields", emptyParameters);
				return true;
			}
		}
		return false;
	}

	
	private boolean checkWrongDataFields(Map<String, String> map, HttpServletRequest request) {
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
			forward("fire_employee.jsp", request, response);
		}
		
		List<Integer> idList = getSelectedItems(request);
		makeErrorNoOneSelectedItem(idList, request, response);
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String day = request.getParameter("day");
		boolean correctDate = checkCorrectInputDateFields(month, day);
		if (!correctDate) {
			request.setAttribute("wrongData", "wrongData");
			forward("error.jsp", request, response);
		}
		String date = year + "-" + month + "-" + day;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fireDate = null;
		try {
			fireDate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<Employee> empList = getSelectedEmployees(idList);
		for (Employee employee : empList) {
			employee.setFireDate(fireDate);
			empDao.updateEmployee(employee);
		}
		forward("employeeBaseServlet", request, response);
		
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
		
		request.setAttribute("years", years);
		request.setAttribute("months", months);
		request.setAttribute("days", days);
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
			forward("error.jsp", request, response);
		}
	}
	
	private void makeErrorTooManySelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if (idList.size() > 1) {
			request.setAttribute("countToEdit", idList.size());
			request.setAttribute("tooManySelectedError", "tooManySelectedError");
			forward("error.jsp", request, response);
		}
	}
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}

}
