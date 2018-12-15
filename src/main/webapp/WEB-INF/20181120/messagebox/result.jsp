<% 
	String messageDisplayLength = (String) request.getAttribute("messageDisplayLength");
	String nextPage = (String) request.getAttribute("nextPage");
	String message_type = (String) request.getAttribute("message_type");
	String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html>
<head>
<title>ERP Demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="./css/messagebox/forms.css" rel="stylesheet" type="text/css" />
<meta HTTP-EQUIV="REFRESH" content="<%=messageDisplayLength%>; url=<%=nextPage%>">
</head>

<body>

<center>

<p class="<%=message_type%>"> <!-- .info, .success, .warning, .error, .validation -->
<%= message %>
</p>

</center>

</body>
</html>