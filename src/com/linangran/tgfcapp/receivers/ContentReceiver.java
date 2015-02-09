package com.linangran.tgfcapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.linangran.tgfcapp.activities.ContentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linangran on 9/2/15.
 */
public class ContentReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Pattern htmlPattern = Pattern.compile("http:\\/\\/club\\.tgfcer\\.com\\/thread-(\\d+)-\\d+-\\d+\\.html");
		Pattern wapPattern = Pattern.compile("http:\\/\\/wap\\.tgfcer\\.com\\/index\\.php\\?.*?action=thread.*?tid=(\\d+)");
		Uri uri = intent.getData();
		Matcher htmlMatcher = htmlPattern.matcher(uri.toString());
		int tid = Integer.MIN_VALUE;
		if (htmlMatcher.find())
		{
			tid = Integer.parseInt(htmlMatcher.group(1));
		}
		else
		{
			Matcher wapMatcher = wapPattern.matcher(uri.toString());
			if (wapMatcher.find())
			{
				tid = Integer.parseInt(wapMatcher.group(1));
			}
		}
		if (tid == Integer.MIN_VALUE)
		{
			Toast.makeText(context, "TGFC: 无法识别的链接", Toast.LENGTH_SHORT);
		}
		else
		{
			Intent contentIntent = new Intent(context, ContentActivity.class);
			contentIntent.putExtra("tid", tid);
			contentIntent.putExtra("title", "正在载入...");
		}
	}
}
