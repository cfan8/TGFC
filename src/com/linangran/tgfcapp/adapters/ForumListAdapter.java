package com.linangran.tgfcapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.ForumListItemData;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by linangran on 2/1/15.
 */
public class ForumListAdapter extends BaseAdapter {

	private List<ForumListItemData> forumDataList;
	private Context context;
	private LayoutInflater layoutInflater;

	public ForumListAdapter(Context context, List<ForumListItemData> forumDataList) {
		super();
		this.forumDataList = forumDataList;
		this.context = context;
		this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return this.forumDataList.size();
	}

	@Override
	public Object getItem(int i) {
		return this.forumDataList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
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
		return convertView;
	}
}
