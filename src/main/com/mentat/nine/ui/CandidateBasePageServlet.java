package main.com.mentat.nine.ui;

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

import main.com.mentat.nine.dao.CandidateDAO;
import main.com.mentat.nine.dao.exceptions.NoSuitableDBPropertiesException;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.ui.util.WebPath;

/**
 * Servlet implementation class CandidateBasePageServlet
 */
@WebServlet("/candidateBaseServlet")
public class CandidateBasePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CandidateDAO candDao;
	private DAOFactory daoFactory;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public CandidateBasePageServlet() {
        super();
        daoFactory = DAOFactory.getFactory();
    }

	/**
	 * @throws IOException 
	 * @throws ServletException 
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
        candDao = daoFactory.getCandidateDAO();
        
	    try {
			DAOFactory.loadConnectProperties(properties);
		} catch (NoSuitableDBPropertiesException e) {
			throw new ServletException();
		}
	    
		Set<Candidate> candSet = null;
		
		if (request.getAttribute("candIncomeList") != null) {
			candSet = (Set<Candidate>)request.getAttribute("candIncomeList");
			createFilterFinder(request, candSet);
		} else {
			candSet = getAllCandidates();
			createFilterFinder(request, candSet);
		}
		
		request.setAttribute("candSet", candSet);
		forward(WebPath.HR_CANDIDATES_JSP, request, response);
	}


	private Set<Candidate> getAllCandidates() throws ServletException {
		try {
			return candDao.getAllCandidates();
		} catch (PersistException e) {
			throw new ServletException();
		}
	}
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}

	
	private void createFilterFinder(HttpServletRequest request, Set<Candidate> candidates) {
		
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
		Set<String> idCandidateSet = new LinkedHashSet<String>();
		Set<String> ageCandidateSet = new LinkedHashSet<String>();
		Set<String> postCandidateSet = new TreeSet<String>();
		Set<String> educationCandidateSet = new TreeSet<String>();
		Set<String> expirienceCandidateSet = new LinkedHashSet<String>();
		
		idCandidateSet.add("");
		ageCandidateSet.add("");
		postCandidateSet.add("");
		educationCandidateSet.add("");
		expirienceCandidateSet.add("");
		
		for (Candidate cand : candidates) {
			if (cand.getId() == null) {
				idCandidateSet.add(" не указано");
			} else {
				originIDSet.add(cand.getId());
			}
			if (cand.getAge() == null) {
				ageCandidateSet.add(" не указано");
			} else {
				originAgeSet.add(cand.getAge());
			}
			if (cand.getPost() == null) {
				postCandidateSet.add(" не указано");
			} else {
				postCandidateSet.add(cand.getPost());
			}
			if (cand.getEducation() == null) {
				educationCandidateSet.add(" не указано");
			} else {
				educationCandidateSet.add(cand.getEducation());
			}
			if (cand.getWorkExpirience() == null) {
				expirienceCandidateSet.add(" не указано");
			} else {
				originExpirienceSet.add(cand.getWorkExpirience());
			}
		}
		
		for (Integer item : originIDSet) {
			idCandidateSet.add(String.valueOf(item));
		}
		for (Integer item : originAgeSet) {
			ageCandidateSet.add(String.valueOf(item));
		}
		for (Integer item : originExpirienceSet) {
			expirienceCandidateSet.add(String.valueOf(item));
		}
		
		request.setAttribute("comparableList", comparableList);
		request.setAttribute("idCandidateSet", idCandidateSet);
		request.setAttribute("ageCandidateSet", ageCandidateSet);
		request.setAttribute("postCandidateSet", postCandidateSet);
		request.setAttribute("educationCandidateSet", educationCandidateSet);
		request.setAttribute("expirienceCandidateSet", expirienceCandidateSet);
	}

}
