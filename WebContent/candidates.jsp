<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HRDepatment - candidates</title>
</head>
<body>
	<h1>Кандидаты</h1>
	<hr />
	<br />
	<form action="candidateControllerServlet" method="post">
		Поиск кандидата по критерию:
		<table>
			<tr>
				<td>ID</td>
				<td></td>
				<td>		
					<select name="id" style="width:100px">
						<c:forEach var="idCandidate" items="${idCandidateSet}">
						<option><c:out value="${idCandidate}"></c:out></option>
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
						<c:forEach var="ageCandidate" items="${ageCandidateSet}">
						<option><c:out value="${ageCandidate}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Должность</td>
				<td></td>
				<td>		
					<select name="post"  style="width:100px">
						<c:forEach var="postCandidate" items="${postCandidateSet}">
						<option><c:out value="${postCandidate}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Образование</td>
				<td></td>
				<td>		
					<select name="education" style="width:100px">
						<c:forEach var="educationCandidate" items="${educationCandidateSet}">
						<option><c:out value="${educationCandidate}"></c:out></option>
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
						<c:forEach var="expirienceCandidate" items="${expirienceCandidateSet}">
						<option><c:out value="${expirienceCandidate}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>  
		</table>
		<input type="submit" name="findCandidate" value="Найти"><br /><br />
	</form>
	<form action="applicationBaseServlet" method="post">
		<input type="submit" name="findNewCandidate" value="Найти нового кандидата">
	</form>
	<br />
	<hr />
	<br />
	
	<form action="candidateControllerServlet" method="post">
		<input type="submit" name="showAllCandidates" value="Показать все" />
		<input type="submit" name="deleteCandidate" value="Удалить отмеченные" />
		<br />
		<br />
		<table border="1">
			<tr>
				<th></th>
				<th>ID</th>
				<th style="width:200px">ФИО</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>E-mail</th>
				<th>Телефон</th>
				<th>Должность</th>
				<th>Навыки</th>
				<th>Опыт работы</th>
			</tr>
			<c:forEach var="cand" items="${candSet}">
			<tr>
				<td><input type="checkbox" name="candId" value="${cand.id}"></td>
				<td><c:out value="${cand.id}" /></td>
				<td><c:out value="${cand.name}" /></td>
				<td><c:out value="${cand.age}" /></td>
				<td><c:out value="${cand.education}" /></td>
				<td><c:out value="${cand.email}" /></td>
				<td><c:out value="${cand.phone}" /></td>
				<td><c:out value="${cand.post}" /></td>
				<td><c:out value="${cand.skills}" /></td>
				<td><c:out value="${cand.workExpirience}" /></td>
			</tr>
			</c:forEach>
		</table>
	</form>
	<br />
	<br />
	<a href="main.jsp">На главную</a>
</body>
</html>