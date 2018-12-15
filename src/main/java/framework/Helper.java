package framework;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Helper {

	private static final DatastoreHelper datastoreHelper = new DatastoreHelper();
	private static final SecurityHelper securityHelper = new SecurityHelper();
	private static final LoginHelper loginHelper = new LoginHelper();
	private static final RequestSessionHelper requestSessionHelper = new RequestSessionHelper();
	private static final String FOLDER_PATH = "WEB-INF/20181120/";
	public boolean MAINTENANCE_MODE = false;
	private static Helper instance = null;

	private Helper() {

	}

	public static Helper getInstance() {
		synchronized (Helper.class) {
			if (instance == null)
				instance = new Helper();
		}
		return instance;
	}

	public LoginHelper getLoginhelper() {
		return loginHelper;
	}

	public DatastoreHelper getDatastorehelper() {
		return datastoreHelper;
	}

	public SecurityHelper getSecurityhelper() {
		return securityHelper;
	}

	public RequestSessionHelper getRequestsessionhelper() {
		return requestSessionHelper;
	}

	public String getTimesteamp() {
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
		String second = ts[5];
		return year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second;
	}

	public void dispatch(HttpServletRequest request, HttpServletResponse response, String path) {
		RequestDispatcher dispatch = request.getRequestDispatcher(FOLDER_PATH + path);
		try {
			dispatch.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public void dispatchResult(HttpServletRequest request, HttpServletResponse response, String messageDisplayLength, String nextPage, String messageType, String message) {
		request.setAttribute("messageDisplayLength", messageDisplayLength);
		request.setAttribute("nextPage", nextPage);
		request.setAttribute("message_type", messageType);
		request.setAttribute("message", message);
		dispatch(request, response, "messagebox/result.jsp");
	}

}
