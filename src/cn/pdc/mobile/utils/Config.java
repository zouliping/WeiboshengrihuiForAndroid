package cn.pdc.mobile.utils;

public class Config {

	public static String uid = "";
	public static String uname = "";

	public static final String IP = "http://192.168.0.126:9000";

	// login uri
	public static final String LOGIN = IP + "/user/login";
	// get user detail info uri
	public static final String GET_USER_INFO = IP
			+ "/indiv/getpro?indivname=$indivname&uid=";
	// update the user info uri
	public static final String UPDATE_USER_INFO = IP + "/indiv/update";
	// get friends list uri
	public static final String GET_FRIENDS_LIST = IP
			+ "/indiv/get?classname=User&uid=";
	// get propertities or wish item uri
	public static final String GET_PROPERTITY = IP
			+ "/indiv/get?classname=$classname&uid=";
}
