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
	<form action="applicantServlet" method="post">
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
				<td><input type="submit" name="login" value="Войти"></td>
				<td><input type="submit" name="registration" value="Зарегестрироваться"></td>
				<td><input type="submit" name="delete" value="Удалить соискателя"></td>
			</tr>
		</table>
		<a href="changeApplicantManager.jsp">Сменить пароль</a>
	</form>
</body>
</html>