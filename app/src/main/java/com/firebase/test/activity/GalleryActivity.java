package com.firebase.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.firebase.test.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgEvent)
    ImageView imgEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        }
        init();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {


        Intent intent = getIntent();
        if (intent != null) {
            String image = intent.getStringExtra("image");
            Picasso.get()
                    .load(image)
                    .into(imgEvent);
        }


    }


}
