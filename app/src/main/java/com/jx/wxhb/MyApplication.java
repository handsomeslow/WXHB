package com.jx.wxhb;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

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
        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("wxhb.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        AVOSCloud.initialize(this,"ROkb5tjAdkUnoc9XOsex9RcX-gzGzoHsz","8No0zMY9131Ev18gKESUPacn");
        AVOSCloud.setDebugLogEnabled(false);
    }
}
