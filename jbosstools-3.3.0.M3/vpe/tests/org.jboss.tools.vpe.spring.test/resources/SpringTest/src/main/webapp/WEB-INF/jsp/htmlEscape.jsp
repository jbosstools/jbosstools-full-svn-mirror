<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <body>
    	<h1>Test of spring:htmlEscape</h1>
    	
    	<form:form modelAttribute="formBean">
			
			<span id="htmlEscapeTrue">
				<spring:htmlEscape defaultHtmlEscape="true" />
			</span>
			<spring:bind path="javascriptStr">
				${status.value}
			</spring:bind>
			
			<span id="htmlEscapeFalse">
				<spring:htmlEscape defaultHtmlEscape="false" />
			</span>
			<spring:bind path="javascriptStr">
				${status.value}
			</spring:bind>
			
		</form:form>
    </body>
</html>