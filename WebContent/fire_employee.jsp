<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Fire employee - HRDepartment</title>
</head>
<body>
	<h1>Уволить сотрудника</h1>
	<hr />
	<form action="employeeControllerServlet" method="post">
		<table>
		<tr>
			<td>Задать дату увольнения:</td>
			<td>
				<select name="year">
					<c:forEach var="item" items="${years}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select name="month">
					<c:forEach var="item" items="${months}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select name="day">
					<c:forEach var="item" items="${days}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		</table>	
		<input type="submit" name="submitFireEmployee" value="Уволить" />

		<br />
		<br />
		<table border="1">
			<tr>
				<th></th>
				<th>ID</th>
				<th style="width:200px">ФИО</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>Должность</th>
				<th>Отдел</th>
				<th>Зарплата</th>
				<th>Опыт работы</th>
				<th>Дата оформления</th>
				<th>Дата увольнения</th>
				<th>E-mail</th>
				<th>Телефон</th>
				<th>Навыки</th>

			</tr>
			<c:forEach var="emp" items="${empList}">
			<tr>
				<td><input type="checkbox" name="empId" value="${emp.id}"></td>
				<td><c:out value="${emp.id}" /></td>
				<td><c:out value="${emp.name}" /></td>
				<td><c:out value="${emp.age}" /></td>
				<td><c:out value="${emp.education}" /></td>
				<td><c:out value="${emp.post}" /></td>
				<td><c:out value="${emp.department.name}" /></td>
				<td><c:out value="${emp.salary}" /></td>
				<td><c:out value="${emp.workExpirience}" /></td>
				<td><c:out value="${emp.hireDate}" /></td>
				<td><c:out value="${emp.fireDate}" /></td>
				<td><c:out value="${emp.email}" /></td>
				<td><c:out value="${emp.phone}" /></td>
				<td><c:out value="${emp.skills}" /></td>				
			</tr>
			</c:forEach>
		</table>
	</form>
	<br />
	<br />
	<a href="employeeBaseServlet">Назад</a> <a href="hrdepartment.jsp">На главную</a>
</body>
</html>