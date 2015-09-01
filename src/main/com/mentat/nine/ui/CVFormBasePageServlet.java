package main.com.mentat.nine.ui;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
@WebServlet("/cvformBaseServlet")
public class CVFormBasePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CVFormDAO cvDao;
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    
	public CVFormBasePageServlet() throws PersistException {
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

	
	@SuppressWarnings("unchecked")
	private void performTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, PersistException, IOException {
		createFilterFinder(request);
		List<CVForm> cvList = null;
		if (request.getAttribute("cvIncomeList") != null) {
			cvList = (List<CVForm>)request.getAttribute("cvIncomeList");
		} 
		else {
			cvList = getAllCV();	
		}
		request.setAttribute("cvList", cvList);
		forward("cvforms.jsp", request, response);
	}
	
	
	private void createFilterFinder(HttpServletRequest request) throws PersistException{
		
		
		List<CVForm> cvList = cvDao.getAllCVForms();
		
		Set<String> idCVSet = new LinkedHashSet<String>();
		Set<String> ageCVSet = new LinkedHashSet<String>();
		Set<String> postCVSet = new LinkedHashSet<String>();
		Set<String> educationCVSet = new LinkedHashSet<String>();
		Set<String> expirienceCVSet = new LinkedHashSet<String>();
		
		idCVSet.add(null);
		ageCVSet.add(null);
		postCVSet.add(null);
		educationCVSet.add(null);
		expirienceCVSet.add(null);
		
		idCVSet.add("не указано");
		ageCVSet.add("не указано");
		postCVSet.add("не указано");
		educationCVSet.add("не указано");
		expirienceCVSet.add("не указано");
		
		for (CVForm cv : cvList) {
			idCVSet.add(String.valueOf(cv.getId()));
			ageCVSet.add(String.valueOf(cv.getAge()));
			postCVSet.add(cv.getPost());
			educationCVSet.add(cv.getEducation());
			expirienceCVSet.add(String.valueOf(cv.getWorkExpirience()));
		}
		
		request.setAttribute("idCVSet", idCVSet);
		request.setAttribute("ageCVSet", ageCVSet);
		request.setAttribute("postCVSet", postCVSet);
		request.setAttribute("educationCVSet", educationCVSet);
		request.setAttribute("expirienceCVSet", expirienceCVSet);
	}
	
	
	private List<CVForm> getAllCV() throws PersistException{
		List<CVForm> cvList = cvDao.getAllCVForms();
		return cvList;
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
