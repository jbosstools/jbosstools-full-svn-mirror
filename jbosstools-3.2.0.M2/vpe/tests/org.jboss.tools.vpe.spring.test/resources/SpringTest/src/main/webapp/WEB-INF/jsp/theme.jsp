<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>
	<spring:theme id="theme1" code="some.id.in.bundle"/>
	<spring:theme id="theme2" code="someId1, someId2, someId3" htmlEscape="true"/>
	<spring:theme id="theme3" code="someId1 someId2 someId3" htmlEscape="true"/>
	<spring:theme id="theme4" code="someId1 someId2 someId3" argumentSeparator=" "/>
</body>
</html>
