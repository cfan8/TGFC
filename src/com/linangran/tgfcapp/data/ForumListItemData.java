package com.linangran.tgfcapp.data;

import java.io.Serializable;

/**
 * Created by linangran on 2/1/15.
 */
public class ForumListItemData implements Serializable
{

	public String title;
	public String posterName;
	public String replierName;
	public int replyCount;
	public int readCount;
	public boolean isTopped;
	public int tid;

	public ForumListItemData()
	{

	}

	public ForumListItemData(String title, String posterName, String replierName, int replyCount, int readCount) {
		this.title = title;
		this.posterName = posterName;
		this.replierName = replierName;
		this.replyCount = replyCount;
		this.readCount = readCount;
	}
}
