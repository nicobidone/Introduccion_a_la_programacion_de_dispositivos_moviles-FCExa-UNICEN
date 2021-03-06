package com.unicen.tandilrecicla.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unicen.tandilrecicla.MainActivitySharedViewModel;
import com.unicen.tandilrecicla.R;
import com.unicen.tandilrecicla.data.model.Recycling;
import com.unicen.tandilrecicla.data.model.Utils;

import static com.unicen.tandilrecicla.data.Constants.RECYCLING_STATUS;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private DashboardViewModel dashboardViewModel;

    private MainActivitySharedViewModel maSharedViewModel;

    private RecyclerView recyclerView;

    private String[] mNames;
    private Integer[] mIcons;
    private Integer[] mGreyIcons;
    private int[] mColors;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = ViewModelProviders.of(this, new DashboardViewModelFactory()).get(DashboardViewModel.class);

        if (getActivity() != null) {
            maSharedViewModel = ViewModelProviders.of(getActivity()).get(MainActivitySharedViewModel.class);
        }

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.fragment_clean_points_recycler_view);
        ImageButton imageButtonDiscard = root.findViewById(R.id.fragment_dashboard_button_discard);
        ImageButton imageButtonRecycle = root.findViewById(R.id.fragment_dashboard_button_recycle);

        initImageMaps();
        initRecyclerView();

        imageButtonRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dashboardViewModel.isEmptyQuantity()) {
                    recycle();
                    Toast.makeText(getContext(), "Your recycling has been recorded!", Toast.LENGTH_LONG).show();
                    dashboardViewModel.clearRecyclingQuantities();
                    initRecyclerView();
                } else {
                    Toast.makeText(getContext(), "Your recycling is empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
        imageButtonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardViewModel.clearRecyclingQuantities();
                initRecyclerView();
            }
        });

        return root;
    }

    private void initImageMaps() {
        Log.d(TAG, "initImageMaps: Preparing items");
        mNames = Utils.getRecyclingNames();
        mIcons = Utils.getRecyclingIcons();
        mGreyIcons = Utils.getGreyRecyclingIcons();
        mColors = Utils.getVordiplomColors();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: Init recyclerView");
        DashboardRecyclerViewAdapter adapter = new DashboardRecyclerViewAdapter(mNames, mIcons, mGreyIcons, mColors, dashboardViewModel);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void recycle() {
        dashboardViewModel.postNewRecyclingData(maSharedViewModel.getSelected().getValue()).observe(this,
                new androidx.lifecycle.Observer<Recycling>() {
                    @Override
                    public void onChanged(Recycling responseBody) {
                        Log.d(TAG, "onChanged: this is a live data response!");
                        Log.d(TAG, "onChanged: " + responseBody.getDate());
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentActivity fragmentActivity = this.getActivity();
        if (fragmentActivity != null) {
            SparseIntArray recyclings = dashboardViewModel.getRecyclings();
            SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(RECYCLING_STATUS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (int i = 0; i < 5; i++) {
                editor.putInt(String.valueOf(i), recyclings.get(i));
            }
            editor.apply();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentActivity fragmentActivity = this.getActivity();
        if (fragmentActivity != null) {
            SparseIntArray recyclings = new SparseIntArray();
            SharedPreferences sharedPreferences = fragmentActivity.getSharedPreferences(RECYCLING_STATUS, Context.MODE_PRIVATE);
            for (int i = 0; i < 5; i++) {
                recyclings.put(i, sharedPreferences.getInt(String.valueOf(i), 0));
            }
            dashboardViewModel.setRecyclings(recyclings);
        }
    }
}
