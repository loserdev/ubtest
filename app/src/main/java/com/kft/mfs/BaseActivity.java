package com.kft.mfs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Toast;
import com.kft.mfs.dialog.DialogManager;
import es.dmoral.toasty.Toasty;

/**
 * Created by apple on 13/03/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected DialogManager dialogManager;

    protected DialogManager getDialogManager(){
        if(dialogManager == null){
            dialogManager = new com.kft.mfs.dialog.DialogManager(this);
        }
        return dialogManager;
    }


    public void dismissDialog() {
        if (dialogManager != null) {
            dialogManager.dismissDialog();
        }
    }

    public void showWarningToast(String msg){
        Toasty.warning(this,msg, Toast.LENGTH_LONG).show();
    }

    public void showSuccessToast(String msg){
        Toasty.success(this,msg, Toast.LENGTH_LONG).show();
    }

    public void showFailedToast(String msg){
        Toasty.error(this,msg, Toast.LENGTH_LONG).show();
    }

    public void showInfoToast(String msg){
        Toasty.info(this,msg, Toast.LENGTH_LONG).show();
    }
}
