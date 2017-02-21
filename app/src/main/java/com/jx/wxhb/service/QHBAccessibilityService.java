package com.jx.wxhb.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jx.wxhb.MainActivity;
import com.jx.wxhb.activity.MainTabActivity;
import com.jx.wxhb.model.HBinfo;
import com.jx.wxhb.utils.VersionParamer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.realm.Realm;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Desc 监控红包通知
 * Created by Jun on 2017/2/15.
 */

public class QHBAccessibilityService extends AccessibilityService {
    private boolean isAutoToDetail = false;
    private boolean isAutoOpen = true;
    private List<AccessibilityNodeInfo> parents;

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
            //当通知栏发生改变时
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains("[微信红包]")) {
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
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
                    //点击最后一个红包
                    Log.e("demo","点击红包");
                    // 这边有个bug：当已被两个的红包正好在最下方，会被视为未两个，会出现进入会话页无限跳转至详情页
                    clickLastLuckyPacket();

                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
                    //开红包
                    Log.e("demo","开红包");
                    onClickView(VersionParamer.OPEN_PACKET);
                    isAutoToDetail = true;
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
                    //退出红包
                    Log.e("demo","获取红包详情");
                    if (isAutoToDetail){
                        isAutoToDetail = false;
                        HBinfo hBinfo = new HBinfo();
                        hBinfo.setTitle(getTextById(VersionParamer.TITLE_TEXT));
                        hBinfo.setDesc(getTextById(VersionParamer.DESC_TEXT));
                        hBinfo.setMoney(Float.parseFloat(getTextById(VersionParamer.MONEY_TEXT)));
                        hBinfo.setDate(System.currentTimeMillis());
                        saveLuckyMoneyData(hBinfo);
                        Intent intent = new Intent(this, MainTabActivity.class);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        if (!isAutoOpen){
                            onClickView(VersionParamer.GO_BACK);
                        }
                    }
                }
                break;
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

    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {

    }
}
