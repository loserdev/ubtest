package com.kft.mfs;

import adapter.TransactionLogListAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import data.TransactionLogList;


public class TransactionLogActivity extends AppCompatActivity {


  private TransactionLogListAdapter mTransactionLogAdapter;
  private Animator spruceAnimator;
  ListView transactionLogListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_log);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    transactionLogListView = findViewById(R.id.transaction_list_view);
    // Create the animator after the list view has finished laying out
    transactionLogListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        initSpruce();
      }
    });

    mTransactionLogAdapter = new TransactionLogListAdapter(TransactionLogList.getAllTransactionLog(),getApplicationContext());
    transactionLogListView.setAdapter(mTransactionLogAdapter);

    /*transactionLogListView.setDivider(null);
    transactionLogListView.setDividerHeight(0);*/

  }

  public void initSpruce(){
    spruceAnimator = new Spruce.SpruceBuilder(transactionLogListView)
        .sortWith(new DefaultSort(100))
        .animateWith(DefaultAnimations.shrinkAnimator(transactionLogListView, 800),
            ObjectAnimator.ofFloat(transactionLogListView, "translationX", -transactionLogListView.getWidth(), 0f).setDuration(800))
        .start();
  }


  @Override
  public void onResume() {
    super.onResume();
    if (spruceAnimator != null) {
      spruceAnimator.start();
    }
  }
}
