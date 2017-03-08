package com.jx.wxhb.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 徐俊 on 2017/3/5.
 */

public class LuckyGroupInfo implements Parcelable {

    private String id;
    private AVUser ower;
    private String title;
    private String content;
    private List<String> photoList;
    private Date publishDate;
    private List<CommentInfo> commentList;


    public AVUser getOwer() {
        return ower;
    }

    public void setOwer(AVUser ower) {
        this.ower = ower;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public List<CommentInfo> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentInfo> commentList) {
        this.commentList = commentList;
    }


    public LuckyGroupInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.ower, flags);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeStringList(this.photoList);
        dest.writeLong(this.publishDate != null ? this.publishDate.getTime() : -1);
        dest.writeList(this.commentList);
    }

    protected LuckyGroupInfo(Parcel in) {
        this.id = in.readString();
        this.ower = in.readParcelable(AVUser.class.getClassLoader());
        this.title = in.readString();
        this.content = in.readString();
        this.photoList = in.createStringArrayList();
        long tmpPublishDate = in.readLong();
        this.publishDate = tmpPublishDate == -1 ? null : new Date(tmpPublishDate);
        this.commentList = new ArrayList<CommentInfo>();
        in.readList(this.commentList, CommentInfo.class.getClassLoader());
    }

    public static final Creator<LuckyGroupInfo> CREATOR = new Creator<LuckyGroupInfo>() {
        @Override
        public LuckyGroupInfo createFromParcel(Parcel source) {
            return new LuckyGroupInfo(source);
        }

        @Override
        public LuckyGroupInfo[] newArray(int size) {
            return new LuckyGroupInfo[size];
        }
    };
}
