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
	<h1>Test of form:select</h1>
	
	<form:form modelAttribute="formBean">
		<form:select id="select1"
				path="selectedCategories1" 
				items="${formBean.availableCategories}" cssClass="yellow-text"
				cssStyle="background-color:black" multiple="false"/>
		<br/>
		<form:select id="select2" size="2"
					path="selectedCategory">
            <form:option id="booksOption2" value="books"
        			label="books selected"/>        	
        	<form:option id="sportOption2" value="sport"
        			label="sport selected"/>
            <form:option id="fishingOption2" value="fishing"
        			label="fishing selected"/>
        </form:select>
	</form:form>
</body>
</html>
