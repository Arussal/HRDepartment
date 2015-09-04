package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.Employee;

/**
 * Servlet implementation class EmployeeControllerServlet
 */
@WebServlet("/employeeControllerServlet")
public class EmployeeControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private EmployeeDAO empDao;
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        empDao = daoFactory.getEmployeeDAO();
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
		}
	}


	private int checkAction(HttpServletRequest request) {
		if (request.getParameter("findEmployee") != null) {
			return 1;
		} else if (request.getParameter("showAllEmployees") != null) {
			return 2;
		} else if (request.getParameter("editEmployee") != null) {
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
				queryParameters.add(conditionConvert(request.getParameter("ageComparable")));
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
				queryParameters.add(conditionConvert("="));
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
				queryParameters.add(conditionConvert("="));
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
				queryParameters.add(conditionConvert(request.getParameter("expirienceComparable")));
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
				queryParameters.add(conditionConvert("="));
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
				queryParameters.add(conditionConvert(request.getParameter("salaryComparable")));
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
				queryParameters.add(conditionConvert(request.getParameter("hireDateComparable")));
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
				queryParameters.add(conditionConvert(request.getParameter("fireDateComparable")));
				parameters.put("fireDate", queryParameters);
			}
		}
	
		Set<Employee> employees = empDao.getEmployees(parameters);
		request.setAttribute("empIncomeList", employees);
		forward("employeeBaseServlet", request, response);
	}

	
	private String conditionConvert(String condition) {
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


	private void editEmployee(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("editEmployee") != null) {
			List<Integer> idList = selectedItems(request);

			if (0 == idList.size()) {
				request.setAttribute("nothingToEditError", "nothingToEditError");
				forward("error.jsp", request, response);
			} else if (idList.size() > 1) {
				request.setAttribute("countToEdit", idList.size());
				request.setAttribute("tooMuchToEditError", "tooMuchToEditError");
				forward("error.jsp", request, response);
			}
			
			Employee employee = empDao.getEmployeeById(idList.get(0));
			request.setAttribute("emp", employee);
			forward("edit_employee.jsp", request, response); //create jsp
		}
		//finish method
		Employee employee = getDataFromForm(request, response);
		Integer id = Integer.parseInt(request.getParameter("id"));
		appForm.setId(id);
		appDao.updateApplicationForm(appForm);
		
		forward("applicationBaseServlet", request, response);
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
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}

}
