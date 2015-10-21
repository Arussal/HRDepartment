<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create CVForm - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Создать новое резюме</h1>
	<hr/>
	<form:form id="appCVform" action="${APPLICANT_CONTROLLER_SERVLET}" method="POST" modelAttribute="appilcantCV">
		<table>
			<tr>
				<td>Возраст</td>
				<td><input type="text" name="age" /></td>
			</tr>
			<tr>
				<td>Образование</td>
				<td><input type="text" name="education" /></td>
			</tr>
			<tr>
				<td>E-mail</td>
				<td><input type="text" name="email" /></td>
			</tr>
			<tr>
				<td>Телефон</td>
				<td><input type="text" name="phone" /></td>
			</tr>
			<tr>
				<td>Должность</td>
				<td><input type="text" name="post" /></td>
			</tr>
			<tr>
				<td>Навыки</td>
				<td>
					<c:forEach var="skill" items="${appilcantCV.skills}" varStatus="i">
						<form:input path="skill[${i.index}].name" type="text"/>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td>Опыт работы</td>
				<td><input type="text" name="expirience" /></td>
			</tr>
			<tr>
				<td>Желаемая зарплата</td>
				<td><input type="text" name="desiredSalary" /></td>
			</tr>
			<tr>
				<td>Дополнительная информация</td>
				<td><input type="text" style="height:200px" name="addInfo" /></td>
			</tr>
		</table>
		<input type="submit" name="confirmCreateCV" value="Создать"/>
		<input type="submit" name="cancel" value="Отмена"/>
	</form:form>
</body>
</html>