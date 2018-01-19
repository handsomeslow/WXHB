package com.jx.wxhb.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.wxhb.R;

/**
 * Author Jun
 * Desc:
 * Created by Jun on 2018/1/18.
 */

public class QuestionFloatView extends LinearLayout {

    private WindowManager.LayoutParams wmParams;

    //创建浮动窗口设置布局参数的对象
    private WindowManager windowManager;

    private TextView answerTextView;

    private boolean isShow = false;

    public QuestionFloatView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        initWindowManager(context);
        LayoutInflater.from(context).inflate(R.layout.question_float_layout, this);

        answerTextView = (TextView) findViewById(R.id.answer_text_view);

        show();
    }

    private void initWindowManager(Context context){
        wmParams = new WindowManager.LayoutParams();

        windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        //设置window type，TOAST不需要申请权限，7.0以上不再支持TOAST
        if (Build.VERSION.SDK_INT > 24) {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public void showAnswer(String a) {
        answerTextView.setText(a);
    }

    public void remove(){
        //移除悬浮窗口
        windowManager.removeView(this);
    }

    public void show() {
        if (!isShown() && !isActivated()) {
            windowManager.addView(this,wmParams);
        }
    }

}
