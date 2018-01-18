package com.jx.wxhb.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.jx.wxhb.MyApplication;
import com.jx.wxhb.R;
import com.jx.wxhb.utils.QuestionParameter;
import com.jx.wxhb.widget.QuestionFloatView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Desc 答题监控
 * Created by Jun on 2017/1/18.
 */

public class QuestionAccessibilityService extends AccessibilityService {
    private String questionContent;
    private List<AccessibilityNodeInfo> parents;
    private QuestionFloatView questionFloatView;
    private Timer timer;
    private Handler handler = new Handler();

    /**
     * 当启动服务的时候就会被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        parents = new ArrayList<>();
    }

    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            //当窗口的状态发生改变时
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                String foregroundPackageName = event.getPackageName().toString();
                if (foregroundPackageName.equals("com.chongdingdahui.app") && questionFloatView == null) {
                    questionFloatView = new QuestionFloatView(MyApplication.context);
                } else if (!foregroundPackageName.equals("com.chongdingdahui.app") && questionFloatView != null) {
                    questionFloatView.remove();
                }
                String foregroundClassName = event.getClassName().toString();
                Log.e("demo","className:"+foregroundClassName);
                Log.e("demo","foregroundPackageName:"+foregroundPackageName);
                // 检测到题目窗口是否出现
                if (foregroundClassName.equals("com.chongdingdahui.app.ui.LiveActivity")) {
                    // 这个地方要加线程检测
                    if (timer == null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
                    }
                }
                break;

            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                break;
        }
    }

    private void findQuestion(final String question, final List<String> answers) {
        try {
            for (int i = 0; i < answers.size(); i++) {
                Document doc = Jsoup.connect("http://www.baidu.com/s").data("wd",question + answers.get(i)).get();
                Elements eles = doc.select("div.nums").first().getAllElements().first().children();
                if (eles.size() > 0) {
                    String numString = eles.get(0).text();
                    // 百度为您找到相关结果约11,700,000个
                    int startIndex = numString.indexOf("百度为您找到相关结果约");
                    int endIndex = numString.indexOf("个");
                    while (true) {

                    }

                }
            }

        } catch (IOException e) {

        }
    }

    /**
     * 通过ID获取控件，并进行模拟点击
     * @param clickId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void onClickView(String clickId) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(clickId);
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getTextById(String id){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            for (AccessibilityNodeInfo item : list) {
                if (!TextUtils.isEmpty(item.getText())){
                    return item.getText().toString();
                }
            }
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getQuestion(){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.chongdingdahui.app:id/layoutQuiz");
            for (AccessibilityNodeInfo item : list) {
                List<AccessibilityNodeInfo> messageTVs = item.findAccessibilityNodeInfosByViewId("com.chongdingdahui.app:id/tvMessage");
                if (messageTVs.size() > 0 && !TextUtils.isEmpty(messageTVs.get(0).getText())) {
                    return messageTVs.get(0).getText().toString();
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private List<String> getAnswers(){
        List<String> answers = new ArrayList<>();
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.chongdingdahui.app:id/layoutQuiz");
            int count = 0;
            for (AccessibilityNodeInfo item : list) {
                List<AccessibilityNodeInfo> messageTVs = item.findAccessibilityNodeInfosByViewId("com.chongdingdahui.app:id/answer"+count);
                if (messageTVs.size() > 0 && !TextUtils.isEmpty(messageTVs.get(0).getText())) {
                    answers.add(messageTVs.get(0).getText().toString());
                    count++;
                }
                if (count >= 3) {
                    break;
                }
            }
        }
        return answers;
    }


    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {

    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            String question = getQuestion();
            if (!TextUtils.isEmpty(question) && !question.equals(questionContent)) {
                questionContent = question;
                Log.e("demo","出现问题弹窗");
                // 检测到题目窗口出来后，查找答案
                List<String> answers = getAnswers();
                Log.d("demo", "onAccessibilityEvent question: "+question+",answer: "+answers);
                findQuestion(question, answers);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }
    }
}
