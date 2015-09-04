package main.com.mentat.nine.ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
			throws ServletException, IOException, PersistException {
		request.setCharacterEncoding("UTF8");
		
		int action = checkAction(request);
		
		if (1 == action) {
			deleteCandidate(request, response);
		} else if (2 == action) {
			findCandidate(request, response);
		} else if (3 == action) {
			forward("/candidateBaseServlet", request, response);
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
			throws ServletException, IOException, PersistException {
		
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		String idParameter = request.getParameter("id");
		String ageParameter = request.getParameter("age");
		String postParameter = request.getParameter("post");
		String educationParameter = request.getParameter("education");
		String expirienceParameter = request.getParameter("expirience");
		

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
	
		Set<Candidate> candList = candDao.getCandidates(parameters);
		request.setAttribute("candIncomeList", candList);
		forward("candidateBaseServlet", request, response);
		
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
	
	
	private void deleteCandidate(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, NumberFormatException, ServletException, IOException {
		
		List<Integer> idList = selectedItems(request);
		
		if (idList.size() == 0) {
			request.setAttribute("noOneCandidateToDelete", "noOneCandidateToDelete");
			forward("error.jsp", request, response);
		}
		
		for (Integer id : idList) {
			Candidate cand = candDao.getCandidateById(id);
			candDao.deleteCandidate(cand);
		}
		
		forward("candidateBaseServlet", request, response);
	}
		
	
	private void hireEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		List<Department> departments = depDao.getAllDepartments();
		
		if (request.getParameter("hireCandidate") != null) {
			List<Integer> idList = selectedItems(request);
			HRDepartment hrDep = new HRDepartment();
			
			if (idList.size() == 0) {
				request.setAttribute("noOneCandidateToHire", "noOneCandidateToHire");		//
				forward("error.jsp", request, response);
			} 
			if (idList.size() > 1) {
				request.setAttribute("noOneCandidateToHire", "noOneCandidateToHire");		//
				forward("error.jsp", request, response);
			}
			
			Candidate cand = candDao.getCandidateById(idList.get(0));
			
			boolean isEmptyFields = checkEmptyFields(request);
			
			String salary = request.getParameter("salaryInput");
			String hireDate = request.getParameter("dateInput");
			String department = request.getParameter("department");
			
			Map<String, String> intData = new HashMap<String, String>();
			intData.put("salary", salary);
			
			boolean isWrongData = checkWrongDataFields(intData, request);
			
			if (isEmptyFields | isWrongData) {
				request.setAttribute("wrongData", "wrongData");
				forward("error.jsp", request, response);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = null;
			try {
				parsedDate = sdf.parse(hireDate);
			} catch (ParseException e) {
				log.warn("can't parse Form's date");
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
			forward("candidateBaseServlet", request, response);
		}
		
		request.setAttribute("departments", departments);
		forward("hire_employee.jsp", request, response);
		
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
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
