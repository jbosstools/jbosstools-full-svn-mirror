<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<style>
		.yellow-text {
			color: yellow
		}
	</style>
</head>
<body>
	<h1>Test of form:form</h1>
	
	<form:form id="form1">
		This is a form:form with id-attribute.
	</form:form>
	
	<form:form id="form2" cssClass="yellow-text" cssStyle="background-color: black">
		This is a form:form with style and class attributes. Yellow text on black background.
	</form:form>
</body>
</html>
