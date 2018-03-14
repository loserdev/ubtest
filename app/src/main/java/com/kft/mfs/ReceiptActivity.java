package com.kft.mfs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.github.glomadrian.codeinputlib.CodeInput;
import java.util.Arrays;

public class ReceiptActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test);
    CodeInput cInput = (CodeInput) findViewById(R.id.pairing);
    /*cInput.setCodeReadyListener(new CodeInput.codeReadyListener() {
      @Override
      public void onCodeReady(Character[] code) {
        // Code has been entered ....
        Toast.makeText(ReceiptActivity.this, "code entered is : " + Arrays.toString(code),
            Toast.LENGTH_SHORT).show();
      }
    });*/
  }
}