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
	<hr />
	<c:if test="${not empty wrongData}">
	Ошибка ввода данных:<br /><br />
		<c:forEach var="item" items="${emptyFields}">
		Пустое поле - <c:out value="${item}" /><br />
		</c:forEach>
		<c:forEach var="item" items="${wrongFields}">
		Неправильный формат данных в поле - <c:out value="${item}" /><br />
		</c:forEach>
	</c:if>
	
	<c:if test="${not empty nothingSelectedError}">
	Ошибка ввода данных: выбрано 0 записей для редактирования<br />
	Надо же выбрать что-то одно<br /><br />
	<a href="applicationBaseServlet">Назад</a>
	</c:if>
	<c:if test="${not empty tooManySelectedError}">
	Ошибка ввода данных: выбрано записей для редактирования - <c:out value="${selectedCount}" /><br />
	Надо же выбрать что-то одно<br /><br />
	<a href="applicationBaseServlet">Назад</a>
	</c:if>
	
	<c:if test="${not empty noOneCVToDelete}">
	Ошибка ввода данных: выбрано 0 записей для удаления<br />
	Надо же выбрать хоть что-то<br /><br />
	<a href="cvformBaseServlet">Назад</a>
	</c:if>
	
	<c:if test="${not empty noOneCandidateToDelete}">
	Ошибка ввода данных: выбрано 0 записей для удаления<br />
	Надо же выбрать хоть что-то<br /><br />
	<a href="candidateBaseServlet">Назад</a>
	</c:if>
	
	<c:if test="${not empty noOneCandidate}">
	Найдено 0 новых кандидатов<br />
	Заходите позже<br /><br />
	<a href="candidateBaseServlet">Назад</a>
	</c:if>
	
	
	
	<c:if test="${not empty noUsersFoundError}">
		Пользователей не найдено<br />
	</c:if>

	<c:if test="${not empty userNotFound}">
	Пользователь с таким логином отсутствует<br />
	Станьте им!<br />
		<c:if test="${not empty notSuccessApplicantLoginOperation}">
			<a href="applicant_registration.jsp">Зарегестрироваться</a><br />
		</c:if>
		<c:if test="${not empty notSuccessManagerLoginOperation}">
			<a href="manager_registration.jsp">Зарегестрироваться</a><br />
		</c:if>
	<br />
	<br />
	</c:if>
	
	<c:if test="${not empty passwordNotFound}">
	Неправильный пароль! Повторите попытку<br />
	<br />
	<br />
	</c:if>
	
	
	<!-- block for registration -->
		
	<c:if test="${not empty emptyLoginFields}">
		Пустое поля логина/пароля/повторного пароля! Введите все данные<br />	
	</c:if>
	
	<c:if test="${not empty emptyNameFields}">
		Пустое поля имени/фамилии/отчества! Введите все данные<br />	
	</c:if>
	
	<c:if test="${not empty incorrectLogin}">
		Неправильное количество символов логина! Логин должен содержать от 6 до 10 символов<br />
	</c:if>
	
	<c:if test="${not empty incorrectPassword}">
		Неправильное количество символов пароля! Пароль должен содержать от 8 до 14 символов<br />
	</c:if>
	
	<c:if test="${not empty passwordSpaceError}">
		В пароле присутствует символ пробела! Пароль не должен содержать пробел<br />
	</c:if>
	
	<c:if test="${not empty loginSpaceError}">
		В логине присутствует символ пробела! Логин не должен содержать пробел<br />
	</c:if>
	
	<c:if test="${not empty existUserError}">
		Пользватель с таким логином уже есть! Придумайте другой логин<br />
	</c:if>
	
	<c:if test="${not empty notEqualsPassword}">
		Пароль и "подтверждение пароля" не совпадают! Введите одинаковые пароли<br />
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
		
	
	
	
</body>
</html>