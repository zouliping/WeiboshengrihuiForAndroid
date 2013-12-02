package cn.pdc.mobile.utils;

public class Config {

	public static String uid = "";
	public static String uname = "";

	public static final String IP = "http://192.168.0.108:9000";

	// login uri
	public static final String LOGIN = IP + "/user/login";
	// get user detail info uri
	public static final String GET_USER_INFO = IP + "/indiv/getpro?indivname=$indivname&uid=";
}
