package com.yikang.heartmark.view;

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
public class CeLingDataView extends LinearLayout{
	private Context context;
	private LinearLayout curveMonthLayout;
	private GraphicalView mGraphicalView;
	public CeLingDataView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.celing_data_view,this, true);
		init();
	}
	
	public CeLingDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.celing_data_view, this, true);
		init();
	}
	
	private void init(){
		curveMonthLayout = (LinearLayout) findViewById(R.id.celing_curve_layout);
		showCurve();
	}
	
	//显示表格
			private void showCurve() {
				double[] x = new double[] { 3, 6, 10, 15, 17, 20, 25 }; // x坐标值
				double[] y = new double[] { 2, 9, 7, 8, 2, 6, 7 }; // y坐标值
				int[] colors = new int[] { Color.GREEN, Color.GRAY, Color.GREEN }; // 用于曲线设置曲线的颜色和曲线的名字颜色
				PointStyle[] styles = new PointStyle[] { PointStyle.SQUARE,
						PointStyle.DIAMOND, PointStyle.TRIANGLE }; // 曲线的样式,有多种
				XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(); // 最主要的一个对象，用于设置曲线的很多参数配置
				renderer.setAxisTitleTextSize(20); // 设置表示x轴，Y轴信息文本的大小
				renderer.setChartTitleTextSize(20); // 曲线图说明文字的大小
				renderer.setLabelsTextSize(20); // x,y轴上面坐标文本的字体大小
				renderer.setLegendTextSize(20); // 设置下方表示曲线名字的文本字体大小
				renderer.setGridColor(Color.LTGRAY); // 设置网格边框的颜色
				renderer.setPointSize(6f); // 设置曲线上面小图形的大小
				renderer.setMargins(new int[] { 50, 50, 50, 10 }); // 数据分别为曲线图离屏幕的上左下右的间距
				renderer.setXLabels(12); // 网格x轴的大概条数
				renderer.setYLabels(10); // 网格Y轴的大概条数
				renderer.setShowGrid(true); // 是否显示网格
				renderer.setApplyBackgroundColor(true); // 是否可以设置背景颜色，默认为false
			    renderer.setMarginsColor(Color.BLUE); //设置外面背景色
			    // renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0)); // 穿透背景色
				renderer.setBackgroundColor(Color.WHITE); // 设置网格背景颜色，需要和上面属性一起用
				renderer.setYLabelsAlign(Align.RIGHT); // 以Y轴的哪个地方对其
			    renderer.setZoomButtonsVisible(true); // 是否显示右下角的放大缩小还原按钮
				// renderer.setPanLimits(new double[] { 1, 10, 0, 20 }); //
				// renderer.setZoomLimits(new double[] { 100, 50, 20, 5 }); //点击放大缩小时候使用
				// renderer.setChartTitle("未来3天 天气情况"); // 设置图标标题，title中上方
				renderer.setZoomEnabled(false, false);// 设置图表是否缩放
				renderer.setPanEnabled(false, false); // 设置图表是否移动 参数为 x,y轴
				renderer.setXTitle("时间(月)"); // 设置x轴名字
				renderer.setYTitle("测量值"); // y轴名字
				renderer.setXAxisMin(0); // x轴坐标最小值
				renderer.setXAxisMax(31); // x轴坐标最大值
				renderer.setYAxisMin(0); // y轴最小值
				renderer.setYAxisMax(10); // y值最大值
				renderer.setLabelsColor(Color.GREEN); //x,y轴标题颜色（时间。。。）
				renderer.setPanEnabled(false);
				renderer.setZoomEnabled(false);
				renderer.setAxesColor(Color.GREEN); // x，y轴的颜色
				
				XYSeriesRenderer r = new XYSeriesRenderer();
				r.setColor(colors[0]);    //曲线颜色
				r.setPointStyle(styles[1]);//点的样式
				r.setFillPoints(true); // 设置填充点
				r.setLineWidth(2);//设置线的宽度
				renderer.addSeriesRenderer(r);
				
				//进行显示
				XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); // 设置图下面显示的信息
				XYSeries series = new XYSeries("血糖趋势分析图");
				double[] xV = x;
				double[] yV = y;
				int seriesLength = xV.length;
				//填充数据
				for (int k = 0; k < seriesLength; k++) {
					series.add(xV[k], yV[k]);
				}
				dataset.addSeries(series);
				// 生成图表视图
				mGraphicalView = ChartFactory.getLineChartView(context,
						dataset, renderer);
				mGraphicalView.setId(0);
				// 添加至父容器
				curveMonthLayout.addView(mGraphicalView, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			}
}

