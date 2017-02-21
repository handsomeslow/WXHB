package com.jx.wxhb.utils;

import io.realm.Realm;

/**
 * Desc
 * Created by Jun on 2017/2/20.
 */

public class DBUtil {

    public Realm realm;

    private DBUtil(){
        realm = Realm.getDefaultInstance();
    }

    public static DBUtil getInstance(){
        return SingletonInstance.instance;
    }

    private static class SingletonInstance{
        static DBUtil instance = new DBUtil();
    }


}
