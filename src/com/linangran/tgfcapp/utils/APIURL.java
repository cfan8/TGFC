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
	public static final String WAP_VIEW_CONTENT_URL = WAP_API_URL + "?action=thread&sc=1&tid=";
	public static final String WAP_LOGIN_URL = WAP_API_URL + "?action=login";
	public static final String WAP_LOGOUT_URL = WAP_API_URL + "?action=login&logout=yes";
	public static final String WAP_MY_INFO = WAP_API_URL + "?action=my";
	public static final String WAP_POST_NEW = WAP_API_URL + "?action=post&do=newthread";
	public static final String WAP_POST_REPLY = WAP_API_URL + "?action=post&do=reply";
	public static final String WAP_POST_EDIT = WAP_API_URL + "?action=post&do=edit";


	public static final String ANDROID_CLIENT_SIGNATURE_DEFAULT = "\r\n________\r\n发送自TGFC Android Beta客户端";
	public static final String ANDROID_CLIENT_SIGNATURE_TGBXS = "\r\n________\r\n发送自TGBXS Android Beta客户端";
	public static final String ANDROID_CLIENT_SIGNATURE_TGYXW = "\r\n________\r\n发送自TG有希望了 Beta客户端";
	public static final String ANDROID_CLIENT_SIGNATURE_REGEX = "<br(?:\\s*?\\/)?>\\s*_{8,}\\s*<br(?:\\s*?\\/)?>\\s*(发送自.+?客户端)";
	public static final String ANDROID_CLIENT_SIGNATURE_EDIT_REGEX = "\\s*_{8,}\\s*(发送自.+?客户端)\\s*";

	public static final byte[] SALT = new byte[] {
			-46, 15, 30, 28, -103, -57, 74, 24, 51, 78, -95,
			-45, 77, 7, -36, 113, -11, -32, -64, 89
	};
	public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkOgFcRsGwMH+H2hgrhEKOovPn0TfClVBL/h2dWo9ajMkY5jo7N4HaRUypS/AyzA5f48GB74vv4ahYC+vQdw8akeJzMg0d6gY3bJDbMjfACYP1fDqI48Ipo/By8Hejdyfxw/CXbf+MNjYsDdO6MUFjbq9gIh5HZ0kiGCTW6qO6ABfA8Sj7OB4O0/nXbuyfGynuNqUw7nk4p8T8dR8JeYwzQaC/7KPDed/PYxrfrXRsxEv3pk2cvrQDHk2JcjQIgNCXSz3kQRRNu3PA1x2Ncr566Dwggt5cTAGdWHum9VRF7m0nCj2h8tNCB4acSuTtFLDXRGisEuwRK/GxXpO4LWq0QIDAQAB";
	public static final String GOOGLE_ANALYTICS_ID = "UA-59499936-1";

}
