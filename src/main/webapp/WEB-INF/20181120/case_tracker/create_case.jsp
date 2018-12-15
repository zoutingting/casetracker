<jsp:include page="/template/header.jsp"/>

<jsp:include page="../messagebox/error-list.jsp" /><br><br>

	<div id="create_case">
		
		<form method="post" action="create_case_result" class="CreateCaseForm">
            
            <table width="540" border="0" align="left" cellpadding="10" cellspacing="0">
            <fieldset>
				<legend>CaseTracker</legend>
                
                <p align="left">Subject
                <input autofocus type="text" name="subject" value="" required>
                  <script>
				    if (!("autofocus" in document.createElement("input"))) {
				      document.getElementById("subject").focus();
				    }
				  </script>                
                </p>
                                
            </fieldset>
            </table>
			
			<button type="submit" name="button">Create</button>
			
        </form>
	
	</div>
	
<jsp:include page="/template/footer.jsp"/>