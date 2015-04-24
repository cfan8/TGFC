package com.linangran.tgfcapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.ForumBasicData;
import com.linangran.tgfcapp.fragments.ForumListFragment;
import com.linangran.tgfcapp.utils.APIURL;
import com.linangran.tgfcapp.utils.ForumBasicDataList;
import com.linangran.tgfcapp.utils.NetworkUtils;
import com.linangran.tgfcapp.utils.PreferenceUtils;

import java.util.List;

public class MainActivity extends AnalyzableActivity
{
	/**
	 * Called when the activity is first created.
	 */

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private TextView usernameTextView;
	private ImageButton loginButton;
	private LinearLayout drawerContentLayout;
	private LinearLayout pinnedList;
	private LinearLayout allList;
	private TextView settingTextView;
	private TextView placeHolder;

	private ForumListFragment forumListFragment;

	private ProgressDialog registerDialog;
	private LicenseChecker licenseChecker;
	private LicenseCheckerCallback licenseCheckerCallback;
	private Handler handler;

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
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("确认登出吗？").setPositiveButton("确定", logoutListner).setNegativeButton("取消", logoutListner);
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
		if (PreferenceUtils.useVirtualKeyOptimization())
		{
			int navigationHeight = (int) (48 * getResources().getDisplayMetrics().density + 0.5f);
			this.drawerContentLayout.setPadding(this.drawerContentLayout.getPaddingLeft(), this.drawerContentLayout.getPaddingTop(),
					this.drawerContentLayout.getPaddingRight(), navigationHeight);
		}
		this.pinnedList = (LinearLayout) findViewById(R.id.main_drawer_pinlist);
		this.allList = (LinearLayout) findViewById(R.id.main_drawer_alllist);
		inflateForumList(ForumBasicDataList.getForumBasicDataList(), this.allList);
		inflateForumList(ForumBasicDataList.getPinnedForumDataList(), this.pinnedList);

		this.settingTextView = (TextView) findViewById(R.id.activity_main_settings);
		this.settingTextView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});

		Bundle bundle = new Bundle();
		//bundle.putSerializable("forumBasicData", ForumBasicDataList.getDefaultForum());
		FragmentManager fragmentManager = getSupportFragmentManager();
		this.forumListFragment = (ForumListFragment) fragmentManager.findFragmentByTag(ForumListFragment.TAG);
		if (this.forumListFragment == null)
		{
			this.forumListFragment = new ForumListFragment();
			this.forumListFragment.setArguments(bundle);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.activity_main, this.forumListFragment, ForumListFragment.TAG);
			fragmentTransaction.commit();
		}


		this.handler = new Handler();

		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Google Play服务不可用").setMessage("应用需要完整的Google Play服务才能正常运行，请检查Google Play服务是否未安装或被禁用，并检查TGFC Beta是否被权限管理软件禁止访问Google Play服务。").setCancelable(false);
			builder.setPositiveButton(R.string.text_exit, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					MainActivity.this.finish();
				}
			});
			builder.show();
		}
		else
		{
			//Ugly wrapper for buggy google service library.
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
			{
				registerOnGoogle();
			}
		}
	}


	private void inflateForumList(List<ForumBasicData> list, LinearLayout layout)
	{
		if (list == null || list.size() == 0)
		{
			return;
		}
		LayoutInflater inflater = getLayoutInflater();
		for (int i = 0; i < list.size(); i++)
		{
			LinearLayout textLinearLayout = (LinearLayout) inflater.inflate(R.layout.forum_list_item, null);
			layout.addView(textLinearLayout);
			TextView textView = (TextView) textLinearLayout.findViewById(R.id.forum_list_item_textview);
			textView.setText(list.get(i).name);
			textView.setTag(list.get(i));
			textView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					ForumBasicData forumBasicData = (ForumBasicData) view.getTag();
					MainActivity.this.drawerLayout.closeDrawers();
					MainActivity.this.drawerLayout.post(new Runnable()
					{
						@Override
						public void run()
						{
							MainActivity.this.drawerToggle.syncState();
						}
					});
					MainActivity.this.forumListFragment.reload(forumBasicData);
				}
			});
		}
	}

	public void reInflatePinnedList()
	{
		this.pinnedList.removeAllViews();
		inflateForumList(ForumBasicDataList.getPinnedForumDataList(), this.pinnedList);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (PreferenceUtils.isLogin())
		{
			loginButton.setImageResource(R.drawable.ic_logout);
			usernameTextView.setText(PreferenceUtils.getUsername() + " (" + PreferenceUtils.getUID() + ")");
		}
		else
		{
			loginButton.setImageResource(R.drawable.ic_login);
			usernameTextView.setText(R.string.prompt_nologinuser);
		}

	}


	public void showExitDialog(final String title, final String content, final boolean gotoMarket)
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				if (MainActivity.this.registerDialog != null && MainActivity.this.registerDialog.isShowing())
				{
					MainActivity.this.registerDialog.dismiss();
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(title).setMessage(content).setPositiveButton(R.string.text_exit, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						MainActivity.this.finish();
						if (gotoMarket)
						{
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse("market://details?id=com.linangran.tgfcapp"));
							startActivity(intent);
						}
					}
				});
				builder.setCancelable(false);
				builder.show();
			}
		});
	}

	public void finishRegisterOnGoogle(final boolean success)
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				if (MainActivity.this.registerDialog != null && MainActivity.this.registerDialog.isShowing())
				{
					MainActivity.this.registerDialog.dismiss();
				}
				if (success)
				{
					if (PreferenceUtils.hasRegisteredOnGooglePlay() == false)
					{
						PreferenceUtils.setRegisteredOnGooglePlay();
						Toast.makeText(MainActivity.this, "已成功注册Google Play服务", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	public void registerOnGoogle()
	{
		if (PreferenceUtils.hasRegisteredOnGooglePlay() == false)
		{
			registerDialog = ProgressDialog.show(this, "注册Google Play服务", "正在连接Google Play服务器");
		}
		this.licenseCheckerCallback = new LicenseCheckerCallback()
		{
			@Override
			public void allow(int reason)
			{
				if (isFinishing())
				{
					return;
				}
				finishRegisterOnGoogle(true);
			}

			@Override
			public void dontAllow(int reason)
			{
				if (reason == Policy.RETRY)
				{
					if (PreferenceUtils.hasRegisteredOnGooglePlay() == false)
					{
						showExitDialog("注册Google Play失败", "网络连接失败，请重启应用以重试。请确保与Google服务器保持畅通连接。", false);
					}
				}
				else
				{
					showExitDialog("注册Google Play失败", "服务器拒绝注册：签名错误，请从Google Play商店重新下载应用", true);
				}
			}

			@Override
			public void applicationError(int errorCode)
			{
				showExitDialog("注册Google Play失败", "应用程序异常，请重启应用以重试；如果此错误反复出现，请重新安装应用", false);
			}
		};
		this.licenseChecker = new LicenseChecker(this, new ServerManagedPolicy(this, PreferenceUtils.AES_OBFUSCATOR), APIURL.BASE64_PUBLIC_KEY);
		//this.licenseChecker = new LicenseChecker(this, new StrictPolicy(), APIURL.BASE64_PUBLIC_KEY);
		this.licenseChecker.checkAccess(this.licenseCheckerCallback);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (this.registerDialog != null && this.registerDialog.isShowing())
		{
			this.registerDialog.dismiss();
		}
		if (this.licenseChecker != null)
		{
			this.licenseChecker.onDestroy();
		}
	}

	public int getNavigationBarHeight()
	{
		Resources resources = getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}
}
