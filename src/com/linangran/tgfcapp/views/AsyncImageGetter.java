package com.linangran.tgfcapp.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.DrawableInfo;
import com.linangran.tgfcapp.data.ImageDownloadInfo;
import com.linangran.tgfcapp.tasks.ImageDownloadTask;
import com.linangran.tgfcapp.utils.ImageDownloadManager;

/**
 * Created by linangran on 30/1/15.
 */
public class AsyncImageGetter implements Html.ImageGetter
{
	TextView textView;
	Context context;

	public AsyncImageGetter(TextView textView, Context context)
	{
		this.textView = textView;
		this.context = context;
	}

	@Override
	public Drawable getDrawable(String s)
	{
		LevelListDrawable levelListDrawable = new LevelListDrawable();
		Drawable loading = context.getResources().getDrawable(R.drawable.prompt_image_loading);
		levelListDrawable.addLevel(0, 0, loading);
		levelListDrawable.setBounds(0, 0, loading.getIntrinsicWidth(), loading.getIntrinsicHeight());
		ImageDownloadTask imageDownloadTask = new ImageDownloadTask(context, new ImageDownloadInfo(s));
		imageDownloadTask.setDrawableInfo(new DrawableInfo(levelListDrawable, textView));
		ImageDownloadManager imageDownloadManager = ImageDownloadManager.getInstance();
		imageDownloadManager.addTask(imageDownloadTask);
		imageDownloadManager.startTask(s);
		return levelListDrawable;
	}
}
