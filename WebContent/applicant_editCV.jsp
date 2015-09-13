<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit CVForm - Applicant - HRDepartment</title>
</head>
<body>
<h1>Редактировать резюме</h1>
	<hr/>

	<form action="applicantCVControllerServlet" method="post">
	<input type="hidden" name="id"  value="${cv.id}"/>
	<table>
			<tr>
				<td>Возраст</td>
				<td><input type="text" name="age"  value="${cv.age}"/></td>
			<tr>
				<td>Образование</td>
				<td><input type="text" name="education" value="${cv.education}"/></td>
			</tr>
			<tr>
				<td>E-mail</td>
				<td><input type="text" name="email" value="${cv.email}"/></td>
			</tr>
			<tr>
				<td>Телефон</td>
				<td><input type="text" name="phone" value="${cv.phone}"/></td>
			</tr>
			<tr>
				<td>Должность</td>
				<td><input type="text" name="post" value="${cv.post}"/></td>
			</tr>
			<tr>
				<td>Навыки</td>
				<td><input type="text" name="skills" value="${cv.skills}"/></td>
			</tr>
			<tr>
				<td>Опыт работы</td>
				<td><input type="text" name="expirience" value="${cv.workExpirience}"/></td>
			</tr>
			<tr>
				<td>Желаемая зарплата</td>
				<td><input type="text" name="desiredSalary"  value="${cv.desiredSalary}"/></td>
			</tr>
			<tr>
				<td>Дополнительная информация</td>
				<td><input type="text" style="height:200px" name="addInfo" value="${cv.additionalInfo}"/></td>
			</tr>
		</table>
	<input type="submit" name="saveChanges" value="Сохранить" />
	</form>
	<br />
	<a href="applicantServlet">Назад</a>  <a href="main.jsp">Выйти из системы</a>
</body>
</html>