package com.linangran.tgfcapp.utils;

/**
 * Created by linangran on 3/1/15.
 */
public class APIURL
{
	public static final String DOMAIN_NAME = "tgfcer.com";
	public static final String WAP_DOMAIN_NAME = "wap." + DOMAIN_NAME;
	public static final String WAP_BASE_URL = "http://" + WAP_DOMAIN_NAME;
	public static final String WAP_API_URL = WAP_BASE_URL + "/index.php";
	public static final String WAP_VIEW_FORUM_URL = WAP_API_URL + "?action=forum&fid=";
	public static final String WAP_VIEW_CONTENT_URL = WAP_API_URL + "?action=thread&tid=";
	public static final String WAP_LOGIN_URL = WAP_API_URL + "?action=login";
	public static final String WAP_LOGOUT_URL = WAP_API_URL + "?action=login&logout=yes";
	public static final String WAP_MY_INFO = WAP_API_URL + "?action=my";
	public static final String WAP_POST_NEW = WAP_API_URL + "?action=post&do=newthread";
	public static final String WAP_POST_REPLY = WAP_API_URL + "?action=post&do=reply";
	public static final String WAP_POST_EDIT = WAP_API_URL + "?action=post&do=edit";


	public static final String ANDROID_CLIENT_SIGNATURE = "\r\n________\r\n发送自TGFC Android测试版客户端";
}
