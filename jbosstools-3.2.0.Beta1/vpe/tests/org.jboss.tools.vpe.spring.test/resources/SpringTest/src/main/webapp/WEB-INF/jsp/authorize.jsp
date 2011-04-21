<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
</head>
<body>
	<div id="authorize1">
		<security:authorize ifAllGranted="ROLE_USER">
			<a href="#">Logout</a>
		</security:authorize>
	</div>
	<div id="authorize2">
		<security:authorize ifAllGranted="ROLE_ANONYMOUS">
			<a href="#">Login</a>
		</security:authorize>
	</div>
</body>
</html>