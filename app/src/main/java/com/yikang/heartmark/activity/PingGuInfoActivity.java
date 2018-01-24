package com.yikang.heartmark.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.ceshi.PromptManager;

import com.example.heartmark.R;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.PingGuResult;
import com.yikang.heartmark.util.ConnectUtil;
import com.yikang.heartmark.util.ConstantUtil;
import com.yikang.heartmark.util.ShowUtil;
import com.yikang.heartmark.view.TopBarView;
import com.yikang.heartmark.view.TopBarView.OnTopbarLeftButtonListener;
import com.yuzhi.framework.util.ConnectionManager;
import com.yuzhi.framework.util.JsonUtil;

public class PingGuInfoActivity extends BaseActivity implements
		OnTopbarLeftButtonListener {
	private TextView textView;
	private ListView listView;
	private Button button;
	
	private String title = null;
	private int count = 0;
	//这个是选项后面的按钮的集合
	private ArrayList<Button> optionButtonList= new ArrayList<Button>();
	//所有数据
	public List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
	//统计答题
	public List<Map<String, String>> uploadList = new ArrayList<Map<String, String>>();
	//单题选项
	public ArrayList<String> optionList = new ArrayList<String>();
	public String infoFrom = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pinggu_info);
		init();
		//PromptManager.showToastTest(this,"这个是从主页的 病情评估 跳转过来的开始心衰评估的页面PingGuInfoActivity");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		TopBarView topbar = (TopBarView) findViewById(R.id.pingguInfoTopBar);
		topbar.setTopbarTitle(R.string.evaluate);
		topbar.setOnTopbarLeftButtonListener(this);

		textView = (TextView) findViewById(R.id.pinggu_info_textview);
		listView = (ListView) findViewById(R.id.pinggu_info_listview);
		button = (Button) findViewById(R.id.pinggu_info_button);

		button.setOnClickListener(listener);
		
		infoFrom = getIntent().getStringExtra("infoFrom");
		//取得所有题目信息
		if(infoFrom.equals(MainPingGuActivity.INFO_STAGE)){
			Map<String, String> params = new HashMap<String, String>();
			if (ConnectUtil.isConnect(AppContext.context)) {
				ConnectionManager.getInstance().send("FN11070WD00",
						"queryAssessmentInfo", params, "pingguInfoCallBack", this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context, R.string.check_network);
			}
		}else if(infoFrom.equals(MainPingGuActivity.INFO_FORTHWITH)){
			Map<String, String> params = new HashMap<String, String>();
			if (ConnectUtil.isConnect(AppContext.context)) {
				ConnectionManager.getInstance().send("FN11070WD00",
						"queryImmediateAssessmentInfo", params, "pingguInfoCallBack", this);
				handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
			} else {
				ShowUtil.showToast(AppContext.context, R.string.check_network);
			}
		}

	
	}

	//取得所有题目信息
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void pingguInfoCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(PingGuInfoActivity.this,R.string.check_network_timeout);
		} else {
			//ShowUtil.showToast(PingGuInfoActivity.this,"成功");
			dataList = (List<Map<String, String>>) resultMap.get("assessmentInfo");
			refreshView();
		}
	}

	//下一步按钮
	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			//判断是否有选中,否则不能   至少有一条被选中
			boolean isSelect = false;
			for(int i=0; i<optionButtonList.size(); i++){
				if(optionButtonList.get(i).isSelected()){
					isSelect = true;
				}
			}
			
			if(!isSelect){
				ShowUtil.showToast(PingGuInfoActivity.this, R.string.pinggu_choose);
				return;
			}
			
			//提取出选中哪一条
			for(int i=0; i<optionButtonList.size(); i++){
				if(optionButtonList.get(i).isSelected()){
					//计算出选中的下标
					int selected = count - (optionButtonList.size() - i);
					Map<String,String> params = new HashMap<String,String>();
					params.put("ITEM_CODE", dataList.get(selected).get("ITEM_CODE"));
					params.put("ITEM_DETAIL_CODE", dataList.get(selected).get("ITEM_DETAIL_CODE"));
					params.put("ITEM_SCORE", String.valueOf(Double.valueOf(String.valueOf(
							dataList.get(selected).get("ITEM_SCORE"))).intValue()));
					uploadList.add(params);
				}
			}
			
			//说明已经答完所有题,那么就把评估结果提交
			if(count >= dataList.size()){
				//ShowUtil.showToast(PingGuInfoActivity.this,"开始评估");
				Map<String, String> params = new HashMap<String, String>();
				String uploadString = JsonUtil.convertObjectToJson(uploadList);
				if (ConnectUtil.isConnect(AppContext.context)) {
					//即时评估结果
					if(infoFrom.equals(MainPingGuActivity.INFO_FORTHWITH)){
						params.put("assessmentContent", uploadString);
						ConnectionManager.getInstance().send("FN11070WD00","startAssessmentIM", params, 
								"forthwithResultCallBack", PingGuInfoActivity.this);
						handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
					}
					//阶段评估
					else if(infoFrom.equals(MainPingGuActivity.INFO_STAGE)){
						params.put("assessmentContent", uploadString);
						ConnectionManager.getInstance().send("FN11070WD00","startAssessment", params, 
								"stageResultCallBack", PingGuInfoActivity.this);
						handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_SHOW);
					}
					
				} else {
					ShowUtil.showToast(AppContext.context, R.string.check_network);
				}
				return;
			}
			//每次 点按钮，都刷新内容
			refreshView();
		}
	};
	
	//即时评估结果
	@SuppressWarnings({ "rawtypes"})
	public void forthwithResultCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(PingGuInfoActivity.this,R.string.check_network_timeout);
		} else {
			Map infoMap = (Map) resultMap.get("assessmentMap");
			String forthwithResult = (String) infoMap.get("RESULT_IM");
			
			Intent detailIntent = new Intent(PingGuInfoActivity.this, PingGuForthwithActivity.class);
			detailIntent.putExtra("pingguResult", forthwithResult);
			startActivity(detailIntent);
			finish();
			//ShowUtil.showToast(PingGuInfoActivity.this,"评估成功");
		}
	}
	
	//阶段评估结果
	@SuppressWarnings({ "rawtypes"})
	public void stageResultCallBack(Object data) {
		handlerBase.sendEmptyMessage(ConstantUtil.DIALOG_HINT);
		Map resultMap = (Map) data;
		if (resultMap == null) {
			ShowUtil.showToast(PingGuInfoActivity.this,R.string.check_network_timeout);
		} else {
//			LAST_MONITOR_DATA：24小时内最后一次Bnp测量结果
//			RESULT_PRO_VALUE：风险率值   ---
//			RESULT_TITLE：标题
//			RESULT_PRO：风险率
//			RESULT_STABLE：稳定性（如果为空则不显示）
//			RESULT_NURSE：日常护理
//			RESULT_BNP_VALUE:Bnp风险率    ---
//			RESULT_BNP：BNP评估结果
//			RESULT_BNP_FLUC_VALUE：BNP浮动值   
			DecimalFormat df = new DecimalFormat("#0.00");
			Map infoMap = (Map) resultMap.get("assessmentMap");
			PingGuResult pingguResult = new PingGuResult();
			pingguResult.result = (String) infoMap.get("LAST_MONITOR_DATA");
			pingguResult.hint = (String) infoMap.get("RESULT_TITLE");
			Double proValue = Double.valueOf(infoMap.get("RESULT_PRO_VALUE").toString());
			Double bnpValue = 0.0;
			if(infoMap.get("RESULT_BNP_VALUE") != null && !infoMap.get("RESULT_BNP_VALUE").equals("")){
				bnpValue = Double.valueOf(infoMap.get("RESULT_BNP_VALUE").toString());
			}
			Double value = (bnpValue*0.6+proValue/0.6*0.4);
			pingguResult.per = df.format(value);
			pingguResult.risk = (String) infoMap.get("RESULT_BNP");
			pingguResult.stable = (String) infoMap.get("RESULT_STABLE");
			pingguResult.huli = (String) infoMap.get("RESULT_NURSE");
			pingguResult.from = PingGuResult.INFO;
			
			Intent detailIntent = new Intent(PingGuInfoActivity.this, PingGuStageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("pingguResult", pingguResult);
			detailIntent.putExtras(bundle);
			startActivity(detailIntent);
			finish();
			//ShowUtil.showToast(PingGuInfoActivity.this,"评估成功");
		}
	}
	

	public void refreshView(){
		//视为新的一题
		boolean isOne = true;
		//多个选项刷新内容
		optionList.clear();
		//对答题进行循环 从count开始  也就是逐个读考题
		for(int i =count; i < dataList.size(); i++){
			if(isOne){
				title = dataList.get(count).get("ITEM_NAME");
				optionList.add(dataList.get(count).get("ITEM_DETAIL_NAME"));
				isOne = false;
			}else{
				//这个是得到标题
				title = dataList.get(count).get("ITEM_NAME");
				//如果这个标题和上一个标题yiy  那么退出循环
				if(!title.equals(dataList.get(count - 1).get("ITEM_NAME"))){
					title = dataList.get(count - 1).get("ITEM_NAME");
					break;
				}else{
					optionList.add(dataList.get(count).get("ITEM_DETAIL_NAME"));
				}
			}
			count ++;
		}
		
		if(count == dataList.size()){
			     button.setText("完成");
		     }
		//ShowUtil.showToast(PingGuInfoActivity.this, "最后 count = "+count);
		textView.setText(title);
		MyAdapter adapter = new MyAdapter(optionList);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onTopbarLeftButtonSelected() {
		finish();
	}

	// listView adapter
	class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater = null;
		private ArrayList<String> optionList; //选则题的数据

		public MyAdapter(ArrayList<String> options) {
			layoutInflater = LayoutInflater.from(PingGuInfoActivity.this);
			optionList = options;
			optionButtonList.clear();
		}

		@Override
		public int getCount() {
			int size = 0;
			if (optionList != null && optionList.size() > 0)
				size = optionList.size();
			return size;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (optionList == null || optionList.size() == 0)
				return null;
			final ViewHolder holder;

			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.pinggu_info_item,null);

				holder = new ViewHolder();
				TextView optionText = (TextView) convertView
						.findViewById(R.id.pinggu_info_item_text);
				Button optionButton = (Button) convertView
						.findViewById(R.id.pinggu_info_item_button);
				optionButtonList.add(optionButton);
				holder.optionText = optionText;
				holder.optionButton = optionButton;

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.optionText.setText(optionList.get(position));

			holder.optionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                     for(int i=0; i<optionButtonList.size(); i++){
                    	 if(i == position){
                    		 optionButtonList.get(i).setSelected(true);
                    	 }else{
                    		 optionButtonList.get(i).setSelected(false);
                    	 }
                     }
				}
			});
			return convertView;
		}

	}

	static class ViewHolder {
		TextView optionText;
		Button optionButton;
	}
}
