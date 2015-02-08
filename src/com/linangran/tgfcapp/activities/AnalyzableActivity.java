package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.linangran.tgfcapp.utils.TGFCApplication;

/**
 * Created by linangran on 8/2/15.
 */
public class AnalyzableActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Tracker t = ((TGFCApplication) getApplication()).getTracker(TGFCApplication.TrackerName.APP_TRACKER);
		t.enableAdvertisingIdCollection(true);
		t.setScreenName(this.getClass().getName());
		t.send(new HitBuilders.AppViewBuilder().build());
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
}
