<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Confirm Delete - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Подтвердить удаление</h1>
	<hr />
	<form action="applicantServlet" method="post">
		<table>
			<tr>
				<td colspan="2">Удалить соискателя <c:out value="${name}" /></td>
			</tr>
			<tr>
				<td><input type="submit" name="confirmDelete" value="OK"></td>
				<td><input type="submit" name="cancel" value="ОТМЕНА"></td>
			</tr>
		</table>
	</form>
</body>
</html>