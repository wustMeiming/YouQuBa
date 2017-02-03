package com.dream4it.youquba.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meiming on 17-2-1.
 */

public class PictureItemData implements Parcelable{
    private String id;
    private String title;
    private String image;
    private String subtype;

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.subtype);
    }

    public PictureItemData(){}

    protected PictureItemData(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.subtype = in.readString();
    }

    public static final Parcelable.Creator<PictureItemData> CREATOR = new Parcelable.Creator<PictureItemData>() {
        @Override
        public PictureItemData createFromParcel(Parcel source) {
            return new PictureItemData(source);
        }

        @Override
        public PictureItemData[] newArray(int size) {
            return new PictureItemData[size];
        }
    };
}
