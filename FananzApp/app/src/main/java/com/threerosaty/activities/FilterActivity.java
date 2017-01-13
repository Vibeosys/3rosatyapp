package com.threerosaty.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.threerosaty.MainActivity;
import com.threerosaty.R;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SELECTED_PRICE = "selectedPrice";
    public static final String SELECTED_SORT = "selectedSort";
    private Spinner spnSort;
    private int sortId = 0;
    private SeekBar mSeekBarPrice;
    private TextView txtMinPrice, txtMaxPrice, filterPriceVal;
    private int mSeekBarPriceStep = 1000, mSeekBarPriceMax = 100000, mSeekBarPriceMin = 1000;
    private int selectedPrice = 1000;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle(getString(R.string.str_filter));
        spnSort = (Spinner) findViewById(R.id.spnSort);
        mSeekBarPrice = (SeekBar) findViewById(R.id.skb_price);
        txtMinPrice = (TextView) findViewById(R.id.txtMinPrice);
        txtMaxPrice = (TextView) findViewById(R.id.txtMaxPrice);
        filterPriceVal = (TextView) findViewById(R.id.filterPriceVal);
        btnApply = (Button) findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(this);
        List<String> spinnerData = new ArrayList<>();
        spinnerData.add(getResources().getString(R.string.str_low_to_high));
        spinnerData.add(getResources().getString(R.string.str_high_to_low));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getBaseContext(), android.R.layout.simple_list_item_1, spinnerData);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spnSort.setAdapter(dataAdapter);
        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sortId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSeekBarPrice.setMax((mSeekBarPriceMax - mSeekBarPriceMin) / mSeekBarPriceStep);
        mSeekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedPrice = mSeekBarPriceMin + (progress * mSeekBarPriceStep);
                filterPriceVal.setText(getResources().getString(R.string.str_aed) + " " + selectedPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_PRICE, 0);
        intent.putExtra(SELECTED_SORT, 0);
        setResult(MainActivity.FILTER_RESULT, intent);
        finish();//finishing activity
        //super.onBackPressed();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_apply:
                Intent intent = new Intent();
                intent.putExtra(SELECTED_PRICE, selectedPrice);
                intent.putExtra(SELECTED_SORT, sortId);
                setResult(MainActivity.FILTER_RESULT, intent);
                finish();//finishing activity
                break;
        }
    }
}
