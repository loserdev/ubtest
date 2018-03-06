package com.example.kaf.mattest;

import adapter.SwipeListItemAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import butterknife.BindView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import data.TransactionLog;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*@BindView(R.id.swipelistView)
    SwipeMenuListView swipeListView;*/

    private List<TransactionLog> mTransLog;
    private SwipeListItemAdapter mTansactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDrawer();
        // set creator
        mTransLog = getTransactionLog();

        SwipeMenuListView swipeListView = (SwipeMenuListView) findViewById(R.id.swipelistView);
        mTansactionAdapter = new SwipeListItemAdapter(mTransLog,getApplicationContext());
        swipeListView.setAdapter(mTansactionAdapter);

        // step 1. create a MenuCreator
        //SwipeMenuCreator creator = new SwipeMenuCreator()
        swipeListView.setMenuCreator(creator);
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
            hideItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
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
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                new ProfileDrawerItem().withName("Mr. han").withEmail("han@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
            )
            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                    return false;
                }
            })
            .build();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_acc);
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withHasStableIds(true)
            .withItemAnimator(new AlphaCrossFadeAnimator())
            .withShowDrawerOnFirstLaunch(true)
            .withAccountHeader(headerResult)
            .addDrawerItems(
                item1,
                new DividerDrawerItem(),
                item2,
                new SecondaryDrawerItem().withName(R.string.drawer_item_purchase),
                new SecondaryDrawerItem().withName(R.string.drawer_item_bill_pay),
                new SecondaryDrawerItem().withName(R.string.drawer_item_my_qr_code),
                new SecondaryDrawerItem().withName(R.string.drawer_item_settings),
                new SecondaryDrawerItem().withName(R.string.drawer_item_finger_print),
                new SecondaryDrawerItem().withName(R.string.drawer_item_logout)
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    // do something with the clicked item :D
                    return true;
                }
            })
            .build();
    }


    private List<TransactionLog> getTransactionLog(){
        List<TransactionLog> tempTransLogList = new ArrayList<TransactionLog>();
        TransactionLog tempTransLog = new TransactionLog();
        tempTransLog.setAmount(10);
        tempTransLog.setDate("12 Jan 2018");
        tempTransLog.setMerchantName("Agora");
        tempTransLogList.add(tempTransLog);

        tempTransLog = new TransactionLog();
        tempTransLog.setAmount(50);
        tempTransLog.setDate("14 Feb 2018");
        tempTransLog.setMerchantName("Agora");
        tempTransLogList.add(tempTransLog);

        tempTransLog = new TransactionLog();
        tempTransLog.setAmount(800);
        tempTransLog.setDate("10 Feb 2018");
        tempTransLog.setMerchantName("Mena");
        tempTransLogList.add(tempTransLog);

        tempTransLog = new TransactionLog();
        tempTransLog.setAmount(90);
        tempTransLog.setDate("15 March 2018");
        tempTransLog.setMerchantName("Shoopers World");
        tempTransLogList.add(tempTransLog);

        tempTransLog = new TransactionLog();
        tempTransLog.setAmount(350);
        tempTransLog.setDate("29 Jan 2018");
        tempTransLog.setMerchantName("Shopno");
        tempTransLogList.add(tempTransLog);
        return tempTransLogList;
    }
}

