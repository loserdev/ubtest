package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.braintreepayments.cardform.view.CardForm;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAccountActivity extends BaseActivity implements View.OnClickListener{

  private Handler mHandler = new Handler();

  @BindView(R.id.add_acc_btn)
  Button verifyBtn;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_acc);
    ButterKnife.bind(this);
    verifyBtn.setOnClickListener(this);
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

  @Override
  public void onClick(View view)
  {
    switch (view.getId()) {
      case R.id.add_acc_btn:
        startActivity(new Intent(this, MainActivity.class));
        return;

      default:
        return;
    }
  }
}