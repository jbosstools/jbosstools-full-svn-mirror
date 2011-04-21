<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<style>
.error {
	-moz-appearance: none;
	color: red;
	font: bold;
}
</style>
</head>
<body>
<h1>Test of spring:hasBindErrors</h1>

<div id="formBeanErrors">
	<spring:hasBindErrors name="formBean">
		<div style="border: 1px solid red; width: 300px;">
			<h2>Errors for all bean fields</h2>
			<div class="error">
				<ul>
					<c:forEach var="error" items="${errors.allErrors}">
						<li>${error.defaultMessage}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</spring:hasBindErrors>
</div><br/>

<div id="userErrors">
	<spring:hasBindErrors name="formBean">
		<div style="border: 1px solid red; width: 300px;">
			<h2>Errors for all user fields only</h2>
			<div class="error">
				<ul>
					<spring:bind path="formBean.user.*">
						<c:forEach var="error" items="${status.errorMessages}">
							<li>${error}</li>
						</c:forEach>
					</spring:bind>
				</ul>
			</div>
		</div>
	</spring:hasBindErrors>
</div>

<form:form modelAttribute="formBean">

	<table>
		<tr>
			<td>First Name:</td>
			<td><form:input path="user.firstName" /></td>
		</tr>
		<tr>
			<td>Last Name:</td>
			<td><form:input path="user.lastName" /></td>
		</tr>
		<tr>
			<td>Verification Number:</td>
			<td><form:input path="verificationNum" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" value="Test" /></td>
		</tr>
	</table>

</form:form>
</body>
</html>
