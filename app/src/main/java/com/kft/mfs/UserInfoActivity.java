package com.kft.mfs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;



public class UserInfoActivity extends BaseActivity {
    private static final String TAG = UserInfoActivity.class.getSimpleName();

    @BindView(R.id.user_info_tool_bar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title_tv)
    TextView toolBarTitleTv;

    @BindView(R.id.user_info_sv)
    ScrollView scrollView;

    @BindView(R.id.user_info_view_pager)
    CustomedViewPager viewPager;

    @BindView(R.id.user_info_dots_ll)
    LinearLayout dotsLl;

    @BindView(R.id.account_owner_name_et)
    EditText nameEt;

    @BindView(R.id.account_owner_email_et)
    EditText emailEt;

    @BindView(R.id.account_owner_info_skip_btn)
    Button skipBtn;

    @BindView(R.id.account_owner_info_next_btn)
    Button nextBtn;

    @Inject
    UserInfoPresenter userInfoPresenter;

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
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initView();
        FirebaseCrash.log(this.getString(R.string.fb_user_info_oncreate));
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerUserInfoComponent.builder()
                .appComponent(appComponent)
                .userInfoModule(new UserInfoModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBarTitleTv.setText(getString(R.string.account_verification_title));
        SingleClick singleClick=new SingleClick();
        skipBtn.setOnClickListener(singleClick);
        nextBtn.setOnClickListener(singleClick);
        nameEt.setOnFocusChangeListener(this);
        emailEt.setOnFocusChangeListener(this);
        nameEt.addTextChangedListener(new UserInfoTextChangeWatcher(nameEt));
        emailEt.addTextChangedListener(new UserInfoTextChangeWatcher(emailEt));
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

    private class UserInfoTextChangeWatcher implements TextWatcher {
        private View view;
        String userName;
        String emailId;

        private UserInfoTextChangeWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userName = nameEt.getText().toString().trim();
            emailId = emailEt.getText().toString().trim();

            if (!TextUtils.isEmpty(userName) || !TextUtils.isEmpty(emailId)) {
                nextBtn.setEnabled(true);
                nextBtn.setBackgroundResource(R.drawable.nexuspay_active_button_dialog);
            }

            if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(emailId)) {
                nextBtn.setEnabled(false);
                nextBtn.setBackgroundResource(R.drawable.nexuspay_fade_button);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }


    @Override
    public void startCardSelectionActivity() {
        Intent intent = new Intent(this, CardSelectionActivity.class);
        intent.putExtra(IntentKey.ACTIVE_CARD_AVAILABLE, false);
        intent.putExtra(IntentKey.INACTIVE_CARD_AVAILABLE, false);
        startActivity(intent);
        finish();
    }

    private class SingleClick extends OnSingleClickListener {
        @Override
        public void onSingleClick(View view) {
            UserInfoActivity.this.onClick(view);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_owner_info_skip_btn:
                userInfoPresenter.skipAdditionalInfo();
                startCardSelectionActivity();
                break;
            case R.id.account_owner_info_next_btn:
                submitAccountInfoForm();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.account_owner_name_et:
                ViewUtility.scrollToPosition(scrollView, 0, scrollView.getBottom(), 100);
                break;

            case R.id.account_owner_email_et:
                ViewUtility.scrollToPosition(scrollView, 0, scrollView.getBottom(), 100);
                break;
        }
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

    @Override
    public void onBackPressed() {
        getDialogManager().showAppExitDialog(this, R.string.app_exit_confirmation_text);
        getDialogManager().showDoubleButtonCustomDialog(getString(R.string.logout_confirmation_title), getString(R.string.logout_confirmation),
                R.string.logout,
                new DoubleButtonCustomDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClick(DoubleButtonCustomDialog dialog) {
                        logout();
                        startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                        finishAffinity();
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

    private void submitAccountInfoForm() {
        if (!TextUtils.isEmpty(emailEt.getText().toString().trim())) {
            if (!validateEmail(emailEt.getText().toString().trim())) {
                return;
            }
        }
        userInfoPresenter.saveUserInfo(nameEt.getText().toString(), emailEt.getText().toString());
    }

    private boolean validateName(String name) {
        if (TextUtils.isEmpty(name)) {
            showWarningToast(getString(R.string.err_msg_name));
            requestFocus(nameEt);
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showWarningToast(getString(R.string.err_msg_email));
            requestFocus(emailEt);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void logout() {
        userInfoPresenter.logout();
    }
}
