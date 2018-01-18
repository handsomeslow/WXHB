package com.jx.wxhb.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.jx.wxhb.MyApplication;
import com.jx.wxhb.widget.QuestionFloatView;

import java.util.Timer;

/**
 * Author Jun
 * Email:xujun02@58ganji.com
 * Desc:
 * Created by Jun on 2018/1/18.
 */

public class QuestionFloatService extends Service {

    private QuestionFloatView questionFloatView;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        questionFloatView = new QuestionFloatView(MyApplication.context);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (questionFloatView != null) {
            //移除悬浮窗口
            questionFloatView.remove();
        }
        timer.cancel();
    }

}
