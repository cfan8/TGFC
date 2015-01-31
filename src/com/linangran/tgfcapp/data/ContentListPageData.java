package com.linangran.tgfcapp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linangran on 5/1/15.
 */
public class ContentListPageData implements Serializable
{
	public int tid;
	public int totalPageCount;
	public int currentPage;
	public int totalReplyCount;
	public boolean isClosed;
	public String title;
	public List<ContentListItemData> dataList;

	public List<String> imgURLList;

	public ContentListPageData()
	{
		this.dataList = new ArrayList<ContentListItemData>();
	}
}
