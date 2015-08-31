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
	<c:if test="${not empty newAppFormError}">
	Ошибка ввода данных:<br /><br />
		<c:forEach var="item" items="${emptyFields}">
		Пустое поле - <c:out value="${item}" /><br />
		</c:forEach>
		<c:forEach var="item" items="${wrongFields}">
		Неправильный формат данных в поле - <c:out value="${item}" /><br />
		</c:forEach>
	</c:if>
	<br /><br />
	<a href="new_application.jsp">Назад</a> <a href="main.jsp">На главную</a> 
</body>
</html>