package com.kft.mfs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;




public class AccountVerificationActivity extends BaseActivity {
    private static final String TAG = AccountVerificationActivity.class.getSimpleName();

    @BindView(R.id.account_verification_tool_bar)
    Toolbar toolbar;

    @BindView(R.id.account_verification_sv)
    ScrollView scrollView;

    @BindView(R.id.verification_cpb)
    ProgressBar accountVerificationCpb;

    @BindView(R.id.verification_progress_count_tv)
    TextView progressCountTv;

    @BindView(R.id.verification_check_iv)
    ImageView verificationCheckIv;

    @BindView(R.id.call_customer_care_iv)
    ImageView callCCIv;

    @BindView(R.id.verification_resend_code_tv)
    TextView resendCodeTv;

    @BindView(R.id.verification_hint_tv)
    TextView verificationHintTv;

    @BindView(R.id.six_digit_code_et)
    EditText sixDigitCodeEt;

    @BindView(R.id.first_digit_tv)
    TextView firstDigitTv;

    @BindView(R.id.second_digit_tv)
    TextView secondDigitTv;

    @BindView(R.id.third_digit_tv)
    TextView thirdDigitTv;

    @BindView(R.id.fourth_digit_tv)
    TextView fourthDigitTv;

    @BindView(R.id.fifth_digit_tv)
    TextView fifthDigitTv;

    @BindView(R.id.sixth_digit_tv)
    TextView sixthDigitTv;

    @BindView(R.id.account_verification_btn)
    Button verificationBtn;

    @BindView(R.id.new_pin_rl)
    RelativeLayout newPinRl;

    @BindView(R.id.new_pin_et)
    EditText newPINEt;

    @BindView(R.id.new_hidden_password_ll)
    LinearLayout newPinLl;

    @BindView(R.id.retype_new_pin_rl)
    RelativeLayout retypeNewPinRl;

    @BindView(R.id.retype_new_pin_et)
    EditText reNewPINEt;

    @BindView(R.id.retype_hidden_password_ll)
    LinearLayout retypePinLl;

    @BindView((R.id.userid_et))
    EditText userIdEditText;



    private TextView[] sixDigitTvArray;
    private boolean isCodeReceived = false;


    int verificationType = VerificationType.REGISTRATION.getCode();
    private String phoneNumber;
    private String password;
    private ImageView[] newPwIvs, retypePwIvs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.stopScreenCapturing();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            verificationType = bundle.getInt(IntentKey.VERIFICATION_TYPE);
            if (verificationType == VerificationType.CHANGE_DEVICE.getCode()) {
                phoneNumber = intent.getStringExtra(IntentKey.PHONE_NUMBER);
                password = intent.getStringExtra(IntentKey.PASSWORD);
            } else if (verificationType == VerificationType.FORGOT_WALLET_PIN.getCode()) {
                phoneNumber = intent.getStringExtra(IntentKey.PHONE_NUMBER);
            }
        }
        setContentView(R.layout.activity_account_verification);
        ButterKnife.bind(this);
        presenter.onCreate();
        initView();
        setSMSReceiver();
        FirebaseCrash.log(this.getString(R.string.fb_acc_verify_oncreate));
    }

    @Override
    protected void setupComponent(AppComponent component) {
        DaggerAccountVerificationComponent.builder()
                .appComponent(component)
                .accountVerificationModule(new AccountVerificationModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (verificationType == VerificationType.FORGOT_WALLET_PIN.getCode()) {
            toolBarTitleTv.setText(R.string.reset_wallet_pin_title);
            verificationBtn.setText(R.string.submit_reset_pin_text);
            if (newPinRl.getVisibility() == View.GONE) {
                newPinRl.setVisibility(View.VISIBLE);
                newPINEt.setVisibility(View.VISIBLE);
            }
            newPwIvs = new ImageView[6];
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < 6; i++) {
                newPwIvs[i] = new ImageView(this);
                newPwIvs[i].setImageResource(R.drawable.img_pw_inactive);
                newPwIvs[i].setLayoutParams(params2);
                newPinLl.addView(newPwIvs[i]);
                if (i == 0) {
                    params2.setMarginStart(getResources().getDimensionPixelSize(R.dimen.password_input_gap));
                }
            }
            ViewUtility.setTextChangedListenerForPinEt(newPINEt, newPinLl, newPwIvs, R.string.new_pin_tag_text);
            if (retypeNewPinRl.getVisibility() == View.GONE) {
                retypeNewPinRl.setVisibility(View.VISIBLE);
                reNewPINEt.setVisibility(View.VISIBLE);
            }
            retypePwIvs = new ImageView[6];
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < 6; i++) {
                retypePwIvs[i] = new ImageView(this);
                retypePwIvs[i].setImageResource(R.drawable.img_pw_inactive);
                retypePwIvs[i].setLayoutParams(params3);
                retypePinLl.addView(retypePwIvs[i]);
                if (i == 0) {
                    params3.setMarginStart(getResources().getDimensionPixelSize(R.dimen.password_input_gap));
                }
            }
            ViewUtility.setTextChangedListenerForPinEt(reNewPINEt, retypePinLl, retypePwIvs, R.string.retype_new_pin_tag_text);
            if (phoneNumber == null) {
                userIdEditText.setVisibility(View.VISIBLE);
            }
        } else {
            toolBarTitleTv.setText(getString(R.string.account_verification_title));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sixDigitTvArray = new TextView[]{firstDigitTv, secondDigitTv, thirdDigitTv, fourthDigitTv, fifthDigitTv, sixthDigitTv};
        verificationBtn.setEnabled(false);
        verificationBtn.setBackgroundResource(R.drawable.nexuspay_fade_button);
        setFocusListener();
        setTextWatcher();
        SingleClick singleClick=new SingleClick();
        if (verificationType == VerificationType.FORGOT_WALLET_PIN.getCode()) {
            prepareUIForPinReset();
            callCCIv.setOnClickListener(singleClick);
            rippleBackgroundForCall.startRippleAnimation();
        } else {
            resendCodeTv.setOnClickListener(singleClick);
            animateProgressBar();
        }
        verificationBtn.setOnClickListener(singleClick);
    }

    private void prepareUIForPinReset() {
        KonaLogger.debugLog(TAG, "In removeProgressbar");
        accountVerificationCpb.setVisibility(View.GONE);
        progressCountTv.setVisibility(View.GONE);
        resetPinRL.setVisibility(View.VISIBLE);
        verificationHintTv.setText(getString(R.string.pin_reset_in_progress_text));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void animateProgressBar() {
        final ObjectAnimator animator = ObjectAnimator.ofInt(accountVerificationCpb, "progress", 0, accountVerificationCpb.getMax());
        animator.setDuration(Constant.account_verification_code_arrival_time);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int secondsRemaining = 60 - ((Integer) animation.getAnimatedValue() * 60) / accountVerificationCpb.getMax();
                progressCountTv.setText(secondsRemaining + "\nsec");

                if (isCodeReceived) {
                    animation.cancel();
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCodeReceived) {
                    progressCountTv.setVisibility(View.GONE);
                    resendCodeTv.setVisibility(View.VISIBLE);
                    resendCodeTv.setText(getString(R.string.resend_code_text));
                    verificationHintTv.setText(getString(R.string.account_verification_hint));
                } else {
                    if (VerificationType.FORGOT_WALLET_PIN.getCode() != verificationType) {
                        submitVerificationForm();
                    }
                }
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
    }

    @Override
    public void onBackPressed() {

        if (verificationType == VerificationType.FORGOT_WALLET_PIN.getCode()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else if (verificationType == VerificationType.CHANGE_DEVICE.getCode()) {
            finish();
            return;
        }
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 < 6) {
                if (msg.arg2 == 0) {
                    sixDigitTvArray[msg.arg1].setText("|");
                } else {
                    sixDigitTvArray[msg.arg1].setText("");
                }
                msg.arg2 = 2 - msg.arg2;
                sendMessageDelayed(Message.obtain(msg), 500);
            }
        }
    };

    private void setFocusListener() {
        sixDigitCodeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    handler.removeMessages(TYPE_CURSOR_BLINK);
                    handler.sendMessage(Message.obtain(handler, TYPE_CURSOR_BLINK, cursorPosition, 0));
                } else {
                    handler.removeMessages(TYPE_CURSOR_BLINK);
                }
            }
        });
    }

    private int cursorPosition = 0;
    private static final int TYPE_CURSOR_BLINK = 0;

    private void setTextWatcher() {
        sixDigitCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = s.toString();
                cursorPosition = code.length();
                handler.removeMessages(TYPE_CURSOR_BLINK);
                for (int i = 0; i < 6; i++) {
                    if (i < cursorPosition) {
                        sixDigitTvArray[i].setText(code.charAt(i) + "");
                    } else {
                        sixDigitTvArray[i].setText("");
                    }
                }
                if (code.length() == Constant.verification_code_length) {
                    hideSystemKeyboard();
                    verificationBtn.setEnabled(true);
                    verificationBtn.setBackgroundResource(R.drawable.nexuspay_active_button_dialog);
                    verificationHintTv.setText(getString(R.string.code_received_hint));
                } else {
                    if (verificationBtn.isEnabled()) {
                        verificationBtn.setEnabled(false);
                        verificationBtn.setBackgroundResource(R.drawable.nexuspay_fade_button);
                        verificationHintTv.setText(getString(R.string.account_verification_hint));
                    }
                    handler.sendMessage(Message.obtain(handler, TYPE_CURSOR_BLINK, cursorPosition, 0));
                }
            }
        });
    }

    public void hideSystemKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        View focusView = this.getCurrentFocus();
        if (focusView != null) {
            inputManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }

    private class SingleClick extends OnSingleClickListener {
        @Override
        public void onSingleClick(View view) {
            AccountVerificationActivity.this.onClick(view);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_verification_btn:

                submitVerificationForm();
                break;
            case R.id.verification_resend_code_tv:

                resendCodeTv.setVisibility(View.GONE);
                progressCountTv.setVisibility(View.VISIBLE);
                sixDigitCodeEt.getText().clear();
                verificationHintTv.setText(getString(R.string.verification_in_progress_text));
                if (verificationType == VerificationType.CHANGE_DEVICE.getCode()) {
                    presenter.resendChangeDeviceVerificationCode(phoneNumber, BuildConfig.WALLET_SERVICE_PROVIDER_ID);
                } else if (verificationType == VerificationType.FORGOT_WALLET_PIN.getCode()) {
                    presenter.resendForgetPinVerificationCode(phoneNumber, BuildConfig.WALLET_SERVICE_PROVIDER_ID);
                    clearPinField();
                } else {
                    presenter.resendVerificationCode();
                }
                break;
            case R.id.call_customer_care_iv:
                Intent dialerIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + this.getString(R.string.customer_service_phone_no)));
                startActivity(dialerIntent);
                break;
        }
    }

    @Override
    public void clearPinField() {
        newPINEt.getText().clear();
        reNewPINEt.getText().clear();
    }

    private void submitVerificationForm() {
        if (isPermissionAvailable(Constant.PERMISSION_READ_PHONE_STATE)) {
            if (verificationType == VerificationType.REGISTRATION.getCode()) {
                presenter.initialize(sixDigitCodeEt.getText().toString().trim());
            } else if (verificationType == VerificationType.CHANGE_DEVICE.getCode()) {
                presenter.reInitializeWallet(password, phoneNumber, sixDigitCodeEt.getText().toString().trim());

            } else if (verificationType == VerificationType.FORGOT_WALLET_PIN.getCode()) {
                if (userIdEditText.getVisibility() == View.VISIBLE) {
                    phoneNumber = userIdEditText.getText().toString().trim();
                }
                String newPin = newPINEt.getText().toString().trim();
                String renewPin = reNewPINEt.getText().toString().trim();
                if (!validateWalletpin(newPin, renewPin)) {
                    return;
                }
                presenter.resetWalletPassword(phoneNumber, newPin, renewPin,
                        sixDigitCodeEt.getText().toString().trim());
            }
        } else {
            requestPermission(Constant.PERMISSION_READ_PHONE_STATE);
        }
    }

    private boolean validateWalletpin(String newPin, String renewPin) {
        String errorMessage = null;
        if (TextUtils.isEmpty(newPin) || newPin.length() < Constant.NEXUS_PAY_PIN_LENGTH) {
            errorMessage = getString(R.string.err_msg_wallet_pin);
        } else if (TextUtils.isEmpty(renewPin) || renewPin.length() < Constant.NEXUS_PAY_PIN_LENGTH) {
            errorMessage = getString(R.string.err_msg_retype_wallet_pin);
        } else if (!newPin.equals(renewPin)) {
            errorMessage = getString(R.string.nexus_pay_pin_does_not_match);
        }

        if (errorMessage != null) {
            showWarningToast(errorMessage);
            newPINEt.getText().clear();
            reNewPINEt.getText().clear();
            newPINEt.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onVerificationFailed(String message) {
        if (isActivityActive()) {
            getDialogManager().showErrorDialog(getString(R.string.error), message,
                    getString(R.string.ok),
                    new ErrorDialog.OnSubmitBtnClickListener() {
                        @Override
                        public void onSubmitBtnClick() {
                            dismissDialog();
                            sixDigitCodeEt.getText().clear();
                        }
                    });
        }
    }

    @Override
    public void startUserInfoActivity() {
        Intent intent = new Intent(new Intent(AccountVerificationActivity.this, UserInfoActivity.class));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void showReIntializeFailedDialog(String message, final String activationCode) {
        if (isActivityActive()) {
            if (message == null || message.isEmpty()) {
                message = getString(R.string.device_change_failed_try_again);
            }

            getDialogManager().showErrorDialog(getString(R.string.change_device), message,
                    getString(R.string.ok),
                    new ErrorDialog.OnSubmitBtnClickListener() {
                        @Override
                        public void onSubmitBtnClick() {
                            dismissDialog();
                        }
                    });
        }
    }

    @Override
    public void showDataSyncFailedDialog(String errorMsg) {
        if (isActivityActive()) {
            if (errorMsg == null || errorMsg.isEmpty()) {
                errorMsg = getString(R.string.device_change_failed_try_again);
            }
            getDialogManager().showDoubleButtonSimpleDialog(R.string.sync_failed, errorMsg,
                    R.string.retry,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissDialog();
                            presenter.loadInactiveCardList();

                        }
                    },
                    R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissDialog();
                        }
                    });
        }
    }


    @Override
    public void showMessage(String string) {
        showWarningToast(string);
    }

    @Override
    public void showSuccessMessage(String string) {
        showSuccessToast(string);
    }

    @Override
    public void showChangePinProgressDialog(String string) {
        showProgressDialog(string);
    }

    @Override
    public void goToNextScreenAfterChangeDevice(List<ServiceData> serviceDataList, boolean isDataSynced, String errorMsg) {
        if (isDataSynced) {
            if (serviceDataList == null || serviceDataList.size() == 0) {
                Intent cardSelectionIntent = new Intent(this, CardSelectionActivity.class);
                cardSelectionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                cardSelectionIntent.putExtra(IntentKey.ACTIVE_CARD_AVAILABLE, false);
                cardSelectionIntent.putExtra(IntentKey.INACTIVE_CARD_AVAILABLE, false);
                startActivity(cardSelectionIntent);
            } else {
                Intent inactiveCardIntent = new Intent(this, InactiveCardActivity.class);
                inactiveCardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                inactiveCardIntent.putExtra(IntentKey.ACTIVE_CARD_AVAILABLE, false);
                startActivity(inactiveCardIntent);
            }
        } else {
            showDataSyncFailedDialog(errorMsg);
        }
    }

    public void setSMSReceiver() {
        receiver = new SMSReceiver();
        receiver.setCallback(new SMSReceiver.SMSReceiveCallback() {
            @Override
            public void onSmsReceived(String code) {
                if (code != null) {
                    if (progressCountTv.getVisibility() == View.VISIBLE) {
                        progressCountTv.setVisibility(View.GONE);
                    }
                    if (resendCodeTv.getVisibility() == View.VISIBLE) {
                        resendCodeTv.setVisibility(View.GONE);
                    }
                    verificationCheckIv.setVisibility(View.VISIBLE);
                    sixDigitCodeEt.setText(code);
                    verificationHintTv.setText(getString(R.string.code_received_hint));
                    verificationBtn.setBackgroundResource(R.drawable.nexuspay_active_button_dialog);
                    verificationBtn.setEnabled(true);

                    isCodeReceived = true;
                    unregisterReceiver(receiver);
                    receiver.setCallback(null);
                    receiver = null;
                }
            }
        });
        registerReceiver(receiver, AppIntentUtil.getSmsReceiverIntentFilter());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showWarningToast(this.getString(R.string.permission_granted_hint));
                } else {
                    showWarningToast(this.getString(R.string.user_permission_required_hint));
                }
            }
            break;
        }
    }

    @Override
    public void openLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        handler.removeMessages(TYPE_CURSOR_BLINK);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(MetaDataUpdated event) {

    }
}
