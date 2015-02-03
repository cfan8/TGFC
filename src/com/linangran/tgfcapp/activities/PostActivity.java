package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.fragments.PostFragment;

/**
 * Created by linangran on 26/1/15.
 */
public class PostActivity extends SwipeBackActivity
{
	Toolbar toolbar;
	PostFragment postFragment;
	String postFragmentID= "postFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		this.toolbar = (Toolbar) findViewById(R.id.activity_post_toolbar);
		this.setSupportActionBar(this.toolbar);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle bundle = getIntent().getExtras();
		if (bundle.getBoolean("isReply", false))
		{
			setTitle("发表回复: " + bundle.getString("mainTitle"));
		}
		else
		{
			setTitle("发表新帖");
		}
		this.postFragment = new PostFragment();
		this.postFragment.setArguments(bundle);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.activity_post, this.postFragment, this.postFragmentID);
		fragmentTransaction.commit();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
