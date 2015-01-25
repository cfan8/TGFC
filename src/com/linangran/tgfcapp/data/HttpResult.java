package com.linangran.tgfcapp.data;

import java.io.Serializable;

/**
 * Created by linangran on 3/1/15.
 */
public class HttpResult<T>
{
	public static final int ERROR_TYPE_NO_ERRORS = -1;

	public static final int ERROR_TYPE_NETWORK_FAIL = 1;
	public static final int ERROR_TYPE_API_ERROR = 2;
	public static final int ERROR_TYPE_LOGIN_REQUIRED = 3;
	public static final int ERROR_TYPE_NOT_AUTHORIZED = 4;
	public static final int ERROR_TYPE_ARGUMENT_ERROR = 5;

	public static final int ERROR_TYPE_OTHERS = 1000;


	public boolean hasError;
	public String errorInfo;
	public int errorType;
	public T result;

	public HttpResult(HttpResult<? extends Object> httpResult)
	{
		this.hasError = httpResult.hasError;
		this.errorInfo = httpResult.errorInfo;
		this.errorType = httpResult.errorType;
	}


	public HttpResult()
	{
		this.errorType = ERROR_TYPE_OTHERS;
		this.hasError = true;
	}

	public void setResult(T result)
	{
		this.result = result;
		this.hasError = false;
		this.errorType = ERROR_TYPE_NO_ERRORS;
	}

	public void setErrorInfo(String errorInfo, int errorType)
	{
		this.hasError = true;
		this.errorInfo = errorInfo;
		this.errorType = errorType;
	}
}
