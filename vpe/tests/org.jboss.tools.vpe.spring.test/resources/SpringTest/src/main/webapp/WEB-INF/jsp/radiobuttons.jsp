<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<style>
		.tall-radiobuttons {
			 -moz-appearance: none;
			  height: 30px;
		}
	</style>
</head>
<body>
	<h1>Test of form:radiobuttons</h1>
	
	<form:form modelAttribute="formBean">
		<form:radiobuttons id="radiobuttons1"
				path="selectedCategories1" 
				items="${formBean.availableCategories}" />
		<br/>form:checkboxes with id, items and path attributes.
		<br/><br/>
		<form:radiobuttons id="radiobuttons2"
					cssClass="tall-radiobuttons" cssStyle="width: 30px;"
					items="${formBean.availableCategories}"
					path="selectedCategories2" />
		<br/>form:radiobuttons with id, cssStyle, cssClass, items and path attributes.  (Must look as a big circle.)
		<br/><br/>
		<form:radiobuttons id="radiobuttons3" disabled="true"
					itemLabel="id" itemValue="id"
					items="${formBean.favoriteCategories}"
					path="selectedCategories3" />
		<br/>disabled form:radiobuttons with itemLabel attribute
		<br/><br/>
	</form:form>
</body>
</html>
