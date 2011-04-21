<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
<head>
</head>
<body>

<f:view>
	<h1><h:outputText value="panelGrid" /></h1>
	<h:panelGrid columns="2" rowClasses="oddRows,evenRows" id="panelGrid" >

		<h:outputText value="column 1" />
		<h:outputText value="column 2" />

		<h:outputText value="1" />
		<h:outputText value="2" />
		<h:outputText value="3" />
		<h:outputText value="4" />

	</h:panelGrid>
</f:view>
</body>
</html>