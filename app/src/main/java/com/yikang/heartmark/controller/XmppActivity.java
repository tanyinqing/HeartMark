package com.yikang.heartmark.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.heartmark.R;


/**
 * Created by weichyang on 2015/5/15.
 */
public class XmppActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_fragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragemnt_content, new ChattingFragment()).commit();

    }
}