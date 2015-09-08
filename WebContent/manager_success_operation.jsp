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
	<c:if test="${not empty successManagerRegistration}">
	<h1>Регистрация успешно выполнена</h1>
	</c:if>
	<c:if test="${not empty successChangePassword}">
	<h1>Смена пароля успешно выполнено</h1>
	</c:if>
	<hr />
	Войдите в систему, используя свой логин и пароль<br />
	<a href="hrdepartment_login.jsp">Войти в систему</a>
</body>
</html>