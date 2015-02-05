package com.linangran.tgfcapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.fragments.SettingFragment;

/**
 * Created by linangran on 4/2/15.
 */
public class SettingActivity extends SwipeBackActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		SettingFragment settingFragment = new SettingFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.activity_setting, settingFragment, SettingFragment.TAG);
		fragmentTransaction.commit();
		Toolbar toolbar = (Toolbar) findViewById(R.id.activity_setting_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
