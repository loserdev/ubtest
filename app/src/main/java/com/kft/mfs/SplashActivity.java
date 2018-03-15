package com.kft.mfs;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Fahad on 3/15/2018.
 */

public class SplashActivity extends BaseActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}
