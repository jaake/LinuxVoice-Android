/**
 * Created by Acer on 08.04.2015.
 */


package nl.matshofman.saxrssreader;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class RssEnclosure implements Parcelable {

    private String url;
    private String type;
    private long length;

    public RssEnclosure() {
    }

    public RssEnclosure(Parcel source) {
        Bundle data = source.readBundle();
        url = data.getString("url");
        type = data.getString("type");
        length = data.getLong("length");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle data = new Bundle();
        data.putString("url", url);
        data.putString("type", type);
        data.putLong("length", length);
        dest.writeBundle(data);
    }

    public static final Parcelable.Creator<RssFeed> CREATOR = new Parcelable.Creator<RssFeed>() {
        public RssFeed createFromParcel(Parcel data) {
            return new RssFeed(data);
        }
        public RssFeed[] newArray(int size) {
            return new RssFeed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

}
