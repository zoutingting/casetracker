package action.case_tracker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;

import framework.DatastoreHelper;
import framework.Helper;
import framework.SecurityHelper;

@WebServlet(name = "EditCase", urlPatterns = { "/edit_case" })

public class EditCase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!Helper.getInstance().getLoginhelper().checkIfLoggedIn(request)) {
			Helper.getInstance().getLoginhelper().check(request, response, this.getClass().getSimpleName());
		} else {
			if (Helper.getInstance().getSecurityhelper().hasAccess(this.getClass().getSimpleName())) {
				String userGoogleEmail = Helper.getInstance().getRequestsessionhelper().getSessionAttribute(request, "GoogleUserEmail").toString();
				try {
					String key = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "key");
					Map<String, Object> properties = Helper.getInstance().getDatastorehelper().getEntityProperties(key);

					String creator = properties.get(DatastoreHelper.CaseTracker.creator.toString()).toString();
					String[] members = properties.get(DatastoreHelper.CaseTracker.members.toString()).toString().split(SecurityHelper.SPACE_SEPARATOR);
					Set<String> membersSet = new HashSet<String>(Arrays.asList(members));
					if (!userGoogleEmail.equals(creator) && !membersSet.contains(userGoogleEmail))
						throw new Exception();

					Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "caseTracker", properties);
					Helper.getInstance().dispatch(request, response, "case_tracker/edit_case.jsp");
				} catch (Exception e) {
					Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "redirectNextPage", "view_all_pending_cases");
					Helper.getInstance().dispatch(request, response, "redirectNextPage.jsp");
				}
			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Helper.getInstance().getLoginhelper().checkIfLoggedIn(request)) {
			Helper.getInstance().getLoginhelper().check(request, response, this.getClass().getSimpleName());
		} else {
			if (Helper.getInstance().getSecurityhelper().hasAccess(this.getClass().getSimpleName())) {
				String userGoogleEmail = Helper.getInstance().getRequestsessionhelper().getSessionAttribute(request, "GoogleUserEmail").toString();
				try {
					String action = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "action");
					if ("modify".equals(action)) {
						String key = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "key");
						Map<String, Object> properties = Helper.getInstance().getDatastorehelper().getEntityProperties(key);

						String subject = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "subject");
						if (subject == null || subject.length() == 0 || subject.matches(".*[<>\"].*"))
							Helper.getInstance().dispatchResult(request, response, "1", "edit_case?key=" + key, "error", "Subject can't be empty or has special characters");
						String creator = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "creator");
						String timestamp = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "timestamp");
						String members = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "members");
						String status = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "status");
						String newMessage = Helper.getInstance().getRequestsessionhelper().getRequestParameter(request, "newMessage");
						String messages = processMessages(userGoogleEmail, ((Text) properties.get(DatastoreHelper.CaseTracker.messages.toString())).getValue(), newMessage.trim(), properties, subject, creator, timestamp, members, status);
						properties.put(DatastoreHelper.CaseTracker.subject.toString(), subject);
						properties.put(DatastoreHelper.CaseTracker.creator.toString(), creator);
						properties.put(DatastoreHelper.CaseTracker.timestamp.toString(), timestamp);
						properties.put(DatastoreHelper.CaseTracker.members.toString(), members);
						properties.put(DatastoreHelper.CaseTracker.status.toString(), status);
						properties.put(DatastoreHelper.CaseTracker.messages.toString(), new Text(messages));

						Helper.getInstance().getDatastorehelper().reviseEntity(key, properties);
						Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "redirectNextPage", "edit_case?key=" + key);
						Helper.getInstance().dispatch(request, response, "redirectNextPage.jsp");
					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "redirectNextPage", "view_all_pending_cases");
					Helper.getInstance().dispatch(request, response, "redirectNextPage.jsp");
				}
			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}

	private String processMessages(String userGoogleEmail, String messages, String newMessage, Map<String, Object> properties, String subject, String creator, String timestamp, String members, String status) {

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

		StringBuilder res = new StringBuilder();
		res.append(messages);

		if (!properties.get(DatastoreHelper.CaseTracker.subject.toString()).equals(subject))
			res.append(year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + userGoogleEmail + "</font> changed subject to \"" + subject + "\"<br>");

		if (!properties.get(DatastoreHelper.CaseTracker.creator.toString()).equals(creator))
			res.append(year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + userGoogleEmail + "</font> changed creator to \"" + creator + "\"<br>");

		if (!properties.get(DatastoreHelper.CaseTracker.timestamp.toString()).equals(timestamp))
			res.append(year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + userGoogleEmail + "</font> changed timestamp to \"" + timestamp + "\"<br>");

		if (!properties.get(DatastoreHelper.CaseTracker.members.toString()).equals(members))
			res.append(year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + userGoogleEmail + "</font> changed members to \"" + members + "\"<br>");

		if (!properties.get(DatastoreHelper.CaseTracker.status.toString()).equals(status))
			res.append(year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + userGoogleEmail + "</font> changed status to \"" + status + "\"<br>");

		if (!"".equals(newMessage))
			res.append(year + "/" + month + "/" + day + "&nbsp;" + hour + ":" + minute + "&nbsp;<font color=\"#0000FF\">" + userGoogleEmail + "</font>&nbsp;" + newMessage + "<br>");

		return res.toString();
	}
}