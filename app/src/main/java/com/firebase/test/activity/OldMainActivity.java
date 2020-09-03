package com.firebase.test.activity;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.test.adapter.OldProductAdapter;
import com.firebase.test.adapter.ProductAdapter;
import com.firebase.test.utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sci.chamunda.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

public class OldMainActivity extends BaseActivity implements
        DiscreteScrollView.ScrollListener<OldProductAdapter.ViewHolder>,
        DiscreteScrollView.OnItemChangedListener<OldProductAdapter.ViewHolder>,
        View.OnClickListener {

    DiscreteScrollView itemPicker;
    TextView txt;
    ProgressBar progressBar;
    ArrayList<String> list;
    private ArgbEvaluator evaluator;
    private int currentOverlayColor;
    private int overlayColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        txt = findViewById(R.id.txt);
        txt.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        evaluator = new ArgbEvaluator();
        currentOverlayColor = ContextCompat.getColor(this, R.color.galleryCurrentItemOverlay);
        overlayColor = ContextCompat.getColor(this, R.color.galleryItemOverlay);

        list = new ArrayList<>();

//        itemPicker = (DiscreteScrollView) findViewById(R.id.item_picker);
        itemPicker.addScrollListener(this);
        itemPicker.addOnItemChangedListener(this);
        if (sessionManager != null && sessionManager.getList() != null && sessionManager.getList().size() > 0) {
            list = sessionManager.getList();

            itemPicker.setAdapter(new ProductAdapter(OldMainActivity.this, list));
            itemPicker.scrollToPosition(0);
            progressBar.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
            itemPicker.setVisibility(View.VISIBLE);
        }
        if (list.size() == 0 && !CommonUtils.isNetworkConnected(OldMainActivity.this)) {
            progressBar.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
            txt.setText(getString(R.string.alert_no_network));
            itemPicker.setVisibility(View.GONE);
            return;
        }
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                itemPicker.setVisibility(View.VISIBLE);
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    list = (ArrayList<String>) childDataSnapshot.getValue();
                    sessionManager.storeList(list);
                    itemPicker.setAdapter(new ProductAdapter(OldMainActivity.this, list));
                    itemPicker.scrollToPosition(0);
                    if (list.size() < 0) {
                        txt.setVisibility(View.VISIBLE);
                        txt.setText("No data found.");
                        itemPicker.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                itemPicker.setVisibility(View.GONE);
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

    @Override
    public void onScroll(
            float currentPosition,
            int currentIndex, int newIndex,
            @Nullable OldProductAdapter.ViewHolder currentHolder,
            @Nullable OldProductAdapter.ViewHolder newCurrent) {
        if (currentHolder != null && newCurrent != null) {
            float position = Math.abs(currentPosition);
            currentHolder.setOverlayColor(interpolate(position, currentOverlayColor, overlayColor));
            newCurrent.setOverlayColor(interpolate(position, overlayColor, currentOverlayColor));
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable OldProductAdapter.ViewHolder viewHolder, int adapterPosition) {
        //viewHolder will never be null, because we never remove items from adapter's list
        if (viewHolder != null) {
            viewHolder.setOverlayColor(currentOverlayColor);
        }
    }

    private void share(View view) {
//        Snackbar.make(view, R.string.msg_unsupported_op, Snackbar.LENGTH_SHORT).show();
    }

    private int interpolate(float fraction, int c1, int c2) {
        return (int) evaluator.evaluate(fraction, c1, c2);
    }


}