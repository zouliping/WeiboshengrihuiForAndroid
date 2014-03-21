package cn.pdc.mobile.utils;

public class Config {

	public static String uid = "";
	public static String uname = "";

	public static String DEVELOPER_EMAIL = "zouliping007@gmail.com";

	public static final String TAOBAO_PACKAGE_NAME = "com.taobao.taobao";
	public static final String CALENDAR_PACKAGE_NAME = "cn.pdc.calendar";
	public static final String CALENDAR_ACTIVITY_NAME = "cn.pdc.calendar.activity.AddActivityActivity";
	public static final String WEICIYUAN_PACKAGE_NAME = "org.qii.weiciyuan";
	public static final String WEICIYUAN_ACTIIVTY_NAME = "org.qii.weiciyuan.ui.send.WriteWeiboActivity";

	public static final String URL_GITHUB = "https://github.com/zouliping/WeiboshengrihuiForAndroid";

	public static final String URL = "http://192.168.0.103:6000";
	public static final String URL_API = "http://192.168.0.103:9000";

	// login uri
	public static final String LOGIN = URL + "/user/login";
	// get user detail info uri
	public static final String GET_USER_INFO = URL + "/user/get?name=";
	// update the user info uri
	public static final String UPDATE_INFO = URL + "/user/update";
	public static final String UPDATE_INFO_API = URL_API + "/indiv/update";
	// get friends list uri
	public static final String GET_FRIENDS_LIST = URL
			+ "/user/get_friends?uid=";
	// get propertities or wish item uri
	public static final String GET_PROPERTITY = URL
			+ "/user/get_production?type=$classname&uid=";

	// remove goods
	public static final String REMOVE_URL_API = URL_API + "/indiv/remove";

	// send present
	public static final String SEND_PRESENT_URL = URL + "/user/send_present";

	// remove relation
	public static final String REMOVE_RELATION_URL = URL
			+ "/user/remove_friend";
	public static final String REMOVE_RELATION_URL_API = URL_API
			+ "/indiv/removerelation";
}
