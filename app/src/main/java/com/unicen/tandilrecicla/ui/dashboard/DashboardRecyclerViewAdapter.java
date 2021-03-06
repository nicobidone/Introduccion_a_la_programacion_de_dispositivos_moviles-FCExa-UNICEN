package com.unicen.tandilrecicla.ui.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.unicen.tandilrecicla.R;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "DashboardRecyclerViewAd";

    private String[] mNames;
    private Integer[] mImages;
    private Integer[] mGrayImages;
    private int[] mColors;
    private ViewModel viewModel;

    DashboardRecyclerViewAdapter(String[] mNames, Integer[] mImages, Integer[] mGrayImages, int[] mColors, ViewModel dashboardViewModel) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mColors = mColors;
        this.mGrayImages = mGrayImages;
        this.viewModel = dashboardViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called.");

        holder.textQuantity.setText(((DashboardViewModel) viewModel).getRecyclingQuantity(position));
        holder.imageView.setBackgroundColor(mColors[position]);
        holder.imageButtonPlus.setImageResource(mImages[position]);
        holder.imageButtonMinus.setImageResource(mGrayImages[position]);
        holder.textName.setText(mNames[position]);
        holder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imageButtonPlus");
                ((DashboardViewModel) viewModel).addQuantity(position);
                holder.textQuantity.setText(((DashboardViewModel) viewModel).getRecyclingQuantity(position));
            }
        });
        holder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: imageButtonMinus");
                ((DashboardViewModel) viewModel).removeQuantity(position);
                holder.textQuantity.setText(((DashboardViewModel) viewModel).getRecyclingQuantity(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButtonMinus;
        ImageButton imageButtonPlus;
        TextView textName;
        ConstraintLayout constraintLayout;
        TextView textQuantity;
        View imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButtonMinus = itemView.findViewById(R.id.layout_list_item_button_left_icon);
            imageButtonPlus = itemView.findViewById(R.id.layout_list_item_button_right_icon);
            constraintLayout = itemView.findViewById(R.id.layout_list_item_parent);
            textName = itemView.findViewById(R.id.layout_list_item_text_name);
            textQuantity = itemView.findViewById(R.id.layout_list_item_text_quantity);
            imageView = itemView.findViewById(R.id.layout_list_item_background);
        }
    }
}
