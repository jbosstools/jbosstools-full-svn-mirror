<%@ page info="JSP Page" %>
<!-- xml comment -->
<tag attr="value" expr="<%= expression() %>">
<%-- jsp comment --%>
text
<%!
/**
 * JavaDoc <tag>text</tag>
 * {@link com.company.package.Class}
 * @see com.company.package.Class
 */ 
private String definition() {
	return "string";
}
%>
text
<%= expression() %>
text
<%
/* multiline comment */ 
// singleline comment
%>
text
</tag>
