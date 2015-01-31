package com.linangran.tgfcapp.utils;

import android.os.AsyncTask;
import com.linangran.tgfcapp.tasks.ImageDownloadTask;

import java.util.HashMap;
import java.util.List;

/**
 * Created by linangran on 30/1/15.
 */
public class ImageDownloadManager
{
	private ImageDownloadManager()
	{
		this.downloadTaskHashMap = new HashMap<String, ImageDownloadTask>();
	}

	private static ImageDownloadManager instance;
	private HashMap<String, ImageDownloadTask>  downloadTaskHashMap;

	public static ImageDownloadManager getInstance()
	{
		if (instance == null)
		{
			instance = new ImageDownloadManager();
		}
		return instance;
	}

	public void addTask(ImageDownloadTask imageDownloadTask)
	{
		if (this.downloadTaskHashMap.containsKey(imageDownloadTask.getSourceURL()))
		{
			ImageDownloadTask task = this.downloadTaskHashMap.get(imageDownloadTask.getSourceURL());
			if (task.getStatus().equals(AsyncTask.Status.RUNNING))
			{
				task.setDrawableInfo(imageDownloadTask.drawableInfo);
				return;
			}
		}
		synchronized(this.downloadTaskHashMap)
		{
			this.downloadTaskHashMap.put(imageDownloadTask.getSourceURL(), imageDownloadTask);
		}
	}

	public void startTask(String url)
	{
		ImageDownloadTask task = this.downloadTaskHashMap.get(url);
		if (task != null && task.getStatus().equals(AsyncTask.Status.PENDING))
		{
			task.execute();
		}
	}

	public void cancelTasks(List<String> list)
	{
		synchronized (this.downloadTaskHashMap)
		{
			for (int i = 0; i < list.size(); i++)
			{
				ImageDownloadTask task = this.downloadTaskHashMap.get(list.get(i));
				if (task != null && task.getStatus().equals(AsyncTask.Status.RUNNING))
				{
					task.cancel(true);
				}
				downloadTaskHashMap.remove(list.get(i));
			}
		}
	}


	public void finishTask(String url)
	{
		synchronized(this.downloadTaskHashMap)
		{
			ImageDownloadTask task = this.downloadTaskHashMap.get(url);
			if (task != null)
			{
				this.downloadTaskHashMap.remove(url);
			}
		}
	}

}
