package com.linangran.tgfcapp.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.*;
import com.linangran.tgfcapp.utils.ImageDownloadManager;
import com.linangran.tgfcapp.utils.NetworkUtils;
import com.linangran.tgfcapp.utils.PreferenceUtils;

import java.io.*;

/**
 * Created by linangran on 30/1/15.
 */
public class ImageDownloadTask extends AsyncTask<Void, Integer, HttpResult<ImageDrawableData>>
{
	Context context;
	ImageDownloadManager manager;
	public ImageDownloadInfo info;
	public DrawableInfo drawableInfo;
	boolean downloadAnyway = false;

	public ImageDownloadTask(Context context, ImageDownloadInfo info)
	{
		this.context = context;
		this.info = info;
		this.manager = ImageDownloadManager.getInstance();
	}

	public void setDrawableInfo(DrawableInfo drawableInfo)
	{
		this.drawableInfo = drawableInfo;
	}

	public void setDownloadAnyway()
	{
		this.downloadAnyway = true;
	}


	@Override
	protected HttpResult<ImageDrawableData> doInBackground(Void... voids)
	{
		File cachedFileDirectory = new File(context.getCacheDir(), "cached_image");

		//		File cachedFileDirectory = new File(Environment.getExternalStorageDirectory(), "tgfcimg");
		if (cachedFileDirectory.exists() == false)
		{
			cachedFileDirectory.mkdir();
		}

		String url = info.url;
		String referer = info.getReferer();
		String md5 = info.getMD5();
		File[] cachedFiles = cachedFileDirectory.listFiles(new CachedImageFilter(md5));
		File cachedImageFile = null;
		HttpResult<ImageDrawableData> imageDrawableDataHttpResult;
		if (cachedFiles.length == 0)
		{
			//Download;
			if (PreferenceUtils.shouldShowImage() == false && downloadAnyway == false)
			{
				HttpResult<ImageDrawableData> data = new HttpResult<ImageDrawableData>();
				data.setErrorInfo("已关闭图片显示", HttpResult.ERROR_TYPE_IMAGE_OFF);
				return data;
			}
			else
			{
				HttpResult<ImageDownloadData> imageDownloadResult = NetworkUtils.downloadImage(info.url, info.getReferer());
				imageDrawableDataHttpResult = new HttpResult<ImageDrawableData>(imageDownloadResult);
				if (imageDownloadResult.hasError == false)
				{
					String fileName = md5 + "." + imageDownloadResult.result.imageType;
					File imageFile = new File(cachedFileDirectory, fileName);
					try
					{
						FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
						fileOutputStream.write(imageDownloadResult.result.imageData);
						fileOutputStream.close();
					}
					catch (IOException e)
					{
						imageDrawableDataHttpResult.setErrorInfo("Image Download Fail", HttpResult.ERROR_TYPE_WRITE_CACHE_FILE_FAIL);
						return imageDrawableDataHttpResult;
					}
					cachedImageFile = imageFile;
				}
				else
				{
					return imageDrawableDataHttpResult;
				}
			}
		}
		else
		{
			cachedImageFile = cachedFiles[0];
			imageDrawableDataHttpResult = new HttpResult<ImageDrawableData>();
		}
		try
		{
			FileInputStream fileInputStream = new FileInputStream(cachedImageFile);
			Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
			fileInputStream.close();
			imageDrawableDataHttpResult.setResult(new ImageDrawableData(bitmap));
			return imageDrawableDataHttpResult;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			imageDrawableDataHttpResult.setErrorInfo("Image Cache Read Fail", HttpResult.ERROR_TYPE_WRITE_CACHE_FILE_FAIL);
			return imageDrawableDataHttpResult;
		}
	}

	@Override
	protected void onPostExecute(HttpResult<ImageDrawableData> httpResult)
	{
		super.onPostExecute(httpResult);
		this.manager.finishTask(this.info.url);
		Drawable drawable;
		if (drawableInfo != null)
		{
			int width, height;
			if (httpResult.hasError)
			{
				switch (httpResult.errorType)
				{
					case HttpResult.ERROR_TYPE_WRITE_CACHE_FILE_FAIL:
						drawable = context.getResources().getDrawable(R.drawable.prompt_image_cache_error);
						break;
					case HttpResult.ERROR_TYPE_IMAGE_OFF:
						drawable = context.getResources().getDrawable(R.drawable.prompt_image_no_image);
						break;
					default:
						drawable = context.getResources().getDrawable(R.drawable.prompt_image_network_fail);
						break;
				}
				width = drawable.getIntrinsicWidth();
				height = drawable.getIntrinsicHeight();
			}
			else
			{
				drawable = new BitmapDrawable(context.getResources(), httpResult.result.bitmap);
				width = httpResult.result.bitmap.getWidth();
				height = httpResult.result.bitmap.getHeight();
			}
			drawableInfo.levelListDrawable.addLevel(1, 1, drawable);
			int maxWidth = drawableInfo.textView.getWidth() - 10;
			if (width > maxWidth)
			{
				height = maxWidth * height / width;
				width = maxWidth;
			}
			drawableInfo.levelListDrawable.setBounds(0, 0, width, height);
			drawableInfo.levelListDrawable.setLevel(1);
			drawableInfo.textView.setText(drawableInfo.textView.getText());
		}
	}


	static class CachedImageFilter implements FilenameFilter
	{
		String fileName;

		public CachedImageFilter(String fileNameMD5)
		{
			this.fileName = fileNameMD5;
		}


		@Override
		public boolean accept(File file, String s)
		{
			if (s.contains(fileName))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	@Override
	public boolean equals(Object o)
	{
		ImageDownloadTask task = (ImageDownloadTask) o;
		return task.info.equals(this.info);
	}

	@Override
	public int hashCode()
	{
		return this.info.hashCode();
	}

	public String getSourceURL()
	{
		return this.info.url;
	}
}
