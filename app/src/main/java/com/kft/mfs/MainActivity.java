package com.kft.mfs;

import adapter.TransactionLogListAdapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.kft.mfs.dialog.TransactionDetailsDialog.OnOkButtonClickListener;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import data.TransactionLog;
import data.TransactionLogList;
import es.dmoral.toasty.Toasty;

import java.util.List;

public class MainActivity extends BaseActivity {

    /*@BindView(R.id.swipelistView)
    SwipeMenuListView swipeListView;*/

    private List<TransactionLog> mTransLog;
    private TransactionLogListAdapter mTansactionSwipeListAdapter;
    private Drawer result;

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDrawer();

        // set creator
        mTransLog = TransactionLogList.getAllTransactionLog();

        SwipeMenuListView swipeListView = (SwipeMenuListView) findViewById(R.id.swipelistView);
        mTansactionSwipeListAdapter = new TransactionLogListAdapter(getApplicationContext(),mTransLog,null);
        swipeListView.setAdapter(mTansactionSwipeListAdapter);

        // step 1. create a MenuCreator
        //SwipeMenuCreator creator = new SwipeMenuCreator()
        swipeListView.setMenuCreator(creator);

        // step 2. listener item click event
        swipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // Detail
                        showTransactionDetailDialog();
                        break;
                    case 1:
                        // Hide
                        /*mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();*/
                        break;
                }
                return false;
            }
        });
    }

    public void showTransactionDetailDialog(){
        TransactionLog transactionLog = new TransactionLog();
        getDialogManager().showTransactionDetailDialog(transactionLog,
            new OnOkButtonClickListener() {
                @Override
                public void onOkBtnClick() {
                    dialogManager.dismissDialog();
                }
            });
    }



    // Swipe List
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem detailsItem = new SwipeMenuItem(getApplicationContext());
            detailsItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
            detailsItem.setWidth(dp2px(90));
            detailsItem.setTitle("Details");
            detailsItem.setTitleSize(18);
            detailsItem.setTitleColor(Color.BLACK);
            menu.addMenuItem(detailsItem);

            SwipeMenuItem hideItem = new SwipeMenuItem(getApplicationContext());
            hideItem.setBackground(new ColorDrawable(Color.rgb(0xCE, 0xCE,0xCE)));
            hideItem.setWidth(dp2px(90));
            hideItem.setTitle("Hide");
            hideItem.setTitleSize(18);
            hideItem.setTitleColor(Color.BLACK);
            menu.addMenuItem(hideItem);
            // set a icon
            //deleteItem.setIcon(R.drawable.ic_launcher_foreground);
            // add to menu

        }
    };


    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            this.getResources().getDisplayMetrics());
    }

    public void createDrawer(){
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.color.colorPrimary)//R.drawable.header
            .addProfiles(
                new ProfileDrawerItem().withName("Md Abul Kalam").withEmail("abul.kamal@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
            )
            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                    return false;
                }
            })
            .build();

        // Create Drawable 0x7f0500cf
        Drawable home = ContextCompat.getDrawable(this,R.drawable.ic_menu_home);
        home.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable addAcc = ContextCompat.getDrawable(this,R.drawable.ic_menu_addac);
        addAcc.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable expenceRpt = ContextCompat.getDrawable(this, R.drawable.ic_menu_charts_pie);
        expenceRpt.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable purchase = ContextCompat.getDrawable(this, R.drawable.ic_menu_debts);
        purchase.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable log = ContextCompat.getDrawable(this, R.drawable.ic_menu_reports);
        log.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable qr = ContextCompat.getDrawable(this, R.drawable.ic_menu_my_qr);
        qr.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable settings = ContextCompat.getDrawable(this, R.drawable.ic_menu_settings);
        settings.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        Drawable logout = ContextCompat.getDrawable(this, R.drawable.md_nav_back);
        logout.setColorFilter(new PorterDuffColorFilter(0x7f0500cf, PorterDuff.Mode.SRC_IN));

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_acc).withIcon(addAcc);
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create the drawer and remember the `Drawer` result object

        result = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withHasStableIds(true)
            .withItemAnimator(new AlphaCrossFadeAnimator())
            .withShowDrawerOnFirstLaunch(true)
            .withAccountHeader(headerResult)
            .addDrawerItems(
                item1,
                //new DividerDrawerItem(),
                item2,
                new SecondaryDrawerItem().withName(R.string.drawer_item_report).withIcon(expenceRpt),
                new SecondaryDrawerItem().withName(R.string.drawer_item_purchase).withIcon(purchase),
                new SecondaryDrawerItem().withName(R.string.drawer_item_transaction_log).withIcon(log),
                new SecondaryDrawerItem().withName(R.string.drawer_item_my_qr_code).withIcon(qr),
                new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(settings),
                //new SecondaryDrawerItem().withName(R.string.drawer_item_finger_print),
                new SecondaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(logout)
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    // do something with the clicked item :D

                    switch(position){
                        case 1:
                            closeDrawer();
                            break ;
                        case 2: // Add Account
                            Intent receipt = new Intent(getApplicationContext(), AddAccountActivity.class);
                            startActivity(receipt);
                            break ;
                        case 3: // Report Activity
                            Intent reportPiePolyIntent = new Intent(getApplicationContext(), ReportPiePolyActivity.class);
                            startActivity(reportPiePolyIntent);
                            break ;
                        case 4: // Scanner Activity
                            launchActivity(ScannerActivity.class);
                            break ;
                        case 5: // Transaction Log
                            Intent transactionLogIntent = new Intent(getApplicationContext(), TransactionLogActivity.class);
                            startActivity(transactionLogIntent);
                            break ;
                        case 6: // My QR
                            Intent myQrIntent = new Intent(getApplicationContext(), MyQrActivity.class);
                            startActivity(myQrIntent);
                            break ;
                        case 7: // Settings
                            Toasty.info(getApplicationContext(),"Under Development", Toast.LENGTH_LONG).show();
                            break ;
                        case 8: // Logout
                            finish();
                            break ;
                        case 9:
                            finish();
                            break ;
                        default:
                            closeDrawer();
                            break ;
                    }
                    return true;
                }
            })
            .build();
    }

    private void closeDrawer(){
        result.closeDrawer();
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


}

