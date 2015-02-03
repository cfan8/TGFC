package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.fragments.ContentFragment;

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
	boolean isShowing = true;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		this.tid = bundle.getInt("tid");
		this.title = bundle.getString("title");
		setContentView(R.layout.activity_content);
		this.toolbar = (Toolbar) findViewById(R.id.content_toolbar);
		this.setSupportActionBar(this.toolbar);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setTitle(title);
		this.contentFragment = new ContentFragment();
		this.contentFragment.setArguments(bundle);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.activity_content_fragment_linear_layout, contentFragment, contentFragmentID);
		fragmentTransaction.commit();
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
}
