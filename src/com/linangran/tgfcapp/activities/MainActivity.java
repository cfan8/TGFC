package com.linangran.tgfcapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.utils.NetworkUtils;
import com.linangran.tgfcapp.utils.PreferenceUtils;

public class MainActivity extends ActionBarActivity
{
	/**
	 * Called when the activity is first created.
	 */

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private TextView usernameTextView;
	private ImageButton loginButton;
	private LinearLayout drawerContentLayout;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		NetworkUtils.init(getApplicationContext());
		PreferenceUtils.setContext(getApplicationContext());
		setContentView(R.layout.activity_main);
		this.drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.app_name, R.string.app_name)
		{
			@Override
			public void onDrawerClosed(View drawerView)
			{
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				invalidateOptionsMenu();
			}
		};
		this.drawerLayout.setDrawerListener(this.drawerToggle);
		this.drawerLayout.post(new Runnable()
		{
			@Override
			public void run()
			{
				MainActivity.this.drawerToggle.syncState();
			}
		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
		drawerLayout.setStatusBarBackground(R.color.tgred);
		this.usernameTextView = (TextView) findViewById(R.id.main_drawer_username_textview);
		this.loginButton = (ImageButton) findViewById(R.id.main_drawer_login_button);
		View.OnClickListener loginPanelListener = new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (PreferenceUtils.isLogin())
				{
					DialogInterface.OnClickListener logoutListner = new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
							switch (i)
							{
								case DialogInterface.BUTTON_POSITIVE:
									Intent intent = new Intent(MainActivity.this, LoginActivity.class);
									intent.putExtra("isLogout", true);
									startActivity(intent);
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									break;
							}
						}
					};
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("确认登出吗？").setPositiveButton("确定", logoutListner ).setNegativeButton("取消", logoutListner);
					builder.show();
				}
				else
				{
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			}
		};
		this.loginButton.setOnClickListener(loginPanelListener);
		this.usernameTextView.setOnClickListener(loginPanelListener);
		this.drawerContentLayout = (LinearLayout) findViewById(R.id.main_drawer_content_layout);
		this.drawerContentLayout.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent)
			{
				return true;
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (PreferenceUtils.isLogin())
		{
			loginButton.setImageResource(R.drawable.ic_logout);
			usernameTextView.setText(PreferenceUtils.getUsername());
		}
		else
		{
			loginButton.setImageResource(R.drawable.ic_login);
			usernameTextView.setText(R.string.prompt_nologinuser);
		}
	}
}
