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
	<jsp:useBean id="calendar" scope="page" class="org.jboss.jsp.test.Calendar" />
	<jsp:setProperty name="calendar" property="username" value="User" id="set_property"/>
	<h2>Calendar for <jsp:getProperty name="calendar"
		property="username" /></h2>
	</body>
	</html>
</jsp:root>
