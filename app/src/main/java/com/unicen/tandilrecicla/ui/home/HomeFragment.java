package com.unicen.tandilrecicla.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.unicen.tandilrecicla.R;

import java.io.IOException;

import okhttp3.ResponseBody;

public class HomeFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this, new HomeViewModelFactory()).get(HomeViewModel.class);
        homeViewModel.setTypefaces(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf"),
                Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        if (getActivity().getWindow() != null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

//        total();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PieChart chart = getActivity().findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        homeViewModel.setPieChart(chart);
        homeViewModel.setChartConfiguration(chart);

        ImageButton centerIcon = getActivity().findViewById(R.id.center_icon);
        centerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.changeDisplayUserValues();
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    private void total() {
        homeViewModel.getTotalRecycling("marroqui2").observe(this, new androidx.lifecycle.Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                Log.d(TAG, "onChanged: this is a live data response!");
                try {
                    Log.d(TAG, "onChanged: " + responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
