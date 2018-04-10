package com.kft.mfs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kft.mfs.dialog.DoubleButtonCustomDialog;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends BaseActivity implements ZBarScannerView.ResultHandler, View.OnClickListener {

  public static String TAG = ScannerActivity.class.getSimpleName();
  private ZBarScannerView mScannerView;

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_scanner);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
    Button payButton = (Button) findViewById(R.id.pay_btn);
    mScannerView = new ZBarScannerView(this);
    contentFrame.addView(mScannerView);
    payButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.pay_btn:
        showPaymentConfirmDialog("Pay with QR code Id");
        return;
      default:
        return;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    mScannerView.setResultHandler(this);
    mScannerView.startCamera();
  }

  @Override
  public void onPause() {
    super.onPause();
    mScannerView.stopCamera();
  }


  private void showPaymentConfirmDialog(String msg)
  {
    getDialogManager().showDoubleButtonCustomDialog("Are you sure ?","You want to Pay 50/= to the Ac. No. 01532476357, Merchant Name: Mena Bazar.",R.string.positive_pay,

            new DoubleButtonCustomDialog.OnPositiveBtnClickListener() {
              @Override
              public void onPositiveBtnClick(DoubleButtonCustomDialog dialog) {
                dismissDialog();
                Intent dashboardIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(dashboardIntent);

              }
            },
            R.string.negative_pay,
            new DoubleButtonCustomDialog.OnNegativeBtnClickListener() {
              @Override
              public void onNegativeBtnClick(DoubleButtonCustomDialog dialog) {
                dismissDialog();
                mScannerView.resumeCameraPreview(ScannerActivity.this);

              }
            });
  }
  @Override
  public void handleResult(Result rawResult) {
    showPaymentConfirmDialog(rawResult.toString());
   /* getDialogManager().showDoubleButtonCustomDialog("Are you sure ?","You want to Pay 50/= to the Ac. No. 01532476357",R.string.positive_pay,

            new DoubleButtonCustomDialog.OnPositiveBtnClickListener() {
              @Override
              public void onPositiveBtnClick(DoubleButtonCustomDialog dialog) {
                dismissDialog();
                Intent dashboardIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(dashboardIntent);

              }
            },
            R.string.negative_pay,
            new DoubleButtonCustomDialog.OnNegativeBtnClickListener() {
              @Override
              public void onNegativeBtnClick(DoubleButtonCustomDialog dialog) {
                dismissDialog();
                mScannerView.resumeCameraPreview(ScannerActivity.this);

              }
            });*/
    // Note:
    // * Wait 2 seconds to resume the preview.
    // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
    // * I don't know why this is the case but I don't have the time to figure out.
     /* Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          mScannerView.resumeCameraPreview(ScannerActivity.this);
        }
      }, 2000);*/
  }
}




