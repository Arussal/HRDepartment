<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HRDepartment - error</title>
</head>
<body>
	<h1>Ошибка!</h1>
	${error}
		
	<hr />
	<c:if test="${not empty WRONG_DATA}">
		<c:if test="${not empty EMPTY_FIELDS_LIST}">
			<c:forEach var="item" items="${emptyFieldsList}">
			Пустое поле - <c:out value="${item}" /><br />
			</c:forEach>
		</c:if>
		<c:if test="${not empty WRONG_DATA_FIELDS_LIST}">
			<c:forEach var="item" items="${wrongFieldsList}">
			Неправильный формат данных в поле - <c:out value="${item}" /><br />
			</c:forEach>
		</c:if>
	</c:if>
	
	<c:if test="${not empty NO_ONE_ITEM_SELECTED}">
		Ошибка ввода данных: выбрано 0 записей для редактирования<br />
		Надо же выбрать хоть что-то<br /><br />
	</c:if>
	
	<c:if test="${not empty TOO_MANY_ITEMS_SELECTED}">
		Ошибка ввода данных: выбрано слишком много записей для редактирования<br />
		Надо же выбрать что-то одно<br /><br />
	</c:if>
	
	<c:if test="${not empty USER_NOT_FOUND}">
		Пользователь с таким логином не найден<br />
	</c:if>
	
	<c:if test="${not empty INCORRECT_PASSWORD}">
		Вы ввели неправильный пароль<br />
	</c:if>
	
	<c:if test="${not empty INVALID_APPLICANT_LOGIN}">
		Введите правильный логин и пароль или <a href="${APPLICANT_LOGIN_JSP}">зарегестрируйтесь</a><br /><br />
	</c:if>
		
	<c:if test="${not empty INVALID_APPLICANT_REGISTRATION}">
		Регистрация не выполнена<br /><br />
	</c:if>
	
	<c:if test="${not empty INVALID_APPLICANT_CHANGE_PASSWORD}">
		Смена пароля не выполнена<br /><br />
	</c:if>
	
	<c:if test="${not empty INVALID_APPLICANT_CHANGE_PASSWORD}">
		Смена пароля не выполнена<br /><br />
	</c:if>
			
	
	
	<!-- register applicant block -->
		
	<c:if test="${not empty EMPTY_LOGIN_FIELDS}">
		Пустое поля логина/пароля/повторного пароля! Введите все данные<br />	
	</c:if>
	
	<c:if test="${not empty EMPTY_NAME_FIELDS}">
		Пустое поля имени/фамилии/отчества! Введите все данные<br />	
	</c:if>
	
	<c:if test="${not empty LOGIN_LENGTH_ERROR}">
		Неправильное количество символов логина! Логин должен содержать от 6 до 10 символов<br />
	</c:if>
	
	<c:if test="${not empty PASSWORD_LENGTH_ERROR}">
		Неправильное количество символов пароля! Пароль должен содержать от 8 до 14 символов<br />
	</c:if>
	
	<c:if test="${not empty PASSWORD_SPACE_ERROR}">
		В пароле присутствует символ пробела! Пароль не должен содержать пробел<br />
	</c:if>
	
	<c:if test="${not empty LOGIN_SPACE_ERROR}">
		В логине присутствует символ пробела! Логин не должен содержать пробел<br />
	</c:if>
	
	<c:if test="${not empty USER_ALREADY_EXIST_ERROR}">
		Пользватель с таким логином уже есть! Придумайте другой логин<br />
	</c:if>
	
	<c:if test="${not empty DIFFERENT_PASSWORDS_ERROR}">
		Пароль и "подтверждение пароля" не совпадают! Введите одинаковые пароли<br />
	</c:if>
	
	<!-- end register applicant block -->
	
			
	<c:if test="${not empty NO_NEW_CANDIDATE}">
		Новых кандидатов не найдено
	</c:if>
	
	
	<c:if test="${not empty notSuccessManagerCreateOperation}">
		Менеджер не создан<br />
	</c:if>
	
	<c:if test="${not empty notSuccessManagerRegistration}">
		<a href="manager_registration.jsp">Назад</a>
	</c:if>
	
	<c:if test="${not empty notSuccessManagerOperation}">
		<a href="changePasswordManager.jsp">Назад</a>
	</c:if>
	
	<c:if test="${not empty notSuccessManagerLoginOperation}">
		<a href="hrdepartment_login.jsp">Назад</a>
	</c:if>
	
	<c:if test="${not empty notSuccessApplicantLoginOperation}">
		<a href="applicant_login.jsp">Назад</a><br />
	</c:if>
	
	<c:if test="${not empty notSuccessApplicantCreateOperation}">
		Соискатель не создан<br />
	</c:if>
	
	<c:if test="${not empty notSuccessApplicantDeleteOperation}">
		Соискатель не удлен<br />
		<a href="applicant_login.jsp">Назад</a><br />
	</c:if>
	
	<c:if test="${not empty notSuccessManagerDeleteOperation}">
		Менеджер не удлен<br />
		<a href="manager_login.jsp">Назад</a><br />
	</c:if>
	
	<c:if test="${not empty notSuccessApplicantRegistration}">
		<a href="applicant_registration.jsp">Назад</a>
	</c:if>
		
	
	 -->
	
</body>
</html>