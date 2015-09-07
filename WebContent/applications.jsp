<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Application forms - HRDepartment</title>
</head>
<body>
	<h1>Вакансии</h1>
	<hr />
	
	<form action="appControllerServlet" method="post">
			<input type="submit" name="createApp" value="Создать" />
			<input type="submit" name="editApp" value="Редактировать" />
			<input type="submit" name="deleteApp" value="Удалить" />
			<input type="submit" name="findNewCandidate" value="Найти нового кандидата" />


		<table border="1">
			<tr>
				<th></th>
				<th>ID</th>
				<th>Дата</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>Требования</th>
				<th>Должность</th>
				<th>Зарплата</th>
				<th>Опыт работы</th>
			</tr>
			<c:forEach var="app" items="${apps}">
			<tr>
				<td><input type="checkbox" name="appId" value="${app.id}"></td>
				<td><c:out value="${app.id}" /></td>
				<td><c:out value="${app.date}" /></td>
				<td><c:out value="${app.age}" /></td>
				<td><c:out value="${app.education}" /></td>
				<td><c:out value="${app.requirements}" /></td>
				<td><c:out value="${app.post}" /></td>
				<td><c:out value="${app.salary}" /></td>
				<td><c:out value="${app.workExpirience}" /></td>

			</tr>
			</c:forEach>
		</table>
	</form>

	<br />	
	<a href="main.jsp">На главную</a>
</body>
</html>