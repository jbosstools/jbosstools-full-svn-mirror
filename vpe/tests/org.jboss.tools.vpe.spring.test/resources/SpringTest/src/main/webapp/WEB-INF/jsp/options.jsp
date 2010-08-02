<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<style>
		.tall-options {
			 -moz-appearance: none;
			  height: 30px;
		}
	</style>
</head>
<body>
	<h1>Test of form:options</h1>
	
	<form:form modelAttribute="formBean">
	
		<form:select path="selectedCategories1">
            <form:options id="options1" items="${formBean.availableCategories}"/>
        </form:select>
        form:options with id and items attribute
        <br/><br/>
        
        <form:select path="selectedCategories2">
        	<form:options id="options2" disabled="true"
        			cssStyle="width: 30px;" cssClass="tall-options"
        			items="${formBean.availableCategories}"/>
        </form:select>
        form:options with id, cssStyle, cssClass and disabled='true' attributes.
		<br/><br/>
		
		<form:select path="selectedCategories3">
            <form:options id="options3"
            		itemLabel="id" itemValue="id"
            		items="${formBean.favoriteCategories}"/>
        </form:select>
        form:options with id and itemLabel attributes.
        <br/><br/>
	</form:form>
</body>
</html>
