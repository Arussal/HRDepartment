<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit CVForm - Applicant - HRDepartment</title>
</head>
<body>
<h1>Редактировать резюме</h1>
	<hr/>

	<form:form id="appCVform" action="${APPLICANT_CONTROLLER_SERVLET}" method="POST" modelAttribute="appilcantCV">
	<input type="hidden" name="id"  value="${applicantCV.id}"/>
	<table>
			<tr>
				<td>Возраст</td>
				<td><input type="text" name="age"  value="${applicantCV.age}"/></td>
			<tr>
				<td>Образование</td>
				<td><input type="text" name="education" value="${applicantCV.education}"/></td>
			</tr>
			<tr>
				<td>E-mail</td>
				<td><input type="text" name="email" value="${applicantCV.email}"/></td>
			</tr>
			<tr>
				<td>Телефон</td>
				<td><input type="text" name="phone" value="${applicantCV.phone}"/></td>
			</tr>
			<tr>
				<td>Должность</td>
				<td><input type="text" name="post" value="${applicantCV.post}"/></td>
			</tr>
			<tr>
				<td>Навыки</td>
				<td>
					<c:forEach var="skill" items="${appilcantCV.skills}" varStatus="i">
						<form:input path="skill[${i.index}].name" type="text" value="${applicantCV.skills[${i.index}]}" />
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td>Опыт работы</td>
				<td><input type="text" name="expirience" value="${applicantCV.workExpirience}"/></td>
			</tr>
			<tr>
				<td>Желаемая зарплата</td>
				<td><input type="text" name="desiredSalary"  value="${applicantCV.desiredSalary}"/></td>
			</tr>
			<tr>
				<td>Дополнительная информация</td>
				<td><input type="text" style="height:200px" name="addInfo" value="${applicantCV.additionalInfo}"/></td>
			</tr>
		</table>
	<input type="submit" name="saveChanges" value="Сохранить" />
	</form:form>
	<br />
	<a href="${APPLICANT_BASE_PAGE_SERVLET}">Назад</a>  <a href="${HOME_PAGE_JSP}">Выйти из системы</a>
</body>
</html>