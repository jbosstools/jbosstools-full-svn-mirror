<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<body>
<form:password id="password1" value="password"/>
<form:password id="password2" value="password" disabled="true"/>
<form:password id="password3" value="password" showPassword="true"/>
<form:password id="password4" value="password" showPassword="true" disabled="true"/>
<form:password id="password5" value="password" showPassword="no"/>
</body>
</html>
