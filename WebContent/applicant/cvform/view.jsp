<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CV #${cv.id} - Applicant - HRDepartment</title>
</head>
<body>
	<h1>Просмотр резюме #${cv.id}</h1>
	<hr />
	<div>
		<label>Фамилия:</label> ${cv.surname}
	</div>
		<div>
		<label>Имя:</label> ${cv.name}
	</div>
		<div>
		<label>Отчество:</label> ${cv.lastName}
	</div>
	<div>
		<label>Возраст:</label> ${cv.age}
	</div>
	<div>
		<label>Образование:</label> ${cv.education}
	</div>
	<div>
		<label>Телефон:</label> ${cv.phone}
	</div>
	<div>
		<label>E-mail:</label> ${cv.email}
	</div>
	<div>
		<label>Должность:</label> ${cv.post}
	</div>
	<div>
		<label>Опыт работы:</label> ${cv.workExpirience}
	</div>
	<div>
		<label>Навыки:</label>
		<div>
			<c:forEach items="${cv.skills}" var="skill">
					${skill.name}
					<c:if test="${not i.last}">, </c:if>
			</c:forEach>
		</div>
	</div>
	<div>
		<label>Желаемая зарплата:</label> ${cv.desiredSalary}
	</div>
	<div>
		<label>Дополнительная информация:</label> ${cv.additionalInfo}
	</div>
</body>
</html>