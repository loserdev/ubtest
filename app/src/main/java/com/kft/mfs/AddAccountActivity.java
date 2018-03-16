package com.kft.mfs;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

public class AddAccountActivity extends BaseActivity {

  private Handler mHandler = new Handler();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_acc);

    final CodeInputView otherCodeInput = (CodeInputView) findViewById(R.id.pairing);
    otherCodeInput.addOnCompleteListener(new OnCodeCompleteListener() {
      @Override
      public void onCompleted(String code) {
        mHandler.postDelayed(new Runnable() {
          @Override
          public void run() {
            //Make the input enable again so the user can change it
            otherCodeInput.setEditable(true);

            //Show error
            otherCodeInput.setError("Your code is incorrect");
          }
        }, 1000);
      }
    });

    /*final CodeInputView codeInputView = (CodeInputView) findViewById(R.id.with_complete_callback);

    //Default value
    codeInputView.setCode("23");
    codeInputView.setInPasswordMode(true);

    //Action to do when completed
    codeInputView.addOnCompleteListener(new OnCodeCompleteListener() {
      @Override
      public void onCompleted(String code) {
        Toast.makeText(AddAccountActivity.this, "Your code: " + code, Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(new Runnable() {
          @Override
          public void run() {
            //Make the input enable again so the user can change it
            codeInputView.setEditable(true);

            //Show error
            codeInputView.setError("Your code is incorrect");
          }
        }, 1000);
      }
    });*/
  }
}