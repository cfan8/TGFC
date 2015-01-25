package com.linangran.tgfcapp.utils;

import com.linangran.tgfcapp.data.ForumBasicData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by linangran on 25/1/15.
 */
public class ForumBasicDataList
{
	private static ForumBasicData[] datas = new ForumBasicData[]{
			new ForumBasicData(10, "完全数码讨论区", 2),
	/*		new ForumBasicData(85,"Apple 专区"),
			new ForumBasicData(5,"手机和掌机游戏讨论区"),
			new ForumBasicData(97,"旅行度假"),
			new ForumBasicData(99,"舌尖上的TG"),
			new ForumBasicData(41,"摄影区"),
			new ForumBasicData(69,"Sony俱乐部"),
			new ForumBasicData(59,"汽车版"),
	*/        new ForumBasicData(33, "游戏业界综合讨论区", 3),
	/*		new ForumBasicData(29,"主机游戏讨论区"),
			new ForumBasicData(101,"经典游戏怀旧专区"),
			new ForumBasicData(45,"硬件维修区"),
			new ForumBasicData(67,"网游业界讨论区"),
			new ForumBasicData(98,"英雄联盟"),
			new ForumBasicData(40,"暴雪游戏专区"),
			new ForumBasicData(94,"龙之谷"),
			new ForumBasicData(81,"AION永恒之塔"),
			new ForumBasicData(20,"梦幻之星专区"),
			new ForumBasicData(65,"ChinaJoy 2013"),
	*/        new ForumBasicData(25, "灌水与情感", 1),
	/*		new ForumBasicData(72,"神鬼奇谈"),
			new ForumBasicData(6,"体育运动专区"),
			new ForumBasicData(48,"体育赛事直喷专区"),
			new ForumBasicData(95,"宠物乐园"),
	*/        new ForumBasicData(12, "影视专区"),
	/*		new ForumBasicData(11,"动漫模型"),
			new ForumBasicData(93,"文史春秋"),
			new ForumBasicData(86,"TGFC招聘求职"),
			new ForumBasicData(47,"资源分享区"),
			new ForumBasicData(42,"三国志大战专区"),
			new ForumBasicData(54,"无双专区"),
			new ForumBasicData(88,"写真摄影服务"),
			new ForumBasicData(90,"二手交易区"),
			new ForumBasicData(17,"新品贩卖区"),
			new ForumBasicData(63,"交易调解区"),
			new ForumBasicData(26,"公共工作区"),
			new ForumBasicData(35,"已处理版务"),
	*/};

	private static List<ForumBasicData> forumBasicDataList = null;


	public static List<ForumBasicData> getForumBasicDataList()
	{
		if (forumBasicDataList == null)
		{
			forumBasicDataList = Arrays.asList(datas);
			Collections.sort(forumBasicDataList);
		}
		return forumBasicDataList;
	}

	public static List<ForumBasicData> getPinnedForumDataList()
	{
		return PreferenceUtils.getPinnedList();
	}

}
