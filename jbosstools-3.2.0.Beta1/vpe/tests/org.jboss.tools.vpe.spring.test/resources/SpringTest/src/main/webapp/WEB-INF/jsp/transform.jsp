<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" 
                 uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" 
                 uri="http://www.springframework.org/tags"%>

<html>
    <body>
    	<h1>Test of spring:transform</h1>
	    <form name="dayOfWeekForm" 
	          action="/transform.htm" 
	          method="POST">
	        <spring:bind path="command.dayOfWeekNumber">
	            <select id="dayOfWeekSelector" name="${status.expression}">
	                <c:forEach var="dayOfWeekNumber" items="${dayOfWeekNumbers}" >
	                	<span id="dayOfWeekTransformer">
	                    	<spring:transform value="${dayOfWeekNumber}" var="dayOfWeekName"/>
	                    </span>
	                    <c:choose>	                    
	                    	<c:when test="${status.value == dayOfWeekName}">
	                    		<option value="<c:out value="${dayOfWeekName}"/>"
	                    				selected="selected">
	                    	</c:when>
	                    	<c:otherwise> 
	                    		<option value="<c:out value="${dayOfWeekName}"/>">
	                    	</c:otherwise>	                    
	                    </c:choose>
	                    	<c:out value="${dayOfWeekName}"/>
	                    </option>
	                </c:forEach>
	            </select>
	        </spring:bind> 
	    </form>
    </body>
</html>