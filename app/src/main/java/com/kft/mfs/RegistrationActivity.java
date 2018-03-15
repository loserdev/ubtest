package com.kft.mfs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistrationActivity extends BaseActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();

    @BindView(R.id.registration_tool_bar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title_tv)
    TextView toolBarTitleTv;

    @BindView(R.id.registration_sv)
    ScrollView scrollView;

    @BindView(R.id.registration_view_pager)
    CustomedViewPager viewPager;

    @BindView(R.id.dots_ll)
    LinearLayout dotsLl;

    @BindView(R.id.registration_phone_number_et)
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

    @BindView(R.id.registration_tc_tv)
    TextView termsAndConditionsTv;

    @BindView(R.id.terms_and_conditions_cb)
    CheckBox termsAndConditionsCb;

    @BindView(R.id.registration_btn)
    Button registrationBtn;

    @Inject
    RegistrationPresenter presenter;

    private PreferenceManager preferenceManager;
    private ImageView[] pwIvs;
    private boolean showWalletPin = false;
    private CarouselPagerAdapter carouselPagerAdapter;
    private TextView[] dotTvs;
    private int normalColor, selectedColor;
    private long DELAY_MS = 500;
    private long PERIOD_MS = 4000;
    private Timer timer;
    private int[] imageIdArray = {
            R.drawable.nexuspay_banner,
            R.drawable.dashboard_banner,
            R.drawable.mobile_pay_banner
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.stopScreenCapturing();
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        presenter.onCreate();
        initView();
        FirebaseCrash.log(this.getString(R.string.fb_acc_regis_oncreate));
    }

    @Override
    protected void setupComponent(AppComponent component) {
        DaggerRegistrationComponent.builder()
                .appComponent(component)
                .registrationModule(new RegistrationModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBarTitleTv.setText(getString(R.string.registration_title));
        walletPinEt.setHint(this.getString(R.string.registration_wallet_pin_hint));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (hiddenPasswordLl.getVisibility() == View.VISIBLE) {
            hiddenPasswordLl.setVisibility(View.GONE);
        }
        pwIvs = new ImageView[]{pw1, pw2, pw3, pw4, pw5, pw6};

        SingleClick singleClick=new SingleClick();
        showPinIv.setOnClickListener(singleClick);
        termsAndConditionsTv.setOnClickListener(singleClick);
        registrationBtn.setOnClickListener(singleClick);
        walletPinEt.setOnFocusChangeListener(this);
        setTextChangedListener();

        getPermissions();
        if (preferenceManager.getPhoneNumber() != null) {
            phoneNumberEt.setText(preferenceManager.getPhoneNumber());
        }
        setupCarousel();
    }

    private void setupCarousel() {
        carouselPagerAdapter = new CarouselPagerAdapter(this, imageIdArray);
        viewPager.setAdapter(carouselPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerChangeListener);
        selectedColor = ContextCompat.getColor(this, R.color.colorPrimaryRed);
        normalColor = Color.WHITE;
        dotTvs = new TextView[carouselPagerAdapter.getCount()];
        for (int i = 0; i < dotTvs.length; i++) {
            dotTvs[i] = new TextView(this);
            dotTvs[i].setText(getString(R.string.dot));
            dotTvs[i].setTextSize(getResources().getDimension(R.dimen.dot_size));
            dotTvs[i].setTextColor(normalColor);
            dotsLl.addView(dotTvs[i]);
        }
        dotTvs[0].setTextColor(selectedColor);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        int position = (viewPager.getCurrentItem() + 1) % carouselPagerAdapter.getCount();
                        viewPager.setCurrentItem(position, true);
                    }
                });
            }
        }, DELAY_MS, PERIOD_MS);
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
                        walletPinEt.setHint(R.string.registration_wallet_pin_hint);
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

    ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < carouselPagerAdapter.getCount(); i++) {
                dotTvs[i].setTextColor(normalColor);
            }
            dotTvs[position].setTextColor(selectedColor);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class SingleClick extends OnSingleClickListener {
        @Override
        public void onSingleClick(View view) {
            RegistrationActivity.this.onClick(view);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_wallet_pin_iv:
                showWalletPin = !showWalletPin;
                if (showWalletPin) {
                    showPinIv.setImageResource(R.drawable.ic_visibility_off);
                    walletPinEt.setHint(getText(R.string.registration_wallet_pin_hint));
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

            case R.id.registration_tc_tv:
                Intent termsAndConditionsIntent = new Intent(this, TermsAndConditionsActivity.class);
                termsAndConditionsIntent.putExtra(IntentKey.SHOW_BUTTON, true);
                startActivity(termsAndConditionsIntent);
                break;

            case R.id.registration_btn:
                submitRegistrationForm();
                break;

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            ArrayList<String> strTcIdList = intent.getStringArrayListExtra(IntentKey.TC_ID_LIST);
            if (strTcIdList != null) {
                termsAndConditionsCb.setChecked(true);
                presenter.setTcIdList(strTcIdList);
            } else {
                termsAndConditionsCb.setChecked(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void submitRegistrationForm() {
        String phoneNumber = phoneNumberEt.getText().toString().trim();
        if (!validatePhoneNumber(phoneNumber)) {
            return;
        }

        String walletPin = walletPinEt.getText().toString().trim();
        if (!validateWalletPin(walletPin)) {
            return;
        }

        if (!termsAndConditionsCb.isChecked()) {
            showWarningToast(getString(R.string.tc_hint));
            return;
        }

        String prevRegistrationUserId = null;
        boolean editRegData = false;
        if (preferenceManager.getPhoneNumber() != null) {
            prevRegistrationUserId = preferenceManager.getPhoneNumber();
            editRegData = true;
        }
        presenter.register(phoneNumber, walletPin, prevRegistrationUserId, editRegData);
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
            walletPinEt.getText().clear();
            walletPinEt.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onRegistrationFailed(String message) {
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
    public void startAccountVerificationActivity(String phoneNumber) {
        preferenceManager.setPhoneNumber(phoneNumber);
        walletPinEt.getText().clear();
        startActivity(new Intent(this, AccountVerificationActivity.class));
    }

    private void getPermissions() {
        if (isPermissionAvailable(Constant.PERMISSION_READ_PHONE_STATE)) {
            readDevicePhoneNo();
        } else {
            requestPermission(Constant.PERMISSION_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readDevicePhoneNo();
                } else {
                    showWarningToast(this.getString(R.string.user_permission_required_hint));
                }
                if (!isPermissionAvailable(Constant.PERMISSION_READ_SMS)) {
                    requestPermission(Constant.PERMISSION_READ_SMS);
                }
            }
            break;
            case Constant.PERMISSION_READ_SMS: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showWarningToast(this.getString(R.string.user_permission_useful_hint));
                }
            }
            break;
        }
    }

    private void readDevicePhoneNo() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String phoneNo = telephonyManager.getLine1Number();
        if (phoneNo == null || TextUtils.isEmpty(phoneNo)) {
        } else {
            if (phoneNo.startsWith(Constant.BD_MOBILE_NO_PREFIX) || phoneNo.startsWith(Constant.BD_PREFIX_NO_PLUS)) {
                phoneNo = phoneNo.replaceAll(Constant.MOBILE_NO_EXTRA_DATA, "").replaceFirst(Constant.BD_PREFIX_NO_PLUS, "");
            } else {
                phoneNo = phoneNo.replaceAll(Constant.MOBILE_NO_EXTRA_DATA, "");
            }
            phoneNumberEt.setText(phoneNo);
        }
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
