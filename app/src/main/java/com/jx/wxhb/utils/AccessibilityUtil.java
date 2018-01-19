package com.jx.wxhb.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.jx.wxhb.MyApplication;

import java.util.List;

/**
 * Author Jun
 * Email:xujun02@58ganji.com
 * Desc:
 * Created by Jun on 2018/1/19.
 */

public class AccessibilityUtil {

    private static final String TAG = "AccessibilityUtil";

    /**
     * 判断自己的应用的AccessibilityService是否在运行
     *
     * @return
     */
    public static boolean serviceIsRunning(String serviceName) {
        ActivityManager am = (ActivityManager) MyApplication.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().equals(MyApplication.context.getPackageName() + serviceName)) {
                return true;
            }
        }
        return false;
    }

    // 此方法用来判断当前应用的辅助功能服务是否开启
    public static boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(MyApplication.context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(MyApplication.context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(MyApplication.context.getPackageName().toLowerCase());
            }
        }

        return false;
    }
}
