package com.jx.wxhb.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Desc
 * Created by Jun on 2017/3/7.
 */

public class TopContent implements Parcelable {
    private String title;

    private String image;

    private int type;

    private String url;

    @JSONField(name = "officialId")
    private String officialId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOfficialId() {
        return officialId;
    }

    public void setOfficialId(String officialId) {
        this.officialId = officialId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeInt(this.type);
        dest.writeString(this.url);
        dest.writeString(this.officialId);
    }

    public TopContent() {
    }

    protected TopContent(Parcel in) {
        this.title = in.readString();
        this.image = in.readString();
        this.type = in.readInt();
        this.url = in.readString();
        this.officialId = in.readString();
    }

    public static final Parcelable.Creator<TopContent> CREATOR = new Parcelable.Creator<TopContent>() {
        @Override
        public TopContent createFromParcel(Parcel source) {
            return new TopContent(source);
        }

        @Override
        public TopContent[] newArray(int size) {
            return new TopContent[size];
        }
    };
}
