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
	<form action="${APPLICANT_MAIN_JSP}" method="post">
		Удалить соискателя: <c:out value="${applicant.name}" />
		<input type="hidden" name="applicantLogin" value="${applicant.login}"/><br />
		<input type="submit" name="confirmDelete" value="OK">
		<input type="submit" name="cancel" value="ОТМЕНА">
	</form>
</body>
</html>