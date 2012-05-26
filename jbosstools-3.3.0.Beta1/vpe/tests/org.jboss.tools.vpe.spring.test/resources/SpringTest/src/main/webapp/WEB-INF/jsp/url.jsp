<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<body>
	<div id="url1">
		<spring:url value="http://community.jboss.org/en/jbosstools?view=discussions"/>
	</div>
	<div id="url2">
		<spring:url value="http://community.jboss.org/en/jbosstools">
			<spring:param name="view" value="discussions" />
			<spring:param name="emptyParam2"/>
		</spring:url>
	</div>
	<div id="url3">
		<spring:url value="http://community.jboss.org/en/jbosstools?view=discussions">
			<spring:param name="emptyParam2"/>
		</spring:url>
	</div>
</body>
</html>