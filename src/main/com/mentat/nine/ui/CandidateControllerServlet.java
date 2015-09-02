package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Candidate;

/**
 * Servlet implementation class CandidateControllerServlet
 */
@WebServlet("/candidateControllerServlet")
public class CandidateControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CandidateDAO candDao;
       
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public CandidateControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
        candDao = daoFactory.getCandidateDAO();
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
		}
		if (2 == action) {
			findCandidate(request, response);
		}
		if (3 == action) {
			forward("/candidateBaseServlet", request, response);
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
		
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals("candId")){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		
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
		
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
