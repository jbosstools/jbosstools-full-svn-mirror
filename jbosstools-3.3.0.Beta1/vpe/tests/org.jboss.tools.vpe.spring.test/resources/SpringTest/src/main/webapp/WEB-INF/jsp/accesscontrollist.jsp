<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
</head>
<body>
	<div id="accesscontrollist1">
		<security:accesscontrollist hasPermission="1,2" domainObject="formBean">
			This will be shown if the user has either of the permissions represented by the values "1" or "2" on the given object.
		</security:accesscontrollist>
	</div>
</body>
</html>
