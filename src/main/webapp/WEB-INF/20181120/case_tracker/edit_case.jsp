<jsp:include page="/template/header.jsp" />

<%@ page import="java.util.Map"%>

<jsp:include page="../messagebox/error-list.jsp" /><br>
<br>

<div id="edit_case">


	<%
		Map<String, Object> properties = (Map<String, Object>) request.getAttribute("caseTracker");
	%>

	<form method="post" action="edit_case?action=modify" class="EditCaseForm">

		<!-- messageboard -->
		<table width="90%" border="0" align="left" cellpadding="10" cellspacing="0">
			<fieldset>
				<legend>
					<%=properties.get("subject")%></legend>

				<!-- messages -->
				<p align="left">
					<font size="-1"><%=((com.google.appengine.api.datastore.Text) properties.get("messages")).getValue()%></font>
				</p>


				<!-- newMessage -->
				<p align="left">
					<input autofocus type="text" name="newMessage" value="">
					<script>
						if (!("autofocus" in document.createElement("input"))) {
							document.getElementById("newMessage").focus();
						}
					</script>
				</p>

				<input type="hidden" name="creator" value="<%=properties.get("creator")%>"> <input type="hidden" name="timestamp" value="<%=properties.get("timestamp")%>"> <input type="hidden" name="key" value="<%=properties.get("key")%>"> <input type="hidden" name="members" value="<%=properties.get("members")%>">

			</fieldset>
		</table>


		<!-- subject -->
		<p align="center">
			Subject <input type="text" name="subject" value="<%=properties.get("subject")%>" style="width: 200px;">
		</p>

		<!-- status -->
		<p align="center">
			Status <input type="text" name="status" value="<%=properties.get("status")%>" style="width: 100px;">
		</p>



		<button type="submit" name="button">Submit</button>

	</form>


</div>

<jsp:include page="/template/footer.jsp" />