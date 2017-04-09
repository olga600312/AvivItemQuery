package com.aviv_pos.olgats.avivitemquery.adapters;

import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;

/**
 * Created by olgats on 29/06/2016.
 */
public class MapExtraModel extends AbstractExtraModel<Integer> {
    private DatabaseHandler.Master master;

    public MapExtraModel(String name, Integer value, DatabaseHandler.Master master) {
        super(AbstractExtraModel.MAP_TYPE,name, value);
        this.master = master;
    }

    @Override
    public String valueToString() {
        Integer i = getValue();
        String str = i != null && master != null ? master.getValue(i) : null;
        return str != null ? str : String.valueOf(i);
    }
}
