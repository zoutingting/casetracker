<html>
<head>
<title>CaseTracker</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="./favicon.ico">

<link rel="stylesheet" type="text/css" href="./css/old/general.css">
<link rel="stylesheet" type="text/css" href="./css/view_all_cases/itunes.css">




<!-- hyperlink on table.tr -->
<script type="text/javascript" src="./js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $('td').click(function(){
        window.location = $(this).attr('data-href');
        return false;
    });
});
</script>


<!-- instant search bar -->
<script type="text/javascript" src="./js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
// When document is ready: this gets fired before body onload :)
$(document).ready(function(){
	// Write on keyup event of keyword input element
	$("#kwd_search").keyup(function(){
		// When value of the input is not blank
		if( $(this).val() != "")
		{
			// Show only matching TR, hide rest of them
			$("#rounded-corner tbody>tr").hide();
			$("#rounded-corner th:contains-ci('" + $(this).val() + "')").parent("tr").show();
			$("#rounded-corner td:contains-ci('" + $(this).val() + "')").parent("tr").show();
		}
		else
		{
			// When there is no input or clean again, show everything back
			$("#rounded-corner tbody>tr").show();
		}
	});
});
// jQuery expression for case-insensitive filter
$.extend($.expr[":"], 
{
    "contains-ci": function(elem, i, match, array) 
	{
		return (elem.textContent || elem.innerText || $(elem).text() || "").toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
	}
});

</script>

</head>

<body><center>
<br><br><img src="./img/logo4.png" height="120px" /><!--br><br><a href="view_all_cases">All Cases</a>   <a href="<%=(String) session.getAttribute("GoogleLogoutUrl") %>">登出</a> --><br><br>

	<a href="create_case">New</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="view_all_pending_cases">Pending</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="view_all_done_cases">Done</a><br><br>