package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.linangran.tgfcapp.R;

public class MainActivity extends ActionBarActivity {
	/**
	 * Called when the activity is first created.
	 */

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.app_name, R.string.app_name)
		{
			@Override
			public void onDrawerClosed(View drawerView) {
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		this.drawerLayout.setDrawerListener(this.drawerToggle);
		this.drawerLayout.post(new Runnable() {
			@Override
			public void run() {
				MainActivity.this.drawerToggle.syncState();
			}
		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
		drawerLayout.setStatusBarBackground(R.color.tgred);
	}
}
