package com.firebase.test.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.test.utils.AndroidLogger;
import com.firebase.test.utils.CommonUtils;
import com.hbb20.CountryCodePicker;
import com.sci.chamunda.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";

    @BindView(R.id.etMob)
    EditText etMob;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @BindView(R.id.txtCountryCode)
    TextView txtCountryCode;

    @BindView(R.id.parent)
    LinearLayout parent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ccp.registerCarrierNumberEditText(etMob);

        txtCountryCode.setText(ccp.getSelectedCountryCodeWithPlus());
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                etMob.setText("");
                txtCountryCode.setText(ccp.getSelectedCountryCodeWithPlus());
            }
        });


    }

    public String getFormattedNumber(String strPhone, String countryCode) {
        AndroidLogger.error(TAG, strPhone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(strPhone, countryCode);
        } else {

            return PhoneNumberUtils.formatNumber(strPhone);
        }
    }

    @OnClick(R.id.btnNext)
    public void onNextClick(View view) {
        CommonUtils.hideKeyboard(LoginActivity.this);
        if (!CommonUtils.isNetworkConnected(LoginActivity.this)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), R.string.alert_no_network, Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
        } else if (ccp.isValidFullNumber()) {
//            CommonUtils.toastShort(LoginActivity.this, ccp.getFullNumberWithPlus());
            CommonUtils.hideKeyboard(LoginActivity.this);
            startActivity(new Intent(LoginActivity.this, OTPActivity.class).putExtra("mobile", ccp.getFullNumberWithPlus()));

        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), R.string.enter_valid_mob_num, Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
        }
//
    }


}
