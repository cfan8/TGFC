package com.linangran.tgfcapp.utils;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.linangran.tgfcapp.R;

/**
 * Created by linangran on 8/2/15.
 */
public class TGFCApplication extends Application
{
	Tracker tracker = null;

	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	}

	synchronized public Tracker getTracker(TrackerName trackerId)
	{
		if (this.tracker == null)
		{
			this.tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.app_tracker);
		}
		return this.tracker;
	}
}
