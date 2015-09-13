<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New candidate - HRDepartment</title>
</head>
<body>
	<h1>Новые кандидаты</h1>
	<hr/>

	<form action="${APPLICATION_CONTROLLER_SERVLET}" method="post">
			<input type="submit" name="createApp" value="Создать" />
			<input type="submit" name="editApp" value="Редактировать" />
			<input type="submit" name="deleteApp" value="Удалить" />
			<input type="submit" name="findNewCandidate" value="Найти нового кандидата" />

		<table border="1">
			<tr>
				<th style="width:200px">ФИО</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>E-mail</th>
				<th>Телефон</th>
				<th>Должность</th>
				<th>Навыки</th>
				<th>Опыт работы</th>
			</tr>
			<c:forEach var="cand" items="${candidates}">
			<tr>
				<td><c:out value="${cand.name}" /></td>
				<td><c:out value="${cand.age}" /></td>
				<td><c:out value="${cand.education}" /></td>
				<td><c:out value="${cand.email}" /></td>
				<td><c:out value="${cand.phone}" /></td>
				<td><c:out value="${cand.post}" /></td>
				<td><c:out value="${cand.skills}" /></td>
				<td><c:out value="${cand.workExpirience}" /></td>
			</c:forEach>
		</table>
	</form>
	
	<a href="${APPLICATION_BASE_PAGE_SERVLET}">Назад</a> 
	<a href="${CANDIDATE_BASE_PAGE_SERVLET}">Перейти в раздел кандидатов</a> 
	<a href="${MANAGER_MAIN_JSP}">На главную</a>
</body>
</html>