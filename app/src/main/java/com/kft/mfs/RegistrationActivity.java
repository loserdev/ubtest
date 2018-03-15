package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.Timer;


public class RegistrationActivity extends BaseActivity {

  private static final String TAG = RegistrationActivity.class.getSimpleName();


  @BindView(R.id.registration_sv)
  ScrollView scrollView;


  @BindView(R.id.dots_ll)
  LinearLayout dotsLl;

  @BindView(R.id.registration_phone_number_et)
  EditText phoneNumberEt;

  @BindView(R.id.wallet_pin_et)
  EditText walletPinEt;



  @BindView(R.id.registration_tc_tv)
  TextView termsAndConditionsTv;

  @BindView(R.id.terms_and_conditions_cb)
  CheckBox termsAndConditionsCb;

  @BindView(R.id.registration_btn)
  Button registrationBtn;


  private PreferenceManager preferenceManager;
  private ImageView[] pwIvs;
  private boolean showWalletPin = false;

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
    walletPinEt.setHint("Hint");
    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   /* showPinIv.setOnClickListener(singleClick);
    termsAndConditionsTv.setOnClickListener(singleClick);
    registrationBtn.setOnClickListener(singleClick);
    walletPinEt.setOnFocusChangeListener(this);
    setTextChangedListener();*/
  }

  public void startAccountVerificationActivity(String phoneNumber) {
    //preferenceManager.setPhoneNumber(phoneNumber);
    walletPinEt.getText().clear();
    startActivity(new Intent(this, AccountVerificationActivity.class));
  }


}
