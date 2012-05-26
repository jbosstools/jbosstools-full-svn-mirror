<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h1>Test of spring:eval</h1>
	
	<form:form modelAttribute="formBean">
	
		<div id="eval1">
			<spring:eval expression="T(org.jboss.tools.vpe.spring.test.springtest.data.CategoryType).values()"/>
		</div>
	
		<div id="eval2">
			<spring:eval var="categories"
					expression="T(org.jboss.tools.vpe.spring.test.springtest.data.CategoryType).values()"/>
		</div>
	
		<form:select path="selectedCategories1">
            <form:options items="${categories}"/>
        </form:select>
        
	</form:form>
</body>
</html>
