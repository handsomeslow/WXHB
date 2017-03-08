package com.jx.wxhb.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Desc
 * Created by Jun on 2017/3/8.
 */

public class Item implements Parcelable {
    private int image;

    public Item(@DrawableRes final int image) {
        setImage(image);
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.image);
    }

    protected Item(Parcel in) {
        this.image = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
