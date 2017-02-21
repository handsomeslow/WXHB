package com.jx.wxhb.model;

import com.avos.avoscloud.AVFile;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Desc
 * Created by Jun on 2017/2/20.
 */

public class UserInfo extends RealmObject {
    // 昵称
    private String name;

    private String id;

    private String email;

    private boolean isEmailVerifiy;

    private String phone;

    private String password;

    private boolean isPhoneVerifiy;

    // 头像的objectID
    private String avatar;

    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerifiy() {
        return isEmailVerifiy;
    }

    public void setEmailVerifiy(boolean emailVerifiy) {
        isEmailVerifiy = emailVerifiy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPhoneVerifiy() {
        return isPhoneVerifiy;
    }

    public void setPhoneVerifiy(boolean phoneVerifiy) {
        isPhoneVerifiy = phoneVerifiy;
    }
}
