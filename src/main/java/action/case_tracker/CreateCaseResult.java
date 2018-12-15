package action.case_tracker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.DatastoreHelper;
import framework.Helper;

@WebServlet(name = "CreateCaseResult", urlPatterns = { "/create_case_result" })

public class CreateCaseResult extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Helper.getInstance().getLoginhelper().checkIfLoggedIn(request)) {
			Helper.getInstance().getLoginhelper().check(request, response, this.getClass().getSimpleName());
		} else {
			if (Helper.getInstance().getSecurityhelper().hasAccess(this.getClass().getSimpleName())) {
				String subject = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, DatastoreHelper.CaseTracker.subject.toString());
				String timestamp = Helper.getInstance().getTimesteamp();
				String creator = Helper.getInstance().getSecurityhelper().getUser().get(DatastoreHelper.User.googleEmail.toString()).toString().trim();
				String status = DatastoreHelper.CaseTracker_Status.created.getNumVal();
				try {
					if (subject == null || subject.length() == 0 || subject.matches(".*[<>\"].*"))
						throw new Exception();

					Date date = new Date();
					DateFormat secondFormat = new SimpleDateFormat("yyyy M d HH mm ss");
					TimeZone secondTime = TimeZone.getTimeZone("America/Los_Angeles");
					secondFormat.setTimeZone(secondTime);
					String t = secondFormat.format(date);
					String[] ts = t.split(" ");
					String year = ts[0];
					String month = ts[1];
					String day = ts[2];
					String hour = ts[3];
					String minute = ts[4];
					String message = year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + creator + "</font> created \"" + subject + "\"<br>";

					Helper.getInstance().getDatastorehelper().addCaseTracker(subject, timestamp, creator, status, "", message);
					Helper.getInstance().dispatchResult(request, response, "0", "view_all_pending_cases", "success", subject + " created successfully!");
				} catch (Exception e) {
					Helper.getInstance().dispatchResult(request, response, "1", "create_case", "error", "Subject can't be empty or has special characters");
				}

			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}
}