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
		
		for (Integer id : idList) {
			CVForm cv = cvDao.getCVFormById(id);
			cvDao.deleteCVForm(cv);
		}
		
		forward("cvformBaseServlet", request, response);
	}
	
	

	private void findCV(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		String idParameter = request.getParameter("id");
		String ageParameter = request.getParameter("age");
		String postParameter = request.getParameter("post");
		String educationParameter = request.getParameter("education");
		String expirienceParameter = request.getParameter("expirience");
		

		if (!idParameter.equals("")) {
			if (idParameter.equals("не указано")) {
				parameters.put("id", null);
			} else {
				parameters.put("id", idParameter);
			}
		}

		if (!ageParameter.equals("")) {
			if (ageParameter.equals("не указано")) {
				parameters.put("age", null);
			} else {
				parameters.put("age", ageParameter);
			}
		}

		if (!postParameter.equals("")) {
			if (postParameter.equals("не указано")) {
				parameters.put("post", null);
			} else {
				parameters.put("post", postParameter);
			}
		}

		if (!educationParameter.equals("")) {
			if (educationParameter.equals("не указано")) {
				parameters.put("education", null);
			} else {
				parameters.put("education", educationParameter);
			}
		}

		if (!expirienceParameter.equals("")) {
			if (expirienceParameter.equals("не указано")) {
				parameters.put("work_expirience", null);
			} else {
				parameters.put("work_expirience", expirienceParameter);
			}
		}
	
		List<CVForm> cvList = cvDao.getCVForm(parameters);
		if (0 == cvList.size()) {
			request.setAttribute("noIncomeList", "noIncomeList");
			forward("cvformBaseServlet", request, response);
		}
		request.setAttribute("cvIncomeList", cvList);
		forward("cvformBaseServlet", request, response);
		
	}

	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
