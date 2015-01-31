package com.linangran.tgfcapp.data;

import com.linangran.tgfcapp.utils.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by linangran on 30/1/15.
 */
public class ImageDownloadInfo
{
	public String url;
	public String referer;

	public static final RefererRecognizer[] REFERER_RECOGNIZERS = new RefererRecognizer[]{
			new RefererRecognizer("club.tgfcer.com", "http://wap.tgfcer.com/index.php"),
	};

	public ImageDownloadInfo(String url)
	{
		this.url = url;
	}

	public String getReferer()
	{
		if (referer == null)
		{
			for (int i = 0; i < REFERER_RECOGNIZERS.length; i++)
			{
				if (url.indexOf(REFERER_RECOGNIZERS[i].host) != -1)
				{
					this.referer = REFERER_RECOGNIZERS[i].referer;
					break;
				}
			}
			if (referer == null)
			{
				referer = "";
			}
		}
		return referer;
	}

	public String getMD5()
	{
		return StringUtils.md5(url);
	}

	@Override
	public boolean equals(Object o)
	{
		ImageDownloadInfo info = (ImageDownloadInfo) o;
		return info.url.equals(this.url);
	}

	@Override
	public int hashCode()
	{
		return this.url.hashCode();
	}

	static class RefererRecognizer
	{

		public String host;
		public String referer;
		public RefererRecognizer(String host, String referer)
		{
			this.host = host;
			this.referer = referer;
		}
	}
}
