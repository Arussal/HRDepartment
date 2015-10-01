package ui;

import java.io.IOException;
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

import dao.CVFormDAO;
import dao.exceptions.NoSuitableDBPropertiesException;
import dao.exceptions.PersistException;
import dao.util.DAOFactory;
import domain.CVForm;
import ui.util.WebPath;

/**
 * Servlet implementation class CandidateControllerServlet
 */
@WebServlet("/cvformBaseServlet")
public class CVFormBasePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CVFormDAO cvDao;
	private DAOFactory daoFactory;
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    
	public CVFormBasePageServlet() throws ServletException {
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
        cvDao = daoFactory.getCVFormDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		List<CVForm> cvList = null;
		if (request.getAttribute("cvIncomeList") != null) {
			cvList = (List<CVForm>)request.getAttribute("cvIncomeList");
			createFilterFinder(request, cvList);
		} 
		else {
			cvList = getAllCV();
			createFilterFinder(request, cvList);
		}
		request.setAttribute("cvList", cvList);
		forward(WebPath.HR_CVFORMS_JSP, request, response);
	}
	
	
	private void createFilterFinder(HttpServletRequest request, List<CVForm> cvList) {
		
		List<String> comparableList = new ArrayList<String>();
		comparableList.add("меньше или равно");
		comparableList.add("меньше");
		comparableList.add("равно");
		comparableList.add("больше");
		comparableList.add("больше или равно");
		
		Set<Integer> originIDSet = new TreeSet<Integer>();
		Set<Integer> originAgeSet = new TreeSet<Integer>();
		Set<Integer> originExpirienceSet = new TreeSet<Integer>();
		
		//form drop-down lists for filter by category
		Set<String> idCVSet = new LinkedHashSet<String>();
		Set<String> ageCVSet = new LinkedHashSet<String>();
		Set<String> postCVSet = new TreeSet<String>();
		Set<String> educationCVSet = new TreeSet<String>();
		Set<String> expirienceCVSet = new LinkedHashSet<String>();
		
		idCVSet.add("");
		ageCVSet.add("");
		postCVSet.add("");
		educationCVSet.add("");
		expirienceCVSet.add("");
		
		for (CVForm cv : cvList) {
			if (cv.getId() == null) {
				idCVSet.add(" не указано");
			} else {
				originIDSet.add(cv.getId());
			}
			if (cv.getAge() == null) {
				ageCVSet.add(" не указано");
			} else {
				originAgeSet.add(cv.getAge());
			}
			if (cv.getPost() == null) {
				postCVSet.add(" не указано");
			} else {
				postCVSet.add(cv.getPost());
			}
			if (cv.getEducation() == null) {
				educationCVSet.add(" не указано");
			} else {
				educationCVSet.add(cv.getEducation());
			}
			if (cv.getWorkExpirience() == null) {
				expirienceCVSet.add(" не указано");
			} else {
				originExpirienceSet.add(cv.getWorkExpirience());
			}
		}
		
		for (Integer item : originIDSet) {
			idCVSet.add(String.valueOf(item));
		}
		for (Integer item : originAgeSet) {
			ageCVSet.add(String.valueOf(item));
		}
		for (Integer item : originExpirienceSet) {
			expirienceCVSet.add(String.valueOf(item));
		}
		
		request.setAttribute("comparableList", comparableList);
		request.setAttribute("idCVSet", idCVSet);
		request.setAttribute("ageCVSet", ageCVSet);
		request.setAttribute("postCVSet", postCVSet);
		request.setAttribute("educationCVSet", educationCVSet);
		request.setAttribute("expirienceCVSet", expirienceCVSet);
	}
	
	
	private List<CVForm> getAllCV() throws ServletException {
		List<CVForm> cvList;
		try {
			cvList = cvDao.getAllCVForms();
		} catch (PersistException e) {
			throw new ServletException();
		}
		return cvList;
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
}
