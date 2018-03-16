package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;
import java.util.Timer;


public class RegistrationActivity extends BaseActivity implements View.OnClickListener{

  private static final String TAG = RegistrationActivity.class.getSimpleName();


  @BindView(R.id.registration_sv)
  ScrollView scrollView;

  @BindView(R.id.registration_phone_number_et)
  EditText phoneNumberEt;

  @BindView(R.id.account_owner_name_et)
  EditText nameEt;

  @BindView(R.id.account_owner_email_et)
  EditText emailEt;

  @BindView(R.id.wallet_pin_et)
  EditText walletPinEt;

  @BindView(R.id.registration_tc_tv)
  TextView termsAndConditionsTv;

  @BindView(R.id.terms_and_conditions_cb)
  CheckBox termsAndConditionsCb;

  @BindView(R.id.registration_btn)
  Button registrationBtn;

  @BindView(R.id.acc_verification_code)
  CodeInputView accountVerificationCode;

  @BindView(R.id.registration_phone_number_ll)
  RelativeLayout phoneNoRL;

  @BindView(R.id.registration_wallet_pin_ll)
  RelativeLayout pinRL;

  @BindView(R.id.account_owner_name_ll)
  RelativeLayout nameRL;

  @BindView(R.id.account_owner_email_ll)
  RelativeLayout emailRL;

  @BindView(R.id.terms_and_conditions_ll)
  LinearLayout tcLL;

  @BindView(R.id.acc_veri_ll)
  LinearLayout accVerificationLL;

  @BindView(R.id.verify_acc_btn)
  Button verifyBtn;


  private PreferenceManager preferenceManager;
  private ImageView[] pwIvs;
  private boolean showWalletPin = false;
  private Handler mHandler = new Handler();

  private TextView[] dotTvs;
  private int normalColor, selectedColor;
  private long DELAY_MS = 500;
  private long PERIOD_MS = 4000;
  private Timer timer;
  private int[] imageIdArray = {
      R.drawable.red_card,
      R.drawable.goat_card,
      R.drawable.black_card
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
    ButterKnife.bind(this);
    initView();
  }

  public void initView() {
    //preferenceManager = PreferenceManager.getPreferenceManager(this);
    registrationBtn.setOnClickListener(this);
    verifyBtn.setOnClickListener(this);
    accountVerificationCode.addOnCompleteListener(new OnCodeCompleteListener() {
      @Override
      public void onCompleted(String code) {
        mHandler.postDelayed(new Runnable() {
          @Override
          public void run() {
            //Make the input enable again so the user can change it
            accountVerificationCode.setEditable(true);

            //Show error
            //accountVerificationCode.setError("Your code is incorrect");
          }
        }, 1000);
      }
    });

    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   /* showPinIv.setOnClickListener(singleClick);
    termsAndConditionsTv.setOnClickListener(singleClick);
    registrationBtn.setOnClickListener(singleClick);
    walletPinEt.setOnFocusChangeListener(this);
    setTextChangedListener();*/
  }

  @Override
  public void onClick(View view){
    switch (view.getId()) {
      case R.id.registration_btn:
        String phoneNo = phoneNumberEt.getText().toString().trim();
        String pin = walletPinEt.getText().toString().trim();
        String name = nameEt.getText().toString().trim();
        if(validatePhoneNumber(phoneNo)&& validateWalletPin(pin) && validateName(name)) {
          ;
          invisibleAllRegistrationControlShowActiviationControl();
        }
        return;
      case R.id.verify_acc_btn:
        startAddAccountActivity();
        return;
      case R.id.tc_agree_btn:
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

  private boolean validateName(String name) {
    if (TextUtils.isEmpty(name) ) {
      showWarningToast(getString(R.string.err_msg_user_name));
      nameEt.setText("");
      nameEt.requestFocus();
      return false;
    }
    return true;
  }

  public void startAddAccountActivity() {
    //preferenceManager.setPhoneNumber(phoneNumber);
    startActivity(new Intent(this, AddAccountActivity.class));
  }

  private void invisibleAllRegistrationControlShowActiviationControl()
  {
    phoneNoRL.setVisibility(View.GONE);
    pinRL.setVisibility(View.GONE);
    nameRL.setVisibility(View.GONE);
    emailRL.setVisibility(View.GONE);
    tcLL.setVisibility(View.GONE);
    registrationBtn.setVisibility(View.GONE);
    accVerificationLL.setVisibility(View.VISIBLE);
  }

}
