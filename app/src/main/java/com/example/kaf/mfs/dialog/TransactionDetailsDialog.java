package com.kft.mfs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kft.mfs.R;
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
    this.setCancelable(true);
    this.setTitle("Transaction Detail");

    ImageView v= (ImageView) findViewById(R.id.broken_arrow);

    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_trans_det_btm);
    BitmapDrawable tile = new BitmapDrawable(context.getResources(),bitmap);


    int width = v.getWidth();
    int height = tile.getIntrinsicHeight();
    Rect bounds = new Rect(0,0,width,height);
    tile.setTileModeX(Shader.TileMode.REPEAT);
    tile.setBounds(bounds);
    v.setBackground(tile);

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
