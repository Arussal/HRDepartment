package main.com.mentat.nine.ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.ApplicationFormDAO;
import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.ApplicationForm;
import main.com.mentat.nine.domain.HRDepartment;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * Servlet implementation class ApplicationFormControllerServlet
 */
@WebServlet("/appControllerServlet")
public class ApplicationFormControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	static {
		LogConfig.loadLogConfig();
	}
	
	private static Logger log = Logger.getLogger(ApplicationFormControllerServlet.class);
	
	private ApplicationFormDAO appDao;
	
    /**
     * @throws PersistException 
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationFormControllerServlet() throws PersistException {
        super();
        DAOFactory daoFactory = DAOFactory.getFactory();
		appDao = daoFactory.getApplicationFormDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			log.error("doGet error");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			performTask(request, response);
		} catch (PersistException e) {
			log.error("doPost error");
		}
	}

	
	private void performTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, PersistException {

		request.setCharacterEncoding("UTF8");
		
		int action = checkAction(request);
		if (1 == action) {
			createNewApp(request, response);
		} else if (2 == action) {
			deleteApp(request, response);
		} else if (3 == action) {
			editApp(request, response);
		}
		
	}
	

	private int checkAction(HttpServletRequest request){
		if ((request.getParameter("createApp") != null) || (request.getParameter("newApp") != null)) {
			return 1;
		} else if (request.getParameter("deleteApp") != null) {
			return 2;
		} else if ((request.getParameter("editApp") != null) || (request.getParameter("edit") != null)) {
			return 3;
		}
		return 0;
	}

	
	private void createNewApp(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {

		if (request.getParameter("createApp") != null) {
			forward("new_application.jsp", request, response);
		}

		ApplicationForm appForm = getDataFromForm(request, response);
		appDao.createApplicationForm(appForm);
		
		forward("applicationBaseServlet", request, response);
		
	}

	
	private ApplicationForm getDataFromForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, PersistException {
				
		boolean isEmptyFields = checkEmptyFields(request);

		String date = request.getParameter("date");
		String age = request.getParameter("age");
		String education = request.getParameter("education");
		String requirements = request.getParameter("requirements");
		String post = request.getParameter("post");
		String salary = request.getParameter("salary");
		String workExpirience = request.getParameter("expirience");
		
		Map<String, String> intData = new HashMap<String, String>();
		intData.put("age", age);
		intData.put("salary", salary);
		intData.put("workExpirience", workExpirience);
		
		boolean isWrongData = checkWrongDataFields(intData, request);
		
		if (isEmptyFields | isWrongData) {
			request.setAttribute("wrongData", "wrongData");
			forward("error.jsp", request, response);
		}
		
		HRDepartment hrDep = new HRDepartment();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date parsedDate = null;
		try {
			parsedDate = sdf.parse(date);
		} catch (ParseException e) {
			log.warn("can't parse Form's date");
		}
		int parsedAge = Integer.parseInt(age);
		int parsedSalary = Integer.parseInt(salary);
		int parsedWorkExpirience = Integer.parseInt(workExpirience);
		Set<String> parsedRequirements = new HashSet<String>(Arrays.asList(requirements.split(";")));
		ApplicationForm appForm = hrDep.formApplicationForm(parsedAge, education, 
				parsedRequirements, post, parsedSalary, parsedWorkExpirience, parsedDate);
		
		return appForm;
	}
	

	private boolean checkEmptyFields(HttpServletRequest request) 
			throws ServletException, IOException {
		
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

	
	private void deleteApp(HttpServletRequest request, HttpServletResponse response) 
			throws PersistException, ServletException, IOException {
		List<Integer> idList = new ArrayList<Integer>();
		Map<String, String[]> parameters = request.getParameterMap();
		for (String key : parameters.keySet()) {
			if (key.equals("appId")){
				for (String values : parameters.get(key)) {
					idList.add(Integer.parseInt(values));
				}
			}
		}
		
		for (Integer id : idList) {
			ApplicationForm appForm = appDao.getApplicationFormById(id);
			appDao.deleteApplicationForm(appForm);
		}
		
		forward("applicationBaseServlet", request, response);
	}
	
	
	private void editApp(HttpServletRequest request, HttpServletResponse response)
			throws PersistException, ServletException, IOException {
						
		if (request.getParameter("editApp") != null) {
			List<Integer> idList = new ArrayList<Integer>();
			Map<String, String[]> parameters = request.getParameterMap();
			for (String key : parameters.keySet()) {
				if (key.equals("appId")){
					for (String values : parameters.get(key)) {
						idList.add(Integer.parseInt(values));
					}
				}
			}
			System.out.println(idList.size());
			if (0 == idList.size()) {
				request.setAttribute("nothingToEditError", "nothingToEditError");
				forward("error.jsp", request, response);
			} else if (idList.size() > 1) {
				request.setAttribute("appCountToEdit", idList.size());
				request.setAttribute("tooMuchToEditError", "tooMuchToEditError");
				forward("error.jsp", request, response);
			}
			ApplicationForm appForm = appDao.getApplicationFormById(idList.get(0));
			request.setAttribute("app", appForm);
			forward("edit_application.jsp", request, response);
		}
		
		ApplicationForm appForm = getDataFromForm(request, response);
		Integer id = Integer.parseInt(request.getParameter("id"));
		appForm.setId(id);
		appDao.updateApplicationForm(appForm);
		
		forward("applicationBaseServlet", request, response);
	}
	
	
	
	private void forward(String path, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}
		

}
