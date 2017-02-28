package com.jx.wxhb.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.jx.wxhb.model.PushMessage;

public class PushRecevier extends BroadcastReceiver {

    static PushRecevierImp pushRecevierImp;

    public PushRecevier() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")){
            Log.d("jun", "onReceive: "+intent.getStringExtra("cn.jpush.android.CONTENT_TYPE"));
            String type = intent.getStringExtra("cn.jpush.android.CONTENT_TYPE");
            if (type.equals("random_funny")){
                if (pushRecevierImp != null){
                    pushRecevierImp.onWinner();
                }
            }

        }
    }

    public void setPushRecevierImp(PushRecevierImp pushRecevierImp) {
        this.pushRecevierImp = pushRecevierImp;
    }
}
