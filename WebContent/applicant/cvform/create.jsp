<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create CVForm - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Создать новое резюме</h1>
	<hr/>
	
	<spring:url value="/${APPLICANT_CREATE_CV_HTML}" var="cvCreateAction" />
	
	<form:form method="POST" modelAttribute="applicantCV" action="${cvCreateAction}">
	
	<form:hidden path="id" />

		<spring:bind path="post">
			<label>Должность</label>
			<div>
				<form:input path="post" type="text" class="form-control" 
                                id="post" placeholder="post" />
			</div>
		</spring:bind>
		
		<spring:bind path="workExpirience">
			<label>Опыт работы</label>
			<div>
				<form:input path="workExpirience" type="text" class="form-control" 
                                id="workExpirience" placeholder="workExpirience" />
			</div>
		</spring:bind>
		
		<spring:bind path="skills">
		
			<label>Навыки</label>
			<div>
				<c:forEach items="${applicantCV.skills}" varStatus="i" >
					<form:input path="skills[${i.index}].name" type="text" placeholder="skills[${i.index}].name"/><br />
				</c:forEach>
			</div>
		</spring:bind>
		
		<spring:bind path="desiredSalary">
			<label>Желаемая зарплата</label>
			<div>
				<form:input path="desiredSalary" type="text" 
                                id="desiredSalary" placeholder="desiredSalary" />
			</div>
		</spring:bind>
		
		<spring:bind path="additionalInfo">
			<label>Дополнительная информация</label>
			<div>
				<form:input path="additionalInfo" type="text"
                                id="additionalInfo" placeholder="additionalInfo" />
			</div>
		</spring:bind>

		
 		<button type="submit">Add</button>
		
		

	</form:form>
</body>
</html>