package framework;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHelper {

	/**
	 * authenticate with Google account
	 */
	public void check(HttpServletRequest request, HttpServletResponse response, String nextPage) {
		try {
			if (request.getUserPrincipal() == null) {
				response.getWriter().println("<p>Please <a href=\"" + Helper.getInstance().getSecurityhelper().createLoginURL(request) + "\">sign in</a> to continue.</p>");
			} else {

				if (!Helper.getInstance().getSecurityhelper().hasGeneralAccess()) {
					response.getWriter().println("<p>Development has been discontinued indefinitely." + " You can <a href=\"" + Helper.getInstance().getSecurityhelper().createLogoutURL(request) + "\">sign out</a>.</p>");
				} else {

					// maintenance mode
					if (Helper.getInstance().MAINTENANCE_MODE) {
						response.getWriter().println("<p>Under maintenance. Please try again later." + " You can <a href=\"" + Helper.getInstance().getSecurityhelper().createLogoutURL(request) + "\">sign out</a>.</p>");
					} else {

						// if logged in as Google user, and has access.
						Map<String, Object> user = Helper.getInstance().getSecurityhelper().getUser();
						Helper.getInstance().getRequestsessionhelper().setSessionAttribute(request, "user", user);
						Helper.getInstance().getRequestsessionhelper().setSessionAttribute(request, "GoogleUserEmail", user.get(DatastoreHelper.User.googleEmail.toString()).toString().trim());
						Helper.getInstance().getRequestsessionhelper().setSessionAttribute(request, "GoogleLogoutUrl", Helper.getInstance().getSecurityhelper().createLogoutURL(request));
						
						nextPage = nextPage.toLowerCase();
						Helper.getInstance().dispatch(request, response, nextPage);
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public boolean checkIfLoggedIn(HttpServletRequest request) {
		return request.getUserPrincipal() != null && Helper.getInstance().getSecurityhelper().hasGeneralAccess() && !Helper.getInstance().MAINTENANCE_MODE && Helper.getInstance().getRequestsessionhelper().getSessionAttribute(request, "GoogleUserEmail") != null;
	}

}
