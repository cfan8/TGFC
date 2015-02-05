package com.linangran.tgfcapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.activities.PostActivity;
import com.linangran.tgfcapp.data.ContentListItemData;
import com.linangran.tgfcapp.fragments.ContentListPageFragment;
import com.linangran.tgfcapp.tasks.ContentListDownloadTask;
import com.linangran.tgfcapp.utils.PreferenceUtils;
import com.linangran.tgfcapp.views.AsyncImageGetter;
import com.linangran.tgfcapp.views.ListLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 5/1/15.
 */
public class ContentListAdapter
{


	private List<ContentListItemData> dataList = new ArrayList<ContentListItemData>();
	private LayoutInflater layoutInflater;
	private Context context;
	private int tid;
	private int page;
	private ContentListDownloadTask downloadTask;
	private ContentListPageFragment contentListPageFragment;

	public ListLinearLayout parentListLinearLayout;


	private int themeColorPrimary;
	private int themeColorAnnotationText;

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
		TypedValue typedValue = new TypedValue();
		this.contentListPageFragment.getActivity().getTheme().resolveAttribute(R.attr.themeColorPrimary, typedValue, true);
		this.themeColorPrimary = typedValue.data;
		this.contentListPageFragment.getActivity().getTheme().resolveAttribute(R.attr.themeColorAnnotationText, typedValue, true);
		this.themeColorAnnotationText = typedValue.data;
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

	public Object getItem(int i)
	{
		return dataList.get(i);
	}

	public long getItemId(int i)
	{
		return dataList.get(i).floorNum;
	}

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
		RelativeLayout postInfoRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.content_list_fragment_page_list_view_item_post_info);
		posterNameTextView.setText(itemData.posterName);
		postTimeTextView.setText(itemData.posterTime);
		if (itemData.ratings == 0)
		{
			ratingTextView.setVisibility(View.INVISIBLE);
			for (int cindex = 0; cindex < postInfoRelativeLayout.getChildCount(); cindex++)
			{
				TextView contentTextView = (TextView) postInfoRelativeLayout.getChildAt(cindex);
				contentTextView.setTextColor(this.themeColorAnnotationText);
			}
		}
		else
		{
			ratingTextView.setVisibility(View.VISIBLE);
			ratingTextView.setText("+" + itemData.ratings);
			for (int cindex = 0; cindex < postInfoRelativeLayout.getChildCount(); cindex++)
			{
				TextView contentTextView = (TextView) postInfoRelativeLayout.getChildAt(cindex);
				contentTextView.setTextColor(this.themeColorPrimary);
			}
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
			editImageView.setOnClickListener(new ReplyListener(itemData.pid));
		}
		else
		{
			editImageView.setVisibility(View.GONE);
			plusImageView.setVisibility(View.VISIBLE);
		}
		//itemData.mainText = itemData.mainText.replaceAll(" ... ", "...");

		//Log.w("", itemData.mainText);
		Spanned spannedText = Html.fromHtml(itemData.mainText, new AsyncImageGetter(mainTextTextView, context), null);
		mainTextTextView.setText(spannedText);

		platformTextView.setText(itemData.platformInfo);

		quoteImageView.setOnClickListener(quoteReplyListener);

		if (PreferenceUtils.hideQuickPanel())
		{
			shareImageView.setVisibility(View.GONE);
			plusImageView.setVisibility(View.GONE);
		}

		return convertView;
	}


	public void updateContentDataList(List<ContentListItemData> dataList)
	{
		this.dataList.clear();
		this.dataList.addAll(dataList);
		this.parentListLinearLayout.updateView();
		return;
	}

	public boolean isEmpty()
	{
		return dataList == null || dataList.size() == 0;
	}

	private class ReplyListener implements View.OnClickListener
	{
		int pid;
		public ReplyListener(int pid)
		{
			this.pid = pid;
		}

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(contentListPageFragment.getActivity(), PostActivity.class);
			intent.putExtra("isEdit", true);
			intent.putExtra("editPid", pid);
			intent.putExtra("tid", tid);
			contentListPageFragment.startActivity(intent);
		}
	}
}
