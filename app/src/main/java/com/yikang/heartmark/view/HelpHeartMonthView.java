package com.yikang.heartmark.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.heartmark.R;
import com.yikang.heartmark.database.HelpHeartDB;
import com.yikang.heartmark.model.HelpHeart;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.DateUtil;

public class HelpHeartMonthView extends LinearLayout {
	private Context context;
	public List<Double> dayList = new ArrayList<Double>();
	public List<Double> valueList = new ArrayList<Double>();
	private String uid;
	private HelpHeartDB heartDB = null;

	public HelpHeartMonthView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.huli_weight_view, this,
				true);
		init();
	}

	public HelpHeartMonthView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.huli_weight_view, this,
				true);
		init();
	}

	private void init() {
		uid = ConstantUtil.getUid(context);
		heartDB = new HelpHeartDB(context);
		showCurve();
	}

	// 显示表格
	public void showCurve() {
		dayList.clear();
		valueList.clear();
		ArrayList<HelpHeart> heartList = heartDB.getHeartListByTime(uid,
				DateUtil.getLongOfFirstDayOfMonth(0),
				DateUtil.getLongOfLatestDayOfMonth(0));
		if(heartList.size() == 0){
			dayList.add(0.0);
			valueList.add(0.0);
		}
		for (int i = 0; i < heartList.size(); i++) {
			dayList.add(Double.valueOf(heartList.get(i).day));
			valueList.add(Double.valueOf(heartList.get(i).heart));
		}

		// dayList.add((double) 1);
		// dayList.add((double) 10);
		// dayList.add((double) 20);
		//
		// valueList.add((double) 1);
		// valueList.add((double) 20);
		// valueList.add((double) 2);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setBackgroundColor(Color.parseColor("#ffffff"));// 表格背景颜色
		renderer.setMarginsColor(Color.parseColor("#ffffff"));// 表格四周的颜色
		renderer.setApplyBackgroundColor(true);// 是否显示表格内的颜色

		renderer.setLabelsTextSize(30f);// 坐标轴字体大小
		renderer.setAxisTitleTextSize(30f); // 设置表示x轴，Y轴信息文本的大小
		renderer.setXTitle("时间(天)"); // 设置x轴名字
		renderer.setYTitle("心率(bpm)"); // y轴名字
		renderer.setXAxisMin(0); // x轴坐标最小值
		renderer.setXAxisMax(31); // x轴坐标最大值
		renderer.setXLabelsColor(Color.parseColor("#000000"));
		renderer.setYAxisMin(0); // y轴最小值
		renderer.setYAxisMax(150); // y值最大值
		renderer.setYLabelsColor(0, (Color.parseColor("#000000")));
		renderer.setPointSize(2f); // 点的大小
		renderer.setLabelsColor(Color.parseColor("#000000")); // x,y轴标题颜色（时间。。。）
		renderer.setAxesColor(Color.parseColor("#000000"));// 坐标轴的颜色
		renderer.setLegendTextSize(10f); // 设置下方表示曲线名字的文本字体大小
		renderer.setChartTitleTextSize(20f); // 曲线图说明文字的大小

		renderer.setMargins(new int[] { 80, 80, 40, 40 }); // 数据分别为曲线图离屏幕的上左下右的间距
		renderer.setPanLimits(new double[] { 0, 31, 0.00, 35.00 });
		renderer.setXLabels(10); // 网格x轴的大概条数
		renderer.setYLabels(10); // 网格Y轴的大概条数
		renderer.setShowGrid(true); // 是否显示网格
		renderer.setGridColor(Color.parseColor("#dfdfdf"));// 分割线的颜色
		renderer.setYLabelsAlign(Align.RIGHT); // 以Y轴的哪个地方对其
		renderer.setZoomButtonsVisible(false); // 是否显示右下角的放大缩小还原按钮
		renderer.setZoomEnabled(false, false);// 设置图表是否缩放
		renderer.setPanEnabled(false, false); // 设置图表是否移动 参数为 x,y轴s

		// 曲线设置
		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
		xyRenderer.setColor(Color.parseColor("#000000"));// 曲线点线的颜色
		xyRenderer.setLineWidth(3f);// 曲线的线宽ee
		xyRenderer.setDisplayChartValues(false);// 是否显示曲线上的点的值
		xyRenderer.setChartValuesTextSize(30f);// 曲线值得字体大小
		// xyRenderer.setDisplayChartValuesDistance(30);
		xyRenderer.setPointStyle(PointStyle.CIRCLE);// 点的形状
		xyRenderer.setFillBelowLine(false);// 是否显示线下的阴影
		xyRenderer.setFillBelowLineColor(Color.parseColor("#4fc1e9"));// 阴影的颜色
		xyRenderer.setFillPoints(true);// 是否为实心点
		renderer.addSeriesRenderer(xyRenderer);

		// 填充数据
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); // 设置图下面显示的信息
		XYSeries series = new XYSeries("");
		int seriesLength = dayList.size();
		for (int k = 0; k < seriesLength; k++) {
			series.add(dayList.get(k), valueList.get(k));
		}
		dataset.addSeries(series);
		// 生成图表视图
		GraphicalView graphicalView = ChartFactory.getCubeLineChartView(
				context, dataset, renderer, 0.3f);
		graphicalView.setId(0);

		LinearLayout curveWeekLayout = (LinearLayout) findViewById(R.id.huli_curve_layout);
		curveWeekLayout.removeAllViews();
		// 添加至父容器
		curveWeekLayout.addView(graphicalView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
}
