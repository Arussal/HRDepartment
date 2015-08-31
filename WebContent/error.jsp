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
	<h1>Error!</h1>
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
	<c:if test="${not empty nothingToEditError}">
	Ошибка ввода данных: выбрано 0 записей для редактирования<br /><br />
	</c:if>
	<c:if test="${not empty tooMuchToEditError}">
	Ошибка ввода данных: выбрано записей для редактирования - <c:out value="${appCountToEdit}" /><br />
	Необходимо выбрать одну запись<br /><br />
	</c:if>
	<br /><br />
	<a href="main.jsp">На главную</a> 
</body>
</html>