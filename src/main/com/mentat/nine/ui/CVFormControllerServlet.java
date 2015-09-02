package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.mentat.nine.dao.CVFormDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.CVForm;

/**
 * Servlet implementation class CandidateControllerServlet
 */
@WebServlet("/cvformControllerServlet")
public class CVFormControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CVFormDAO cvDao;
       
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public CVFormControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
		cvDao = daoFactory.getCVFormDAO();
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
		request.setCharacterEncoding("UTF8");
		
		int action = checkAction(request);
		
		if (1 == action) {
			deleteCV(request, response);
		}
		if (2 == action) {
			findCV(request, response);
		}
		if (3 == action) {
			forward("/cvformBaseServlet", request, response);
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
			throws PersistException, ServletException, IOException {
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals("cvId")){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		
		if (idList.size() == 0) {
			request.setAttribute("noOneCVToDelete", "noOneCVToDelete");
			forward("error.jsp", request, response);
		}
		
		for (Integer id : idList) {
			CVForm cv = cvDao.getCVFormById(id);
			cvDao.deleteCVForm(cv);
		}
		
		forward("cvformBaseServlet", request, response);
	}
	
	

	private void findCV(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		String idParameter = request.getParameter("id");
		String ageParameter = request.getParameter("age");
		String postParameter = request.getParameter("post");
		String educationParameter = request.getParameter("education");
		String expirienceParameter = request.getParameter("expirience");
		

		if (!idParameter.equals("")) {
			List<String> queryParameters = new ArrayList<String>();
			if (idParameter.equals("�� �������")) {
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
			if (ageParameter.equals("�� �������")) {
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
			if (postParameter.equals("�� �������")) {
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
			if (educationParameter.equals("�� �������")) {
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
			if (expirienceParameter.equals("�� �������")) {
				queryParameters.add(null);
				queryParameters.add("is");
				parameters.put("work_expirience", queryParameters);
			} else {
				queryParameters.add(expirienceParameter);
				queryParameters.add(conditionConvert(request.getParameter("expirienceComparable")));
				parameters.put("work_expirience", queryParameters);
			}
		}
	
		List<CVForm> cvList = cvDao.getCVForm(parameters);
		request.setAttribute("cvIncomeList", cvList);
		forward("cvformBaseServlet", request, response);
		
	}

	
	private String conditionConvert(String condition) {
		String conditionOperator = "";
		if (condition.equals("������ ��� �����")) {
			conditionOperator = "<=";
		} else if (condition.equals("������")) {
			conditionOperator = "<";
		} else if (condition.equals("������ ��� �����")) {
			conditionOperator = ">=";
		} else if (condition.equals("������")) {
			conditionOperator = ">";
		} else {
			conditionOperator = "=";
		}
		return conditionOperator;
	}
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}