package com.firebase.test.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.test.adapter.ProductAdapter;
import com.firebase.test.utils.AndroidLogger;
import com.firebase.test.utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sci.chamunda.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements

        View.OnClickListener {

    RecyclerView recyclerView;
    TextView txt;
    ProgressBar progressBar;
    ArrayList<String> list, catList;

    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.v2)
    View v2;

    @BindView(R.id.recyclerViewCatalogue)
    RecyclerView recyclerViewCatalogue;

    @OnClick(R.id.rl1)
    public void onRL1Click() {
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);
        recyclerViewCatalogue.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        recyclerViewCatalogue.setAdapter(new ProductAdapter(MainActivity.this, catList));
        if (list.size() < 0) {
            txt.setVisibility(View.VISIBLE);
            txt.setText("No data found.");
            recyclerViewCatalogue.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.rl2)
    public void onRL2Click() {
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.VISIBLE);
        recyclerViewCatalogue.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new ProductAdapter(MainActivity.this, list));
        if (list.size() < 0) {
            txt.setVisibility(View.VISIBLE);
            txt.setText("No data found.");
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar = findViewById(R.id.progressBar);
        txt = findViewById(R.id.txt);
        txt.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerViewCatalogue.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewCatalogue.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();
        catList = new ArrayList<>();


        if (sessionManager != null && sessionManager.getCatList() != null && sessionManager.getCatList().size() > 0) {
            catList = sessionManager.getCatList();
            recyclerViewCatalogue.setAdapter(new ProductAdapter(MainActivity.this, catList));
            progressBar.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
            recyclerViewCatalogue.setVisibility(View.VISIBLE);
        }

        if (sessionManager != null && sessionManager.getList() != null && sessionManager.getList().size() > 0) {
            list = sessionManager.getList();
            recyclerView.setAdapter(new ProductAdapter(MainActivity.this, list));
            progressBar.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
        if (catList.size() == 0 && list.size() == 0 && !CommonUtils.isNetworkConnected(MainActivity.this)) {
            progressBar.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
            txt.setText(getString(R.string.alert_no_network));
            recyclerView.setVisibility(View.GONE);
            recyclerViewCatalogue.setVisibility(View.GONE);
            return;
        }
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference();
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AndroidLogger.error(dataSnapshot.getValue().toString());
                progressBar.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                list = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    AndroidLogger.error("value " + childDataSnapshot.getValue().toString());
                    list.add(childDataSnapshot.getValue().toString());
                }
                sessionManager.storeList(list);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                txt.setVisibility(View.VISIBLE);
                txt.setText(getString(R.string.alert_no_network));
            }
        });

        databaseReference.child("catalogue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AndroidLogger.error(dataSnapshot.getValue().toString());
                progressBar.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                recyclerViewCatalogue.setVisibility(View.VISIBLE);
                catList = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    AndroidLogger.error("value " + childDataSnapshot.getValue().toString());
                    catList.add(childDataSnapshot.getValue().toString());
                }
                sessionManager.storeCatList(catList);
                recyclerViewCatalogue.setAdapter(new ProductAdapter(MainActivity.this, catList));
                recyclerView.setVisibility(View.GONE);
                if (list.size() < 0) {
                    txt.setVisibility(View.VISIBLE);
                    txt.setText("No data found.");
                    recyclerViewCatalogue.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                recyclerViewCatalogue.setVisibility(View.GONE);
                txt.setVisibility(View.VISIBLE);
                txt.setText(getString(R.string.alert_no_network));
            }
        });


//        findViewById(R.id.home).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                finish();
                break;

        }
    }


}