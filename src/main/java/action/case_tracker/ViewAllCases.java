package action.case_tracker;

import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.Helper;

@WebServlet(name = "ViewAllCases", urlPatterns = { "/view_all_cases" })

public class ViewAllCases extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!Helper.getInstance().getLoginhelper().checkIfLoggedIn(request)) {
			Helper.getInstance().getLoginhelper().check(request, response, this.getClass().getSimpleName());
		} else {
			if (Helper.getInstance().getSecurityhelper().hasAccess(this.getClass().getSimpleName())) {
				Map<String, Map<String, Object>> caseTrackers = Helper.getInstance().getDatastorehelper().getCaseTrackers();
				Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "caseTrackers", caseTrackers);
				Helper.getInstance().dispatch(request, response, "case_tracker/view_all_cases.jsp");
			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}
}