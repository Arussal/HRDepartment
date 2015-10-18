<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login - Applicant Page - HRDepartment</title>
</head>
<body>
	<h1>Вход в раздел</h1>
	<hr />
	<form action="${pageContext.request.contextPath}/${APPLICANT_MAIN_HTML}" method="POST">
		<table>
			<tr>
				<td>Логин</td>
				<td><input type="text" name="login"></td>
			</tr>
			<tr>
				<td>Пароль</td>
				<td><input type="password" name="password"/></td>
			</tr>
			<tr>
				<td><input type="submit" name="enter" value="Войти"></td>
			</tr>
			<tr>
				<td><a href="${pageContext.request.contextPath}/${APPLICANT_REGISTRATE_HTML}">Зарегестрироваться</a></td>
				<td><a href="${pageContext.request.contextPath}/${APPLICANT_DELETE_HTML}">Удалить соискателя</a></td>
				<td><a href="${pageContext.request.contextPath}/${APPLICANT_CHANGE_PASSWORD_HTML}">Сменить пароль</a></td>
			</tr>
		</table>
	</form>
	${footer}
</body>
</html>