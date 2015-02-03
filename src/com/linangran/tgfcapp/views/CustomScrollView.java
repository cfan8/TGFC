package com.linangran.tgfcapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by linangran on 3/2/15.
 */
public class CustomScrollView extends ScrollView
{
	OnScrollChangedListener listener;

	public CustomScrollView(Context context)
	{
		super(context);
	}

	public CustomScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		if (listener != null)
		{
			listener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}
}
