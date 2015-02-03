package com.linangran.tgfcapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.linangran.tgfcapp.R;

/**
 * Created by linangran on 3/2/15.
 */
public class SwipeBackActivity extends ActionBarActivity
{

	private LinearLayout containerLinearLayout;
	private ImageView leftImageView, rightImageView;
	private GestureDetector.SimpleOnGestureListener onSwipeListener;
	private GestureDetector detector;

	private int swipeThreshold = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(R.layout.activity_swipe_back);
		this.containerLinearLayout = (LinearLayout) findViewById(R.id.activity_swipe_back_container);
		this.swipeThreshold = (int) (this.containerLinearLayout.getWidth() * 0.2);
		getLayoutInflater().inflate(layoutResID, this.containerLinearLayout);
		this.leftImageView = (ImageView) findViewById(R.id.activity_swipe_back_left);
		this.rightImageView = (ImageView) findViewById(R.id.activity_swipe_back_right);
		this.onSwipeListener = new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
			{
				super.onFling(e1, e2, velocityX, velocityY);
				//Log.w("", e1.toString() + e2.toString());
				if (e1.getX() - e2.getX() < -swipeThreshold)
				{
					finishToRight();
				}
				else if (e1.getX() - e2.getX() > swipeThreshold)
				{
					finishToLeft();
				}
				return true;
			}
		};
		this.detector = new GestureDetector(this, this.onSwipeListener);
		this.leftImageView.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return !detector.onTouchEvent(event);
			}
		});
		this.rightImageView.setOnTouchListener(new View.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return !detector.onTouchEvent(event);
			}
		});
	}

	private void finishToLeft()
	{
		this.finish();
		overridePendingTransition(R.anim.hold, R.anim.slide_to_left_out);
	}

	private void finishToRight()
	{
		this.finish();
		overridePendingTransition(R.anim.hold, R.anim.slide_to_right_out);
	}


}
