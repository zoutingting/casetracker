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

@WebServlet(name = "RemoveCase", urlPatterns = { "/remove_case" })

public class RemoveCase extends HttpServlet {
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

					Helper.getInstance().getDatastorehelper().removeEntity(key);
					Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "redirectNextPage", "view_all_pending_cases");
					Helper.getInstance().dispatch(request, response, "redirectNextPage.jsp");
				} catch (Exception e) {
					Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "redirectNextPage", "view_all_pending_cases");
					Helper.getInstance().dispatch(request, response, "redirectNextPage.jsp");
				}
			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}
}