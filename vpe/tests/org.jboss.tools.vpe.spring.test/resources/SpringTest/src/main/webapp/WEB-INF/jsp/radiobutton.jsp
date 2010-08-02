<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
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
	<h1>Test of form:radiobutton</h1>
	
	<form:form modelAttribute="formBean">
		<form:radiobutton id="radiobutton1" path="selectedCategory"
				value="books"/>
			form:radiobutton with id and path attributes 
		<br/>
		<form:radiobutton id="radiobutton2" path="selectedCategory"
				value="sport" 
				cssStyle="width: 30px;" cssClass="tall-radiobutton" 
				label="form:radiobutton with id, path, cssStyle, cssClass and label attributes. (Must look as a big circle.)"/>
		<br/>
		<form:radiobutton id="radiobutton3" path="selectedCategory" 
				disabled="true" value="films"
				label="form:radiobutton with id, path and disabled='true' attributes."/>
		<br/>
	</form:form>
</body>
</html>
