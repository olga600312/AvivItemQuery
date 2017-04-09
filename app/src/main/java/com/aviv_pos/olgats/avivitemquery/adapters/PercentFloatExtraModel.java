package com.aviv_pos.olgats.avivitemquery.adapters;

/**
 * Created by olgats on 29/06/2016.
 */
public class PercentFloatExtraModel  extends FloatExtraModel{

    public PercentFloatExtraModel(String name, Float value) {
        super(name, value);
    }

    @Override
    public String valueToString() {
        return super.valueToString()+"%";
    }
}

