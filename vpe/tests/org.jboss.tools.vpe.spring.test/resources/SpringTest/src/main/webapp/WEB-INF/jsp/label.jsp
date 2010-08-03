<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<style type="text/css">
.green {
	background-color: green;
}
.yellow {
	background-color: yellow;
	font-weight: bold;
	font-style: italic;
}
.blue {
	background-color: blue;
}
</style>
</head>
<body>
<form:label id="label1" cssClass="green" cssStyle="font-weight: bold;">
Label 1 
</form:label>
<form:label id="label2" for="text" cssClass="blue" cssStyle="font-style: italic;">
Label 2 
</form:label>
<input type="text" id="text" />
</body>
</html>
