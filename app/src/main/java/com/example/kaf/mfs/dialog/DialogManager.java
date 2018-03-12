package com.kft.mfs.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import com.kft.mfs.R;
import data.TransactionLog;

public class DialogManager {

  private final String TAG = DialogManager.class.getSimpleName();
  private Context context;
  private Dialog dialog;
  //private CustomProgressDialog progressDialog;

  public DialogManager(Context context) {
    this.context = context;
  }

  DialogInterface.OnKeyListener backKeyListener = new DialogInterface.OnKeyListener() {
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
      return keyCode == KeyEvent.KEYCODE_BACK;
    }
  };

  private synchronized void showDialog(Dialog dialog) {
    if (this.dialog != null && this.dialog.isShowing()) {
      this.dialog.dismiss();
    }

    this.dialog = dialog;
    this.dialog.setCancelable(false);
    this.dialog.show();
  }

  public synchronized void dismissDialog() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }


  //Simply finishes current activity, it will be appropriate to invoke when this is the only activity in the stack
  public void showAppExitDialog(final Activity activity, int messageId) {

    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage(messageId)
        .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            activity.finishAffinity();
          }
        })
        .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

    AlertDialog dialog = builder.create();
    dialog.setOnKeyListener(backKeyListener);
    this.showDialog(dialog);
  }


  //shows a two button dialog with default layout, listeners are to be implemented from where it is invoked
  public void showDoubleButtonSimpleDialog(int titleTextId, String messageText,
      int positiveBtnTextId, DialogInterface.OnClickListener positiveBtnListener,
      int negativeBtnTextId, DialogInterface.OnClickListener negativeBtnListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    AlertDialog dialog = builder.setTitle(titleTextId)
        .setMessage(messageText)
        .setPositiveButton(positiveBtnTextId, positiveBtnListener)
        .setNegativeButton(negativeBtnTextId, negativeBtnListener)
        .create();
    this.showDialog(dialog);
  }

  public void showDoubleButtonSimpleDialog(String messageText,
      int positiveBtnTextId, DialogInterface.OnClickListener positiveBtnListener,
      int negativeBtnTextId, DialogInterface.OnClickListener negativeBtnListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    AlertDialog dialog = builder.setMessage(messageText)
        .setPositiveButton(positiveBtnTextId, positiveBtnListener)
        .setNegativeButton(negativeBtnTextId, negativeBtnListener)
        .create();
    this.showDialog(dialog);
  }


  public void showTransactionDetailDialog(TransactionLog transactionLog,TransactionDetailsDialog.OnOkButtonClickListener btnClickListener) {
    TransactionDetailsDialog transactionSuccessDialog = new TransactionDetailsDialog(context, transactionLog, btnClickListener);
    this.showDialog(transactionSuccessDialog);
  }


  public static void showNoInternet(final Activity activity, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            activity.finish();
          }
        });
    AlertDialog alert = builder.create();
    alert.show();
  }


  public void showInformationDialog(final Activity activity, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
    AlertDialog alert = builder.create();
    alert.show();
  }

}
