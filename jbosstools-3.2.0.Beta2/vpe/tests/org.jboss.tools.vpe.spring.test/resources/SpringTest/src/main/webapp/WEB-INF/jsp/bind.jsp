<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<style>
.tall-radiobutton {
	-moz-appearance: none;
	height: 30px;
}
</style>
</head>
<body>
<h1>Test of spring:bind</h1>

<form:form modelAttribute="formBean">
	<table>
		<tr>
			<td>First Name:</td>
			<td><form:input path="user.firstName" /></td>
			<td>
				<spring:bind path="user.firstName">
					<font color="red">
						<b>${status.errorMessage}</b>
					</font>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td>Last Name:</td>
			<td><form:input path="user.lastName" /></td>
			<td>
				<div id="validationBinding">
					<spring:bind path="user.lastName">
						<font color="red">
							<b>${status.errorMessage}</b>
						</font>
					</spring:bind>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" value="Test" /></td>
		</tr>
	</table>

	<div id="outputBinding">
		<spring:bind path="selectedCategory">
			${status.value}
		</spring:bind>
	</div><br/>

	<div id="inputBinding">
		<spring:bind path="selectedCategory">
			<input type="text" name="${status.expression}" value="${status.value}" />
		</spring:bind>
	</div>
	
</form:form>
</body>
</html>
