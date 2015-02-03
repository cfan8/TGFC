package com.linangran.tgfcapp.data;

import java.io.Serializable;

/**
 * Created by linangran on 25/1/15.
 */
public class ForumBasicData implements Comparable<ForumBasicData>, Serializable
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

	@Override
	public boolean equals(Object o)
	{
		ForumBasicData data = (ForumBasicData) o;
		return this.fid == data.fid;
	}

	@Override
	public int hashCode()
	{
		return this.fid;
	}
}
