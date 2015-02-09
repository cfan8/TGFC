package com.linangran.tgfcapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.adapters.ContentListAdapter;

/**
 * Created by linangran on 31/1/15.
 */
public class ListLinearLayout extends LinearLayout
{
	public ListLinearLayout(Context context)
	{
		super(context);
	}

	public ListLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	ContentListAdapter adapter;

	public void setAdapter(ContentListAdapter adapter)
	{
		this.adapter = adapter;
		this.adapter.parentListLinearLayout = this;
		updateView();
	}

	public void updateView()
	{
		if (this.getChildCount() > 0)
		{
			this.removeAllViews();
		}
		float scale = getResources().getDisplayMetrics().density;
		int dividerHeight = (int) (1 * scale + 0.5f);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		for (int i = 0; i < this.adapter.getCount(); i++)
		{
			View contentView = this.adapter.getView(i, null, this);
			this.addView(contentView);
			if (i != this.adapter.getCount() - 1)
			{
				ImageView imageView = new ImageView(this.getContext());
				imageView.setLayoutParams(layoutParams);
				imageView.setBackgroundColor(getResources().getColor(R.color.lightgray));
				this.addView(imageView);
			}
		}
	}


}
