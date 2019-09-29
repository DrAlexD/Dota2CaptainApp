package com.example.dotabuffapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<Item> items;

    private ArrayList<Item> originalItems;

    ItemsAdapter(Context context, ArrayList<Item> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    @NonNull
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_holder, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView itemImageView;
        final TextView itemNameView;
        final TextView itemWinRateDiffView;
        final TextView itemNewWinRateView;

        ViewHolder(View view) {
            super(view);
            itemImageView = view.findViewById(R.id.itemImage);
            itemNameView = view.findViewById(R.id.itemName);
            itemWinRateDiffView = view.findViewById(R.id.itemWinRateDiff);
            itemNewWinRateView = view.findViewById(R.id.itemNewWinRate);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.itemImageView.setImageResource(item.getImage());
        holder.itemNameView.setText(item.getName());
        holder.itemWinRateDiffView.setText(textForWinRateDiffView(item));
        holder.itemNewWinRateView.setText(item.getNewWinRate() + "%");
    }

    private String textForWinRateDiffView(Item item) {
        String frontSign = "";
        if (item.getWinRateDiff() > 0.0)
            frontSign = "+";
        return frontSign + item.getWinRateDiff() + "%";
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<Item> filterResults = new ArrayList<>();

                if (originalItems == null) {
                    originalItems = items;
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Item item : originalItems) {
                        if ((item.getName().toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(item);
                    }
                } else {
                    filterResults = originalItems;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}