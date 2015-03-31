package com.chu.adapter;

import com.chu.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

public class CornerListView extends ListView {

	/*
	 * Ô²½ÇListviewÊ¾Àý
	 */

	public CornerListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CornerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CornerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			int itemnum = pointToPosition(x, y);

			if (itemnum == AdapterView.INVALID_POSITION)
				break;
			else {
				if (itemnum == 0) {
					if (itemnum == (getAdapter().getCount() - 1)) {
						setSelector(R.drawable.app_list_corner_round);
					} else {
						setSelector(R.drawable.app_list_corner_round_top);
					}
				} else if (itemnum == (getAdapter().getCount() - 1)) {
					setSelector(R.drawable.app_list_corner_round_bottom);
				} else {
					setSelector(R.drawable.app_list_corner_shape);
				}
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
