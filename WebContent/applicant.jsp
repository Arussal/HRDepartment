<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My CVForms - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Мои резюме</h1>
	<hr />
	
	<form action="applicantCVControllerServlet" method="post">
			<input type="submit" name="createCV" value="Создать" />
			<input type="submit" name="editCV" value="Редактировать" />
			<input type="submit" name="deleteCV" value="Удалить" />
			<input type="submit" name="sendCV" value="Отослать" />
	
		<table border="1">
		<tr>
				<th></th>
				<th>ID</th>
				<th>Статус отправки</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>E-mail</th>
				<th>Телефон</th>
				<th>Должность</th>
				<th>Навыки</th>
				<th>Опыт работы</th>
				<th>Желаемая зарплата</th>
				<th>Дополнительная информация</th>
			</tr>
			<c:forEach var="cv" items="${cvList}">
			<tr>
				<td><input type="checkbox" name="cvId" value="${cv.id}"></td>
				<td><c:out value="${cv.id}" /></td>
				<td><c:out value="${cv.sendStatus}" /></td>
				<td><c:out value="${cv.age}" /></td>
				<td><c:out value="${cv.education}" /></td>
				<td><c:out value="${cv.email}" /></td>
				<td><c:out value="${cv.phone}" /></td>
				<td><c:out value="${cv.post}" /></td>
				<td><c:out value="${cv.skills}" /></td>
				<td><c:out value="${cv.workExpirience}" /></td>
				<td><c:out value="${cv.desiredSalary}" /></td>
				<td><c:out value="${cv.additionalInfo}" /></td>
			</tr>
			</c:forEach>
		</table>
	</form>

	<br />	
	<a href="main.jsp">Выйти из раздела</a>
</body>
</html>
