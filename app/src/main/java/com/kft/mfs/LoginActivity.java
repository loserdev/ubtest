package com.kft.mfs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.login_phone_number_et)
    EditText phoneNumberEt;

    @BindView(R.id.wallet_pin_et)
    EditText walletPinEt;

    @BindView(R.id.hidden_password_ll)
    LinearLayout hiddenPasswordLl;

    @BindView(R.id.img_pw_01)
    ImageView pw1;

    @BindView(R.id.img_pw_02)
    ImageView pw2;

    @BindView(R.id.img_pw_03)
    ImageView pw3;

    @BindView(R.id.img_pw_04)
    ImageView pw4;

    @BindView(R.id.img_pw_05)
    ImageView pw5;

    @BindView(R.id.img_pw_06)
    ImageView pw6;

    @BindView(R.id.show_wallet_pin_iv)
    ImageView showPinIv;

    @BindView(R.id.login_btn)
    Button loginBtn;

    @BindView(R.id.auto_login_cb)
    CheckBox autoLoginCb;

    @BindView(R.id.registration_ll)
    LinearLayout registrationLl;

    @BindView(R.id.register_tv)
    TextView registerTv;

    @BindView(R.id.forgot_wallet_pin_tv)
    TextView forgotPinTv;

    private ImageView[] pwIvs;
    private boolean showWalletPin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {

        if (applicationStateDao.getState().getCode() < ApplicationState.INITIALIZED.getCode()) {
            autoLoginCb.setVisibility(View.GONE);
            registrationLl.setVisibility(View.VISIBLE);
            forgotPinTv.setVisibility(View.GONE);
        } else {
            phoneNumberEt.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryGrey));
            phoneNumberEt.setText(PreferenceManager.getPreferenceManager(this).getPhoneNumber());
            phoneNumberEt.setEnabled(false);
        }
        walletPinEt.setHint(getText(R.string.wallet_pin_hint));
        if (hiddenPasswordLl.getVisibility() == View.VISIBLE) {
            hiddenPasswordLl.setVisibility(View.GONE);
        }
        pwIvs = new ImageView[]{pw1, pw2, pw3, pw4, pw5, pw6};

        final SingleClick singleClick = new SingleClick();
        showPinIv.setOnClickListener(singleClick);
        loginBtn.setOnClickListener(singleClick);
        registerTv.setOnClickListener(singleClick);
        forgotPinTv.setOnClickListener(singleClick);
        walletPinEt.setOnFocusChangeListener(this);
        walletPinEt.getText().clear();
        setTextChangedListener();

    }

    private class SingleClick extends OnSingleClickListener {
        @Override
        public void onSingleClick(View view) {
            //KonaLogger.debugLog(TAG, "onSingleClick");
            switch (view.getId()) {
                case R.id.show_wallet_pin_iv:
                    showWalletPin = !showWalletPin;
                    if (showWalletPin) {
                        showPinIv.setImageResource(R.drawable.ic_visibility_off);
                        walletPinEt.setHint(getText(R.string.wallet_pin_hint));
                        walletPinEt.setTextColor(Color.BLACK);
                        walletPinEt.setCursorVisible(true);
                        hiddenPasswordLl.setVisibility(View.GONE);
                    } else {
                        showPinIv.setImageResource(R.drawable.ic_visibility);
                        walletPinEt.setHint(null);
                        walletPinEt.setCursorVisible(false);
                        walletPinEt.setTextColor(Color.TRANSPARENT);
                        hiddenPasswordLl.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.login_btn:
                    submitLoginForm();
                    break;
                case R.id.register_tv:
                    startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                    finish();
                    break;
                case R.id.forgot_wallet_pin_tv:
                    String phoneNumber = phoneNumberEt.getText().toString().trim();
                    int validPhoneLength = getResources().getInteger(R.integer.phone_min_length_without_country_code);
                    phoneNumber = (phoneNumber.length() > validPhoneLength) ? phoneNumber.substring(phoneNumber.length() - validPhoneLength) : phoneNumber;
                    if (TextUtils.isEmpty(phoneNumber)) {
                        showWarningToast(getString(R.string.err_msg_phone_number));
                    } else {
                        showResetPinInfoDialogToCallCC();
                    }
                    break;
            }

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.wallet_pin_et:
                String pin = walletPinEt.getText().toString().trim();
                if (hasFocus) {
                    if (!showWalletPin && hiddenPasswordLl.getVisibility() == View.GONE) {
                        walletPinEt.setHint(null);
                        walletPinEt.setTextColor(Color.TRANSPARENT);
                        hiddenPasswordLl.setVisibility(View.VISIBLE);
                    }
                    if ((pin.length() > 0) && (pin.length() < Constant.NEXUS_PAY_PIN_LENGTH)) {
                        walletPinEt.getText().clear();
                    }
                } else {

                    if (pin.length() == 0) {
                        hiddenPasswordLl.setVisibility(View.GONE);
                        walletPinEt.setHint(R.string.wallet_pin_hint);
                    }
                }
                break;
        }
    }

    private void setTextChangedListener() {
        walletPinEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (hiddenPasswordLl.getVisibility() == View.VISIBLE) {
                    walletPinEt.setSelection(s.length());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ViewUtility.setWalletPinView(s, pwIvs);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void submitLoginForm() {
        String phoneNumber = phoneNumberEt.getText().toString().trim();
        if (!validatePhoneNumber(phoneNumber)) {
            return;
        }
        String walletPin = walletPinEt.getText().toString().trim();
        if (!validateWalletPin(walletPin)) {
            return;
        }
        boolean isAutoLogin = false;
        if (autoLoginCb.getVisibility() == View.VISIBLE) {
            isAutoLogin = autoLoginCb.isChecked();
        }

    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < getResources().getInteger(R.integer.phone_min_length_without_country_code)
                || !phoneNumber.startsWith(getString(R.string.required_phone_number_prefix_without_country_code))) {
            showWarningToast(getString(R.string.err_msg_phone_number));
            requestFocus(phoneNumberEt);
            return false;
        }
        return true;
    }

    private boolean validateWalletPin(String walletPin) {
        if (TextUtils.isEmpty(walletPin) || walletPin.length() < Constant.wallet_pin_length) {
            showWarningToast(getString(R.string.err_msg_wallet_pin));
            walletPinEt.setText("");
            walletPinEt.requestFocus();

            return false;
        }
        return true;
    }

    @Override
    public void startNextActivity() {
        Intent intent = new Intent();
        final List<ServiceData> inactiveCardDataList = new ArrayList<>();
        final List<ServiceData> activatedCardDataList = new ArrayList<>();
        final List<ServiceData> allOwnList = konaPaymentDataProvider.getAllOwnedServiceList();
        if (allOwnList != null && !allOwnList.isEmpty()) {
            activatedCardDataList.addAll(AppCardsUtil.getListOfActivatedCardsWithProfile(allOwnList));
            inactiveCardDataList.addAll(AppCardsUtil.getListOfInActivateCards(allOwnList));
        }
        ApplicationState currentState = applicationStateDao.getState();
        if (currentState == ApplicationState.INITIALIZED) {
            intent.setClass(this, UserInfoActivity.class);
        } else if (activatedCardDataList != null && activatedCardDataList.size() != 0) {
            intent.setClass(this, DashboardActivity.class);
        } else {
            if (inactiveCardDataList != null && inactiveCardDataList.size() != 0) {

                intent.setClass(this, InactiveCardActivity.class);
                intent.putExtra(IntentKey.ACTIVE_CARD_AVAILABLE, false);
            } else {
                intent.setClass(this, CardSelectionActivity.class);
                intent.putExtra(IntentKey.ACTIVE_CARD_AVAILABLE, false);
                intent.putExtra(IntentKey.INACTIVE_CARD_AVAILABLE, false);
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void openAccountVerficationActivityForChangeDevice(String phoneNumber, String password) {
        Intent intent = new Intent(this, AccountVerificationActivity.class);
        intent.putExtra(IntentKey.PHONE_NUMBER, phoneNumber);
        intent.putExtra(IntentKey.PASSWORD, password);
        intent.putExtra(IntentKey.VERIFICATION_TYPE, VerificationType.CHANGE_DEVICE.getCode());
        startActivity(intent);
    }

    @Override
    public void prepareBundleAndCallForgetPINActivity() {
        Intent forgetActivityIntent = new Intent(getBaseContext(), AccountVerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.VERIFICATION_TYPE, VerificationType.FORGOT_WALLET_PIN.getCode());
        String phoneNumber = phoneNumberEt.getText().toString().trim();
        int validPhoneLength = getResources().getInteger(R.integer.phone_min_length_without_country_code);
        phoneNumber = (phoneNumber.length() > validPhoneLength) ? phoneNumber.substring(phoneNumber.length() - validPhoneLength) : phoneNumber;
        bundle.putString(IntentKey.PHONE_NUMBER, phoneNumber);
        forgetActivityIntent.putExtras(bundle);
        startActivity(forgetActivityIntent);
        LoginActivity.this.finish();
    }

    @Override
    public void showResetPinInfoDialogToCallCC() {
        if (isActivityActive()) {
            walletPinEt.getText().clear();
            getDialogManager().showDoubleButtonCustomDialog(getString(R.string.forget_pin_dialog_title), getString(R.string.forget_pin_dialog_message_to_call_cc),
                    R.string.ok,
                    new DoubleButtonCustomDialog.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick(DoubleButtonCustomDialog dialog) {
                            dismissDialog();
                            prepareBundleAndCallForgetPINActivity();
                        }
                    },
                    R.string.cancel,
                    new DoubleButtonCustomDialog.OnNegativeBtnClickListener() {
                        @Override
                        public void onNegativeBtnClick(DoubleButtonCustomDialog dialog) {
                            dismissDialog();
                        }
                    });
        }
    }

    @Override
    public void showResetPinToasty() {
        showFailToast(getString(R.string.max_wrong_wallet_pin_exceed));
    }

    @Override
    public void onLoginFailed(String message) {
        if (isActivityActive()) {
            getDialogManager().showErrorDialog(getString(R.string.error), message,
                    getString(R.string.ok),
                    new ErrorDialog.OnSubmitBtnClickListener() {
                        @Override
                        public void onSubmitBtnClick() {
                            dismissDialog();
                            walletPinEt.getText().clear();
                        }
                    });
        }
    }

    @Override
    public void showChangeDeviceDialog() {
        if (isActivityActive()) {
            getDialogManager().showDoubleButtonCustomDialog(getString(R.string.change_device), getString(R.string.change_device_confirm_dialog),
                    R.string.yes,
                    new DoubleButtonCustomDialog.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick(DoubleButtonCustomDialog dialog) {
                            dismissDialog();
                            checkReadSmsPermission();
                        }
                    },
                    R.string.no,
                    new DoubleButtonCustomDialog.OnNegativeBtnClickListener() {
                        @Override
                        public void onNegativeBtnClick(DoubleButtonCustomDialog dialog) {
                            dismissDialog();
                        }
                    });
        }
    }

    @Override
    public void checkReadSmsPermission() {
        if (!isPermissionAvailable(Constant.PERMISSION_READ_SMS)) {
            requestPermission(Constant.PERMISSION_READ_SMS);
        } else {
            String phoneNumber = phoneNumberEt.getText().toString().trim();
            String walletPin = walletPinEt.getText().toString().trim();
            presenter.requestForChangeDeviceToken(phoneNumber, walletPin);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PERMISSION_READ_SMS: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showWarningToast(this.getString(R.string.user_permission_useful_hint));
                }
                String phoneNumber = phoneNumberEt.getText().toString().trim();
                String walletPin = walletPinEt.getText().toString().trim();

            }
            break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
