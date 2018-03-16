package com.kft.mfs;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.braintreepayments.cardform.view.CardForm;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

public class AddAccountActivity extends BaseActivity {

  private Handler mHandler = new Handler();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_acc);

    CardForm cardForm = (CardForm) findViewById(R.id.card_form);
    cardForm.cardRequired(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .postalCodeRequired(true)
        .mobileNumberRequired(true)
        .mobileNumberExplanation("SMS is required on this number")
        .actionLabel("Purchase")
        .setup(this);
  }
}