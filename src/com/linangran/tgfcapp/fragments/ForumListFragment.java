package com.linangran.tgfcapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.adapters.ForumListAdapter;
import com.linangran.tgfcapp.data.ForumListItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 2/1/15.
 */
public class ForumListFragment extends Fragment {

	private ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.forum_list_fragment, container, false);
		this.listView = (ListView) fragmentView.findViewById(R.id.forum_list_fragment_list_view);
		List<ForumListItemData> list = new ArrayList<ForumListItemData>();
		ForumListItemData data = new ForumListItemData("这是帖子的标题", "这是发帖人", "这是回复者", 10, 100);
		for (int i = 0; i < 20; i++) {
			list.add(data);
		}
		this.listView.setAdapter(new ForumListAdapter(this.getActivity(), list));
		return fragmentView;
	}
}
