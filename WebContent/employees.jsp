<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HRDepartment - employees</title>
</head>
<body>
	<h1>Сотрудники</h1>
	<hr />
	<form action="employeeControllerServlet" method="post">
		Поиск сотрудника по критерию:
		<table>
			<tr>
				<td>ID</td>
				<td></td>
				<td>		
					<select name="id" style="width:100px">
						<c:forEach var="idEmployee" items="${idEmployeeSet}">
						<option><c:out value="${idEmployee}"></c:out></option>
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
						<c:forEach var="ageEmployee" items="${ageEmployeeSet}">
						<option><c:out value="${ageEmployee}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Должность</td>
				<td></td>
				<td>		
					<select name="post"  style="width:100px">
						<c:forEach var="postEmployee" items="${postEmployeeSet}">
						<option><c:out value="${postEmployee}"></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Образование</td>
				<td></td>
				<td>		
					<select name="education" style="width:100px">
						<c:forEach var="educationEmployee" items="${educationEmployeeSet}">
						<option><c:out value="${educationEmployee}"></c:out></option>
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
						<c:forEach var="expirienceEmployee" items="${expirienceEmployeeSet}">
						<option><c:out value="${expirienceEmployee}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Отдел</td>
				<td></td>
				<td>		
					<select name="department" style="width:100px">
						<c:forEach var="departmentEmployee" items="${departmentEmployeeSet}">
						<option><c:out value="${departmentEmployee}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr> 
			<tr>
				<td>Зарплата</td>
				<td>
					<select name="salaryComparable" style="width:150px">
						<c:forEach var="comparator" items="${comparableList}">
						<option><c:out value="${comparator}"></c:out></option>
						</c:forEach>
					</select>
				</td>
				<td>		
					<select name="salary" style="width:100px">
						<c:forEach var="salaryEmployee" items="${salaryEmployeeSet}">
						<option><c:out value="${salaryEmployee}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Дата приема на работу</td>
				<td>
					<select name="hireDateComparable" style="width:150px">
						<c:forEach var="comparator" items="${comparableList}">
						<option><c:out value="${comparator}"></c:out></option>
						</c:forEach>
					</select>
				</td>
				<td>		
					<select name="hireDate" style="width:100px">
						<c:forEach var="hireDateEmployee" items="${hireDateEmployeeSet}">
						<option><c:out value="${hireDateEmployee}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr> 			
			<tr>
				<td>Дата увольнения</td>
				<td>
					<select name="fireDateComparable" style="width:150px">
						<c:forEach var="comparator" items="${comparableList}">
						<option><c:out value="${comparator}"></c:out></option>
						</c:forEach>
					</select>
				</td>
				<td>		
					<select name="fireDate" style="width:100px">
						<c:forEach var="fireDateEmployee" items="${fireDateEmployeeSet}">
						<option><c:out value="${fireDateEmployee}" ></c:out></option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<input type="submit" name="findEmployee" value="Найти"><br /><br />
	</form>
	<br />
	<hr />
	<br />
	<form action="employeeControllerServlet" method="post">
		<input type="submit" name="showAllEmployees" value="Показать все" />
		<input type="submit" name="editEmployee" value="Редактировать" />	
		<input type="submit" name="fireEmployee" value="Уволить из штата" />
		<input type="submit" name="hireEmployee" value="Принять в штат" />

		<br />
		<br />
		<table border="1">
			<tr>
				<th></th>
				<th>ID</th>
				<th style="width:200px">ФИО</th>
				<th>Возраст</th>
				<th>Образование</th>
				<th>Должность</th>
				<th>Отдел</th>
				<th>Зарплата</th>
				<th>Опыт работы</th>
				<th>Дата оформления</th>
				<th>Дата увольнения</th>
				<th>E-mail</th>
				<th>Телефон</th>
				<th>Навыки</th>

			</tr>
			<c:forEach var="emp" items="${empList}">
			<tr>
				<td><input type="checkbox" name="candId" value="${emp.id}"></td>
				<td><c:out value="${emp.id}" /></td>
				<td><c:out value="${emp.name}" /></td>
				<td><c:out value="${emp.age}" /></td>
				<td><c:out value="${emp.education}" /></td>
				<td><c:out value="${emp.post}" /></td>
				<td><c:out value="${emp.department.name}" /></td>
				<td><c:out value="${emp.salary}" /></td>
				<td><c:out value="${emp.workExpirience}" /></td>
				<td><c:out value="${emp.hireDate}" /></td>
				<td><c:out value="${emp.fireDate}" /></td>
				<td><c:out value="${emp.email}" /></td>
				<td><c:out value="${emp.phone}" /></td>
				<td><c:out value="${emp.skills}" /></td>				
			</tr>
			</c:forEach>
		</table>
	</form>
	<a href="main.jsp">На главную</a>
</body>
</html>