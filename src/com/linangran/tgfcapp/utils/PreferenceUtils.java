package com.linangran.tgfcapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linangran.tgfcapp.data.ForumBasicData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 25/1/15.
 */
public class PreferenceUtils
{
	public static final String KEY_IS_LOGIN = "is_login";
	public static final String KEY_PIKA_UID = "tgc_pika_uid";
	public static final String KEY_PIKA_VERIFY = "tgc_pika_verify";
	public static final String KEY_USERNAME = "username";

	public static final String KEY_PINNED_LIST = "pinned_list";
	public static final String KEY_DRAFT_TITLE = "draft_title";
	public static final String KEY_DRAFT_CONTENT = "draft_content";
	public static final String KEY_HAS_DRAFT = "has_draft";

	public static final String KEY_LAST_VIEWED_FORUM_ID = "last_viewed_forum_id";


	public static final String KEY_ABOUT_VERSION = "about_version";
	public static final String KEY_ITEMS_PER_PAGE = "items_per_page";
	public static final String KEY_POSTS_PER_PAGE = "posts_per_page";
	public static final String KEY_SHOW_PINNED_POSTS = "show_pinned_posts";
	public static final String KEY_SHOW_IMAGE_ON_WIFI = "show_image_on_wifi";
	public static final String KEY_SHOW_IMAGE_ON_CELLULAR = "show_image_on_cellular";
	public static final String KEY_HIDE_QUICK_PANEL = "hide_quick_panel";

	public static final String KEY_HAS_CHECKED_GOOGLE_PLAY = "has_checked_google_play";

	public static AESObfuscator AES_OBFUSCATOR;


	static SharedPreferences pref = null;
	static Context applicationContext = null;

	public static void setContext(Context context)
	{
		PreferenceUtils.applicationContext = context;
		PreferenceUtils.pref = PreferenceManager.getDefaultSharedPreferences(context);
		AES_OBFUSCATOR = new AESObfuscator(APIURL.SALT, applicationContext.getPackageName(), Settings.Secure.ANDROID_ID);
	}

	public static boolean isLogin()
	{
		return pref.getBoolean(KEY_IS_LOGIN, false);
	}

	public static String getUID()
	{
		return pref.getString(KEY_PIKA_UID, null);
	}

	public static ForumBasicData getLastViewedForum()
	{
		int fid = pref.getInt(KEY_LAST_VIEWED_FORUM_ID, -1);
		if (fid == -1)
		{
			return ForumBasicDataList.getDefaultForum();
		}
		else
		{
			ForumBasicData data = ForumBasicDataList.getForumBasicDataByFid(fid);
			if (data != null)
			{
				return data;
			}
			else
			{
				return ForumBasicDataList.getDefaultForum();
			}
		}
	}

	public static void saveLastViewedForum(int fid)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(KEY_LAST_VIEWED_FORUM_ID, fid);
		editor.commit();
		return;
	}

	private static <T> T getObject(String key, Type type)
	{
		String s = pref.getString(key, null);
		if (s == null)
		{
			return null;
		}
		else
		{
			Gson gson = new Gson();
			return gson.fromJson(s, type);
		}
	}

	private static <T> void putObject(String key, Object value)
	{
		Gson gson = new Gson();
		String gsonData = gson.toJson(value);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, gsonData);
		//Log.i("", gsonData);
		editor.commit();
		return;
	}

	public static List<ForumBasicData> getPinnedList()
	{
		Type type = new TypeToken<List<ForumBasicData>>()
		{
		}.getType();
		List<ForumBasicData> storedValue = getObject(KEY_PINNED_LIST, type);
		if (storedValue == null)
		{
			return new ArrayList<ForumBasicData>();
		}
		else
		{
			return storedValue;
		}
	}

	public static void addToPinnedList(int fid)
	{
		List<ForumBasicData> storedValue = getPinnedList();
		int t = getPinnedDataById(fid, storedValue);
		if (t == -1)
		{
			storedValue.add(ForumBasicDataList.getForumBasicDataByFid(fid));
			putObject(KEY_PINNED_LIST, storedValue);
		}
		return;
	}

	public static void removeFromPinnedList(int fid)
	{
		List<ForumBasicData> storedValue = getPinnedList();
		int t = getPinnedDataById(fid, storedValue);
		if (t != -1)
		{
			storedValue.remove(t);
			putObject(KEY_PINNED_LIST, storedValue);
		}
		return;
	}

	public static boolean isPinned(int fid)
	{
		List<ForumBasicData> storedValue = getPinnedList();
		int t = getPinnedDataById(fid, storedValue);
		return t != -1;
	}

	private static int getPinnedDataById(int fid, List<ForumBasicData> list)
	{
		int r = -1;
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).fid == fid)
			{
				r = i;
				break;
			}
		}
		return r;
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

	public static boolean hasDraft()
	{
		return pref.getBoolean(KEY_HAS_DRAFT, false);
	}

	public static String getDraftTitle()
	{
		return pref.getString(KEY_DRAFT_TITLE, "");
	}

	public static String getDraftContent()
	{
		return pref.getString(KEY_DRAFT_CONTENT, "");
	}

	public static void saveDraft(String title, String content)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_HAS_DRAFT, true);
		if (title != null && title.length() != 0)
		{
			editor.putString(KEY_DRAFT_TITLE, title);
		}
		editor.putString(KEY_DRAFT_CONTENT, content);
		editor.commit();
	}

	public static void discardDraft()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_HAS_DRAFT, false);
		editor.commit();
	}

	public static boolean shouldShowImage()
	{
		if (isOnWifi())
		{
			return showImageOnWifi();
		}
		else
		{
			return showImageOnCellular();
		}
	}

	public static int getItemCountOnForumList()
	{
		String value = pref.getString(KEY_ITEMS_PER_PAGE, "30");
		return Integer.valueOf(value);
	}

	public static int getPostCountOnContentList()
	{
		String value = pref.getString(KEY_POSTS_PER_PAGE, "30");
		return Integer.valueOf(value);
	}

	public static boolean showPinnedPosts()
	{
		return pref.getBoolean(KEY_SHOW_PINNED_POSTS, true);
	}

	public static boolean hideQuickPanel()
	{
		return pref.getBoolean(KEY_HIDE_QUICK_PANEL, false);
	}

	private static boolean showImageOnWifi()
	{
		return pref.getBoolean(KEY_SHOW_IMAGE_ON_WIFI, true);
	}

	private static boolean showImageOnCellular()
	{
		return pref.getBoolean(KEY_SHOW_IMAGE_ON_CELLULAR, false);
	}

	private static boolean isOnWifi()
	{
		ConnectivityManager connManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	public static boolean hasRegisteredOnGooglePlay()
	{
		return true;
		//return pref.getBoolean(KEY_HAS_CHECKED_GOOGLE_PLAY, false);
	}

	public static void setRegisteredOnGooglePlay()
	{
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean(KEY_HAS_CHECKED_GOOGLE_PLAY, true);
		edit.commit();
		return;
	}
}
