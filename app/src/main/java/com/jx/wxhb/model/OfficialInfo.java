package com.jx.wxhb.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Desc 公众号信息
 * Created by Jun on 2017/2/24.
 */

public class OfficialInfo implements Parcelable {
    private String id;

    private String name;

    // 微信ID
    private String wxId;

    // 描述
    private String desc;

    // 封面图
    private String image;

    // 二维码
    private String qrCode;

    // 分类、标签
    private String tags;

    // 账号主体
    private String organization;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.wxId);
        dest.writeString(this.desc);
        dest.writeString(this.image);
        dest.writeString(this.qrCode);
        dest.writeString(this.tags);
        dest.writeString(this.organization);
    }

    public OfficialInfo() {
    }

    protected OfficialInfo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.wxId = in.readString();
        this.desc = in.readString();
        this.image = in.readString();
        this.qrCode = in.readString();
        this.tags = in.readString();
        this.organization = in.readString();
    }

    public static final Parcelable.Creator<OfficialInfo> CREATOR = new Parcelable.Creator<OfficialInfo>() {
        @Override
        public OfficialInfo createFromParcel(Parcel source) {
            return new OfficialInfo(source);
        }

        @Override
        public OfficialInfo[] newArray(int size) {
            return new OfficialInfo[size];
        }
    };
}
