<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My CVForms - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Моя страница</h1>
	<hr />
	<br />
	<h2>Личная информация</h2>
	<hr />	
	<table>
			<tr>
				<th>ФИО</th>
				<th>Телефон</th>
				<th>E-mail</th>
				<th></th>
			</tr>
			<tr>
				<td>${applicant.surname} ${applicant.name} ${applicant.lastName}</td>
				<td>${applicant.phone}</td>
				<td>${applicant.email}</td>
				<td>
					<spring:url value="/applicant/${applicant.id}/edit.html" var="updateApplicant" />
					<button onclick="location.href='${updateApplicant}'">Редактировать</button>
				</td>
			</tr>
	</table>
	<h2>Мои резюме</h2>
	<hr />
	<table border="1">
		<tr>
			<th>#ID</th>
			<th>Статус отправки</th>
			<th>Должность</th>
			<th>Навыки</th>
			<th>Желаемая зарплата</th>
			<th></th>
		</tr>
		<c:forEach var="cv" items="${cvList}">
			<tr>
				<td>
					<spring:url value="/applicant/cvform/${cv.id}/view.html" var="cvformUrl" />
					<button onclick="location.href='${cvformUrl}'">${cv.id}</button>
				<td>${cv.sendStatus}</td>
				<td>${cv.post}</td>
				<td>
					<c:forEach var="skill" items="${cv.skills}" varStatus="i">
						${skill.name}
						<c:if test="${not i.last}">, </c:if>
					</c:forEach>
				<td>${cv.desiredSalary}</td>
				<td>
					<spring:url value="/applicant/cvform/${cv.id}/delete.html" var="deleteUrl" /> 
					<spring:url value="/applicant/cvform/${cv.id}/edit.html" var="updateUrl" />
					<spring:url value="/applicant/cvform/${cv.id}/send.html" var="sendUrl" />
					<button onclick="location.href='${updateUrl}'">Update</button>
				  	<button onclick="location.href='${deleteUrl}'">Delete</button>
				  	<button onclick="location.href='${sendUrl}'">Send</button>
                </td>
			</tr>
		</c:forEach>
	</table>

	<div>
		<spring:url value="/applicant/cvform/create.html" var="createUrl" />
		<button onclick="location.href='${createUrl}'">Создать</button>
	</div>
	<br />	
	<a href="${pageContext.request.contextPath}/${HOME_PAGE_JSP}">Выйти из раздела</a>
	${footer}
</body>
</html>
