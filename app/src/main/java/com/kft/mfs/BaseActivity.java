package com.kft.mfs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kft.mfs.dialog.DialogManager;

/**
 * Created by apple on 13/03/2018.
 */

public class BaseActivity extends AppCompatActivity {
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
}
