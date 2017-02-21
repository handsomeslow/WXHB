package com.jx.wxhb.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.jx.wxhb.model.UserInfo;

/**
 * Desc
 * Created by Jun on 2017/2/20.
 */

public class UserUtil {
    public static AVUser user;

    public static AVUser getLoginedUser(){
        return AVUser.getCurrentUser();
    }

    /**
     * 修改姓名
     * @param name
     */
    public static void updateUserName(final String name){
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVUser.getCurrentUser().setUsername(name);
                AVUser.getCurrentUser().saveInBackground();
            }
        });
    }

    /**
     * 更换头像
     * @param avatar
     */
    public static void updateUserAvatarId(final String avatar){
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVUser.getCurrentUser().put("avatar",avatar);
                AVUser.getCurrentUser().saveInBackground();
            }
        });
    }

    public static String getUserAvatarId(){
        return AVUser.getCurrentUser().getString("avatar");
    }

    /**
     * 登出
     */
    public static void loginOut(){
        AVUser.logOut();
    }
}
