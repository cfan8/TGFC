package com.linangran.tgfcapp.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.data.EditPostData;
import com.linangran.tgfcapp.data.HttpResult;
import com.linangran.tgfcapp.tasks.EditPostTask;
import com.linangran.tgfcapp.tasks.PostTask;
import com.linangran.tgfcapp.utils.ErrorHandlerUtils;
import com.linangran.tgfcapp.utils.PreferenceUtils;

/**
 * Created by linangran on 26/1/15.
 */
public class PostFragment extends Fragment
{
	EditText titleEditText;
	TextView quotedTextView;
	EditText contentEditText;
	LinearLayout quotedTextLayout;
	LinearLayout titleLayout;
	public boolean doNotSave = false;

	int fid = -1;
	int tid = -1;
	int quotePid = -1;
	int editPid = -1;
	boolean isReply;
	String mainTitle;
	boolean hasQuote;
	String quotedText;
	boolean isEdit = false;

	ProgressDialog loadingDialog;


	PostTask postTask;
	EditPostTask editPostTask;

	@Override

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		Bundle bundle = getArguments();
		isReply = bundle.getBoolean("isReply", false);
		isEdit = bundle.getBoolean("isEdit", false);
		if (isEdit)
		{
			editPid = bundle.getInt("editPid");
			tid = bundle.getInt("tid");
		}
		else
		{
			fid = bundle.getInt("fid");
			if (isReply)
			{
				tid = bundle.getInt("tid");
				hasQuote = bundle.getBoolean("hasQuote", false);
				if (hasQuote)
				{
					quotePid = bundle.getInt("quotePid");
					quotedText = bundle.getString("quotedText");
				}
				mainTitle = bundle.getString("mainTitle");
			}
		}
		View fragment = inflater.inflate(R.layout.fragment_post, container, false);
		this.titleEditText = (EditText) fragment.findViewById(R.id.fragment_post_title_edit_text);
		this.quotedTextView = (TextView) fragment.findViewById(R.id.fragment_post_quoted_text_view);
		this.contentEditText = (EditText) fragment.findViewById(R.id.fragment_post_content_edit_text);
		this.titleLayout = (LinearLayout) fragment.findViewById(R.id.fragment_post_title_layout);
		this.quotedTextLayout = (LinearLayout) fragment.findViewById(R.id.fragment_post_quoted_text_layout);
		updateUI();
		if (isEdit)
		{
			startFetchEditText(editPid, tid);
		}
		return fragment;
	}

	public void updateUI()
	{
		if (isReply)
		{
			this.titleLayout.setVisibility(View.GONE);
			if (hasQuote)
			{
				this.quotedTextLayout.setVisibility(View.VISIBLE);
				this.quotedTextView.setText(Html.fromHtml(quotedText), TextView.BufferType.SPANNABLE);
			}
			else
			{
				this.quotedTextLayout.setVisibility(View.GONE);
			}
		}
		else
		{
			this.titleLayout.setVisibility(View.VISIBLE);
			this.quotedTextLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_fragment_post, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		ClearContentListener clearContentListener;
		AlertDialog.Builder builder;
		switch (item.getItemId())
		{
			case R.id.menu_fragment_post_clear:
				clearContentListener = new ClearContentListener(false);
				builder = new AlertDialog.Builder(this.getActivity()).setMessage("确定清空吗？\n这也会同时清除您已保存的草稿").setPositiveButton(R.string.text_positive, clearContentListener).setNegativeButton(R.string.text_negative, clearContentListener);
				builder.show();
				return true;
			case R.id.menu_fragment_post_discard:
				clearContentListener = new ClearContentListener(true);
				builder = new AlertDialog.Builder(this.getActivity()).setMessage("确定放弃所有改动吗？\n您自上次保存后的所有改动均会丢失").setPositiveButton(R.string.text_positive, clearContentListener).setNegativeButton(R.string.text_negative, clearContentListener);
				builder.show();
				return true;
			case R.id.menu_fragment_post_send:
				startPostTask();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

	class ClearContentListener implements DialogInterface.OnClickListener
	{
		public boolean isDiscard = false;

		public ClearContentListener(boolean isDiscard)
		{
			this.isDiscard = isDiscard;
		}

		@Override
		public void onClick(DialogInterface dialogInterface, int i)
		{
			switch (i)
			{
				case DialogInterface.BUTTON_POSITIVE:
					if (isDiscard)
					{
						discardDraft();
					}
					else
					{
						clearText();
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
			}
		}
	}

	public void clearText()
	{
		this.titleEditText.setText("");
		this.contentEditText.setText("");
		PreferenceUtils.discardDraft();
		Toast.makeText(this.getActivity(), "草稿已清空", Toast.LENGTH_SHORT).show();
	}

	public void discardDraft()
	{
		if (PreferenceUtils.hasDraft())
		{
			this.titleEditText.setText(PreferenceUtils.getDraftTitle());
			this.contentEditText.setText(PreferenceUtils.getDraftContent());
		}
		this.doNotSave = true;
		Toast.makeText(this.getActivity(), "已撤销所有改动", Toast.LENGTH_SHORT).show();
		navigationReturn();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (this.titleEditText.getText().toString().trim().length() != 0 || this.contentEditText.getText().toString().trim().length() != 0)
		{
			if (doNotSave == false)
			{
				PreferenceUtils.saveDraft(this.titleEditText.getText().toString().trim(), this.contentEditText.getText().toString().trim());
				Toast.makeText(this.getActivity(), "已保存为草稿", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (PreferenceUtils.hasDraft())
		{
			this.titleEditText.setText(PreferenceUtils.getDraftTitle());
			this.contentEditText.setText(PreferenceUtils.getDraftContent());
		}
	}

	public void finishPost(HttpResult<Boolean> result)
	{
		this.loadingDialog.dismiss();
		if (result.hasError)
		{
			ErrorHandlerUtils.handleError(result, this.getActivity());
		}
		else
		{
			if (result.result == true)
			{
				Toast.makeText(this.getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
				PreferenceUtils.discardDraft();
				this.doNotSave = true;
				this.navigationReturn();
			}
			else
			{
				Toast.makeText(this.getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void navigationReturn()
	{
		this.getActivity().finish();
	}

	public void startPostTask()
	{
		this.postTask = new PostTask(this);
		String title = isReply ? mainTitle : titleEditText.getText().toString();
		String content = contentEditText.getText().toString();
		this.postTask.setPostData(isReply, hasQuote, isEdit, fid, tid, quotePid, editPid, title, content);
		this.loadingDialog = ProgressDialog.show(this.getActivity(), "", "正在提交，请稍后...", true);
		this.postTask.execute();
	}

	public void startFetchEditText(int pid, int tid)
	{
		loadingDialog = ProgressDialog.show(this.getActivity(), "", "正在加载原始回帖", true);
		if (this.editPostTask == null || this.editPostTask.getStatus().equals(AsyncTask.Status.RUNNING) == false)
		{
			this.editPostTask = new EditPostTask(pid, tid, this);
			this.editPostTask.execute();
		}
	}

	public void finishFetchEditText(HttpResult<EditPostData> result)
	{
		loadingDialog.dismiss();
		if (result.hasError)
		{
			ErrorHandlerUtils.handleError(result, this.getActivity());
		}
		else
		{
			this.isReply = result.result.isReply;
			//Log.w("", String.valueOf(this.isReply));
			this.hasQuote = false;
			if (isReply == false)
			{
				this.titleEditText.setText(result.result.title);
			}
			this.contentEditText.setText(result.result.content);
			updateUI();
		}
	}
}
