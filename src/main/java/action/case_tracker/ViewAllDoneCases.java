package action.case_tracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.DatastoreHelper;
import framework.Helper;
import framework.SecurityHelper;

@WebServlet(name = "ViewAllDoneCases", urlPatterns = { "/view_all_done_cases" })

public class ViewAllDoneCases extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!Helper.getInstance().getLoginhelper().checkIfLoggedIn(request)) {
			Helper.getInstance().getLoginhelper().check(request, response, this.getClass().getSimpleName());
		} else {
			if (Helper.getInstance().getSecurityhelper().hasAccess(this.getClass().getSimpleName())) {
				String userGoogleEmail = Helper.getInstance().getRequestsessionhelper().getSessionAttribute(request, "GoogleUserEmail").toString();

				Map<String, Map<String, Object>> caseTrackers = Helper.getInstance().getDatastorehelper().getCaseTrackers();
				Set<String> toBeRemoved = new HashSet<String>();
				for (Map.Entry<String, Map<String, Object>> caseTracker : caseTrackers.entrySet()) {
					Map<String, Object> properties = caseTracker.getValue();
					String status = properties.get(DatastoreHelper.CaseTracker.status.toString()).toString();
					String creator = properties.get(DatastoreHelper.CaseTracker.creator.toString()).toString();
					String[] members = properties.get(DatastoreHelper.CaseTracker.members.toString()).toString().split(SecurityHelper.SPACE_SEPARATOR);
					Set<String> membersSet = new HashSet<String>(Arrays.asList(members));
					if (!userGoogleEmail.equals(creator) && !membersSet.contains(userGoogleEmail))
						toBeRemoved.add(caseTracker.getKey());
					if (!DatastoreHelper.CaseTracker_Status.closed.getNumVal().equals(status))
						toBeRemoved.add(caseTracker.getKey());
				}
				for (String id : toBeRemoved)
					caseTrackers.remove(id);

				Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "caseTrackers", caseTrackers);
				Helper.getInstance().dispatch(request, response, "case_tracker/view_all_cases.jsp");
			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}
}