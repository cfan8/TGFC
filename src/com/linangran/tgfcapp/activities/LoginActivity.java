package com.linangran.tgfcapp.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.tasks.FetchUserInfoTask;
import com.linangran.tgfcapp.utils.APIURL;
import com.linangran.tgfcapp.utils.ErrorHandlerUtils;
import com.linangran.tgfcapp.utils.PreferenceUtils;
import com.linangran.tgfcapp.utils.WebViewUtils;

import java.net.CookieManager;


/**
 * Created by linangran on 24/1/15.
 */
public class LoginActivity extends SwipeBackActivity
{

	WebView loginWebView;
	String verify;
	String uid;
	ProgressDialog loadingDialog;
	FetchUserInfoTask fetchUserInfoTask;
	ProgressBarIndeterminateDeterminate webViewProgress;
	Toolbar toolbar;
	boolean isLogout = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.loginWebView = (WebView) findViewById(R.id.activity_login_webview);
		this.isLogout = this.getIntent().getBooleanExtra("isLogout", false);
		this.loginWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				LoginActivity.this.webViewProgress.setVisibility(View.INVISIBLE);
				String uid = WebViewUtils.getCookie(APIURL.WAP_DOMAIN_NAME, PreferenceUtils.KEY_PIKA_UID);
				String verify = WebViewUtils.getCookie(APIURL.WAP_DOMAIN_NAME, PreferenceUtils.KEY_PIKA_VERIFY);
				if (isLogout == false)
				{
					if (verify != null && uid != null)
					{
						LoginActivity.this.loginFinish(uid, verify);
					}
				}
				else
				{
					if (verify == null)
					{
						LoginActivity.this.logoutFinish();
					}
				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				LoginActivity.this.webViewProgress.setVisibility(View.VISIBLE);
				LoginActivity.this.webViewProgress.reset();
			}
		});
		this.loginWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				super.onProgressChanged(view, newProgress);
				LoginActivity.this.webViewProgress.setProgress(newProgress);
			}
		});

		if (isLogout == false)
		{
			WebViewUtils.clearCookies();
			this.loginWebView.loadUrl(APIURL.WAP_LOGIN_URL);
			this.setTitle("请登录");
		}
		else
		{
			this.loginWebView.loadUrl(APIURL.WAP_LOGOUT_URL);
			this.setTitle("正在注销，请稍后");
		}
		this.loginWebView.getSettings().setJavaScriptEnabled(true);
		this.toolbar = (Toolbar) findViewById(R.id.activity_login_toolbar);
		setSupportActionBar(this.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.webViewProgress = (ProgressBarIndeterminateDeterminate) findViewById(R.id.activity_login_webview_progress_bar);
		this.webViewProgress.setVisibility(View.VISIBLE);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_activity_login_refresh:
				this.loginWebView.reload();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		if (this.loginWebView.canGoBack())
		{
			this.loginWebView.goBack();
		}
		else
		{
			this.finish();
		}
	}

	public void loginFinish(String uid, String verify)
	{
		this.loadingDialog = ProgressDialog.show(LoginActivity.this, "", "正在获取用户信息，请稍后", true);
		this.uid = uid;
		this.verify = verify;
		PreferenceUtils.prepareLogin(uid, verify);
		this.fetchUserInfoTask = new FetchUserInfoTask(this);
		this.fetchUserInfoTask.execute();
	}

	public void logoutFinish()
	{
		Toast.makeText(this, "注销成功！", Toast.LENGTH_SHORT).show();
		PreferenceUtils.setLogout();
		LoginActivity.this.finish();
	}

	public void loadingUsernameFinish(HttpResult<String> result)
	{
		this.loadingDialog.dismiss();
		if (result.hasError)
		{
			ErrorHandlerUtils.handleError(result, this);
		}
		else
		{
			PreferenceUtils.setLogin(this.uid, this.verify, result.result);
			Toast.makeText(this, "用户" + result.result + "(" + this.uid + ")" + "已登录", Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	public void redirectToLogin()
	{
		this.loginWebView.loadUrl(APIURL.WAP_LOGIN_URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_activity_login, menu);
		return super.onCreateOptionsMenu(menu);
	}


}
