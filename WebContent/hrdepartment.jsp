<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manager page - HRDepartment</title>
</head>
<body>
	<h1>Кабинет отдела кадров</h1>
	<hr />
	<div id="button">
		<a href="applicationBaseServlet">Картотека вакансий</a><br/><br/>
		<a href="cvformBaseServlet">Картотека резюме</a><br/><br/>
		<a href="candidateBaseServlet">Картотека кандидатов</a><br/><br/>
		<a href="employeeBaseServlet">Картотека сотрудников</a>
		<br/>
		<br/>
		<br/>
		<br/>
		<a href="${HOME_PAGE_JSP}">Выйти из раздела</a>
	</div>
</body>
</html>