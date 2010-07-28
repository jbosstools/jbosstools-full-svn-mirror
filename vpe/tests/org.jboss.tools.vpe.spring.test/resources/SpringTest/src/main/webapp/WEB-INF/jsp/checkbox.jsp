<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<style>
		.tall-checkbox {
			 -moz-appearance: none;
			  height: 30px;
		}
	</style>
</head>
<body>
	<h1>Test of form:checkbox</h1>
	
	<form:form modelAttribute="formBean">
		<form:checkbox id="checkbox1" path="chechbox1Selected" />
			form:checkbox with id and path attributes 
		<br/>
		<form:checkbox id="checkbox2" path="chechbox2Selected" 
				cssStyle="width: 30px;" cssClass="tall-checkbox" 
				label="form:checkbox with id, path, cssStyle, cssClass and label attributes. (Must look as a big square.)"/>
		<br/>
		<form:checkbox id="checkbox3" path="chechbox3Selected" 
				disabled="true"
				label="form:checkbox with id, path and disabled='true' attributes."/>
		<br/>
	</form:form>
</body>
</html>
