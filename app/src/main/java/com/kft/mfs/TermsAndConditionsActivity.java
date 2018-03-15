package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



public class TermsAndConditionsActivity extends BaseActivity {
    @BindView(R.id.tc_tool_bar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title_tv)
    TextView toolBarTitleTv;

    @BindView(R.id.tc_agree_btn)
    Button tcAgreeBtn;

    @BindView(R.id.tc_decline_btn)
    Button tcDeclineBtn;

    @BindView(R.id.tc_bottom_ll)
    LinearLayout buttonPanelLinearLayout;

    @BindView(R.id.terms_and_conditions_tv)
    TextView tcTextView;

    @Inject
    TermsAndConditionsPresenter termsAndConditionsPresenter;

    private boolean showAgreeDisagreeButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
        showAgreeDisagreeButton = getIntent().getExtras().getBoolean(IntentKey.SHOW_BUTTON);
        termsAndConditionsPresenter.onCreate();
        initView();
        FirebaseCrash.log(this.getString(R.string.fb_tandc_oncreate));
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerTermsAndConditionsComponent.builder()
                .appComponent(appComponent)
                .termsAndConditionsModule(new TermsAndConditionsModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBarTitleTv.setText(getString(R.string.tc_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tcAgreeBtn.setVisibility(View.GONE);
        tcDeclineBtn.setVisibility(View.GONE);
        buttonPanelLinearLayout.setVisibility(View.GONE);

        SingleClick singleClick=new SingleClick();
        tcAgreeBtn.setOnClickListener(singleClick);
        tcDeclineBtn.setOnClickListener(singleClick);
        termsAndConditionsPresenter.prepareTermsAndConditionsData();
    }


    @Override
    public void bindTermsAndConditionToView(ArrayList<TermsAndConditionData> termsAndConditionDataList) {
        if (showAgreeDisagreeButton) {
            tcAgreeBtn.setVisibility(View.VISIBLE);
            tcDeclineBtn.setVisibility(View.VISIBLE);
            buttonPanelLinearLayout.setVisibility(View.VISIBLE);
        } else {
            tcAgreeBtn.setVisibility(View.GONE);
            tcDeclineBtn.setVisibility(View.GONE);
            buttonPanelLinearLayout.setVisibility(View.GONE);
        }
        String allTermsAndCondition = "";
        if (termsAndConditionDataList.size() == 0) {
            allTermsAndCondition = this.getString(R.string.no_terms_Conditoin_from_server);
        } else {
            for (TermsAndConditionData termsAndConditionData : termsAndConditionDataList) {
                allTermsAndCondition = allTermsAndCondition + termsAndConditionData.getTitle();
            }
        }
        tcTextView.setText(Html.fromHtml(changedHeaderHtml(allTermsAndCondition)));
    }

    public static String changedHeaderHtml(String htmlText) {
        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String closedTag = "</body></html>";
        String changeFontHtml = head + htmlText + closedTag;
        return changeFontHtml;
    }

    @Override
    public void retrieveTermsAndConditionFailed(String errorCode, String message) {
        if (isActivityActive()) {
            getDialogManager().showErrorDialog(getString(R.string.error), message,
                    getString(R.string.ok),
                    new ErrorDialog.OnSubmitBtnClickListener() {
                        @Override
                        public void onSubmitBtnClick() {
                            dismissDialog();
                            finish();
                        }
                    });
        }
    }

    @Override
    public void showRetryCancelDialog(String message) {
        if (isActivityActive()) {
            getDialogManager().showDoubleButtonErrorDialog(getString(R.string.error), message,
                    R.string.retry,
                    new DoubleButtonErrorDialog.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick() {
                            dismissDialog();
                            termsAndConditionsPresenter.prepareTermsAndConditionsData();
                        }
                    },
                    R.string.cancel,
                    new DoubleButtonErrorDialog.OnNegativeBtnClickListener() {
                        @Override
                        public void onNegativeBtnClick() {
                            dismissDialog();
                            finish();
                        }
                    });
        }
    }

    private class SingleClick extends OnSingleClickListener {
        @Override
        public void onSingleClick(View view) {
            TermsAndConditionsActivity.this.onClick(view);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tc_agree_btn:
                startRegistrationActivity(true);
                break;

            case R.id.tc_decline_btn:
                startRegistrationActivity(false);
                break;
        }
    }

    private void startRegistrationActivity(boolean isAgreed) {
        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        if (isAgreed) {
            registrationIntent.putStringArrayListExtra(IntentKey.TC_ID_LIST, Utility.getStringArrayFromLong(termsAndConditionsPresenter.getTcIdList()));
        } else {
            registrationIntent.putStringArrayListExtra(IntentKey.TC_ID_LIST, null);
        }
        startActivity(registrationIntent);
        finish();
    }
}
