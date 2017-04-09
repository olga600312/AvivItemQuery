package com.aviv_pos.olgats.avivitemquery.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aviv_pos.olgats.avivitemquery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgats on 29/06/2016.
 */
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ItemExtraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*RecyclerView.Adapter<ItemExtraAdapter.ViewHolder>*/ {
    private List<AbstractExtraModel> modelList;

    public ItemExtraAdapter() {
        this.modelList = new ArrayList<>();
    }

    public void setModel(List<AbstractExtraModel> modelList) {
        this.modelList = modelList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case AbstractExtraModel.BOOL_TYPE:
                View vBool = inflater.inflate(R.layout.item_extra_bool, parent, false);
                viewHolder = new BooleanViewHolder(vBool);
                break;
            default:
                View vDefault = inflater.inflate(R.layout.item_extra, parent, false);
                viewHolder = new DefaultViewHolder(vDefault);

        }
        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case AbstractExtraModel.BOOL_TYPE:
                BooleanViewHolder vh1 = (BooleanViewHolder) viewHolder;
                configureBooleanViewHolder(vh1, position);
                break;
            default:
                DefaultViewHolder dvh = (DefaultViewHolder) viewHolder;
                configureDefaultViewHolder(dvh, position);
        }

    }

    private void configureDefaultViewHolder(DefaultViewHolder holder, int position) {
        // Get the data model based on position
        AbstractExtraModel model = modelList.get(position);
        int type = model.getType();
        // Set item views based on the data model
        TextView textViewName = holder.tvName;
        textViewName.setText(model.getName());

        TextView tvValue = holder.tvValue;
        tvValue.setText(model.valueToString());
    }

    private void configureBooleanViewHolder(BooleanViewHolder holder, int position) {
        // Get the data model based on position
        BooleanExtraModel model = (BooleanExtraModel) modelList.get(position);
        int type = model.getType();
        // Set item views based on the data model
        TextView textViewName = holder.tvName;
        textViewName.setText(model.getName());

        CheckBox checkBox = holder.cbValue;
        checkBox.setChecked(model.getValue());
    }

    @Override
    public int getItemViewType(int position) {
        AbstractExtraModel model = modelList.get(position);
        return model.getType();
    }

    @Override
    public int getItemCount() {
        return modelList != null ? modelList.size() : 0;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public static class DefaultViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvName;
        public TextView tvValue;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public DefaultViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.extraName);
            tvValue = (TextView) itemView.findViewById(R.id.extraValue);
        }
    }

    public static class BooleanViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvName;
        public CheckBox cbValue;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public BooleanViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.extraBoolName);
            cbValue = (CheckBox) itemView.findViewById(R.id.extraBoolValue);
        }
    }
}

