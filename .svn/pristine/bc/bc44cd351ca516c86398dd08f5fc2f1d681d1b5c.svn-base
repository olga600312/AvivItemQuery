package com.aviv_pos.olgats.avivitemquery.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by olgats on 08/12/2015.
 */
public class ItemSet  implements Parcelable{
    public static final int SUCCESS = 0;
    public static final int DB_ERROR = 3;
    public static final int CLIENT_CONNECTION_ERROR = 5;
    public static final int PRIVATE_WS_NOT_REACHABLE = 8;
    private Item[] items = new Item[]{};
    private int status;

    public ItemSet() {
    }

    protected ItemSet(Parcel in) {
        items = in.createTypedArray(Item.CREATOR);
        status = in.readInt();
    }

    public static final Creator<ItemSet> CREATOR = new Creator<ItemSet>() {
        @Override
        public ItemSet createFromParcel(Parcel in) {
            return new ItemSet(in);
        }

        @Override
        public ItemSet[] newArray(int size) {
            return new ItemSet[size];
        }
    };

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] v) {
        items = v;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ItemSet{" +
                "items=" + itemsToString() +
                ", status=" + status +
                '}';
    }

    private String itemsToString(){
        String str="[";
        for(Item item:items){
            str+=item.toString();
        }
        str+="]";
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(items, flags);
        dest.writeInt(status);
    }
}
