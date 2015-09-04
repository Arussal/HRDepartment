<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HRDepartment - fire employee</title>
</head>
<body>
	<h1>Оформить кандидата в штат</h1>
	<hr/>
	<form action="" method="post">
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
			<td>Зарплата</td>
			<td><input type="text" name="salaryInput"/></td>
		</tr>
		<tr>
			<td>Дата</td>
			<td><input type="text" name="dateInput"/></td>
		</tr>
	</table>
	<input type="submit" name="hireCandidate" value="Оформить"/>
	</form>
</body>
</html>