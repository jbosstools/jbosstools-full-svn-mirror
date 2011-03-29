<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<body>
<form:password id="password1" path="password"/>
<form:password id="password2" path="password" disabled="true"/>
<form:password id="password3" path="password" showPassword="true"/>
<form:password id="password4" path="password" showPassword="true" disabled="true"/>
<form:password id="password5" path="password" showPassword="no"/>
</body>
</html>
