package com.linangran.tgfcapp.data;

/**
 * Created by linangran on 25/1/15.
 */
public class ForumBasicData implements Comparable<ForumBasicData>
{
	static int counter = 0;

	public int fid;
	public String name;
	public int order;

	public ForumBasicData(int fid, String name)
	{
		this.fid = fid;
		this.name = name;
		counter += 100;
		this.order = counter;
	}

	public ForumBasicData(int fid, String name, int order)
	{
		this.fid = fid;
		this.name = name;
		this.order = order;
	}

	@Override
	public int compareTo(ForumBasicData forumBasicData)
	{
		return this.order - forumBasicData.order;
	}
}
