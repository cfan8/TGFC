package com.linangran.tgfcapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.linangran.tgfcapp.R;

/**
 * Created by linangran on 30/1/15.
 */

public class URLDrawable extends BitmapDrawable
{

	protected Drawable drawable;
	Context context;

	public URLDrawable(Context context)
	{
		this.context = context;
		this.drawable = context.getResources().getDrawable(R.drawable.prompt_image_loading);
		this.updateImageBounds();
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (this.drawable != null)
		{
			this.drawable.draw(canvas);
		}
	}

	public void updateDrawable(Drawable drawable)
	{
		this.drawable = drawable;
		this.updateImageBounds();
	}

	protected void updateImageBounds()
	{
		this.drawable.setBounds(0, 0, this.drawable.getIntrinsicWidth(), this.drawable.getIntrinsicHeight());
	}
}
