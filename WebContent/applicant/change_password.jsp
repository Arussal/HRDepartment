<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Change Password - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Смена пароля</h1>
	<hr/>
	<form action="${pageContext.request.contextPath}/${APPLICANT_CHANGE_PASSWORD_HTML}" method="post">
		<table>
			<tr>
				<td>Логин</td>
				<td><input type="text" name="login"></td>
			</tr>
			<tr>
				<td>Старый пароль</td>
				<td><input type="password" name="oldPassword"/></td>
			</tr>
			<tr>
				<td>Новый пароль</td>
				<td><input type="password" name="newPassword"/></td>
			</tr>
			<tr>
				<td>Повторите новый пароль</td>
				<td><input type="password" name="repeatePassword"/></td>
			</tr>
			<tr>
				<td><input type="submit" name="confirmChangePassword" value="Cменить пароль"></td>
			</tr>
		</table>
	</form>
	<br />
	<a href="${pageContext.request.contextPath}/${APPLICANT_LOGIN_JSP}">Назад</a> <a href="${pageContext.request.contextPath}/${HOME_PAGE_JSP}">Выйти из раздела</a>
</body>
</html>