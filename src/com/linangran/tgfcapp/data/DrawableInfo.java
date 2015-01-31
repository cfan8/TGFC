package com.linangran.tgfcapp.data;

import android.graphics.drawable.LevelListDrawable;
import android.widget.TextView;
import com.linangran.tgfcapp.views.URLDrawable;

/**
 * Created by linangran on 30/1/15.
 */
public class DrawableInfo
{
	public LevelListDrawable levelListDrawable;
	public TextView textView;

	public DrawableInfo(LevelListDrawable levelListDrawable, TextView textView)
	{
		this.levelListDrawable = levelListDrawable;
		this.textView = textView;
	}
}
