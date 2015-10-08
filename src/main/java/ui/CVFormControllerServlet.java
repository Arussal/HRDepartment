package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CVFormManagerDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.CVFormManager;
import ui.util.WebAttributes;
import ui.util.WebPath;

/**
 * Servlet implementation class CandidateControllerServlet
 */
@WebServlet("/cvformControllerServlet")
public class CVFormControllerServlet extends HttpServlet {
 	
	private static final long serialVersionUID = 1L;
	private CVFormManagerDAO cvDao;
	private DAOFactory daoFactory;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public CVFormControllerServlet() throws ServletException {
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
		request.setCharacterEncoding("UTF8");
		
		HttpSession session = request.getSession(false);
        Properties properties = (Properties) session.getAttribute("properties");
        daoFactory.setLogPath(properties);
		cvDao = daoFactory.getCVFormDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		int action = checkAction(request);
		
		if (1 == action) {
			deleteCV(request, response);
		}
		if (2 == action) {
			findCV(request, response);
		}
		if (3 == action) {
			forward(WebPath.CVFORM_BASE_PAGE_SERVLET, request, response);
		}
	}


	private int checkAction(HttpServletRequest request) {
		if (request.getParameter("deleteCV") != null) {
			return 1;
		} 
		if (request.getParameter("findCV") != null) {
			return 2;
		}
		if (request.getParameter("showAllCV") != null) {
			return 3;
		}
		return 0;
	}
	
	
	private void deleteCV(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals("cvId")){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		
		makeErrorNoOneSelectedItem(idList, request, response);
		
		for (Integer id : idList) {
			CVFormManager cv = cvDao.getCVFormById(id);
			try {
				cvDao.deleteCVForm(cv);
			} catch (PersistException e) {
				throw new ServletException();
			}
		}
		
		forward(WebPath.CVFORM_BASE_PAGE_SERVLET, request, response);
	}
	
	

	private void findCV(HttpServletRequest request, HttpServletResponse response) 
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
	
		List<CVFormManager> cvList = cvDao.getCVForm(parameters);	
		if (null == cvList) {
			cvList = new ArrayList<CVFormManager>();
		}
		request.setAttribute("cvIncomeList", cvList);
		forward(WebPath.CVFORM_BASE_PAGE_SERVLET, request, response);		
	}
	
	
	private void addToParameterMap(Map <String, List<String>> map, 
			String parameter, String field, String symbol) {
		
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
	
	
	private void makeErrorNoOneSelectedItem(List<Integer> idList, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if (0 == idList.size()) {
			WebAttributes.loadAttribute(request, WebAttributes.NO_ONE_ITEM_SELECTED);
			forward(WebPath.ERROR_JSP, request, response);
		}
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
