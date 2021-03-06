package main.com.mentat.nine.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.com.mentat.nine.dao.EmployeeDAO;
import main.com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Department;
import main.com.mentat.nine.domain.Employee;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class EmployeeBasePageServlet
 */
@WebServlet("/employeeBaseServlet")
public class EmployeeBasePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeDAO empDao;
	private DAOFactory daoFactory;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeBasePageServlet() throws ServletException {
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

	
	@SuppressWarnings("unchecked")
	private void performTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
        Properties properties = (Properties) session.getAttribute("properties");
        daoFactory.setLogPath(properties);
		empDao = daoFactory.getEmployeeDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		Set<Employee> empList = null;
		if (request.getAttribute("empIncomeList") != null) {
			empList = (Set<Employee>)request.getAttribute("empIncomeList");
			createFilterFinder(request, empList);
		} 
		else {
			empList = getAllEmployees();
			createFilterFinder(request, empList);
		}
		request.setAttribute("empList", empList);
		forward(WebPath.HR_EMPLOYEES_JSP, request, response);
	}

	
	private Set<Employee> getAllEmployees() throws ServletException {
		Set<Employee> employees;
		try {
			employees = empDao.getAllEmployees();
		} catch (PersistException e) {
			throw new ServletException();
		}
		return employees;
	}

	
	private void createFilterFinder(HttpServletRequest request, Set<Employee> employees) 
			throws ServletException  {
		
		List<String> comparableList = new ArrayList<String>();
		comparableList.add("������ ��� �����");
		comparableList.add("������");
		comparableList.add("�����");
		comparableList.add("������");
		comparableList.add("������ ��� �����");
		
		Set<Integer> originIDSet = new TreeSet<Integer>();
		Set<Integer> originAgeSet = new TreeSet<Integer>();
		Set<Integer> originExpirienceSet = new TreeSet<Integer>();
		Set<Integer> originSalarySet = new TreeSet<Integer>();
		
		//form drop-down lists for filter by category
		Set<String> idEmployeeSet = new LinkedHashSet<String>();
		Set<String> ageEmployeeSet = new LinkedHashSet<String>();
		Set<String> postEmployeeSet = new TreeSet<String>();
		Set<String> departmentEmployeeSet = new TreeSet<String>();
		Set<String> educationEmployeeSet = new TreeSet<String>();
		Set<String> salaryEmployeeSet = new TreeSet<String>();
		Set<String> expirienceEmployeeSet = new LinkedHashSet<String>();
		Set<String> hireDateEmployeeSet = new TreeSet<String>();
		Set<String> fireDateEmployeeSet = new TreeSet<String>();
		
		idEmployeeSet.add("");
		ageEmployeeSet.add("");
		postEmployeeSet.add("");
		educationEmployeeSet.add("");
		expirienceEmployeeSet.add("");
		departmentEmployeeSet.add("");
		salaryEmployeeSet.add("");
		hireDateEmployeeSet.add("");
		fireDateEmployeeSet.add("");		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Employee employee : employees) {
			if (employee.getId() == null) {
				idEmployeeSet.add(" �� �������");
			} else {
				originIDSet.add(employee.getId());
			}
			if (employee.getAge() == null) {
				ageEmployeeSet.add(" �� �������");
			} else {
				originAgeSet.add(employee.getAge());
			}
			if (employee.getPost() == null) {
				postEmployeeSet.add(" �� �������");
			} else {
				postEmployeeSet.add(employee.getPost());
			}
			if (employee.getEducation() == null) {
				educationEmployeeSet.add(" �� �������");
			} else {
				educationEmployeeSet.add(employee.getEducation());
			}
			if (employee.getWorkExpirience() == null) {
				expirienceEmployeeSet.add(" �� �������");
			} else {
				originExpirienceSet.add(employee.getWorkExpirience());
			} 
			
			Department department;
			try {
				department = employee.getDepartment();
			} catch (PersistException e) {
				throw new ServletException();
			}
			
			if (department == null) {
				departmentEmployeeSet.add(" �� �������");
			} else {
				String name = department.getName();
				departmentEmployeeSet.add(name);
			}			
			if (employee.getSalary() == null) {
				salaryEmployeeSet.add(" �� �������");
			} else {
				originSalarySet.add(employee.getSalary());
			}
			if (employee.getHireDate() == null) {
				hireDateEmployeeSet.add(" �� �������");
			} else {
				hireDateEmployeeSet.add(dateFormat.format(employee.getHireDate()));
			}
			if (employee.getFireDate() == null) {
				fireDateEmployeeSet.add(" �� �������");
			} else {
				fireDateEmployeeSet.add(dateFormat.format(employee.getFireDate()));
			}
		}
		
		for (Integer item : originIDSet) {
			idEmployeeSet.add(String.valueOf(item));
		}
		for (Integer item : originAgeSet) {
			ageEmployeeSet.add(String.valueOf(item));
		}
		for (Integer item : originExpirienceSet) {
			expirienceEmployeeSet.add(String.valueOf(item));
		}
		for (Integer item : originSalarySet) {
			salaryEmployeeSet.add(String.valueOf(item));
		}
		
		request.setAttribute("comparableList", comparableList);
		request.setAttribute("idEmployeeSet", idEmployeeSet);
		request.setAttribute("ageEmployeeSet", ageEmployeeSet);
		request.setAttribute("postEmployeeSet", postEmployeeSet);
		request.setAttribute("educationEmployeeSet", educationEmployeeSet);
		request.setAttribute("expirienceEmployeeSet", expirienceEmployeeSet);
		request.setAttribute("departmentEmployeeSet", departmentEmployeeSet);
		request.setAttribute("salaryEmployeeSet", salaryEmployeeSet);
		request.setAttribute("hireDateEmployeeSet", hireDateEmployeeSet);
		request.setAttribute("fireDateEmployeeSet", fireDateEmployeeSet);
	}
	
	
	private void forward(String path, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
