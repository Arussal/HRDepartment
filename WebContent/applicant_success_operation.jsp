<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Complete - Manager - HRDepartment</title>
</head>
<body>
	<c:if test="${not empty SUCCESS_APPLICANT_REGISTRATION}">
	<h1>Регистрация успешно выполнена</h1>
		<hr />
		Войдите в систему, используя свой логин и пароль<br />
		<a href="${APPLICANT_LOGIN_JSP}">Войти в систему</a>
	</c:if>
	<c:if test="${not empty SUCCESS_APPLICANT_REGISTRATION}">
		<h1>Смена пароля успешно выполнено</h1>
		<hr />
		Войдите в систему, используя свой логин и пароль<br />
		<a href="${APPLICANT_LOGIN_JSP}">Войти в систему</a>
	</c:if>
	<c:if test="${not empty SUCCESS_APPLICANT_DELETE}">
		<h1>Соискатель успешно удален</h1>
		<hr />
		<a href="${HOME_PAGE_JSP}">Выйти из раздела</a>
	</c:if>

</body>
</html>