package com.linangran.tgfcapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by linangran on 25/1/15.
 */
public class PreferenceUtils
{
	public static final String KEY_IS_LOGIN = "is_login";
	public static final String KEY_PIKA_UID = "tgc_pika_uid";
	public static final String KEY_PIKA_VERIFY = "tgc_pika_verify";
	public static final String KEY_USERNAME = "username";


	static SharedPreferences pref = null;

	public static void setContext(Context context)
	{
		PreferenceUtils.pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static boolean isLogin()
	{
		return pref.getBoolean(KEY_IS_LOGIN, false);
	}

	public static String getUID()
	{
		return pref.getString(KEY_PIKA_UID, null);
	}

	public static String getVerify()
	{
		return pref.getString(KEY_PIKA_VERIFY, null);
	}

	public static String getUsername()
	{
		return pref.getString(KEY_USERNAME, null);
	}

	public static void setLogin(String uid, String verify, String username)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_IS_LOGIN, true);
		editor.putString(KEY_PIKA_UID, uid);
		editor.putString(KEY_PIKA_VERIFY, verify);
		editor.putString(KEY_USERNAME, username);
		editor.commit();
	}

	public static void prepareLogin(String uid, String verify)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(KEY_PIKA_UID, uid);
		editor.putString(KEY_PIKA_VERIFY, verify);
		editor.commit();
	}

	public static void setLogout()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_IS_LOGIN, false);
		editor.commit();
	}
}
