package com.aviv_pos.olgats.avivitemquery;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.aviv_pos.olgats.avivitemquery.acync.ItemSaveTask;
import com.aviv_pos.olgats.avivitemquery.beans.Item;
import com.aviv_pos.olgats.avivitemquery.dao.DAOHelper;
import com.aviv_pos.olgats.avivitemquery.dao.DatabaseHandler;

import java.util.Hashtable;

public class ItemUpdateActivity extends AppCompatActivity {
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE_WITH_ITEMSAVETASK";
    private EditText tvCode, tvName, tvPrice, tvCostBrutto, tvCostNetto, tvVat, tvProfit, tvSupplierDiscount, tvItemDiscount, tvBonusBase, tvBonusCount, tvBonusDiscount;
    private CheckBox cbWeight, cbDiscountable;
    private Item currentItem;
    private Spinner spGroup;
    private Spinner spDepartment;
    private Spinner spUnit;
    private Spinner spSupplier;

    private ExtraCursorAdapter<? extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> departmentAdapter;
    private ExtraCursorAdapter<? extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> groupAdapter;
    private ExtraCursorAdapter<? extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> unitAdapter;
    private ExtraCursorAdapter<? extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> supplierAdapter;


    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Save the current item", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Utilities.hideKeyboard(view);
                updateItem();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvCode = (EditText) findViewById(R.id.textBarcode);
        tvName = (EditText) findViewById(R.id.textName);
        tvPrice = (EditText) findViewById(R.id.textPrice);
        cbWeight = (CheckBox) findViewById(R.id.cbWeight);
        cbDiscountable = (CheckBox) findViewById(R.id.cbDiscountable);
        tvCostBrutto = (EditText) findViewById(R.id.textCostBrutto);
        tvCostNetto = (EditText) findViewById(R.id.textCostNetto);
        tvVat = (EditText) findViewById(R.id.textVat);
        tvProfit = (EditText) findViewById(R.id.textProfit);
        tvSupplierDiscount = (EditText) findViewById(R.id.textSupplierDiscount);
        tvItemDiscount = (EditText) findViewById(R.id.textItemDiscount);
        tvBonusBase = (EditText) findViewById(R.id.textBonusBase);
        tvBonusCount = (EditText) findViewById(R.id.textBonusCount);
        tvBonusDiscount = (EditText) findViewById(R.id.textBonusDiscount);


        spDepartment = (Spinner) findViewById(R.id.spDepartment);
        departmentAdapter = initSpinner(new DatabaseHandler.Departments(this));
        spDepartment.setAdapter(departmentAdapter);

        spGroup = (Spinner) findViewById(R.id.spGroup);
        groupAdapter = initSpinner(new DatabaseHandler.Groups(this));
        spGroup.setAdapter(groupAdapter);

        spUnit = (Spinner) findViewById(R.id.spUnit);
        unitAdapter = initSpinner(new DatabaseHandler.Units(this));
        spUnit.setAdapter(unitAdapter);

        spSupplier = (Spinner) findViewById(R.id.spSupplier);
        supplierAdapter = initSpinner(new DatabaseHandler.Suppliers(this));
        spSupplier.setAdapter(supplierAdapter);

    }


    private ExtraCursorAdapter<? extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> initSpinner(DatabaseHandler.Master<? extends DAOHelper.MASTER> master) {
        // Columns from DB to map into the view file
        String[] fromColumns = {master.getHelper().NAME};
        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };

        Cursor cursor = master.getRawCursor();

        ExtraCursorAdapter<? extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> adapter = new ExtraCursorAdapter<>(master,
                this, // context
                android.R.layout.simple_spinner_item, // layout file
                cursor, // DB cursor
                fromColumns, // data to bind to the UI
                toViews, // views that'll represent the data from `fromColumns`
                0
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create the list view and bind the adapter
        return adapter;
    }

    private class ExtraCursorAdapter<T extends DatabaseHandler.Master<? extends DAOHelper.MASTER>> extends SimpleCursorAdapter {
        private T t;

        public ExtraCursorAdapter(T t, Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.t = t;
        }

        public int getRowId(Integer key) {
            int pos = -1;
            if (key != null) {
                int count = this.getCount();
                int i = 0;
                Cursor c = getCursor();
                int idColumn = c.getColumnIndex(t.getHelper().ID);
                for (; i < count; i++) {
                    if (c.moveToPosition(i)) {
                        if (key.equals(c.getInt(idColumn))) {
                            return i;
                        }
                    }
                }
            }
            return pos;
        }

        public int keyAtPosition(int position) {
            Cursor c = getCursor();
            int res = -1;
            int idColumn = c.getColumnIndex(t.getHelper().ID);
            if (c.moveToPosition(position)) {
                res = c.getInt(idColumn);
            }
            return res;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String code = intent.getStringExtra("itemCode");
        if (code != null) {
            getContent(code);
        }

    }

    private void getContent(String code) {
        DatabaseHandler.Items items = new DatabaseHandler.Items(this);
        Item item = items.retrieveItem(code, true);
        if (item != null) {
            tvCode.setText(item.getCode());
            tvName.setText(item.getName());
            tvPrice.setText(String.format("%.2f", item.getPrice()));
            cbWeight.setChecked(item.isWeightable());

            String extra = (String) item.getExtra().get("department");
            int pos = extra != null ? departmentAdapter.getRowId(Integer.parseInt(extra)) : -1;
            spDepartment.setSelection(pos >= 0 ? pos : 0);

            extra = (String) item.getExtra().get("group");
            pos = extra != null ? groupAdapter.getRowId(Integer.parseInt(extra)) : -1;
            spGroup.setSelection(pos >= 0 ? pos : 0);

            extra = (String) item.getExtra().get("unit");
            pos = extra != null ? unitAdapter.getRowId(Integer.parseInt(extra)) : -1;
            spUnit.setSelection(pos >= 0 ? pos : 0);

            extra = (String) item.getExtra().get("supplier");
            pos = extra != null ? supplierAdapter.getRowId(Integer.parseInt(extra)) : -1;
            spSupplier.setSelection(pos >= 0 ? pos : 0);


            extra = (String) item.getExtra().get("cost_brutto");
            tvCostBrutto.setText(extra);

            extra = (String) item.getExtra().get("cost_netto");
            tvCostNetto.setText(extra);

            extra = (String) item.getExtra().get("vat_sum");
            tvVat.setText(extra);

            extra = (String) item.getExtra().get("profit");
            tvProfit.setText(extra != null ? extra : "");

            extra = (String) item.getExtra().get("discountSupplier");
            tvSupplierDiscount.setText(extra != null ? extra : "");

            extra = (String) item.getExtra().get("bonus_discount");
            tvBonusDiscount.setText(extra != null ? extra : "");

            extra = (String) item.getExtra().get("discountItem");
            tvItemDiscount.setText(extra != null ? extra : "");


            extra = (String) item.getExtra().get("bonus_count");
            tvBonusCount.setText(extra != null ? extra : "");

            extra = (String) item.getExtra().get("bonus_base");
            tvBonusBase.setText(extra != null ? extra : "");

            extra = (String) item.getExtra().get("discountable");
            cbDiscountable.setChecked("true".equalsIgnoreCase(extra));


        }
        currentItem = item;
    }

    private void updateItem() {
        if (currentItem != null) {
            try {

                currentItem.setName(tvName.getText().toString());
                String str=tvPrice.getText().toString();
                currentItem.setPrice(!str.isEmpty()?Float.parseFloat(str):0f);
                currentItem.setWeightable(cbWeight.isChecked());

                Hashtable<String,Object> extra=currentItem.getExtra();
                if(extra==null){
                    extra=new Hashtable<>();
                    currentItem.setExtra(extra);
                }
                int key=departmentAdapter.keyAtPosition(spDepartment.getSelectedItemPosition());
                if (key >= 0) {
                    extra.put("department", String.valueOf(key));
                }

                key=groupAdapter.keyAtPosition(spGroup.getSelectedItemPosition());
                if (key >= 0) {
                    extra.put("group", String.valueOf(key));
                }

                key=supplierAdapter.keyAtPosition(spSupplier.getSelectedItemPosition());
                if (key >= 0) {
                    extra.put("supplier", String.valueOf(key));
                }

                key=unitAdapter.keyAtPosition(spUnit.getSelectedItemPosition());
                if (key >= 0) {
                    extra.put("unit", String.valueOf(key));
                }

                str=tvCostBrutto.getText().toString();
                extra.put("cost_brutto",!str.isEmpty()?Float.parseFloat(str):0f);



                str=tvSupplierDiscount.getText().toString();
                extra.put("discountSupplier",!str.isEmpty()?Float.parseFloat(str):0f);

                str=tvBonusDiscount.getText().toString();
                extra.put("bonus_discount",!str.isEmpty()?Float.parseFloat(str):0f);

                str=tvItemDiscount.getText().toString();
                extra.put("discountItem",!str.isEmpty()?Float.parseFloat(str):0f);

                str=tvBonusCount.getText().toString();
                extra.put("bonus_count",!str.isEmpty()?Float.parseFloat(str):0f);

                str=tvBonusBase.getText().toString();
                extra.put("bonus_base",!str.isEmpty()?Float.parseFloat(str):0f);


                extra.put("discountable",cbDiscountable.isChecked());



                final ItemSaveTask task = new ItemSaveTask(this, ACTION_FOR_INTENT_CALLBACK);
                task.execute(currentItem);
                progress = new ProgressDialog(this);
                progress.setTitle(getString(R.string.saving));
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
                Log.e(WSConstants.TAG, "Item " + currentItem.getCode(), e);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (currentItem != null) {
            Intent intent = new Intent();
            intent.putExtra("itemCode", currentItem.getCode());
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // clear the progress indicator
            if (progress != null) {
                progress.dismiss();
            }
            int response = intent.getIntExtra(ItemSaveTask.RESPONSE, -1);
            switch (response) {
                case WSConstants.SUCCESS:
                    String code = intent.getStringExtra(ItemSaveTask.RESPONSE_FIRST);
                    // update the item info from DB
                    getContent(code);
                    break;
                case WSConstants.APP_ERROR:
                    android.support.v7.app.AlertDialog ad = new AlertDialog.Builder(ItemUpdateActivity.this).setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setMessage(getString(R.string.alertClientConnectionError)).show();
                    break;

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}
