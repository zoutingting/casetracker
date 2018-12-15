<% 
	String error_type = (String) request.getAttribute("error_type");
	if (error_type==null) error_type = "error";
%>


<%
	java.util.List<String> errors = (java.util.List<String>) request.getAttribute("errors");
	if (errors != null && errors.size() > 0) {
%>
		<p class="<%=error_type%>"> <!-- .info, .success, .warning, .error, .validation -->
<%
		for (String error : errors) {
			out.println(error + "<br>");
		}
		out.println("</p>");
	}
%>