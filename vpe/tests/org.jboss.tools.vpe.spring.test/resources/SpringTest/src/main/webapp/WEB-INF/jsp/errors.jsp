<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
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
	<table>
		<tr>
			<td />
			<%-- Show errors for newName field --%>
			<td>
				<form:errors id="nameErrors1" path="newName" />
			</td>
		</tr>
		<tr>
			<td />
			<%-- Show errors for newName field --%>
			<td>
				<form:errors id="nameErrors2" path="newName"
						cssClass="red-bordered-text"
						cssStyle="color: green;" />
			</td>
		</tr>
		<tr>
			<td>New Name:</td>
			<td><form:input path="newName" /></td>			
		</tr>
		<tr>
			<td/>
			<td align="right"><input type="submit" value="Test" /></td>
		</tr>
	</table>
</form:form>
</body>
</html>
