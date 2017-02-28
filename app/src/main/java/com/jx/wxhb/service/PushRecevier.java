package com.jx.wxhb.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.jx.wxhb.model.PushMessage;

public class PushRecevier extends BroadcastReceiver {
    public PushRecevier() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("jun", "onReceive: "+intent.getAction());
        if (intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")){
            Log.d("jun", "onReceive: "+intent.getStringExtra("cn.jpush.android.CONTENT_TYPE"));
        }
    }
}
