package com.yiduihuan.tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yiduihuan.bean.User;

/**
 * 
 * @author Howe(howechiang@gmail.com)
 *
 */
public class SessionManger {

	public static final String USER_SESSION_KEY = "a9fa9a2e9cb87fad";

	/**
	 * 
	 * @param req
	 * @return
	 */
	public static boolean checkLogin(HttpServletRequest req) {
		
		return getCurrentUser(req) != null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	public static User getCurrentUser(HttpServletRequest req) {
		
		HttpSession session = req.getSession(false);
		if (session != null) {
			Object obj = session.getAttribute(USER_SESSION_KEY);
			if (obj != null)		
				return (User) obj;
			else			
				return null;
		} else
			return null;
	}
}
