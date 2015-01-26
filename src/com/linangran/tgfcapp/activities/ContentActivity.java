package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.fragments.ContentFragment;

/**
 * Created by linangran on 6/1/15.
 */
public class ContentActivity extends ActionBarActivity
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
		Bundle fragmentBundle = new Bundle();
		fragmentBundle.putInt("tid", this.tid);
		this.contentFragment.setArguments(fragmentBundle);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.activity_content_fragment_linear_layout, contentFragment, contentFragmentID);
		fragmentTransaction.commit();
		showActionBar();
	}

	public void showActionBar()
	{
		if (getSupportActionBar().isShowing() == false)
		{
			getSupportActionBar().show();
		}
	}

	public void hideActionBar()
	{
		if (getSupportActionBar().isShowing())
		{
			getSupportActionBar().hide();
		}
	}
}
