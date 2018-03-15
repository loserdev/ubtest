package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TermsAndConditionsActivity extends BaseActivity {


  @BindView(R.id.tc_agree_btn)
  Button tcAgreeBtn;

  @BindView(R.id.tc_decline_btn)
  Button tcDeclineBtn;

  @BindView(R.id.tc_bottom_ll)
  LinearLayout buttonPanelLinearLayout;

  @BindView(R.id.terms_and_conditions_tv)
  TextView tcTextView;


  private boolean showAgreeDisagreeButton = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_terms_and_conditions);
    ButterKnife.bind(this);
    showAgreeDisagreeButton = getIntent().getExtras().getBoolean("true");
    initView();
  }


  public void initView() {
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    tcAgreeBtn.setVisibility(View.GONE);
    tcDeclineBtn.setVisibility(View.GONE);
    buttonPanelLinearLayout.setVisibility(View.GONE);

    //tcAgreeBtn.setOnClickListener(clickListener);
    //tcDeclineBtn.setOnClickListener(clickListener);
  }

  public void bindTermsAndConditionToView() {

    if (showAgreeDisagreeButton) {
      tcAgreeBtn.setVisibility(View.VISIBLE);
      tcDeclineBtn.setVisibility(View.VISIBLE);
      buttonPanelLinearLayout.setVisibility(View.VISIBLE);
    }
    //tcTextView.setText(Html.fromHtml(changedHeaderHtml(allTermsAndCondition)));
  }

  public static String changedHeaderHtml(String htmlText) {
    String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
    String closedTag = "</body></html>";
    String changeFontHtml = head + htmlText + closedTag;
    return changeFontHtml;
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
//    if (isAgreed) {
//      registrationIntent.putStringArrayListExtra(IntentKey.TC_ID_LIST,
//          Utility.getStringArrayFromLong(termsAndConditionsPresenter.getTcIdList()));
//    } else {
//      registrationIntent.putStringArrayListExtra(IntentKey.TC_ID_LIST, null);
//    }
    startActivity(registrationIntent);
    finish();
  }
}
