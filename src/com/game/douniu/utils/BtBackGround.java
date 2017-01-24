package com.game.douniu.utils;

import android.graphics.ColorMatrixColorFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BtBackGround implements OnTouchListener {

	public final static float[]	BT_SELECTED		= new float[]{1.5f, 0, 0, 0, 1.5f, 0, 1.5f, 0, 0, 1.5f, 0, 0, 1.5f, 0, 1.5f, 0, 0, 0, 1, 0};

	public final static float[]	BT_NOT_SELECTED	= new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};

	public ColorMatrixColorFilter	cm_select;

	public ColorMatrixColorFilter	cm_no_select;

	public BtBackGround() {
		cm_select = new ColorMatrixColorFilter(BT_SELECTED);
		cm_no_select = new ColorMatrixColorFilter(BT_NOT_SELECTED);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && v.isClickable()) {
			v.getBackground().setColorFilter(cm_select);
			v.setBackgroundDrawable(v.getBackground());
			//((GameActivity) GameActivity.getInstance()).onPlayGameSound(GDF.SOUND_CLICK);
		} else if (event.getAction() != MotionEvent.ACTION_MOVE) {
			v.getBackground().setColorFilter(cm_no_select);
			v.setBackgroundDrawable(v.getBackground());
		}
		return false;
	}

}
