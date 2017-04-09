package com.aviv_pos.olgats.avivitemquery;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aviv_pos.olgats.avivitemquery.acync.InitTask;
import com.aviv_pos.olgats.avivitemquery.acync.ItemSearchTask;
import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;
import com.aviv_pos.olgats.avivitemquery.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class ScannerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ScannerFragment.OnFragmentInteractionListener, ItemListFragment.OnFragmentInteractionListener {
    private static final String TAG = "ScannerActivity";
    private static final String INIT_ACTION_FOR_INTENT_CALLBACK = "INIT_ACTION_FOR_INTENT_CALLBACK";
    private static final String SEARCH_ACTION_FOR_INTENT_CALLBACK = "SEARCH_ACTION_FOR_INTENT_CALLBACK";
    public static final String SCAN_ACTION_FOR_INTENT_CALLBACK = "SCAN_ACTION_FOR_INTENT_CALLBACK";

    private final static int SCAN_BAR = 1;
    private final static int SCAN_QR = 2;
    private static final int TAB_RESULTS = 2;
    private static final int TAB_SEARCH = 1;
    private static final int TAB_SCAN = 0;
    public static final String CURRENT_TAB = "currentTab";

    private DrawerLayout mDrawerLayout;
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public final static String ZFETCH_PARAM_PINTENT = "pendingIntent";
    public final static String ZFETCH_RESULT = "zfetchresult";
    public final static int ZFETCH_STATUS_START = 100;
    public final static int ZFETCH_STATUS_FINISH = 300;
    public final static int ZFETCH_TASK_CODE = 999;

    private ViewPager viewPager;
    private ItemListFragment itemListFragment;
    private boolean zFetchRunning;
    private ScannerFragment scannerFragment;
    private ProgressDialog progress;

    /////////////////// GCM ///////////////////
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mGCMRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);


        //final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        //ab.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int numTab = tab.getPosition();
                        if (scannerFragment != null) {
                            if (numTab == 0) {

                                scannerFragment.resumeCapture();

                            } else {
                                scannerFragment.onPause();
                                itemListFragment.updateList();
                            }
                        }
                    }
                });
        createGCMProcess();
        if (savedInstanceState != null && viewPager != null) {
            int currentTab = savedInstanceState.getInt(CURRENT_TAB);
            if (currentTab > 0 && currentTab < viewPager.getAdapter().getCount()) {
                viewPager.setCurrentItem(currentTab);
            }
        }
    }

    private void createGCMProcess() {
        mGCMRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(WSConstants.GCM_SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.i(TAG, getString(R.string.gcm_send_message));
                } else {
                    Log.i(TAG, getString(R.string.token_error_message));
                }
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(scannerFragment = ScannerFragment.newInstance(SCAN_BAR, SCAN_QR), getString(R.string.search));
        adapter.addFragment(itemListFragment = ItemListFragment.newInstance(), getString(R.string.result));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            // disable going back to the MainActivity
            moveTaskToBack(true);
            finishAffinity();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(INIT_ACTION_FOR_INTENT_CALLBACK));
        registerReceiver(receiver, new IntentFilter(SEARCH_ACTION_FOR_INTENT_CALLBACK));
        registerReceiver(receiver, new IntentFilter(SCAN_ACTION_FOR_INTENT_CALLBACK));
        if (viewPager.getCurrentItem() == TAB_RESULTS) {
            itemListFragment.updateList();
        }
        if (!zFetchRunning) {
            PendingIntent pi;
            Intent intent;

            // Создаем PendingIntent для ZFetchService
            pi = createPendingResult(ZFETCH_TASK_CODE, new Intent(), 0);
            // Создаем Intent для вызова сервиса, кладем туда созданный PendingIntent
           /* intent = new Intent(this, DataFetchService.class).putExtra(ZFETCH_PARAM_PINTENT, pi);
            // стартуем сервис
            startService(intent);*/
        }
        // embeddedScanBar();
        DatabaseHandler.Settings settings = new DatabaseHandler.Settings(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        if ("com.google.zxing.client.android.SCAN".equalsIgnoreCase(action)) {
            String str = settings.getValue(WSConstants.SETTINGS_BARCODE);
            settings.replaceValue(WSConstants.SETTINGS_BARCODE, null);
            if (str != null && str.trim().length() >= 3) {
                ItemSearchCriteria c = new ItemSearchCriteria();
                c.setCode(str);
                getContent(c);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(CURRENT_TAB, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private boolean validate(EditText editText, int limit) {
        int l = editText.getText().toString().trim().length();
        boolean empty = l > 0 && l < limit;
        if (empty) {
            Snackbar.make(editText, getString(R.string.invalid_character_count), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            editText.requestFocus();
            editText.selectAll();
        }
        return !empty;
    }


    private void showItem(String code) {
        Intent intentItem = new Intent(this, ItemActivity.class);
        intentItem.putExtra("itemCode", code);
        startActivity(intentItem);
    }


    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scan = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scan != null) {
            String contents = scan.getContents();
            if (contents != null) {
                String format = scan.getFormatName();
                Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG).show();
                ItemSearchCriteria c = new ItemSearchCriteria();
                c.setCode(contents);
                getContent(c);
            }
        }
    }

    private void getContent(ItemSearchCriteria c) {
        // the request
        if (c != null) {
            try {
                final ItemSearchTask task = new ItemSearchTask(this, SEARCH_ACTION_FOR_INTENT_CALLBACK);
                task.execute(c);
                progress = new ProgressDialog(this);
                progress.setTitle(getString(R.string.loading));
                progress.setIndeterminate(true);
                progress.setMessage(getString(R.string.waiting_for_result));
                progress.setCancelable(false);
                progress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        task.cancel(true);
                    }
                });
                progress.show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }


    public void setProgressMessage(String msg) {
        if (progress != null) {
            progress.setMessage(msg);
        }
    }

    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // clear the progress indicator
            if (progress != null) {
                progress.dismiss();
            }
            int response = intent.getIntExtra(ItemSearchTask.RESPONSE, -1);
            switch (response) {
                case WSConstants.SUCCESS:
                    int size = intent.getIntExtra(ItemSearchTask.RESPONSE_SIZE, 0);
                    switch (size) {
                        case 0:
                            android.support.v7.app.AlertDialog ad = new AlertDialog.Builder(ScannerActivity.this).setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).setMessage(getString(R.string.alertItemNotFind)).show();
                            break;
                        case 1:
                            String code = intent.getStringExtra(ItemSearchTask.RESPONSE_FIRST);
                            if (code != null) {
                                showItem(code);
                            }
                            break;
                        default:
                            //showItemList(size);
                            itemListFragment.updateList();
                            viewPager.setCurrentItem(TAB_RESULTS, true);
                            Toast.makeText(ScannerActivity.this, "Item list:" + size, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case WSConstants.CLIENT_CONNECTION_ERROR:
                    android.support.v7.app.AlertDialog ad = new AlertDialog.Builder(ScannerActivity.this).setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setMessage(getString(R.string.alertClientConnectionError)).show();
                    break;
                case WSConstants.PRIVATE_WS_NOT_REACHABLE:
                    ad = new AlertDialog.Builder(ScannerActivity.this).setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setMessage(getString(R.string.alertPrivateWSConnectionError)).show();
                    break;
                case WSConstants.SESSION_CLOSED:
                    onSessionClose();
                    break;

            }
        }
    };


    private void showItemList(int size) {
        Toast.makeText(this, "Item list:" + size, Toast.LENGTH_SHORT).show();
       /* Intent intentItem = new Intent(this, ItemListActivity.class);
        intentItem.putExtra("itemCode", code);
        startActivity(intentItem);*/
    }

    private void onSessionClose() {
        new AlertDialog.Builder(this).setPositiveButton(getString(R.string.login),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ScannerActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton(getString(R.string.exit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                }).setMessage(getString(R.string.alertSessionClosed)).show();
    }

    @Override
    public void onItemListFragmentInteraction(String code) {
        showItem(code);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_status) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            i.putExtra(Intent.EXTRA_TEXT, "body of email");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onScanerFragmentInteraction(ItemSearchCriteria criteria) {
        getContent(criteria);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                // finish();
            }
            return false;
        }
        return true;
    }
}
