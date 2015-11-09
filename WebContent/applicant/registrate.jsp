<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Регистрация нового соискателя</h1>
	<hr/>
	
	<form:errors path="applicant.*" />
	
	<form action="${pageContext.request.contextPath}/${APPLICANT_REGISTRATE_HTML}" method="post">
		<table>
			<tr>
				<td>Фамилия</td>
				<td><input type="text" name="surname">*</td>
			</tr>
			<tr>
				<td>Имя</td>
				<td><input type="text" name="name">*</td>
			</tr>
			<tr>
				<td>Отчество</td>
				<td><input type="text" name="lastName">*</td>
			</tr>
			<tr>
				<td>Дата рождения (дд/мм/гггг)</td>
				<td><input type="text" name="birthDay">*</td>
			</tr>
			<tr>
				<td>Образование</td>
				<td><input type="text" name="education">*</td>
			</tr>
			<tr>
				<td>Телефон (380XXXXXXXXX)</td>
				<td><input type="text" name="phone">*</td>
			</tr>
			<tr>
				<td>E-mail</td>
				<td><input type="text" name="email">*</td>
			</tr>
			<tr>
				<td>Логин (6-10 смволов)</td>
				<td><input type="text" name="login">*</td>
			</tr>
			<tr>
				<td>Пароль (8-14 символов)</td>
				<td><input type="password" name="password"/>*</td>
			</tr>
			<tr>
				<td>Повторите пароль</td>
				<td><input type="password" name="confirmPassword"/>*</td>
			</tr>
			<tr>
				<td><input type="submit" name="completeRegistration" value="Зарегистрироваться"></td>
			</tr>
		</table>
	</form>
	<br />
	<a href="${pageContext.request.contextPath}/${APPLICANT_LOGIN_JSP}">Назад</a> <a href="${pageContext.request.contextPath}/${HOME_PAGE_JSP}">Выйти из раздела</a>
</body>
</html>