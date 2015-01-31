package com.linangran.tgfcapp.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.linangran.tgfcapp.activities.LoginActivity;
import com.linangran.tgfcapp.data.HttpResult;

import java.lang.annotation.Target;

/**
 * Created by linangran on 25/1/15.
 */
public class ErrorHandlerUtils
{
	public static void handleError(HttpResult result, Context context)
	{
		if (result.hasError == false)
		{
			return;
		}
		else
		{
			String toastString = "";
			switch (result.errorType)
			{
				case HttpResult.ERROR_TYPE_NETWORK_FAIL:
					toastString = "网络错误";
					break;
				case HttpResult.ERROR_TYPE_API_ERROR:
					toastString = "API错误";
					break;
				case HttpResult.ERROR_TYPE_LOGIN_REQUIRED:
					toastString = "需要登录";
					break;
				case HttpResult.ERROR_TYPE_NOT_AUTHORIZED:
					toastString = "缺少权限";
					break;
				case HttpResult.ERROR_TYPE_ARGUMENT_ERROR:
					toastString = "参数错误";
					break;
				case HttpResult.ERROR_TYPE_NOT_IMAGE:
					toastString = "图片错误";
					break;
				case HttpResult.ERROR_TYPE_WRITE_CACHE_FILE_FAIL:
					toastString = "写入缓存失败";
					break;
				default:
					toastString = "未知错误";
					break;
			}
			toastString += ": " + result.errorInfo;
			Toast errorToast = Toast.makeText(context, toastString, Toast.LENGTH_SHORT);
			errorToast.show();
			if (result.errorType == HttpResult.ERROR_TYPE_LOGIN_REQUIRED)
			{
				if (context instanceof LoginActivity == false)
				{
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
				}
				else
				{
					LoginActivity loginActivity = (LoginActivity) context;
					loginActivity.redirectToLogin();
				}
			}
		}
	}

}
