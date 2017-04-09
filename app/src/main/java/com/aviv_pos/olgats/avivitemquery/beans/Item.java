package com.aviv_pos.olgats.avivitemquery.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

/**
 * Created by olgats on 08/12/2015.
 */
public class Item  implements Parcelable{
    private String code;
    private String name;
    private float price;
    private float count;
    private boolean weightable;

    private transient int supplier;
    private Hashtable<String,Object> extra;
    private  transient Collection<Promo> promotions;


    public Item() {
        promotions=new ArrayList<>();
    }

    public Item(Parcel in) {
        code = in.readString();
        name = in.readString();
        price = in.readFloat();
        count = in.readFloat();
        weightable = in.readByte() != 0;
        supplier=in.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeFloat(count);
        dest.writeByte((byte) (weightable ? 1 : 0));
        dest.writeInt(supplier);

    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public boolean isWeightable() {
        return weightable;
    }

    public void setWeightable(boolean weightable) {
        this.weightable = weightable;
    }

    public Hashtable<String,Object> getExtra() {
        return extra;
    }

    public void setExtra(Hashtable<String,Object> extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        String str= "Item{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", weightable=" + weightable +
                '}';
        str+=" Extra: "+(extra!=null?extra.size()+" > ":"empty");
        if(extra!=null) {
            for (Object entry : extra.entrySet()) {
                str += ";" + entry;
            }
        }
        return str;
    }

    public Collection<Promo> getPromotions() {
        return promotions;
    }

    public void setPromotions(Collection<Promo> promotions) {
        this.promotions = promotions;
    }


}
