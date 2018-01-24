package com.yikang.heartmark.util;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class PicChartUtil{
	public View execute(Context context,int bottom, int top) {
		int[] colors = new int[] {Color.GREEN,Color.RED};
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		CategorySeries categorySeries = new CategorySeries("Vehicles Chart");
		categorySeries.add("", bottom);
		categorySeries.add("", top);
		return ChartFactory.getPieChartView(context, categorySeries, renderer);
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		// 显示标签
		renderer.setShowLabels(false);
		// 不显示底部说明
		renderer.setShowLegend(false);
		// 设置标签字体大小
		renderer.setLabelsTextSize(15);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setZoomEnabled(false);
		renderer.setPanEnabled(false);
		return renderer;
	}
}
