<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hire employee - HRDepartment</title>
</head>
<body>
	<h1>Оформить кандидата в штат</h1>
	<hr/>
	<form action="${CANDIDATE_CONTROLLER_SERVLET}" method="post">
	<table>
		<tr>
			<td>Отдел</td>
			<td>
				<select name="department">
					<c:forEach var="dep" items="${departments}">
						<option><c:out value="${item.name}"></c:out></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Зарплата, грн</td>
			<td><input type="text" name="salaryInput"/></td>
		</tr>
		<tr>
			<td>Дата</td>
			<td>
				<select name="year">
					<c:forEach var="item" items="${years}">
						<option selected="${item}"><c:out value="${item}"/></option>
					</c:forEach>
				</select>
				<select name="month">
					<c:forEach var="item" items="${months}">
						<option selected="${item}"><c:out value="${item}"/></option>
					</c:forEach>
				</select>
				<select name="day">
					<c:forEach var="item" items="${days}">
						<option selected="${item}"><c:out value="${item}"/></option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" name="hireCandidate" value="Оформить"/>
	</form>
	<br />
	<br />
	<a href="${EMPLOYEE_BASE_PAGE_SERVLET}">Назад</a> <a href="${MANAGER_MAIN_JSP}">На главную</a>
</body>
</html>