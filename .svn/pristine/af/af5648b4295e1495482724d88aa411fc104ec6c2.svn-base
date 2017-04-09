package com.aviv_pos.olgats.avivitemquery.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aviv_pos.olgats.avivitemquery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgats on 29/06/2016.
 */
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ItemPromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*RecyclerView.Adapter<ItemExtraAdapter.ViewHolder>*/ {
    private List<ItemPromoModel> modelList;

    public ItemPromoAdapter() {
        this.modelList = new ArrayList<>();
    }

    public void setModel(List<ItemPromoModel> modelList) {
        this.modelList = modelList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vDefault = inflater.inflate(R.layout.item_promo, parent, false);
        return new DefaultViewHolder(vDefault);
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DefaultViewHolder dvh = (DefaultViewHolder) viewHolder;
        configureDefaultViewHolder(dvh, position);
    }

    private void configureDefaultViewHolder(DefaultViewHolder holder, int position) {
        // Get the data model based on position
        ItemPromoModel model = modelList.get(position);

        // Set item views based on the data model
        TextView textViewId = holder.tvId;
        textViewId.setText(""+model.getId());

        TextView tvName = holder.tvName;
        tvName.setText(model.getName());
    }

    @Override
    public int getItemViewType(int position) {
        ItemPromoModel model = modelList.get(position);
        return model.getId();
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
        public TextView tvId;
        public TextView tvName;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public DefaultViewHolder(View itemPromoView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemPromoView);

            tvId = (TextView) itemPromoView.findViewById(R.id.promoId);
            tvName = (TextView) itemPromoView.findViewById(R.id.promoName);
        }
    }

}


