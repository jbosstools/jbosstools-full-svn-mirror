<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
	<head>
		<title></title>
	</head>
	<body>
		<f:view>
	<h:form>
		<h:outputText value="#{bean1.property3.property1}" />
		<h:commandButton action="page2" value="Go to page2" />
		<h:commandButton action="page3" value="Go to page3" />
	</h:form>			
		</f:view>
	</body>	
</html>  
