package com.aviv_pos.olgats.avivitemquery.adapters;

/**
 * Created by olgats on 29/06/2016.
 */
public class ItemPromoModel {
    private String barcode;
    private int id;
    private String name;
    private long fromDate;
    private long toDate;

    public ItemPromoModel() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }
}
