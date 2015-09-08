<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CVForms - HRDepatment</title>
</head>
<body>
	<h1>Резюме</h1>
	<hr />
	<br />
	<form action="cvformControllerServlet" method="post">
		Поиск резюме по критерию:
		<table>
			<tr>
				<td>ID</td>
				<td></td>
				<td>		
					<select name="id" style="width:100px">
						<c:forEach var="idCV" items="${idCVSet}">
						<option><c:out value="${idCV}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Возраст</td>
				<td>
					<select name="ageComparable" style="width:150px">
						<c:forEach var="comparator" items="${comparableList}">
						<option><c:out value="${comparator}"></c:out></option>
						</c:forEach>
					</select>
				</td>
				<td>		
					<select name="age" style="width:100px">
						<c:forEach var="ageCV" items="${ageCVSet}">
						<option><c:out value="${ageCV}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Должность</td>
				<td></td>
				<td>		
					<select name="post"  style="width:100px">
						<c:forEach var="postCV" items="${postCVSet}">
						<option><c:out value="${postCV}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Образование</td>
				<td></td>
				<td>		
					<select name="education" style="width:100px">
						<c:forEach var="educationCV" items="${educationCVSet}">
						<option><c:out value="${educationCV}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Опыт работы</td>
				<td>
					<select name="expirienceComparable" style="width:150px">
						<c:forEach var="comparator" items="${comparableList}">
						<option><c:out value="${comparator}"></c:out></option>
						</c:forEach>
					</select>
				</td>
				<td>		
					<select name="expirience" style="width:100px">
						<c:forEach var="expirienceCV" items="${expirienceCVSet}">
						<option><c:out value="${expirienceCV}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>  
		</table>
		<input type="submit" name="findCV" value="Найти">
	</form>
	<br />
	<hr />
	<br />
	
	<form action="cvformControllerServlet" method="post">
		<input type="submit" name="showAllCV" value="Показать все" />
		<input type="submit" name="deleteCV" value="Удалить отмеченные" />
		<br />
		<br />
		<table border="1">
			<tr>
				<th></th>
				<th>ID</th>
				<th>ФИО</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>E-mail</th>
				<th>Телефон</th>
				<th>Должность</th>
				<th>Навыки</th>
				<th>Опыт работы</th>
				<th>Желаемая зарплата</th>
				<th>Дополнительная информация</th>
			</tr>
			<c:forEach var="cv" items="${cvList}">
			<tr>
				<td><input type="checkbox" name="cvId" value="${cv.id}"></td>
				<td><c:out value="${cv.id}" /></td>
				<td><c:out value="${cv.name}" /></td>
				<td><c:out value="${cv.age}" /></td>
				<td><c:out value="${cv.education}" /></td>
				<td><c:out value="${cv.email}" /></td>
				<td><c:out value="${cv.phone}" /></td>
				<td><c:out value="${cv.post}" /></td>
				<td><c:out value="${cv.skills}" /></td>
				<td><c:out value="${cv.workExpirience}" /></td>
				<td><c:out value="${cv.desiredSalary}" /></td>
				<td><c:out value="${cv.additionalInfo}" /></td>
			</tr>
			</c:forEach>
		</table>
	</form>
	<br />
	<br />
	<a href="hrdepartment.jsp">На главную</a>
</body>
</html>