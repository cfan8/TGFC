package com.linangran.tgfcapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.linangran.tgfcapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linangran on 9/2/15.
 */
public class ViewActivity extends ActionBarActivity
{
	public static final int ViewActivityResult = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		onView(this, getIntent());
	}

	private void onView(Context context, Intent intent)
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
			this.finish();
		}
		else
		{
			Intent contentIntent = new Intent(context, ContentActivity.class);
			contentIntent.putExtra("tid", tid);
			contentIntent.setFlags(0);
			contentIntent.putExtra("title", "正在载入...");
			startActivityForResult(intent, ViewActivityResult);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		this.finish();
	}
}
