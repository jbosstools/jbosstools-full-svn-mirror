<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<style>
.redText {
	-moz-appearance: none;
	color: red;
	font: bold;
}
</style>
</head>
<body>
<h1>Test of spring:nestedPath</h1>

<form:form modelAttribute="formBean">
	<div id="userNestedPath">
		<spring:nestedPath path="user">
			<table>
				<tr>
					<td>First Name:</td>
					<td><form:input path="firstName" /></td>
					<td>
						<form:errors id="firstNameErrors"
								path="firstName"						
								cssClass="redText" />
					</td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><form:input path="lastName" /></td>
					<td>
						<form:errors id="lastNameErrors"
								path="lastName"						
								cssClass="redText" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right"><input type="submit" value="Test" /></td>
				</tr>
			</table>
		</spring:nestedPath>
	</div>
</form:form>
</body>
</html>
