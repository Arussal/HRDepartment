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

	<spring:url value="/${APPLICANT_EDIT_CV_HTML}" var="editUrl" />
	<form:form action="${editUrl}" method="POST" modelAttribute="applicantCV">
	<form:hidden path="id" />
	<form:hidden path="surname" />
	<form:hidden path="name" />
	<form:hidden path="lastName" />
	<form:hidden path="age" />
	<form:hidden path="education" />
	<form:hidden path="email" />
	<form:hidden path="phone" />
	<table>
			<tr>
					<td>
						<label>Должность</label>
					</td>
					<td>
						<div>
							<input type="text" value="${applicantCV.post}" name="post" />
						</div>
					</td>
			</tr>
			<tr>
				<td>Опыт работы</td>
				<td><input type="text" name="workExpirience" value="${applicantCV.workExpirience}"/></td>
			</tr>
			<spring:bind path="skills">
				<tr>
					<td><label>Навыки</label></td>
					<td>
					<div>
					<c:forEach items="${applicantCV.skills}" var="skill" varStatus="i">
						<form:input path="skills[${i.index}].name" type="text" placeholder="skills[${i.index}].name" />
					</c:forEach>
					</div>
					</td>
				</tr>
				</spring:bind>
			<tr>
				<td>Желаемая зарплата</td>
				<td><input type="text" name="desiredSalary"  value="${applicantCV.desiredSalary}"/></td>
			</tr>
			<tr>
				<td>Дополнительная информация</td>
				<td><input type="text" style="height:200px" name="additionalInfo" value="${applicantCV.additionalInfo}"/></td>
			</tr>
		</table>
	<input type="submit" name="saveChanges" value="Сохранить" />
	</form:form>
	<br />
	<a href="${APPLICANT_BASE_PAGE_SERVLET}">Назад</a>  <a href="${HOME_PAGE_JSP}">Выйти из системы</a>
</body>
</html>