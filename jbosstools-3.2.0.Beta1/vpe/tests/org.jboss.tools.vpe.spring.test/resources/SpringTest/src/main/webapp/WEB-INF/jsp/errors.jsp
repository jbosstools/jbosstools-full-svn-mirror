<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<style>
.red-bordered-text {
	border: 1px solid red;
}
</style>
</head>
<body>
<h1>Test of form:errors</h1>

<form:form commandName="formBean" method="POST">
	<form:errors id="allFieldsErrors" path="*" />
	<table>
		<tr>
			<td>First Name:</td>
			<td><form:input path="user.firstName" /></td>
			<td>
				<form:errors id="firstNameErrors"
						path="user.firstName"						
						cssClass="red-bordered-text" />
			</td>			
		</tr>
		<tr>
			<td>Last Name:</td>
			<td><form:input path="user.lastName" /></td>
			<td>
				<form:errors id="lastNameErrors"
						path="user.lastName"
						cssStyle="color: red;" />
			</td>			
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" value="Test" /></td>
		</tr>
	</table>
</form:form>
</body>
</html>
