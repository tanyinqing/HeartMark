/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.yikang.heartmark.view;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @ClassName PieChart01View
 * @Description 饼图的例子
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class PieChart01View extends DemoView implements Runnable {

	private String TAG = "PieChart01View";
	private PieChart chart = new PieChart();
	private ArrayList<PieData> chartData = new ArrayList<PieData>();//这个圆分为几个部分  2个部分

	public PieChart01View(Context context) {
		super(context);
	}

	public PieChart01View(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PieChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initView(int chance) {
		chartDataSet(chance);
		chartRender();
		new Thread(this).start();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {

			chart.setPadding(20, 20, 20, 20);

			// 设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
			// chart.setInitialAngle(90);

			// 标签显示(隐藏，显示在中间，显示在扇区外面)
			chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
			chart.getLabelPaint().setColor(Color.WHITE);
			chart.getLabelPaint().setTextSize(40);
			// 禁止平移
			chart.disablePanMode();
			// 禁止缩放
			chart.disableScale();

			// 标题
			// chart.setTitle("饼图-Pie Chart");
			// chart.addSubtitle("(XCL-Charts Demo)");
			// chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);

			// chart.setDataSource(chartData);

			// 激活点击监听
			// chart.ActiveListenItemClick();
			// chart.showClikedFocus();

			// 设置允许的平移模式
			// chart.enablePanMode();
			// chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

			// 显示图例
			PlotLegend legend = chart.getPlotLegend();
			legend.show();
			legend.setType(XEnum.LegendType.COLUMN);
			legend.setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
			legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
			legend.showBox();

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	public void chartDataSet(int chance) {
		int normal = 100 - chance;
		chartData.add(new PieData(chance+"%", chance, Color.rgb(243, 195, 82)));
		chartData.add(new PieData(normal+"%", normal, Color.rgb(190, 209, 131)));
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public List<XChart> bindChart() {
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		return lst;
	}

	@Override
	public void run() {
		try {
			chartAnimation();
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}

	private void chartAnimation() {
		try {

			chart.setDataSource(chartData);
			int count = 360 / 10;

			for (int i = 1; i < count; i++) {
				Thread.sleep(40);

				chart.setTotalAngle(10 * i);

				// 激活点击监听
				if (count - 1 == i) {
					chart.setTotalAngle(360);

					// chart.ActiveListenItemClick();
					// 显示边框线，并设置其颜色
					//chart.getArcBorderPaint().setColor(Color.YELLOW);
					//chart.getArcBorderPaint().setStrokeWidth(3);
				}

				postInvalidate();
			}

		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}

	}

}
