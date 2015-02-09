package com.linangran.tgfcapp.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.linangran.tgfcapp.data.*;
import com.linangran.tgfcapp.tasks.EditPostTask;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linangran on 3/1/15.
 */
public class NetworkUtils
{
	private static Context applicationContext;
	private static String userAgent = null;
	private static BasicHttpParams httpParams = null;


	public static void init(Context context)
	{
		applicationContext = context;
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 15000);
	}


	private static String getUserAgent()
	{
		if (userAgent == null)
		{
			String base = "Mozilla/5.0 (Linux; Android [AndroidVersion]; [DeviceName] Build/[BuildName]) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Mobile Safari/537.36";
			String buildId = "KTU84Q";
			//PackageInfo packageInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
			userAgent = base.replace("[BuildName]", buildId).replace("[AndroidVersion]", Build.VERSION.RELEASE).replace("[DeviceName]", Build.MODEL);

		}
		return userAgent;
	}

	private static HttpResponse httpRequest(String url, String referer, boolean shouldLogin, boolean isDownload, List<NameValuePair> getParams, List<NameValuePair> postParams)
	{
		try
		{
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			String structuredURL = url;
			if (getParams != null && getParams.size() > 0)
			{
				StringBuilder urlBuilder = new StringBuilder(url);
				for (int i = 0; i < getParams.size(); i++)
				{
					NameValuePair pair = getParams.get(i);
					if (urlBuilder.indexOf("?") == -1)
					{
						urlBuilder.append("?");
					}
					else
					{
						urlBuilder.append("&");
					}
					urlBuilder.append(pair.getName());
					urlBuilder.append("=");
					urlBuilder.append(pair.getValue());
				}
				structuredURL = urlBuilder.toString();
			}
			HttpUriRequest httpRequest;
			if (isDownload)
			{
				httpRequest = new HttpGet(structuredURL);
			}
			else
			{
				httpRequest = new HttpPost(structuredURL);
			}
			if (postParams != null && postParams.size() > 0)
			{
				HttpPost httpPost = (HttpPost) httpRequest;
				httpPost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
			}
			if (referer != null && referer.length() != 0)
			{
				httpRequest.addHeader("Referer", referer);
			}
			httpRequest.addHeader("User-Agent", getUserAgent());
			HttpContext httpContext = new BasicHttpContext();
			if (isDownload == false && (shouldLogin || PreferenceUtils.isLogin()))
			{
				String cookies = PreferenceUtils.KEY_PIKA_UID + "=" + PreferenceUtils.getUID() + "; " + PreferenceUtils.KEY_PIKA_VERIFY + "=" + PreferenceUtils.getVerify();
				httpRequest.addHeader("Cookie", cookies);
			}
			HttpResponse response = httpClient.execute(httpRequest, httpContext);
			return response;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static HttpResponse httpGet(String url, String referer, boolean shouldLogin, boolean isDownload,String... args)
	{
		List<NameValuePair> getParams = new ArrayList<NameValuePair>();
		if (args.length % 2 != 0)
		{
			return null;
		}
		else
		{
			for (int i = 0; i < args.length; i += 2)
			{
				NameValuePair pair = new BasicNameValuePair(args[i], args[i + 1]);
				getParams.add(pair);
			}
			return httpRequest(url, referer, shouldLogin, isDownload,getParams, null);
		}
	}

	public static HttpResponse httpPost(String url, String referer, boolean isDownload, List<NameValuePair> postParams, List<NameValuePair> getParams)
	{
		return httpRequest(url, referer, false, isDownload, getParams, postParams);
	}

	public static HttpResponse httpPost(String url, String referer, boolean isDownload, List<NameValuePair> postParams, String... args)
	{
		List<NameValuePair> getParams = new ArrayList<NameValuePair>();
		if (args.length % 2 != 0)
		{
			return null;
		}
		else
		{
			for (int i = 0; i < args.length; i += 2)
			{
				NameValuePair pair = new BasicNameValuePair(args[i], args[i + 1]);
				getParams.add(pair);
			}
			return httpPost(url, referer, isDownload, postParams, getParams);
		}
	}

	private static HttpResult<String> httpPostString(String url, String referer, boolean isDownload, List<NameValuePair> postParams, List<NameValuePair> getParams)
	{
		HttpResponse response = httpPost(url, referer, isDownload, postParams, getParams);
		HttpResult<String> result = new HttpResult<String>();
		if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
		{
			result.hasError = true;
			result.errorType = HttpResult.ERROR_TYPE_NETWORK_FAIL;
			if (response != null)
			{
				result.errorInfo = response.getStatusLine().getReasonPhrase();
			}
			else
			{
				result.errorInfo = "网络连接错误";
			}
		}
		else
		{
			String encoding = "UTF-8";
			if (response.getEntity().getContentEncoding() != null && response.getEntity().getContentEncoding().getValue().length() != 0)
			{
				encoding = response.getEntity().getContentEncoding().getValue();
			}
			try
			{
				result.setResult(IOUtils.toString(response.getEntity().getContent(), encoding));
			}
			catch (IOException e)
			{
				e.printStackTrace();
				result.setErrorInfo(e.toString(), HttpResult.ERROR_TYPE_OTHERS);
			}
		}
		return result;
	}

	private static HttpResult<String> httpPostString(String url, String referer, boolean isDownload, List<NameValuePair> postParams, String... args)
	{
		List<NameValuePair> getParams = new ArrayList<NameValuePair>();
		if (args.length % 2 != 0)
		{
			return null;
		}
		else
		{
			for (int i = 0; i < args.length; i += 2)
			{
				NameValuePair pair = new BasicNameValuePair(args[i], args[i + 1]);
				getParams.add(pair);
			}
			return httpPostString(url, referer, isDownload, postParams, getParams);
		}
	}

	private static HttpResult<String> httpGetString(String url, String referer, boolean shouldLogin, boolean isDownload, String... args)
	{
		HttpResponse response = httpGet(url, referer, shouldLogin, isDownload, args);
		HttpResult<String> result = new HttpResult<String>();
		if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
		{
			result.hasError = true;
			result.errorType = HttpResult.ERROR_TYPE_NETWORK_FAIL;
			if (response != null)
			{
				result.errorInfo = response.getStatusLine().getReasonPhrase();
			}
			else
			{
				result.errorInfo = "网络连接错误";
			}
		}
		else
		{
			String encoding = "UTF-8";
			if (response.getEntity().getContentEncoding() != null && response.getEntity().getContentEncoding().getValue().length() != 0)
			{
				encoding = response.getEntity().getContentEncoding().getValue();
			}
			try
			{
				result.setResult(IOUtils.toString(response.getEntity().getContent(), encoding));
			}
			catch (IOException e)
			{
				e.printStackTrace();
				result.setErrorInfo(e.toString(), HttpResult.ERROR_TYPE_OTHERS);
			}
		}
		return result;
	}

	public static HttpResult<List<ForumListItemData>> getForumList(int fid, int page)
	{
		String url = APIURL.WAP_VIEW_FORUM_URL + fid + "&page=" + page;
		String iam = "";
		if (PreferenceUtils.showPinnedPosts() == false)
		{
			iam = "notop";
		}
		HttpResult<String> stringResult = httpGetString(url, APIURL.WAP_API_URL, false, false, "tp", String.valueOf(PreferenceUtils.getItemCountOnForumList()), "iam", iam);
		HttpResult<List<ForumListItemData>> listResult = new HttpResult<List<ForumListItemData>>(stringResult);
		if (stringResult.hasError == false)
		{
			String html = StringEscapeUtils.unescapeHtml(stringResult.result);
			if (html.contains("<div>无权访问本版块</div>"))
			{
				listResult.setErrorInfo("无权访问本版块", HttpResult.ERROR_TYPE_NOT_AUTHORIZED);
				return listResult;
			}
			else if (html.contains("<title>TGFC 手机版</title>"))
			{
				listResult.setErrorInfo("版面不存在", HttpResult.ERROR_TYPE_ARGUMENT_ERROR);
				return listResult;
			}

			Pattern pattern = Pattern.compile("<span class=\"title\">(<b>)?<a href=\"([^\"]+)\">([^<]+)<\\/a>(<\\/b>)?<\\/span>(?:<span class=\"paging\">.*<\\/span>)?<br \\/>\\s*<span class=\"author\">\\[(?:<b>)?([^\\/]+)(?:<\\/b>)?\\/(\\d+)\\/(\\d+)\\/(?:<b>)?([^\\/]+)(?:<\\/b>)?]<\\/span>");
			Matcher matcher = pattern.matcher(html);
			List<ForumListItemData> dataList = new ArrayList<ForumListItemData>();
			while (matcher.find())
			{
				ForumListItemData line = new ForumListItemData();
				String topFlag = matcher.group(1);
				String itemURL = matcher.group(2);
				String title = matcher.group(3);
				String posterName = matcher.group(5);
				String replyCount = matcher.group(6);
				String readCount = matcher.group(7);
				String replierName = matcher.group(8);
				line.isTopped = topFlag == null;
				line.tid = getTidFromURL(itemURL);
				line.title = title;
				line.posterName = posterName;
				line.replierName = replierName;
				line.readCount = Integer.parseInt(readCount);
				line.replyCount = Integer.parseInt(replyCount);
				dataList.add(line);
			}
			listResult.setResult(dataList);
		}
		return listResult;
	}

	private static int getTidFromURL(String url)
	{
		Pattern pattern = Pattern.compile("index.php\\?action=thread&tid=(\\d+)");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find())
		{
			return Integer.parseInt(matcher.group(1));
		}
		else
		{
			return -1;
		}
	}

	public static HttpResult<ContentListPageData> getContentList(int tid, int page)
	{
		String url = APIURL.WAP_VIEW_CONTENT_URL + tid + "&page=" + page;
		HttpResult<String> stringResult = httpGetString(url, APIURL.WAP_API_URL, false, false, "pp", String.valueOf(PreferenceUtils.getPostCountOnContentList()));
		HttpResult<ContentListPageData> contentResult = new HttpResult<ContentListPageData>(stringResult);
		ContentListPageData pageData = new ContentListPageData();
		if (stringResult.hasError == false)
		{
			String html = StringEscapeUtils.unescapeHtml(stringResult.result);
			if (html.contains("<div>指定主题不存在</div>"))
			{
				contentResult.setErrorInfo("指定的主题不存在", HttpResult.ERROR_TYPE_ARGUMENT_ERROR);
				return contentResult;
			}
			else if (html.contains("<div>无权查看本主题</div>"))
			{
				contentResult.setErrorInfo("无权查看本主题", HttpResult.ERROR_TYPE_NOT_AUTHORIZED);
				return contentResult;
			}
			else
			{
				generalContentParser(html, pageData, tid);
				contentListParser(html, pageData, tid, url);
				contentResult.setResult(pageData);
				return contentResult;
			}
		}
		else
		{
			return contentResult;
		}

	}

	public static void contentListParser(String html, ContentListPageData pageData, int tid, String referer)
	{
		List<ContentListItemData> dataList = pageData.dataList;
		Document htmlDoc = Jsoup.parse(html, referer);
		Elements lightMessageEmelents = htmlDoc.select(".lightmessage");
		lightMessageEmelents.remove();
		Elements messageElements = htmlDoc.select(".message");
		Elements infobarElements = htmlDoc.select(".infobar");
		int messageStart = 0;
		Pattern mainPostPattern = Pattern.compile("标题:<b>(.+)<\\/b><br \\/>时间:(.+)<br \\/>作者:<a href=\".+uid=(\\d+).*\">(?:<b>)?(.+?)(<\\/b>)?<\\/a>");
		Matcher mainPostMatcher = mainPostPattern.matcher(html);
		Pattern urlReplacePattern = Pattern.compile("<a\\s*.*?href=\"(.*?)\"\\s*.*?>.*?\\s\\.\\.\\.\\s.*?<\\/a>");
		if (mainPostMatcher.find())
		{
			messageStart++;
			ContentListItemData itemData = new ContentListItemData();
			itemData.floorNum = 1;
			itemData.posterTime = mainPostMatcher.group(2);
			itemData.posterUID = Integer.parseInt(mainPostMatcher.group(3));
			itemData.posterName = mainPostMatcher.group(4);
			itemData.canEdit = mainPostMatcher.group(5) != null;
			Pattern ratingPattern = Pattern.compile("评分记录\\(.+?=(\\d+)\\)");
			Matcher ratingMatcher = ratingPattern.matcher(html);
			if (ratingMatcher.find())
			{
				itemData.ratings = Integer.parseInt(ratingMatcher.group(1));
			}
			Pattern pidPattern = Pattern.compile("作者:<a href=\".*?pid=(\\d+)[^\\>]*?>");
			Matcher pidMatcher = pidPattern.matcher(html);
			if (pidMatcher.find())
			{
				itemData.pid = Integer.parseInt(pidMatcher.group(1));
			}
			itemData.mainText = messageElements.get(0).html();
			extractPlatform(itemData);
			dataList.add(itemData);
		}
		for (int i = messageStart, j = 0; i < messageElements.size(); i++, j++)
		{
			ContentListItemData itemData = new ContentListItemData();
			Element msgElement = messageElements.get(i);
			Element barElement = infobarElements.get(j);
			String infoString = StringEscapeUtils.unescapeHtml(barElement.html());
			Pattern barPattern = Pattern.compile("<a href=\".*?pid=(\\d+).*?>#(\\d+)[\\s\\S]*?<a href=\".*?uid=(\\d+).*?>(?:<b>)?(.+?)(?:<\\/b>)?<\\/a>[\\s\\S]*?(?:骚\\((\\d+)\\)[\\s\\S]*?)?<span class=\"nf\">(?:<font \\S*>)? (?:<b>)?(.*?)(<\\/b>)?(?:<\\/font>)?<\\/span>");
			Matcher barMatcher = barPattern.matcher(infoString);
			if (barMatcher.find())
			{
				itemData.pid = Integer.parseInt(barMatcher.group(1));
				itemData.floorNum = Integer.parseInt(barMatcher.group(2));
				itemData.posterUID = Integer.parseInt(barMatcher.group(3));
				itemData.posterName = barMatcher.group(4);
				if (barMatcher.group(5) != null)
				{
					itemData.ratings = Integer.parseInt(barMatcher.group(5));
				}
				itemData.posterTime = barMatcher.group(6);
				itemData.canEdit = barMatcher.group(7) != null;
			}
			Elements quotedElements = msgElement.select(".quote-bd");
			if (quotedElements.size() > 0)
			{
				String quoteString = quotedElements.get(0).html();
				String divider = "<br>";
				int t = quoteString.indexOf(divider);
				if (t != -1)
				{
					itemData.quotedInfo = quoteString.substring(0, t);
					itemData.quotedInfo = cleanQuote(itemData.quotedInfo);
					itemData.quotedText = quoteString.substring(t + divider.length());
					itemData.quotedText = getPlainText(cleanText(itemData.quotedText)).trim();
				}
				msgElement.select(".ui-topic-content").remove();
			}
			itemData.mainText = msgElement.html();
			extractPlatform(itemData);
			dataList.add(itemData);
		}
		Pattern imgURLPattern = Pattern.compile("<img\\s*[^>]*?src\\s*=\\s*['\\\"]([^'\\\"]*?)['\\\"][^>]*?\\s*\\/?>");
		List<String> imgURLList = new ArrayList<String>();
		for (int i = 0; i < dataList.size(); i++)
		{
			ContentListItemData itemData = dataList.get(i);
//			itemData.mainText = Jsoup.clean(itemData.mainText, Whitelist.basicWithImages());
			Matcher urlReplaceMatcher = urlReplacePattern.matcher(itemData.mainText);
//			Log.w("Matcher", String.valueOf(urlReplaceMatcher.find()));
			itemData.mainText = urlReplaceMatcher.replaceAll("<a href=\"$1\">$1</a>");
		}
		for (int i = 0; i < dataList.size(); i++)
		{
			String itemHTML = dataList.get(i).mainText;
			Matcher imgURLMatcher = imgURLPattern.matcher(itemHTML);
			while (imgURLMatcher.find())
			{
				imgURLList.add(imgURLMatcher.group(1));
			}
		}
		pageData.imgURLList = imgURLList;
	}

	public static void extractPlatform(ContentListItemData itemData)
	{
		Pattern platformPattern = Pattern.compile("(posted by wap, platform: .+?)\\s*<br(\\s*?\\/)*?>");
		Pattern androidPattern = Pattern.compile(APIURL.ANDROID_CLIENT_SIGNATURE_REGEX);
		Matcher platformMatcher = platformPattern.matcher(itemData.mainText);
		if (platformMatcher.find())
		{
			itemData.platformInfo = platformMatcher.group(1);
			itemData.mainText = itemData.mainText.replaceAll(platformPattern.pattern(), "");
		}
		Matcher androidMatcher = androidPattern.matcher(itemData.mainText);
		if (androidMatcher.find())
		{
			itemData.platformInfo = androidMatcher.group(1);
			itemData.mainText = itemData.mainText.replaceAll(androidPattern.pattern(), "");
		}
		itemData.mainText = cleanText(itemData.mainText);
		itemData.mainText = cleanEditHistory(itemData.mainText);
	}

	public static String cleanQuote(String html)
	{
		String s = html.replaceAll("<\\/?i>", "").replaceAll("\\s{2}", " ");
		return s;
	}


	public static String cleanText(String html)
	{
		html = html.replaceAll("\\[color=#(.{6})\\]", "").replaceAll("\\[\\/color\\]", "");
		html = html.replaceAll("\\[size=([^\\[])+\\]", "").replaceAll("\\[\\/size\\]", "");
		html = html.replaceAll("(\\s*<\\/?br\\s*\\/?>\\s*){2,}", "<br />");
		Document doc = Jsoup.parse(html);
		return doc.body().html();
	}

	public static String getPlainText(String html)
	{
		Document htmlDoc = Jsoup.parse(html);
		htmlDoc.select("br").append("#br#");
		String text = htmlDoc.text();
		String s = text.replaceAll("(\\s*#br#\\s*)+", "\n").replaceAll("^\n", "").replaceAll("\n$", "");
		s = cleanEditHistory(s);
		s = s.replaceAll("(posted by wap, platform: .+?)\\s*", "").replaceAll("\\s*_{8,}\\s*(发送自.+?客户端)", "");
		return s;
	}

	public static String cleanEditHistory(String html)
	{
		return html
				.replaceAll("\\[?\\s*?(<i>)?\\s*本帖最后由.+?于.+?编辑\\s*(<\\/i>)?\\s*?\\]?", "");
	}

	public static void generalContentParser(String html, ContentListPageData pageData, int tid)
	{
		pageData.tid = tid;
		Pattern titlePattern = Pattern.compile("<title>(.+)-TGFC 手机版<\\/title>");
		Matcher titleMatcher = titlePattern.matcher(html);
		if (titleMatcher.find())
		{
			pageData.title = titleMatcher.group(1);
		}
		Pattern pageInfoPattern = Pattern.compile("\\((\\d+)\\/(\\d+)页\\)<\\/span>");
		Matcher pageInfoMatcher = pageInfoPattern.matcher(html);
		if (pageInfoMatcher.find())
		{
			pageData.currentPage = Integer.parseInt(pageInfoMatcher.group(1));
			pageData.totalPageCount = Integer.parseInt(pageInfoMatcher.group(2));
		}
		if (html.contains("[此主题已关闭]"))
		{
			pageData.isClosed = true;
		}
		Pattern replyCountPattern = Pattern.compile("<b>回复列表 (\\d+)<\\/b>");
		Matcher replyCountMatcher = replyCountPattern.matcher(html);
		if (replyCountMatcher.find())
		{
			pageData.totalReplyCount = Integer.parseInt(replyCountMatcher.group(1));
		}
		else
		{
			pageData.totalReplyCount = 1;
		}
		Pattern fidPattern = Pattern.compile("fid=(\\d+)");
		Matcher fidMatcher = fidPattern.matcher(html);
		if (fidMatcher.find())
		{
			pageData.fid = Integer.parseInt(fidMatcher.group(1));
		}
	}

	public static HttpResult<String> fetchUsername()
	{
		HttpResult<String> httpResult = httpGetString(APIURL.WAP_MY_INFO, APIURL.WAP_API_URL, true, false);
		HttpResult<String> usernameResult = new HttpResult<String>();
		if (httpResult.hasError == false)
		{
			String html = StringEscapeUtils.unescapeHtml(httpResult.result);
			if (html.contains("请登录后使用本功能"))
			{
				usernameResult.setErrorInfo("登录态失效，请重新登录", HttpResult.ERROR_TYPE_LOGIN_REQUIRED);
			}
			else
			{
				Pattern usernamePattern = Pattern.compile("用户名:\\s(.+?)<br \\/>");
				Matcher matcher = usernamePattern.matcher(html);
				if (matcher.find())
				{
					usernameResult.setResult(matcher.group(1));
				}
				else
				{
					usernameResult.setErrorInfo("API错误！可能您的网络连接受到干扰或者已中断", HttpResult.ERROR_TYPE_API_ERROR);
				}
			}
		}
		return usernameResult;
	}

	public static HttpResult<Boolean> post(boolean isReply, boolean hasQuote, boolean isEdit, Integer fid, Integer tid, Integer quotePid, Integer editPid, String title, String content)
	{
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		String apiURL;
		postParams.add(new BasicNameValuePair("subject", title));
		postParams.add(new BasicNameValuePair("message", content));
		List<NameValuePair> getParams = new ArrayList<NameValuePair>();
		getParams.add(new BasicNameValuePair("fid", fid.toString()));
		if (isEdit)
		{
			apiURL = APIURL.WAP_POST_EDIT;
			postParams.add(new BasicNameValuePair("pid", editPid.toString()));
			postParams.add(new BasicNameValuePair("tid", tid.toString()));
			postParams.add(new BasicNameValuePair("fid", fid.toString()));
			if (isReply == false)
			{
				postParams.add(new BasicNameValuePair("first", "1"));
			}
			else
			{
				postParams.add(new BasicNameValuePair("first", "0"));
			}
		}
		else
		{
			if (isReply == false)
			{
				apiURL = APIURL.WAP_POST_NEW;
			}
			else
			{
				apiURL = APIURL.WAP_POST_REPLY;
				postParams.add(new BasicNameValuePair("tid", tid.toString()));
				if (hasQuote)
				{
					postParams.add(new BasicNameValuePair("pid", quotePid.toString()));
				}
			}
		}
		HttpResult<String> httpResult = httpPostString(apiURL, APIURL.WAP_API_URL, false, postParams, getParams);
		HttpResult<Boolean> postResult = new HttpResult<Boolean>(httpResult);
		String html = StringEscapeUtils.unescapeHtml(httpResult.result);
		if (html.contains("成功"))
		{
			postResult.result = true;
		}
		else
		{
			postResult.result = false;
			Document doc = Jsoup.parse(html);
			HtmlToPlainText htmlToPlainText = new HtmlToPlainText();
			postResult.setErrorInfo(htmlToPlainText.getPlainText(doc.select("body").first()), HttpResult.ERROR_TYPE_OTHERS);
		}
		return postResult;
	}

	public static HttpResult<ImageDownloadData> downloadImage(String url, String referer){
		HttpResponse response = httpGet(url, referer, false, true);
		HttpResult<ImageDownloadData> httpResult = new HttpResult<ImageDownloadData>();
		if (response != null && response.getStatusLine()!= null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			String contentType = response.getEntity().getContentType().getValue();
			if (contentType.contains("image") == false)
			{
				httpResult.setErrorInfo("Not Image", HttpResult.ERROR_TYPE_NOT_IMAGE);
				return httpResult;
			}
			else
			{
				ImageDownloadData data = new ImageDownloadData();
				if (contentType.contains("jpg") || contentType.contains("jpeg"))
				{
					data.setImageTypeJpeg();
				}
				else if (contentType.contains("png"))
				{
					data.setImageTypePng();
				}
				else if (contentType.contains("gif"))
				{
					data.setImageTypeGif();
				}
				else
				{
					httpResult.setErrorInfo("Not Supported Image Type", HttpResult.ERROR_TYPE_NOT_IMAGE);
					return httpResult;
				}
				try
				{
					data.loadFromInputStream(response.getEntity().getContent());
					httpResult.setResult(data);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					httpResult.setErrorInfo("Image Download Error", HttpResult.ERROR_TYPE_NETWORK_FAIL);
				}
				return httpResult;
			}
		}
		else
		{
			httpResult.setErrorInfo(response.getStatusLine().toString(), HttpResult.ERROR_TYPE_NETWORK_FAIL);
			return httpResult;
		}
	}


	public static HttpResult<EditPostData> fetchEditText(int pid, int tid)
	{
		HttpResult<String> httpResult = httpGetString(APIURL.WAP_POST_EDIT, APIURL.WAP_API_URL, false, false, "pid", String.valueOf(pid), "tid", String.valueOf(tid));
		HttpResult<EditPostData> editResult = new HttpResult<EditPostData>(httpResult);
		if (httpResult.hasError)
		{
			return editResult;
		}
		else
		{
			String html = StringEscapeUtils.unescapeHtml(httpResult.result);
			Pattern editTitlePattern = Pattern.compile("<input.*?(type=\"hidden\")?\\s*name=\"subject\".*?value=\"(.*?)\"\\s*\\/>");
			Matcher editTitleMatcher = editTitlePattern.matcher(html);
			EditPostData editPostData = new EditPostData();
			editPostData.tid = tid;
			editPostData.pid = pid;
			if (editTitleMatcher.find())
			{
				//Log.w("group 1", editTitleMatcher.group(1));
				if (editTitleMatcher.group(1) == null || editTitleMatcher.group(1).trim().length() ==0)
				{
					editPostData.isReply = false;
					editPostData.title = editTitleMatcher.group(2);
				}
				else
				{
					editPostData.isReply = true;
				}
			}
			Pattern editTextPattern = Pattern.compile("<textarea[^>]*>([^<]*)<\\/textarea>");
			Matcher editTextMatcher = editTextPattern.matcher(html);
			if (editTextMatcher.find())
			{
				editPostData.content = editTextMatcher.group(1);
				editPostData.content = cleanEditHistory(editPostData.content);
				editPostData.content = editPostData.content.replaceAll(APIURL.ANDROID_CLIENT_SIGNATURE_EDIT_REGEX, "");
				editResult.setResult(editPostData);
			}
			else
			{
				editResult.setErrorInfo("网页解析错误", HttpResult.ERROR_TYPE_API_ERROR);
			}
			return editResult;
		}
	}
}
