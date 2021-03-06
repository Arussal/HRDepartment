<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit application - HRDepartment</title>
</head>
<body>
<h1>Редактировать вакансию</h1>
	<hr/>

	<form action="${APPLICATION_CONTROLLER_SERVLET}" method="post">
	<input type="hidden" name="id"  value="${app.id}"/>
	<table>
		<tr>
			<td>Дата:</td>
			<td>
				<select name="year">
					<c:forEach var="item" items="${years}">
						<option selected="${item}"><c:out value="${item}"/></option>
					</c:forEach>
				</select>
				<select name="month">
					<c:forEach var="item" items="${months}">
						<option selected="${item}"><c:out value="${item}"/></option>
					</c:forEach>
				</select>
				<select name="day">
					<c:forEach var="item" items="${days}">
						<option selected="${item}"><c:out value="${item}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>	
			<td>Возраст, лет:</td>
			<td><input type="text" name="age"  value="${app.age}"/></td>
		</tr>
		<tr>
			<td>Образование:</td>
			<td><input type="text" name="education" value="${app.education}"/></td>
		</tr>
		<tr>
			<td>Требования:</td>
			<td><input type="text" name="requirements" value="${app.requirements}"/></td>
		</tr>
		<tr>
			<td>Должность:</td>
			<td><input type="text" name="post" value="${app.post}"/></td>
		</tr>
		<tr>
			<td>Зарплата, грн:</td>
			<td><input type="text" name="salary" value="${app.salary}"/></td>
		</tr>
		<tr>
			<td>Опыт работы, лет:</td>
			<td><input type="text" name="expirience" value="${app.workExpirience}"/></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" name="confirmEditApp" value="Сохранить"></td>
		</tr>
	</table>
	</form>
	<br />
	<a href="${APPLICATION_BASE_PAGE_SERVLET}">Назад</a>  <a href="${MANAGER_MAIN_JSP}">На главную</a>
</body>
</html>