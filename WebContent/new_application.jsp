<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HRDeparmtent - create application form</title>
</head>
<body>
	<h1>New Application Form</h1>
	<hr/>

	<form action="appControllerServlet" method="post">
	<table>
		<tr>        
			<td>Дата (гггг-мм-дд):</td>
			<td><input type="text" name="date" /></td>
		</tr>
		<tr>	
			<td>Возраст, лет:</td>
			<td><input type="text" name="age" /></td>
		</tr>
		<tr>
			<td>Образование:</td>
			<td><input type="text" name="education" /></td>
		</tr>
		<tr>
			<td>Требования:</td>
			<td><input type="text" name="requirements" /></td>
		</tr>
		<tr>
			<td>Должность:</td>
			<td><input type="text" name="post" /></td>
		</tr>
		<tr>
			<td>Зарплата, грн:</td>
			<td><input type="text" name="salary" /></td>
		</tr>
		<tr>
			<td>Опыт работы, лет:</td>
			<td><input type="text" name="expirience" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" name="newApp" value="Создать"></td>
		</tr>
	</table>
	</form>
	<br />
	<a href="applicationsServlet">Назад</a> <a href="main.jsp">На главную</a>
	
</body>
</html>