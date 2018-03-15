package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountVerificationActivity extends BaseActivity {

  private static final String TAG = AccountVerificationActivity.class.getSimpleName();


  @BindView(R.id.account_verification_sv)
  ScrollView scrollView;

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


  int verificationType = 1234;
  private String phoneNumber;
  private String password;
  private ImageView[] newPwIvs, retypePwIvs;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    setContentView(R.layout.activity_account_verification);
    ButterKnife.bind(this);
    initView();
  }


  public void initView() {

    verificationBtn.setEnabled(false);
    //verificationBtn.setOnClickListener(singleClick);
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


  public void hideSystemKeyboard() {
    InputMethodManager inputManager = (InputMethodManager) this
        .getSystemService(this.INPUT_METHOD_SERVICE);
    View focusView = this.getCurrentFocus();
    if (focusView != null) {
      inputManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }
  }

  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.account_verification_btn:

        submitVerificationForm();
        break;
    }
  }


  public void clearPinField() {
    newPINEt.getText().clear();
    reNewPINEt.getText().clear();
  }

  private void submitVerificationForm() {
    if (userIdEditText.getVisibility() == View.VISIBLE) {
      phoneNumber = userIdEditText.getText().toString().trim();
    }
    String newPin = newPINEt.getText().toString().trim();
    String renewPin = reNewPINEt.getText().toString().trim();


  }

  public void startUserInfoActivity() {
    Intent intent = new Intent(new Intent(AccountVerificationActivity.this, ReportActivity.class));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }

  public void openLoginActivity() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override
  protected void onPause() {
    super.onPause();
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
