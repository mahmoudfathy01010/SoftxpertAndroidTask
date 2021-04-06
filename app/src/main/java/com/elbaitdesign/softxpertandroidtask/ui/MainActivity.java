package com.elbaitdesign.softxpertandroidtask.ui;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.elbaitdesign.softxpertandroidtask.R;
import com.elbaitdesign.softxpertandroidtask.model.Car;
import com.elbaitdesign.softxpertandroidtask.model.CarApiResponse;
import com.elbaitdesign.softxpertandroidtask.viewmodel.CarViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //ui
    SwipeRefreshLayout swipeLayout;
    View shimmerLayout;
    RecyclerView recyclerView;


    //paging
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    //data
    ArrayList<Car> cars = new ArrayList<>();
    CarAdapter adapter;
    CarViewModel viewModel;

    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUi();
        viewModel = new ViewModelProvider(this).get(CarViewModel.class);
        getData();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerLayout.setVisibility(View.VISIBLE);
                cars.clear();
                adapter.notifyDataSetChanged();
                viewModel.resetPageCounter();
                getData();
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        viewModel.carApiResponse.observe(this, new Observer<CarApiResponse>() {
            @Override
            public void onChanged(CarApiResponse carApiResponse) {
                if(carApiResponse.getData()!=null) {
                    shimmerLayout.setVisibility(View.GONE);
                    cars.addAll(carApiResponse.getData());
                    adapter.notifyDataSetChanged();
                }

            }
        });

        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(recyclerView, getString(R.string.error_message), Snackbar.LENGTH_LONG).show();
            }
        });
        initializeRecycleView();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            getData();
                            loading = true;
                        }
                    }
                }
            }
        });

    }

    private void initializeRecycleView() {
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CarAdapter(MainActivity.this,cars);
        recyclerView.setAdapter(adapter);
    }

    private void initializeUi() {
        recyclerView = findViewById(R.id.car_recycle_view);
        swipeLayout = findViewById(R.id.swipe_refresh_layout);
        shimmerLayout = findViewById(R.id.shimmer);
        shimmerLayout.setVisibility(View.VISIBLE);
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
    }

    private void getData() {
        if(isInternetConnected()) {
           viewModel.getData();
        }
        else {
            showCustomDialog(this);
        }
    }

    private void showCustomDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.internet_connection_error)
                .setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        shimmerLayout.setVisibility(View.VISIBLE);
                        cars.clear();
                        adapter.notifyDataSetChanged();
                        getData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .show();
    }

    public boolean isInternetConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifiConn!=null && wifiConn.isConnected()|| (mobilConn!=null)&&mobilConn.isConnected()){
            return true;
        }
        else {
            return false;
        }
    }
}