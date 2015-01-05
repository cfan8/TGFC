package com.linangran.tgfcapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.ContentListItemData;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by linangran on 5/1/15.
 */
public class ContentListAdapter extends BaseAdapter
{


	private List<ContentListItemData> dataList;
	private LayoutInflater layoutInflater;
	private Context context;

	public ContentListAdapter(Context context)
	{
		super();
		this.context = context;
		this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		return dataList.size();
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
			//SetListenerHere;
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
		quoteInfoTextView.setText(itemData.quotedInfo);
		quotedTextTextView.setText(itemData.quotedText);
		mainTextTextView.setText(itemData.mainText);
		return convertView;
	}
}
