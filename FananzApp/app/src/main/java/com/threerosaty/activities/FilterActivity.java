package com.threerosaty.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.adapters.CategorySpinnerAdapter;
import com.threerosaty.adapters.SubcategorySpinnerAdapter;
import com.threerosaty.data.responsedata.CategoryResponseDTO;
import com.threerosaty.data.responsedata.SubcategoryDTO;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;


import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    public static final String SELECTED_PRICE = "selectedPrice";
    public static final String SELECTED_SORT = "selectedSort";
    public static final String SELECTED_CATEGORY = "selectedCategory";
    public static final String SELECTED_SUBCATEGORY = "selectedSubcategory";
    private Spinner spnSort;
    private int sortId = 0;
    private SeekBar mSeekBarPrice;
    private TextView txtMinPrice, txtMaxPrice, filterPriceVal, txtSub;
    private int mSeekBarPriceStep = 1000, mSeekBarPriceMax = 100000, mSeekBarPriceMin = 1000;
    private int selectedPrice = 1000;
    private Button btnApply;
    private Spinner spnCategory, spnSubCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private SubcategorySpinnerAdapter subcategorySpinnerAdapter;
    private int categoryId = 0;
    private int subCategoryId = 0;

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
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnSubCategory = (Spinner) findViewById(R.id.spnSubCategory);
        btnApply = (Button) findViewById(R.id.btn_apply);
        txtSub = (TextView) findViewById(R.id.txtSub);
        btnApply.setOnClickListener(this);
        List<String> spinnerData = new ArrayList<>();
        spinnerData.add(getResources().getString(R.string.str_low_to_high));
        spinnerData.add(getResources().getString(R.string.str_high_to_low));
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
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
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_CATEGORY_TOKEN, mSessionManager.getCategoryListUrl());
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
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                CategoryResponseDTO categoryResponseDTO = (CategoryResponseDTO) categorySpinnerAdapter.getItem(position);
                categoryId = categoryResponseDTO.getCategoryId();
                if (categoryResponseDTO.getSubCategories() != null) {
                    categoryResponseDTO.getSubCategories().add(0, new SubcategoryDTO(0, getString(R.string.str_all_subcategories), categoryId));
                    subcategorySpinnerAdapter = new SubcategorySpinnerAdapter(getApplicationContext(),
                            categoryResponseDTO.getSubCategories());
                    spnSubCategory.setAdapter(subcategorySpinnerAdapter);
                    spnSubCategory.setVisibility(View.VISIBLE);
                    txtSub.setVisibility(View.VISIBLE);
                    if (subCategoryId != 0) {
                        spnSubCategory.setSelection(subcategorySpinnerAdapter.getPosition(subCategoryId));
                    }
                } else {
                    spnSubCategory.setVisibility(View.GONE);
                    txtSub.setVisibility(View.GONE);
                    subCategoryId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SubcategoryDTO subcategoryDTO = (SubcategoryDTO) subcategorySpinnerAdapter.getItem(position);
                subCategoryId = subcategoryDTO.getSubCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_PRICE, 0);
        intent.putExtra(SELECTED_SORT, 0);
        intent.putExtra(SELECTED_CATEGORY, 0);
        intent.putExtra(SELECTED_SUBCATEGORY, 0);
        setResult(Activity.RESULT_CANCELED, intent);
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
                intent.putExtra(SELECTED_CATEGORY, categoryId);
                intent.putExtra(SELECTED_SUBCATEGORY, subCategoryId);
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity
                break;
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_CATEGORY_TOKEN:
                customAlterDialog(getString(R.string.str_get_category_err_title), errorMessage);
                recreate();
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_CATEGORY_TOKEN:
                ArrayList<CategoryResponseDTO> categoryResponseDTOs = CategoryResponseDTO.deserializeToArray(data);
                categoryResponseDTOs.add(0, new CategoryResponseDTO(0, getString(R.string.str_all_categories), null));
                categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), categoryResponseDTOs);
                spnCategory.setAdapter(categorySpinnerAdapter);
                break;
        }
    }
}
