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
	<h1>Test of form:checkboxes</h1>
	
	<form:form modelAttribute="formBean">
		<form:checkboxes id="checkboxes1" items="${formBean.availableCategories}"
					path="selectedCategories" />
		<br/>form:checkboxes with id, items and path attributes.
		<br/><br/>		
		<form:checkboxes id="checkboxes2"
					cssClass="tall-checkbox" cssStyle="width: 30px;"
					items="${formBean.availableCategories}"
					path="selectedCategories" />
		<br/>form:checkboxes with id, cssStyle, cssClass, items and path attributes.  (Must look as a big square.)
		<br/><br/>
		<form:checkboxes id="checkboxes3" disabled="true"
					items="${formBean.availableCategories}"
					path="selectedCategories" />
		<br/>disabled form:checkboxes
		<br/><br/>
	</form:form>
</body>
</html>
