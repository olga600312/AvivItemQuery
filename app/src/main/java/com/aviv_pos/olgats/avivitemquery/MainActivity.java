package com.aviv_pos.olgats.avivitemquery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aviv_pos.olgats.avivitemquery.acync.ItemSearchTask;
import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler.Settings settings=new DatabaseHandler.Settings(this);
        settings.replaceValue("filtered", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        embeddedScanBar();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent!=null&&"com.google.zxing.client.android.SCAN".equalsIgnoreCase(intent.getAction())) {
            IntentResult scan = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scan != null) {
                String contents = scan.getContents();
                if (contents != null) {
                    String format = scan.getFormatName();
                    Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG).show();
                    getContent(contents);
                }
            }
        }
    }

    private void showEmptySearchResult() {
        android.support.v7.app.AlertDialog ad = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage(getString(R.string.alertClientConnectionError)).show();
    }

    private void getContent(String code) {
        DatabaseHandler.Settings settings = new DatabaseHandler.Settings(this);
        settings.setValue("filtered", 1);
        settings.setValue(WSConstants.SETTINGS_BARCODE, code);
    }


    public void embeddedScanBar() {
        try {
            // Check that the device will let you use the camera
            PackageManager pm = getPackageManager();

            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setCaptureActivity(ScannerActivity.class);
                integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt(getString(R.string.scan_barcode));
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            } else {
                showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


}
