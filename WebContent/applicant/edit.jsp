<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Редактирование нового соискателя</h1>
	<hr/>
	<spring:url value="/${APPLICANT_EDIT_HTML}" var="editUrl" />
	<form:form action="${editUrl}" method="POST" modelAttribute="applicant">
		<form:hidden path="id" />
		<form:hidden path="login" />
		<form:hidden path="password" />
		<table>
			<tr>
				<td>Фамилия</td>
				<td><input type="text" name="surname" value="${applicant.surname}"></td>
			</tr>
			<tr>
				<td>Имя</td>
				<td><input type="text" name="name" value="${applicant.name}"></td>
			</tr>
			<tr>
				<td>Отчество</td>
				<td><input type="text" name="lastName" value="${applicant.lastName}"></td>
			</tr>
			<tr>
				<td>Дата рождения (дд/мм/гггг)</td>
				<td><input type="text" name="birthday" value="${applicant.birthday}"></td>
			</tr>
			<tr>
				<td>Образование</td>
				<td><input type="text" name="education" value="${applicant.education}"></td>
			</tr>
			<tr>
				<td>Телефон (+380XXXXXXXXX)</td>
				<td><input type="text" name="phone" value="${applicant.phone}"></td>
			</tr>
			<tr>
				<td>E-mail</td>
				<td><input type="text" name="email" value="${applicant.email}"></td>
			</tr>
			<tr>
				<td><input type="submit" name="completeRegistration" value="Сохранить"></td>
			</tr>
		</table>
	</form:form>
	<br />
	<a href="${pageContext.request.contextPath}/${APPLICANT_LOGIN_JSP}">Назад</a> <a href="${pageContext.request.contextPath}/${HOME_PAGE_JSP}">Выйти из раздела</a>
</body>
</html>