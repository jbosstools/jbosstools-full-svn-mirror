<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="application/xhtml+xml; charset=UTF-8" />
	<![CDATA[<?xml version="1.0" encoding="UTF-8"?>]]>
	<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">]]>
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>jsp:setProperty test</title>
	</head>
	<body>
	<h1>jsp:setProperty test</h1>
	<jsp:useBean scope="page" class="org.jboss.jsp.test.Calendar" id="useBean"/>
	<jsp:setProperty name="useBean" property="username" value="User" />
	<h2>Calendar for <jsp:getProperty name="useBean"
		property="username" /></h2>
	</body>
	</html>
</jsp:root>
