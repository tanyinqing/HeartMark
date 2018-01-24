package com.yikang.heartmark.util;

import android.content.Context;

public class DpPxUtils {
	
	/** dip转换px */
	public static int dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** px转换dip */
	public static int px2dip(Context context, int px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
}
