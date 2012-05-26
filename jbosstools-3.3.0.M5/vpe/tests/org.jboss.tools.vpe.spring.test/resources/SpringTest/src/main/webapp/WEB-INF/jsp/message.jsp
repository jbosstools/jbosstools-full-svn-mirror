<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>
	<spring:message id="message1" code="some.id.in.bundle"/>
	<spring:message id="message2" code="someId1, someId2, someId3" htmlEscape="true"/>
	<spring:message id="message3" code="someId1 someId2 someId3" htmlEscape="true"/>
	<spring:message id="message4" code="someId1 someId2 someId3" argumentSeparator=" "/>
</body>
</html>
