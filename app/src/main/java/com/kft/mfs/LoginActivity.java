package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import appconfig.Constant;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = LoginActivity.class.getSimpleName();

  @BindView(R.id.login_phone_number_et)
  EditText phoneNumberEt;

  @BindView(R.id.wallet_pin_et)
  EditText walletPinEt;

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


  public void initView() {
    loginBtn.setOnClickListener(this);
    registerTv.setOnClickListener(this);
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.login_btn:
        String phoneNo = phoneNumberEt.getText().toString().trim();
        String pin = walletPinEt.getText().toString().trim();
        if (validatePhoneNumber(phoneNo) && validateWalletPin(pin)) {
          startDashBoardActivity();
        }
        return;
      case R.id.register_tv:
        startRegistrationActivity();
        return;
      default:
        return;
    }
  }


  private boolean validatePhoneNumber(String phoneNumber) {
    if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < getResources()
        .getInteger(R.integer.phone_min_length_without_country_code) || !phoneNumber.startsWith(getString(R.string.required_phone_number_prefix_without_country_code))) {
      showWarningToast(getString(R.string.err_msg_phone_number)+phoneNumber);
      phoneNumberEt.requestFocus();
      return false;
    }
    return true;
  }

  private boolean validateWalletPin(String walletPin) {
    if (TextUtils.isEmpty(walletPin) || walletPin.length() < getResources().getInteger((R.integer.wallet_pin_length))) {
      showWarningToast(getString(R.string.err_msg_wallet_pin));
      walletPinEt.setText("");
      walletPinEt.requestFocus();

      return false;
    }
    return true;
  }


  public void startDashBoardActivity() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  public void startRegistrationActivity() {
    Intent intent = new Intent(this, RegistrationActivity.class);
    startActivity(intent);
    //finish();
  }

}
