package com.jx.wxhb;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;
import com.crashlytics.android.Crashlytics;
import com.jx.wxhb.service.FetchPatchHandler;
import com.jx.wxhb.utils.UIUtils;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;

import cn.jpush.android.api.JPushInterface;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Desc
 * Created by Jun on 2017/2/15.
 */

public class MyApplication extends Application {

    ApplicationLike tinkerAppLike;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        if (BuildConfig.TINKER_ENABLE){
            tinkerAppLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
            TinkerPatch.init(tinkerAppLike)
                    //是否自动反射Library路径,无须手动加载补丁中的So文件
                    //注意,调用在反射接口之后才能生效,你也可以使用Tinker的方式加载Library
                    .reflectPatchLibrary()
                    //设置收到后台回退要求时,锁屏清除补丁
                    //默认是等主进程重启时自动清除
                    .setPatchRollbackOnScreenOff(true)
                    //设置补丁合成成功后,锁屏重启程序
                    //默认是等应用自然重启
                    .setPatchRestartOnSrceenOff(true);
            // 12小时检查一次
            new FetchPatchHandler().fetchPatchWithInterval(12);
        }

        
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
