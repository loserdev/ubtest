package com.kft.mfs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.kft.mfs.dialog.DoubleButtonCustomDialog;
import com.kft.mfs.dialog.TransactionDetailsDialog.OnOkButtonClickListener;
import com.kft.mfs.viewpage.CommonFragment;
import com.kft.mfs.viewpage.CustPagerTransformer;
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
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;
import java.util.List;

import adapter.TransactionLogListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import data.TransactionLog;
import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity implements View.OnClickListener {

   @BindView(R.id.main_card_pay_iv)
    ImageView payIv;

    private List<TransactionLog> mTransLog;
    private TransactionLogListAdapter mTansactionSwipeListAdapter;
    private Drawer result;

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    //String[] web = { "Sopno","Agora", "Pathao", "Uber", "MinaBazar","7 Eleven"};
    //int[] imageId = { R.drawable.gift4,R.drawable.gift3,R.drawable.gift3,R.drawable.gift4,R.drawable.gift4,R.drawable.gift3};
    private ViewPager viewPager;
    private List<CommonFragment> fragments = new ArrayList<>(); // 供ViewPager使用
    private final String[] imageArray = {"assets://coupon_2.jpeg", "assets://coupon_3.jpeg", "assets://coupon_4.jpeg", "assets://coupon1.jpeg", "assets://coupon_5.jpeg"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createDrawer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 2. 初始化ImageLoader
        initImageLoader();

        // 3. 填充ViewPager
        fillViewPager();

        payIv.setOnClickListener(this);


        /*CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);

        dashboardGrid.setAdapter(adapter);
        dashboardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                showInfoToast("25% Discount for purchase in "+web[+ position]);

                //getDialogManager().showAppExitDialog(MainActivity.this,R.string.app_name);
            }
        });*/

    }

    /**
     * 填充ViewPager
     */
    private void fillViewPager() {
        //indicatorTv = (TextView) findViewById(R.id.indicator_tv);
        viewPager = (ViewPager) findViewById(R.id.coupon_viewpager);

        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        viewPager.setPageTransformer(false, new CustPagerTransformer(this));

        // 2. viewPager添加adapter
        for (int i = 0; i < 10; i++) {
            // 预先准备10个fragment
            fragments.add(new CommonFragment());
        }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                CommonFragment fragment = fragments.get(position % 10);
                fragment.bindData(imageArray[position % imageArray.length]);
                return fragment;
            }

            @Override
            public int getCount() {
                return 666;
            }
        });


        // 3. viewPager滑动时，调整指示器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorTv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateIndicatorTv();
    }

    /**
     * 更新指示器
     */
    private void updateIndicatorTv() {
        int totalNum = viewPager.getAdapter().getCount();
        int currentItem = viewPager.getCurrentItem() + 1;
        //indicatorTv.setText(Html.fromHtml("<font color='#12edf0'>" + currentItem + "</font>  /  " + totalNum));
    }

    @SuppressWarnings("deprecation")
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024) // 缓冲大小
                .discCacheFileCount(100) // 缓冲文件数目
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.单例ImageLoader类的初始化
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_card_pay_iv:
                launchActivity(ScannerActivity.class);
                return;
            default:
                return;
        }
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
                            onBackPressed();

                            break ;
                        case 9:
                            onBackPressed();
                            //finish();
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

    @Override
    public void onBackPressed() {
        getDialogManager().showDoubleButtonCustomDialog(getString(R.string.logout_confirmation_title), getString(R.string.logout_confirmation),
                R.string.logout,
                new DoubleButtonCustomDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClick(DoubleButtonCustomDialog dialog) {
                        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        //finishAffinity();
                        finish();
                    }
                },
                R.string.cancel,
                new DoubleButtonCustomDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClick(DoubleButtonCustomDialog dialog) {
                        dismissDialog();
                    }
                });
    }

}

