<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<style>
		.yellow-text {
			color: yellow
		}
	</style>
</head>
<body>
	<h1>Test of spring:escapeBody</h1>
	
	Must be 3 buttons in one row:
	<span id="escapeBody">
	    <spring:escapeBody>
			<input type="button" value="button1"/><input type="button" value="button2"/>
	    </spring:escapeBody>
	    <spring:escapeBody>
			<input type="button" value="button3"/>
	    </spring:escapeBody>
	</span>
</body>
</html>
