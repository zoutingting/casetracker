package action;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.Helper;

@WebServlet(name = "Menu", urlPatterns = { "/menu" })
public class Menu extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!Helper.getInstance().getLoginhelper().checkIfLoggedIn(request)) {
			Helper.getInstance().getLoginhelper().check(request, response, this.getClass().getSimpleName());
		} else {
			if (Helper.getInstance().getSecurityhelper().hasAccess(this.getClass().getSimpleName())) {
				Helper.getInstance().getRequestsessionhelper().setRequestAttribute(request, "redirectNextPage", "view_all_pending_cases");
				Helper.getInstance().dispatch(request, response, "redirectNextPage.jsp");
			} else {
				Helper.getInstance().dispatch(request, response, "error.jsp");
			}
		}
	}

}
