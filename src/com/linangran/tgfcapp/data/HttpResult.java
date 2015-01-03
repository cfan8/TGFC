package com.linangran.tgfcapp.data;

/**
 * Created by linangran on 3/1/15.
 */
public class HttpResult<T>
{
	public boolean hasError;
	public String errorInfo;
	public T result;

	public HttpResult(HttpResult<? extends Object> httpResult)
	{
		this.hasError = httpResult.hasError;
		this.errorInfo = httpResult.errorInfo;
	}

	public HttpResult(String errorInfo, T result)
	{
		this.hasError = true;
		this.errorInfo = errorInfo;
		this.result = result;
	}

	public HttpResult(T result)
	{
		this.hasError = false;
		this.result = result;
	}

	public HttpResult()
	{
	}

	public void setResult(T result)
	{
		this.result = result;
		this.hasError = false;
	}

	public void setErrorInfo(String errorInfo)
	{
		this.hasError = true;
		this.errorInfo = errorInfo;
	}
}
