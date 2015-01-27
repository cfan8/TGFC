package com.linangran.tgfcapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.PostActivity;
import com.linangran.tgfcapp.data.ContentListItemData;
import com.linangran.tgfcapp.fragments.ContentListPageFragment;
import com.linangran.tgfcapp.tasks.ContentListDownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 5/1/15.
 */
public class ContentListAdapter extends BaseAdapter
{


	private List<ContentListItemData> dataList = new ArrayList<ContentListItemData>();
	private LayoutInflater layoutInflater;
	private Context context;
	private int tid;
	private int page;
	private ContentListDownloadTask downloadTask;
	private ContentListPageFragment contentListPageFragment;

	private View.OnClickListener quoteReplyListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			View item = (View) view.getParent().getParent();
			ContentListItemData itemData = (ContentListItemData) item.getTag();
			Intent intent = new Intent(view.getContext(), PostActivity.class);
			intent.putExtra("isReply", true);
			intent.putExtra("hasQuote", true);
			intent.putExtra("isEdit", false);
			intent.putExtra("fid", contentListPageFragment.fid);
			intent.putExtra("tid", contentListPageFragment.tid);
			intent.putExtra("quotePid", itemData.pid);
			intent.putExtra("quotedText", itemData.mainText);
			intent.putExtra("mainTitle", contentListPageFragment.title);
			view.getContext().startActivity(intent);
		}
	};


	public void abortTask()
	{
		if (this.downloadTask != null && this.downloadTask.getStatus().equals(AsyncTask.Status.RUNNING))
		{
			this.downloadTask.cancel(true);
		}
	}

	public ContentListAdapter(ContentListPageFragment contentListPageFragment, int tid, int page)
	{
		super();
		this.context = contentListPageFragment.getActivity();
		this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.tid = tid;
		this.page = page;
		this.contentListPageFragment = contentListPageFragment;
		startDownloading(false);
	}

	public ContentListAdapter(ContentListPageFragment contentListPageFragment, int tid, int page, List<ContentListItemData> dataList)
	{
		super();
		this.context = contentListPageFragment.getActivity();
		this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.tid = tid;
		this.page = page;
		this.contentListPageFragment = contentListPageFragment;
		this.dataList = dataList;
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	public boolean isTaskRunning()
	{
		return this.downloadTask != null && this.downloadTask.getStatus().equals(AsyncTask.Status.RUNNING);
	}

	public void startDownloading(boolean isRefreshing)
	{
		if (isTaskRunning() == false)
		{
			this.downloadTask = new ContentListDownloadTask(this.contentListPageFragment);
			this.downloadTask.setRefreshing(isRefreshing);
			this.downloadTask.execute(this.tid, this.page);
		}
	}

	@Override
	public Object getItem(int i)
	{
		return dataList.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return dataList.get(i).floorNum;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup)
	{
		if (convertView == null)
		{
			convertView = this.layoutInflater.inflate(R.layout.content_list_fragment_page_list_view_item, viewGroup, false);
		}
		ContentListItemData itemData = dataList.get(i);
		convertView.setTag(itemData);
		TextView posterNameTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_poster_name);
		TextView ratingTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_rating);
		TextView postTimeTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_time);
		TextView floorNumTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_floor_num);
		TextView quoteInfoTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_quote_info);
		TextView quotedTextTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_quoted_text);
		TextView mainTextTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_main_text);
		LinearLayout quoteLayout = (LinearLayout) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_quote_layout);
		TextView platformTextView = (TextView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_platform);
		ImageView shareImageView = (ImageView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_share);
		ImageView editImageView = (ImageView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_edit);
		ImageView plusImageView = (ImageView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_rate);
		ImageView quoteImageView = (ImageView) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_quote);
		posterNameTextView.setText(itemData.posterName);
		postTimeTextView.setText(itemData.posterTime);
		if (itemData.ratings == 0)
		{
			ratingTextView.setVisibility(View.INVISIBLE);
		}
		else
		{
			ratingTextView.setVisibility(View.VISIBLE);
			ratingTextView.setText("+" + itemData.ratings);
		}
		floorNumTextView.setText("#" + itemData.floorNum);
		if (itemData.quotedInfo != null)
		{
			quoteLayout.setVisibility(View.VISIBLE);
			quoteInfoTextView.setText(itemData.quotedInfo);
			quotedTextTextView.setText(itemData.quotedText);
		}
		else
		{
			quoteLayout.setVisibility(View.GONE);
		}
		if (itemData.canEdit)
		{
			editImageView.setVisibility(View.VISIBLE);
			plusImageView.setVisibility(View.GONE);
		}
		else
		{
			editImageView.setVisibility(View.GONE);
			plusImageView.setVisibility(View.VISIBLE);
		}
		mainTextTextView.setText(Html.fromHtml(itemData.mainText), TextView.BufferType.SPANNABLE);
		platformTextView.setText(itemData.platformInfo);

		quoteImageView.setOnClickListener(quoteReplyListener);

		return convertView;
	}


	public void updateContentDataList(List<ContentListItemData> dataList)
	{
		this.dataList.clear();
		this.dataList.addAll(dataList);
		this.notifyDataSetChanged();
		return;
	}
}
