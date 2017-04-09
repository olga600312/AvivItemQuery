package com.aviv_pos.olgats.avivitemquery;

/**
 * Created by olgats on 12/04/2016.
 */
import android.os.Parcel;
import android.os.Parcelable;


public class ItemSearchCriteria  implements Parcelable{
    private String code;
    private String name;
    private int supplier =-1;

    public ItemSearchCriteria() {

    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeInt(supplier);
    }

    protected ItemSearchCriteria(Parcel in) {
        code = in.readString();
        name = in.readString();
        supplier=in.readInt();
    }

    public static final Creator<ItemSearchCriteria> CREATOR = new Creator<ItemSearchCriteria>() {
        @Override
        public ItemSearchCriteria createFromParcel(Parcel in) {
            return new ItemSearchCriteria(in);
        }

        @Override
        public ItemSearchCriteria[] newArray(int size) {
            return new ItemSearchCriteria[size];
        }
    };

}
