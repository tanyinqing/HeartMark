package com.yikang.heartmark.controller.album;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.common.alummage.ImageBucket;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.controller.adapter.AlbumListAdapter;

import java.util.List;


/**
 * 相册列表
 * Created by chuanyhu on 2014/8/2.
 */
public class AlbumListActivity extends BaseActivity {

    public ImageView img_cancel;
    private ListView albumsListView;

    private List<ImageBucket> dataList;
    private AlbumListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_album_list);
        Log.i("执行了基类的方法", "-----------------AlbumListFragment-----------------------");
        dataList = AlbumHelper.getHelper().getImagesBucketList(false);
        albumsListView = (ListView) findViewById(R.id.listview_albums);
        img_cancel = (ImageView) findViewById(R.id.bt_cancel);
        initView();
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        adapter = new AlbumListAdapter(AlbumListActivity.this, dataList);
        albumsListView.setAdapter(adapter);

        albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //todo 跳转到其他地方
                Intent intent = new Intent();
                intent.putExtra(Constants.ImageBucket_ParamName, dataList.get(position));
                setResult(1, intent);
                finish();

            }

        });

        //取消
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
