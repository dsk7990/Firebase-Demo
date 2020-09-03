package com.firebase.test.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.test.R;
import com.firebase.test.utils.AndroidLogger;
import com.firebase.test.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OTPActivity extends BaseActivity {


    private static final String TAG = "OTPActivity";

    @BindView(R.id.etOTP)
    EditText etOTP;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.txtTimer)
    TextView txtTimer;
    private String mobile;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private CountDownTimer cTimer = null;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            AndroidLogger.error("onVerificationCompleted");
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
//                editTextCode.setText(code);
//                verifying the code
//                verifyVerificationCode(code);
                if (code.isEmpty() || code.length() < 6) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), R.string.enter_valid_code, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                    return;
                }
                char[] chars = code.toCharArray();
                etOTP.setText(chars + "");

                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            AndroidLogger.error(e + "");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), e.getMessage(), Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            AndroidLogger.error("onCodeSent " + s);
//            mResendToken = forceResendingToken;
        }
    };

    @OnClick(R.id.txtResend)
    public void OnResendClick(View view) {
        if (!CommonUtils.isNetworkConnected(OTPActivity.this)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), R.string.alert_no_network, Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
        } else {
            cancelTimer();
            startTimer();
            if (!TextUtils.isEmpty(mobile))
                sendVerificationCode(mobile);
        }

    }

    void startTimer() {
        txtTimer.setVisibility(View.VISIBLE);
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String sec;
                if (seconds < 10) {
                    sec = "0" + String.valueOf(seconds);
                } else {
                    sec = String.valueOf(seconds);
                }
                txtTimer.setText("00 : " + sec);
            }

            public void onFinish() {
                txtTimer.setVisibility(View.INVISIBLE);
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            mobile = intent.getStringExtra("mobile");
            sendVerificationCode(mobile);
        }


    }

    private void sendVerificationCode(String mobile) {
        AndroidLogger.error("sendVerificationCode");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        startTimer();
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifyVerificationCode(String code) {
        progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setMessage(getString(R.string.progress_msg));
        progressDialog.setCancelable(false);
        progressDialog.show();
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            FirebaseUser user = task.getResult().getUser();
                            AndroidLogger.error(user.getDisplayName() + "<>" + user.getUid());
                            sessionManager.createLoginSession();

                            Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }


    @OnClick(R.id.btnNext)
    public void onNextClick(View view) {
        CommonUtils.hideKeyboard(OTPActivity.this);
        String code = etOTP.getText().toString().trim();
        if (code.isEmpty() || code.length() < 6) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), R.string.enter_valid_code, Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
            return;
        }
        if (!CommonUtils.isNetworkConnected(OTPActivity.this)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), R.string.alert_no_network, Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
        } else {
            verifyVerificationCode(code);
        }

    }


}
