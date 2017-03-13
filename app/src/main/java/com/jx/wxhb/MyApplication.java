package com.jx.wxhb;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.avos.avoscloud.AVOSCloud;
import com.crashlytics.android.Crashlytics;
import com.jx.wxhb.utils.UIUtils;

import cn.jpush.android.api.JPushInterface;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Desc
 * Created by Jun on 2017/2/15.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("wxhb.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        AVOSCloud.initialize(this,"ROkb5tjAdkUnoc9XOsex9RcX-gzGzoHsz","8No0zMY9131Ev18gKESUPacn");
        AVOSCloud.setDebugLogEnabled(false);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        WindowManager mwm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        UIUtils.initDisplayMetrics(mwm);
    }
}
