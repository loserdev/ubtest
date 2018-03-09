package com.example.kaf.mattest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.kaf.mattest.R;
import data.TransactionLog;

/**
 * Created by Fahad on 3/9/2018.
 */


public class TransactionDetailsDialog extends Dialog {

  private OnOkButtonClickListener onOkButtonClickListener;
  private Context context;
  private TransactionLog transactionLog;
  Button okButton;

  public TransactionDetailsDialog(Context context, TransactionLog transactionLog, OnOkButtonClickListener onOkButtonClickListener) {
    super(context);
    this.context = context;
    this.transactionLog = transactionLog;
    this.onOkButtonClickListener = onOkButtonClickListener;
  }

  @Override
  protected void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    setContentView(R.layout.trans_detail_dialog);
    okButton = findViewById(R.id.ok_dialog_btn);

    okButton.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        onOkButtonClickListener.onOkBtnClick();
      }
    });

  }

  public interface OnOkButtonClickListener {

    void onOkBtnClick();
  }

}
