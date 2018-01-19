package com.jx.wxhb.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jx.wxhb.MyApplication;
import com.jx.wxhb.activity.MainTabActivity;
import com.jx.wxhb.model.HBinfo;
import com.jx.wxhb.utils.CheckFloatWindowUtil;
import com.jx.wxhb.utils.WeChatVersionParameter;
import com.jx.wxhb.widget.QuestionFloatView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Desc 监控红包通知
 * Created by Jun on 2017/2/15.
 */

public class QHBAccessibilityService extends AccessibilityService {
    private static final String TAG = "QHBAccessibilityService";

    private boolean isAutoToDetail = false;
    private boolean isAutoOpen = true;
    private List<AccessibilityNodeInfo> parents;
    private Timer timer;
    private Handler handler = new Handler();
    private String questionContent;
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
        if (eventType != 2048) {
            Log.d(TAG, "eventType: " + eventType);
        } else {
            return;
        }
        switch (eventType) {
            //当通知栏发生改变时
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains("微信红包")) {
                            //模拟打开通知栏消息，即打开微信
                            if (event.getParcelableData() != null &&
                                    event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    pendingIntent.send();
                                    Log.e("demo","进入微信");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            //当窗口的状态发生改变时
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String foregroundPackageName = event.getPackageName().toString();
                String foregroundClassName = event.getClassName().toString();
                Log.e("demo","foregroundClassName:"+foregroundClassName);
                Log.e("demo","foregroundPackageName:"+foregroundPackageName);

                if (foregroundPackageName.equals("com.chongdingdahui.app")) {
                    handlerChongding(foregroundClassName);
                } else if(foregroundPackageName.equals("com.tencent.mm")) {
                    handlerWeChat(foregroundClassName);
                } else if (foregroundPackageName.equals("com.jx.wxhb") && foregroundClassName.equals("com.jx.wxhb.activity.MainTabActivity")) {
                    if (questionFloatView != null && questionFloatView.isShown()) {
                        questionFloatView.remove();
                    }
                    if (timer != null) {
                        timer.cancel();
                    }
                } else if (foregroundPackageName.equals("com.jun.testapp")) {
                    handlerTest(foregroundClassName);
                }
                break;
        }
    }

    private void handlerTest(String className) {
        if (className.equals("com.jun.testapp.MainActivity")) {
            // 这个地方要加线程检测
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
            } else {
                timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
            }
        }
    }

    private QuestionFloatView questionFloatView;

    private void handlerChongding(String className) {
        if (Build.VERSION.SDK_INT > 24 && !CheckFloatWindowUtil.checkPermission(this)) {
            CheckFloatWindowUtil.applyPermission(this);
        } else {
            if (questionFloatView == null) {
                questionFloatView = new QuestionFloatView(MyApplication.context);
            } else {
                questionFloatView.show();
            }
        }

        if (className.equals("com.chongdingdahui.app.ui.MainActivity")) {
            if (questionFloatView != null) {
                questionFloatView.showAnswer("欢迎使用");
            }
            if (timer != null) {
                timer.cancel();
            }

        }
        // 检测到题目窗口是否出现
        else if (className.equals("com.chongdingdahui.app.ui.LiveActivity")) {
            // 这个地方要加线程检测
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
            }
        }
    }

    private void handlerWeChat(String className) {
        if(className.equals("com.tencent.mm.ui.LauncherUI")) {
            //点击最后一个红包
            Log.e("demo","点击红包");
            // 这边有个bug：当已被两个的红包正好在最下方，会被视为未两个，会出现进入会话页无限跳转至详情页
            clickLastLuckyPacket();

        } else if(className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
            //开红包
            Log.e("demo","开红包");
            onClickView(WeChatVersionParameter.V661.OPEN_PACKET);
            isAutoToDetail = true;
        } else if(className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
            //退出红包
            Log.e("demo","获取红包详情");
            if (isAutoToDetail){
                isAutoToDetail = false;
                HBinfo hBinfo = new HBinfo();
                hBinfo.setUser(getTextById(WeChatVersionParameter.V661.USER_TEXT));
                hBinfo.setDesc(getTextById(WeChatVersionParameter.V661.DESC_TEXT));
                hBinfo.setMoney(Float.parseFloat(getTextById(WeChatVersionParameter.V661.MONEY_TEXT)));
                hBinfo.setDate(System.currentTimeMillis());
                saveLuckyMoneyData(hBinfo);
                Intent intent = new Intent(this, MainTabActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                if (!isAutoOpen){
                    onClickView(WeChatVersionParameter.V661.GO_BACK);
                }
            }
        }
    }

    private void saveLuckyMoneyData(HBinfo hBinfo){
        if (hBinfo == null){
            return;
        }

        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(hBinfo);
        realm.commitTransaction();
        realm.close();
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
                if (item.getText() != null){
                    return item.getText().toString();
                }
            }
        }
        return null;
    }

    /**
     * 获取List中最后一个红包，并进行模拟点击
     */
    private void clickLastLuckyPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        findLuckyPacket(rootNode);
        if(parents.size()>0){
            parents.get(parents.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        parents.clear();
    }

    /**
     * 回归函数遍历每一个节点，并将含有"领取红包"存进List中
     *
     * @param info
     */
    public void findLuckyPacket(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("领取红包".equals(info.getText().toString())) {
                    AccessibilityNodeInfo parent = info.getParent();
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parents.clear();
                            parents.add(parent);
                            break;
                        }
                        parent = parent.getParent();
                    }
                }

                if (info.getText().toString().contains("你领取了")){
                    parents.clear();
                }
            }
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    findLuckyPacket(info.getChild(i));
                }
            }
        }
    }


    ////
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getQuestion(){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.chongdingdahui.app:id/layoutQuiz");
            if (list.size()>0) {
                AccessibilityNodeInfo messageNodeInfo = list.get(0);
                if (messageNodeInfo != null) {
                    AccessibilityNodeInfo questionNodeInfo =  messageNodeInfo.getChild(1);
                    return questionNodeInfo.getText().toString();
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getQuestionTest(){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.jun.testapp:id/layoutQuiz");
            if (list.size()>0) {
                AccessibilityNodeInfo messageNodeInfo = list.get(0);
                if (messageNodeInfo != null) {
                    AccessibilityNodeInfo questionNodeInfo =  messageNodeInfo.getChild(1);
                    return questionNodeInfo.getText().toString();
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private List<String> getAnswersTest2(){
        List<String> answers = new ArrayList<>();
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            for (int i = 0; i < 3; i++) {
                List<AccessibilityNodeInfo> messageTVs = nodeInfo.findAccessibilityNodeInfosByViewId("com.jun.testapp:id/answer"+i);
                if (messageTVs.size() > 0 && !TextUtils.isEmpty(messageTVs.get(0).getText())) {
                    answers.add(messageTVs.get(0).getText().toString());
                }
            }
        }
        return answers;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private List<String> getAnswers(){
        List<String> answers = new ArrayList<>();
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            for (int i = 0; i < 3; i++) {
                List<AccessibilityNodeInfo> messageTVs = nodeInfo.findAccessibilityNodeInfosByViewId("com.chongdingdahui.app:id/answer"+i);
                if (messageTVs.size() > 0 && !TextUtils.isEmpty(messageTVs.get(0).getText())) {
                    answers.add(messageTVs.get(0).getText().toString());
                }
            }
        }
        return answers;
    }

    private String answer;

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            String question = getQuestion();
            if (!TextUtils.isEmpty(question)) {
                if (!question.equals(questionContent)) {
                    questionContent = question;
                    Log.e("demo","出现问题弹窗");
                    // 检测到题目窗口出来后，查找答案
                    List<String> answers = getAnswers();
                    answer = findAnswer(question, answers);
                    //Log.d("demo", "onAccessibilityEvent question: "+question+",answer: "+answers.toString()+",answer: " + answer);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (questionFloatView == null) {
                            questionFloatView = new QuestionFloatView(MyApplication.context);
                        }
                        questionFloatView.showAnswer(questionContent+ "\n" +answer);
                    }
                });
            } else {
                Log.e(TAG, "未检测到题目");
            }
        }
    }

    private String findAnswer(final String question, final List<String> selectAnswers) {
        boolean isNoneQuestion = false;
        long min = Integer.MAX_VALUE;
        long max = Integer.MIN_VALUE;
        if (question.contains("不")) {
            isNoneQuestion = true;
        }
        int index = 0;
        StringBuilder answer = new StringBuilder();
        try {
            for (int i = 0; i < selectAnswers.size(); i++) {
                Document doc = Jsoup.connect("http://www.baidu.com/s").data("wd",question + selectAnswers.get(i)).get();
                Elements eles = doc.select("div.nums").first().getAllElements();
                for (int j = 0; j < eles.size(); j++) {
                    String element = eles.get(j).text();
                    if (element.contains("百度为您找到相关结果")) {
                        Log.d(TAG, "findAnswer element: "+element);
                        String regEx="[^0-9]";
                        Pattern pattern = Pattern.compile(regEx);
                        Matcher m = pattern.matcher(element);
                        String num = m.replaceAll("").trim();
                        answer.append(selectAnswers.get(i));
                        answer.append(" : ");
                        answer.append(num);
                        answer.append("\n");
                        long number = Long.valueOf(num);
                        max = Math.max(number,max);
                        min = Math.max(number,min);
                        if (isNoneQuestion) {
                            if (min > number) {
                                min = number;
                                index = i;
                            }
                        } else {
                            if (max < number) {
                                max = number;
                                index = i;
                            }
                        }
                        break;
                    }
                }

            }

        } catch (IOException e) {
            Log.e(TAG, "findAnswer", e);
        }

        String indexString = "第"+(index+1)+"个\n";
        answer.append(indexString);
        return answer.toString();
    }

    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {

    }

}
