package main.com.mentat.nine.ui.util;

import javax.servlet.http.HttpServletRequest;

public class WebAttributes {
	
	public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
	public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
	public static final String WRONG_DATA = "WRONG_DATA";
	public static final String EMPTY_FIELDS_LIST = "EMPTY_FIELDS_LIST";
	public static final String WRONG_DATA_FIELDS_LIST = "WRONG_DATA_FIELDS_LIST";
	public static final String NO_ONE_ITEM_SELECTED = "NO_ONE_ITEM_SELECTED";
	public static final String TOO_MANY_ITEMS_SELECTED = "TOO_MANY_ITEMS_SELECTED";
	public static final String NO_NEW_CANDIDATE = "NO_NEW_CANDIDATE";
	public static final String USER_ALREADY_EXIST_ERROR = "USER_ALREADY_EXIST_ERROR";
	
	public static final String SUCCESS_APPLICANT_REGISTRATION = "SUCCESS_APPLICANT_REGISTRATION"; 
	public static final String SUCCESS_APPLICANT_CHANGE_PASSWORD = "SUCCESS_APPLICANT_CHANGE_PASSWORD";
	public static final String SUCCESS_APPLICANT_DELETE = "SUCCESS_APPLICANT_DELETE";
	public static final String INVALID_APPLICANT_LOGIN = "INVALID_APPLICANT_LOGIN";
	public static final String INVALID_APPLICANT_DELETE = "INVALID_APPLICANT_DELETE"; 	
	public static final String INVALID_APPLICANT_REGISTRATION = "INVALID_APPLICANT_REGISTRATION"; 
	public static final String INVALID_APPLICANT_CHANGE_PASSWORD = "INVALID_APPLICANT_CHANGE_PASSWORD";
	
	public static final String SUCCESS_MANAGER_REGISTRATION = "SUCCESS_MANAGER_REGISTRATION";
	public static final String SUCCESS_MANAGER_CHANGE_PASSWORD = "SUCCESS_MANAGER_CHANGE_PASSWORD";
	public static final String SUCCESS_MANAGER_DELETE = "SUCCESS_MANAGER_DELETE";
	public static final String INVALID_MANAGER_LOGIN = "INVALID_MANAGER_LOGIN";
	public static final String INVALID_MANAGER_REGISTRATION = "INVALID_MANAGER_REGISTRATION";
	public static final String INVALID_MANAGER_CHANGE_PASSWORD = "INVALID_MANAGER_CHANGE_PASSWORD";
	public static final String INVALID_MANAGER_DELETE = "INVALID_MANAGER_DELETE";
	 		
	public static final String EMPTY_NAME_FIELDS = "EMPTY_NAME_FIELDS";
	public static final String EMPTY_LOGIN_FIELDS = "EMPTY_LOGIN_FIELDS";
	public static final String LOGIN_LENGTH_ERROR = "LOGIN_LENGTH_ERROR";
	public static final String LOGIN_SPACE_ERROR = "LOGIN_SPACE_ERROR";
	public static final String PASSWORD_LENGTH_ERROR = "PASSWORD_LENGTH_ERROR";
	public static final String PASSWORD_SPACE_ERROR = "PASSWORD_SPACE_ERROR";
	public static final String DIFFERENT_PASSWORDS_ERROR = "DIFFERENT_PASSWORDS_ERROR";

	public static void loadAttribute(HttpServletRequest request, String attribute) {
		request.setAttribute(attribute, attribute);
	}
	
}
