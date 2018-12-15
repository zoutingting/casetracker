package framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class RequestSessionHelper {

	public void setSessionAttribute(HttpServletRequest request, String sessionName, Object sessionValue) {
		HttpSession session = request.getSession();
		session.setAttribute(sessionName, sessionValue);
	}

	public Object getSessionAttribute(HttpServletRequest request, String sessionName) {
		HttpSession session = request.getSession();
		return session.getAttribute(sessionName);
	}

	public void setRequestAttribute(HttpServletRequest request, String requestName, Object requestValue) {
		request.setAttribute(requestName, requestValue);
	}

	public Object getRequestAttribute(HttpServletRequest request, String requestName) {
		return request.getAttribute(requestName);
	}

	/**
	 * get user form data
	 */
	public String getRequestParameter(HttpServletRequest request, String requestName) {
		return request.getParameter(requestName) == null ? "" : request.getParameter(requestName).toString().trim();
	}

}
