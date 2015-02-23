package com.linangran.tgfcapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.fragments.ContentFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linangran on 6/1/15.
 */
public class ContentActivity extends SwipeBackActivity
{
	int tid;
	String title;
	ContentFragment contentFragment;
	String contentFragmentID = "contentFragment";
	Toolbar toolbar;

	private int onURLView(Context context, Intent intent)
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
		return tid;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (getIntent().getData() != null)
		{
			this.tid = onURLView(this, getIntent());
			if (tid == Integer.MIN_VALUE)
			{
				Toast.makeText(this, "TGFC: 无法识别的链接", Toast.LENGTH_SHORT).show();
				bundle = new Bundle();
				bundle.putBoolean("shouldExit", true);
				this.finish();
			}
			else
			{
				this.title = "正在载入...";
				if (bundle == null)
				{
					bundle = new Bundle();
				}
				bundle.putInt("tid", this.tid);
				bundle.putInt("fid", Integer.MIN_VALUE);
				bundle.putString("title", this.title);
			}
		}
		else
		{
			this.tid = bundle.getInt("tid");
			this.title = bundle.getString("title");
		}
		setContentView(R.layout.activity_content);
		this.toolbar = (Toolbar) findViewById(R.id.content_toolbar);
		this.setSupportActionBar(this.toolbar);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setTitle(title);
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		if (supportFragmentManager.findFragmentByTag(contentFragmentID) == null)
		{
			this.contentFragment = new ContentFragment();
			this.contentFragment.setArguments(bundle);
			FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
			fragmentTransaction.add(R.id.activity_content_fragment_linear_layout, contentFragment, contentFragmentID);
			fragmentTransaction.commit();
		}
		showActionBar();
	}

	public void showActionBar()
	{
		//Log.w("", "Actionbar show Request");
		if (getSupportActionBar().isShowing() == false)
		{
			getSupportActionBar().show();
			//Log.w("", "Actionbar shown");
		}
	}

	public void hideActionBar()
	{
		//Log.w("", "Actionbar hide Request");
		if (getSupportActionBar().isShowing())
		{
			getSupportActionBar().hide();
			//Log.w("", "Actionbar hided");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish()
	{
		if (getCallingActivity() != null)
		{
			Intent resultIntent = new Intent();
			setResult(ViewActivity.RESULT_CANCELED, resultIntent);
		}
		super.finish();
	}
}
