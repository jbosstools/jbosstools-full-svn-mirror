<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<style type="text/css">
.green {
	background-color: green;
}
.yellow {
	background-color: yellow;
}
.blue {
	background-color: blue;
}
</style>
</head>
<body>
<form:textarea id="textArea1" cssClass="green" cssStyle="font-weight: bold;" path="textarea1" /> 
<form:textarea id="textArea2" cssClass="blue" cssStyle="font-style: italic;" path="textarea2"/>
</body>
</html>
