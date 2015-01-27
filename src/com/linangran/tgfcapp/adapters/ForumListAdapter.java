package com.linangran.tgfcapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.ContentActivity;
import com.linangran.tgfcapp.data.ForumListItemData;
import com.linangran.tgfcapp.fragments.ForumListFragment;
import com.linangran.tgfcapp.tasks.ForumListDownloadTask;
import com.linangran.tgfcapp.tasks.ForumListRefreshTask;

import java.util.List;

/**
 * Created by linangran on 2/1/15.
 */
public class ForumListAdapter extends BaseAdapter
{

	private List<ForumListItemData> forumDataList;
	private Context context;
	private LayoutInflater layoutInflater;
	private View loadingView;
	private ForumListFragment forumListFragment;

	private int page = 1;
	private int fid;
	private ForumListDownloadTask downloadTask = null;
	private ForumListRefreshTask refreshTask = null;

	public static int VIEW_CONTENT_TYPE_ITEM = 0;
	public static int VIEW_CONTENT_TYPE_LOADING = 1;
	public static int VIEW_TYPE_COUNT = 2;

	public ForumListAdapter(Context context, List<ForumListItemData> forumDataList, ForumListFragment forumListFragment, int fid)
	{
		super();
		this.forumDataList = forumDataList;
		this.forumListFragment = forumListFragment;
		this.context = context;
		this.fid = fid;
		this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return this.forumDataList.size() + 1;
	}

	@Override
	public Object getItem(int i)
	{
		if (i < this.forumDataList.size())
		{
			return this.forumDataList.get(i);
		}
		else
		{
			return null;
		}
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (position >= this.forumDataList.size())
		{
			return VIEW_CONTENT_TYPE_LOADING;
		}
		else
		{
			return VIEW_CONTENT_TYPE_ITEM;
		}
	}


	@Override
	public int getViewTypeCount()
	{
		return VIEW_TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (position >= this.forumDataList.size())
		{
			if (loadingView == null)
			{
				loadingView = this.layoutInflater.inflate(R.layout.forum_list_fragment_item_loading, parent, false);
			}
			//Trigger Loading Action
			if (this.downloadTask == null || this.downloadTask.getStatus().equals(AsyncTask.Status.FINISHED))
			{
				this.downloadTask = new ForumListDownloadTask(this.forumListFragment);
				this.downloadTask.execute(fid, page);
			}
			return loadingView;
		}
		if (convertView == null)
		{
			convertView = this.layoutInflater.inflate(R.layout.forum_list_fragment_item, parent, false);
		}
		View rowView = convertView;
		TextView titleTextView = (TextView) rowView.findViewById(R.id.forum_list_fragment_item_title);
		TextView posterTextView = (TextView) rowView.findViewById(R.id.forum_list_fragment_item_poster);
		TextView countTextView = (TextView) rowView.findViewById(R.id.forum_list_fragment_item_read_count);
		TextView replierTextView = (TextView) rowView.findViewById(R.id.forum_list_fragment_item_replier);
		ForumListItemData forumListItemData = forumDataList.get(position);
		titleTextView.setText(forumListItemData.title);
		posterTextView.setText(forumListItemData.posterName);
		replierTextView.setText(forumListItemData.replierName);
		countTextView.setText(forumListItemData.replyCount + "/" + forumListItemData.readCount);


		convertView.setTag(this.forumDataList.get(position));
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				ForumListItemData itemData = (ForumListItemData) view.getTag();
				Intent intent = new Intent(ForumListAdapter.this.context, ContentActivity.class);
				intent.putExtra("tid", itemData.tid);
				intent.putExtra("title", itemData.title);
				intent.putExtra("fid", ForumListAdapter.this.fid);
				ForumListAdapter.this.context.startActivity(intent);
			}
		});

		return convertView;
	}

	public void updateDataCallback(List<ForumListItemData> newList)
	{
		int size = this.forumDataList.size();
		for (int i = 0; i < newList.size(); i++)
		{
			boolean hasID = false;
			for (int j = 0; j < size; j++)
			{
				if (this.forumDataList.get(j).tid == newList.get(i).tid)
				{
					hasID = true;
					break;
				}
			}
			if (hasID == false)
			{
				this.forumDataList.add(newList.get(i));
			}
		}
		this.page++;
		this.notifyDataSetChanged();
	}

	public void refreshData()
	{
		if (this.refreshTask == null || this.refreshTask.getStatus().equals(AsyncTask.Status.FINISHED))
		{
			this.refreshTask = new ForumListRefreshTask(this.forumListFragment);
			this.refreshTask.execute(fid, 1);
		}
	}

	public void refreshDataCallback(List<ForumListItemData> newList)
	{
		this.forumDataList = newList;
		this.page = 1;
		this.notifyDataSetChanged();
	}

	public void abortTask()
	{
		if (this.downloadTask != null && this.downloadTask.getStatus().equals(AsyncTask.Status.RUNNING))
		{
			this.downloadTask.cancel(true);
		}
		if (this.refreshTask != null && this.refreshTask.getStatus().equals(AsyncTask.Status.RUNNING))
		{
			this.refreshTask.cancel(true);
		}
	}
}
