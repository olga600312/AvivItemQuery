package com.aviv_pos.olgats.avivitemquery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.aviv_pos.olgats.avivitemquery.acync.TransactionDataTask;
import com.aviv_pos.olgats.avivitemquery.beans.Result;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.internal.LinkedTreeMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;

import lecho.lib.hellocharts.model.PointValue;

public class ItemChartActivity extends AppCompatActivity implements OnChartValueSelectedListener, TransactionDataTask.Listener {
    private static final String TAG = "ItemChartActivity";
    private LineChart chart;
    private ProgressDialog progress;
    private TextView tvFromDate;
    private TextView tvToDate;
    private String currentCode;
    private long fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_chart);
        chart = (LineChart) findViewById(R.id.salesChart);
        initChart(chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemChartToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvFromDate = (TextView) findViewById(R.id.tvFromDate);
        tvToDate = (TextView) findViewById(R.id.tvToDate);
        TextView lblFromDate = (TextView) findViewById(R.id.lblFromDate);
        TextView lblToDate = (TextView) findViewById(R.id.lblToDate);


        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar c = new GregorianCalendar();
                c.setTimeInMillis(fromDate);
                DialogFragment newFragment = new DatePickerFragment(c) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        GregorianCalendar gc = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        fromDate = gc.getTimeInMillis();
                        tvFromDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));
                    }
                };
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        };
        tvFromDate.setOnClickListener(l);
        lblFromDate.setOnClickListener(l);

        View.OnClickListener l1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar c = new GregorianCalendar();
                c.setTimeInMillis(toDate);
                DialogFragment newFragment = new DatePickerFragment(c) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        GregorianCalendar gc = new GregorianCalendar(year, monthOfYear, dayOfMonth, 23, 59, 59);
                        toDate = gc.getTimeInMillis();
                        tvToDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));
                    }
                };
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        };
        tvToDate.setOnClickListener(l1);
        lblToDate.setOnClickListener(l1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabChart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideKeyboard(view);
                if (currentCode != null) {
                    showChart(currentCode);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        currentCode = intent.getStringExtra("itemCode");
        GregorianCalendar gc = new GregorianCalendar();
        if (toDate > 0) {
            gc.setTimeInMillis(toDate);
        }
        tvToDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));
        toDate = gc.getTime().getTime();
        if (fromDate > 0) {
            gc.setTimeInMillis(fromDate);
        } else {
            int month = gc.get(Calendar.MONTH);
            int day = gc.get(Calendar.DAY_OF_MONTH);
            if (day < 10) {
                month = Math.max(0, month - 1);
            }
            gc.set(Calendar.MONTH, month);
            gc.set(Calendar.DAY_OF_MONTH, 1);
            fromDate = gc.getTime().getTime();
        }
        tvFromDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));

        showChart(currentCode);
    }

    private void showChart(String code) {
        if (fromDate > 0 && toDate > 0 && code != null && !code.isEmpty()) {
            final TransactionDataTask task = new TransactionDataTask(this, this);
            task.execute(new String[]{code, String.valueOf(Math.min(fromDate, toDate)), String.valueOf(Math.max(fromDate, toDate)), "" + WSConstants.TR_SALE});
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
        }
    }


    private void initChart(LineChart lineChart) {
        lineChart.setOnChartValueSelectedListener(this);

        // no description text
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        lineChart.setData(data);

        // Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XAxis xl = lineChart.getXAxis();
        //xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i(TAG, "Entry selected: " + e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "Nothing selected.");
    }


    @Override
    public void onResult(Result result, boolean isCanceled) {
        createChartData(result);
        progress.dismiss();
       // feedMultiple();
    }

    private void createChartData(Result result) {
        LineData data = chart.getLineData();
        if (data == null) {
            data = new LineData();
            data.setValueTextColor(Color.WHITE);
            // add empty data
            chart.setData(data);
        }
        data.clearValues();
        chart.invalidate();
        for (Object o : result.getData()) {
            LinkedTreeMap p = (LinkedTreeMap) o;
            addEntry(p);
        }
    }


    private void addEntry(LinkedTreeMap p) {

        LineData data = chart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            int x = (int) Float.parseFloat(p.get("x").toString());
            float y = Float.parseFloat(p.get("y").toString());
            String str = (String) p.get("lable");

            // add a new x-value first
            data.addXValue(str);
            data.addEntry(new Entry(y, set.getEntryCount()), 0);


            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getXValCount() - 121);

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private abstract class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private Calendar calendar;

        public DatePickerFragment(Calendar calendar) {
            this.calendar = calendar;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }

    private void feedMultiple() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    final Random r = new Random(System.currentTimeMillis());
                    final int finalI = i;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            LinkedTreeMap m = new LinkedTreeMap();
                            m.put("x",  finalI+200);
                            m.put("y", r.nextFloat()*10f);
                            m.put("lable", "label " + finalI);
                            addEntry(m);
                        }
                    });

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
