<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Delete - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Удалить пользователя</h1>
	<hr />
	<form action="${pageContext.request.contextPath}/${APPLICANT_CONFIRM_DELETE_HTML}" method="post">
		<table>
			<tr>
				<td>Логин</td>
				<td>Пароль</td>
			</tr>
			<tr>
				<td><input type="text" name="login"></td>
				<td><input type="password" name="password"></td>
			</tr>
		</table>
		<input type="submit" value="Удалить пользователя">
	</form>
</body>
</html>