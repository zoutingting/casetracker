﻿<jsp:include page="/template/header_view_all_cases.jsp" />

<%@ page import="java.util.Map"%>

<div id="view_all_cases">

	<%
		Map<String, Map<String, Object>> caseTrackers = (Map<String, Map<String, Object>>) request.getAttribute("caseTrackers");
	%>

	Total:<%=caseTrackers.size()%><br>
	<p>
		<input type="text" id="kwd_search" value="" />
	</p>

	<table id="rounded-corner">
		<thead>
			<tr>
				<th scope="col" width="100">Status</th>
				<th scope="col" width="500">Subject</th>
			</tr>
		</thead>

		<tbody>

			<%
				for (Map.Entry<String, Map<String, Object>> caseTracker : caseTrackers.entrySet()) {
			%>

			<tr>

				<%
					String subject = String.valueOf(caseTracker.getValue().get("subject"));
						if (subject.length() > 20)
							subject = subject.substring(0, 21) + "...";
				%>
				<th><%=String.valueOf(caseTracker.getValue().get("status"))%></th>
				<td data-href="edit_case?key=<%=String.valueOf(caseTracker.getValue().get("key"))%>"><%=subject%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
</div>

<jsp:include page="/template/footer.jsp" />