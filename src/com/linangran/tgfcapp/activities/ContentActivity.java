package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.fragments.ContentFragment;

/**
 * Created by linangran on 6/1/15.
 */
public class ContentActivity extends ActionBarActivity
{
	int tid;
	ContentFragment contentFragment;
	String contentFragmentID = "contentFragment";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		this.tid = bundle.getInt("tid");
		setContentView(R.layout.activity_content);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_content_fragment_linear_layout);
		this.contentFragment = new ContentFragment();
		Bundle fragmentBundle = new Bundle();
		fragmentBundle.putInt("tid", this.tid);
		this.contentFragment.setArguments(fragmentBundle);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.activity_content_fragment_linear_layout, contentFragment, contentFragmentID);
		fragmentTransaction.commit();
	}
}
