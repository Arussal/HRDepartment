package main.com.mentat.nine.ui.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebPath {

	public static final String APPLICANT_BASE_PAGE_SERVLET = "applicantServlet";
	public static final String APPLICANT_CONTROLLER_SERVLET = "applicantCVControllerServlet";
	public static final String APPLICATION_BASE_PAGE_SERVLET = "applicationBaseServlet";
	public static final String APPLICATION_CONTROLLER_SERVLET = "appControllerServlet";
	public static final String CANDIDATE_BASE_PAGE_SERVLET = "candidateBaseServlet";
	public static final String CANDIDATE_CONTROLLER_SERVLET = "candidateControllerServlet";
	public static final String CVFORM_BASE_PAGE_SERVLET = "cvformBaseServlet";
	public static final String CVFORM_CONTROLLER_SERVLET = "cvformControllerServlet";
	public static final String EMPLOYEE_BASE_PAGE_SERVLET = "employeeBaseServlet";
	public static final String EMPLOYEE_CONTROLLER_SERVLET = "employeeControllerServlet";
	public static final String HR_MANAGER_SERVLET = "hrManagerServlet";
	
	public static final String HOME_PAGE_JSP = "main.jsp";
	
	public static final String APPLICANT_MAIN_JSP = "applicant.jsp";
	public static final String APPLICANT_LOGIN_JSP = "applicant_login.jsp";
	public static final String APPLICANT_REGISTRATE_JSP = "applicant_registration.jsp";
	public static final String APPLICANT_CREATE_CV_JSP = "main.jsp";
	public static final String APPLICANT_EDIT_CV_JSP = "main.jsp";
	public static final String APPLICANT_CHANGE_PASSWORD_JSP = "changeApplicantManager.jsp";
	public static final String APPLICANT_SUCCESS_JSP = "applicant_success_operation.jsp";
	public static final String APPLICANT_DELETE_JSP = "delete_applicant.jsp";
	
	public static final String HR_APPLICAION_FORMS_JSP = "applications.jsp";
	public static final String HR_CANDIDATES_JSP = "candidates.jsp";
	public static final String HR_CVFORMS_JSP = "cvforms.jsp";
	public static final String HR_EMPLOYEES_JSP = "employees.jsp";
	
	public static final String HR_EDIT_APPLICATION_FORM_JSP = "edit_application.jsp";
	public static final String HR_EDIT_EMPLOYEE_JSP = "edit_employee.jsp";
	public static final String HR_FIRE_EMPLOYEE_JSP = "fire_employee.jsp";
	public static final String HR_HIRE_EMPLOYEE_JSP = "hire_employee.jsp";
	public static final String HR_NEW_CANDIDATE_JSP = "new_candidate.jsp";
	public static final String HR_NEW_APPLICATION_FORM_JSP = "new_application.jsp";

	public static final String MANAGER_MAIN_JSP = "hrdepartment.jsp";
	public static final String MANAGER_LOGIN_JSP = "hrdepartment_login.jsp";
	public static final String MANAGER_REGISTRATE_JSP = "manager_registration.jsp";
	public static final String MANAGER_SUCCESS_JSP = "manager_success_operation.jsp";
	public static final String MANAGER_CHANGE_PASSWORD_JSP = "changePasswordManager.jsp";
	public static final String MANAGER_DELETE_JSP = "delete_manager.jsp";
	
	public static final String ERROR_JSP = "error.jsp";
	
	
	public void loadPathValues(HttpSession session) {
		
		session.setAttribute("APPLICANT_BASE_PAGE_SERVLET", APPLICANT_BASE_PAGE_SERVLET);
		
	}
}
