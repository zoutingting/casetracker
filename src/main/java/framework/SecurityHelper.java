package framework;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class SecurityHelper {

	private static final UserService userService = UserServiceFactory.getUserService();
	public static final String SPACE_SEPARATOR = " ";

	private String getGoogleEmailForCurrentUser() {
		// String GoogleName = request.getUserPrincipal().getName();
		return userService.getCurrentUser().getEmail();
	}

	public boolean hasGeneralAccess() {
		return getUser() != null;
	}

	public boolean hasAccess(String permission) {
		final Map<String, Object> user = getUser();
		if (user != null) {
			String[] permissions = user.get(DatastoreHelper.User.permissions.toString()).toString().trim().split(SPACE_SEPARATOR);
			Set<String> lookup = new HashSet<String>(Arrays.asList(permissions));
			return lookup.contains(permission);
		}
		return false;
	}

	public Map<String, Object> getUser() {
		String googleUserEmail = getGoogleEmailForCurrentUser();
		final Map<String, Map<String, Object>> users = Helper.getInstance().getDatastorehelper().getUsers();
		return users.get(googleUserEmail) != null ? users.get(googleUserEmail) : null;
	}

	public String createLoginURL(HttpServletRequest request) {
		String thisUrI = request.getRequestURI();
		return userService.createLoginURL(thisUrI);
	}

	public String createLogoutURL(HttpServletRequest request) {
		String thisUrI = request.getRequestURI();
		return userService.createLogoutURL(thisUrI);
	}

}
