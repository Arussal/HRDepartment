<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit employee - HRDepartment</title>
</head>
<body>
<h1>Редактировать данные о сотруднике</h1>
	<hr/>
	<br/>
	<h2>
		Сотрудник: <c:out value="${emp.name}" />
	</h2>
	<br/>
	<form action="${EMPLOYEE_CONTROLLER_SERVLET}" method="post">
		<table>
		<tr>
			<td>Возраст, лет:</td>
			<td><input type="hidden" name="id" value="${emp.id}"/><input type="text" name="age"  value="${emp.age}"/></td>
		</tr>
		<tr>
			<td>Образование:</td>
			<td><input type="text" name="education" value="${emp.education}"/></td>
		</tr>
		<tr>
			<td>Должность:</td>
			<td><input type="text" name="post" value="${emp.post}"/></td>
		</tr>
		<tr>
			<td>Отдел:</td>
			<td>
			<select name="department" style="width:150px">
				<c:forEach var="item" items="${departments}" >
					<option selected="${emp.department}"><c:out value="${item.name}"/></option>
				</c:forEach>
			</select>
			</td>
		</tr>
		<tr>
			<td>Зарплата, грн:</td>
			<td><input type="text" name="salary" value="${emp.salary}"/></td>
		</tr>
		<tr>
			<td>Опыт работы, лет:</td>
			<td><input type="text" name="expirience" value="${emp.workExpirience}"/></td>
		</tr>
		<tr>
			<td>Дата оформления:</td>
			<td>
				<select name="yearHire">
					<c:forEach var="item" items="${years}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
				<select name="monthHire">
					<c:forEach var="item" items="${months}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
				<select name="dayHire">
					<c:forEach var="item" items="${days}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Дата увольнения:</td>
			<td>
				<select name="yearFire">
					<c:forEach var="item" items="${years}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
				<select name="monthFire">
					<c:forEach var="item" items="${months}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
				<select name="dayFire">
					<c:forEach var="item" items="${days}">
						<option><c:out value="${item}" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>E-mail:</td>
			<td><input type="text" name="email" value="${emp.email}"/></td>
		</tr>
		<tr>
			<td>Телефон:</td>
			<td><input type="text" name="phone" value="${emp.phone}"/></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" name="saveEmployeeChanges" value="Сохранить"></td>
		</tr>
	</table>
	</form>
	<br />
	<a href="${EMPLOYEE_BASE_PAGE_SERVLET}">Назад</a>  <a href="${MANAGER_MAIN_JSP}">На главную</a>
</body>
</html>