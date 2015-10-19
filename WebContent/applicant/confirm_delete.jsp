<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Confirm delete - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Подтвердить удаление пользователя</h1>
	<hr />
	<form action="${pageContext.request.contextPath}/${APPLICANT_SUCCESS_DELETE_HTML}" method="post">
		Удалить соискателя: ${applicantToDelete.login} <br/>
		<input type="submit" name="apply" value="OK">
		<input type="submit" name="cancel" value="ОТМЕНА">
	</form>
</body>
</html>