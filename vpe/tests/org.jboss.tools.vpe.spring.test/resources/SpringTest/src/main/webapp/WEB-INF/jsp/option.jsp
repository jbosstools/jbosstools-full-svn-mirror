<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
	<style>
		.tall-option{
			 -moz-appearance: none;
			  height: 30px;
		}
	</style>
</head>
<body>
	<h1>Test of form:option</h1>
	
	<form:form modelAttribute="formBean">	
		<form:select path="selectedCategory">
            <form:option id="booksOption1" value="books"/>
        	<form:option id="sportOption1" value="sport"/>
            <form:option id="fishingOption1" value="fishing"/>
        </form:select>
        form:option with id attribute
		<br/><br/>	
		<form:select path="selectedCategory">
            <form:option id="booksOption2" value="books"
        			label="books selected"
        			cssStyle="width: 30px;" cssClass="tall-option"/>        	
        	<form:option id="sportOption2" value="sport"
        			label="sport selected"
        			cssStyle="width: 30px;" cssClass="tall-option"/>
            <form:option id="fishingOption2" value="fishing"
        			label="fishing selected"
        			cssStyle="width: 30px;" cssClass="tall-option"/>
        </form:select>
        form:option with id, cssStyle, cssClass and label attributes.
		<br/><br/>
		<form:select path="selectedCategory">
            <form:option id="booksOption3" value="books" disabled="true"/>
        	<form:option id="sportOption3" value="sport" disabled="true"/>
            <form:option id="fishingOption3" value="fishing"/>
        </form:select>
        form:option with id and disabled='true' attributes.
	</form:form>
</body>
</html>

