package com.threerosaty.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.adapters.CategorySpinnerAdapter;
import com.threerosaty.adapters.SubcategorySpinnerAdapter;
import com.threerosaty.data.requestdata.AddPortfolioDataReqDTO;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.GetPortfolioDetailReqDTO;
import com.threerosaty.data.requestdata.UpdatePortfolioReqDTO;
import com.threerosaty.data.responsedata.AddPortfolioResDTO;
import com.threerosaty.data.responsedata.CategoryResponseDTO;
import com.threerosaty.data.responsedata.PortfolioDetailsResDTO;
import com.threerosaty.data.responsedata.SubcategoryDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddPortfolioDataActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived, View.OnClickListener {

    public static final String PORTFOLIO_DETAILS = "portfolioDetails";
    public static final String PORTFOLIO_DETAILS_BUNDLE = "portfolioDetailsBundle";
    private static final String TAG = AddPortfolioDataActivity.class.getSimpleName();
    private ArrayList<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();
    private Spinner spnCategory, spnSubCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private SubcategorySpinnerAdapter subcategorySpinnerAdapter;
    private EditText edtFbLink, edtYouLink, edtMinPrice, edtMaxPrice, edtDetails;
    private Button btnNext;
    private int categoryId = 0;
    private int subCategoryId = 0;
    private long portfolioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_data);
        setTitle(getString(R.string.str_add_portfolio_title));
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnSubCategory = (Spinner) findViewById(R.id.spnSubCategory);
        edtFbLink = (EditText) findViewById(R.id.edt_fb_link);
        edtYouLink = (EditText) findViewById(R.id.edt_you_link);
        edtMinPrice = (EditText) findViewById(R.id.edt_min_price);
        edtMaxPrice = (EditText) findViewById(R.id.edt_max_price);
        edtDetails = (EditText) findViewById(R.id.edt_details);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnNext.setOnClickListener(this);
        progressDialog.show();
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_CATEGORY_TOKEN, mSessionManager.getCategoryListUrl());
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle = bundle.getBundle(PORTFOLIO_DETAILS_BUNDLE);
            portfolioId = bundle.getInt(PORTFOLIO_DETAILS);

        } else {

        }

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                CategoryResponseDTO categoryResponseDTO = (CategoryResponseDTO) categorySpinnerAdapter.getItem(position);
                categoryId = categoryResponseDTO.getCategoryId();
                if (categoryResponseDTO.getSubCategories() != null) {
                    subcategorySpinnerAdapter = new SubcategorySpinnerAdapter(getApplicationContext(),
                            categoryResponseDTO.getSubCategories());
                    spnSubCategory.setAdapter(subcategorySpinnerAdapter);
                    spnSubCategory.setVisibility(View.VISIBLE);
                    if (subCategoryId != 0) {
                        spnSubCategory.setSelection(subcategorySpinnerAdapter.getPosition(subCategoryId));
                    }
                } else {
                    spnSubCategory.setVisibility(View.GONE);
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
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_CATEGORY_TOKEN:
                customAlterDialog(getString(R.string.str_get_category_err_title), errorMessage);
                recreate();
                break;
            case ServerRequestToken.REQUEST_ADD_PORTFOLIO:
                customAlterDialog(getString(R.string.str_add_portfolio_err_title), errorMessage);
                break;
            case ServerRequestToken.REQUEST_DETAILS_PORTFOLIO:
                customAlterDialog(getString(R.string.str_portfolio_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_CATEGORY_TOKEN:
                categoryResponseDTOs = CategoryResponseDTO.deserializeToArray(data);
                categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), categoryResponseDTOs);
                spnCategory.setAdapter(categorySpinnerAdapter);
                Log.d(TAG, "##" + categoryResponseDTOs);
                if (portfolioId != 0) {
                    progressDialog.show();
                    GetPortfolioDetailReqDTO getPortfolioReqDTO = new GetPortfolioDetailReqDTO(portfolioId);
                    Gson gson = new Gson();
                    String serializedJsonString = gson.toJson(getPortfolioReqDTO);
                    BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                    baseRequestDTO.setData(serializedJsonString);
                    mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_DETAILS_PORTFOLIO,
                            mSessionManager.getPortfolioDetailUrl(), baseRequestDTO);
                }
                break;
            case ServerRequestToken.REQUEST_ADD_PORTFOLIO:
                Log.d(TAG, "##" + data);
                AddPortfolioResDTO addPortfolioResDTO = AddPortfolioResDTO.deserializeJson(data);
                Intent intent = new Intent(getApplicationContext(), AddPortfolioPhotosActivity.class);
                intent.putExtra(AddPortfolioPhotosActivity.PORTFOLIO_ID, addPortfolioResDTO.getPortfolioId());
                startActivity(intent);
                break;
            case ServerRequestToken.REQUEST_DETAILS_PORTFOLIO:
                showDetails(data);
                break;
            case ServerRequestToken.REQUEST_UPDATE_PORTFOLIO:
                showConfirmDialog();
                break;
        }
    }

    private void showConfirmDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_photos);
        dialog.setTitle(getString(R.string.str_update_photos));
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), AddPortfolioPhotosActivity.class);
                intent.putExtra(AddPortfolioPhotosActivity.PORTFOLIO_ID, portfolioId);
                startActivity(intent);
            }
        });
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_next:
                if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    addPortfolio();
                } else {
                    createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                            getResources().getString(R.string.str_err_net_msg));
                }
                break;
        }
    }

    private void addPortfolio() {
        String strFbLink = edtFbLink.getText().toString();
        String strYouLink = edtYouLink.getText().toString();

        int minPrice = 0;
        try {
            minPrice = Integer.parseInt(edtMinPrice.getText().toString());
        } catch (Exception e) {
            minPrice = 0;
        }
        int maxPrice = 0;
        try {
            maxPrice = Integer.parseInt(edtMaxPrice.getText().toString());
        } catch (Exception e) {
            maxPrice = 0;
        }

        String strDetails = edtDetails.getText().toString();

        View focusView = null;
        boolean check = false;
        if (strFbLink.isEmpty()) {
            focusView = edtFbLink;
            check = true;
            edtFbLink.setError(getString(R.string.str_fb_empty));
        } else if (strYouLink.isEmpty()) {
            focusView = edtYouLink;
            check = true;
            edtYouLink.setError(getString(R.string.str_you_empty));
        } else if (minPrice == 0) {
            focusView = edtMinPrice;
            check = true;
            edtMinPrice.setError(getString(R.string.str_min_price_error));
        } else if (maxPrice == 0) {
            focusView = edtMaxPrice;
            check = true;
            edtMaxPrice.setError(getString(R.string.str_max_price_error));
        } else if (maxPrice < minPrice) {
            focusView = edtMaxPrice;
            check = true;
            edtMaxPrice.setError(getString(R.string.str_max_price_greater_error));
        } else if (strDetails.isEmpty()) {
            focusView = edtDetails;
            check = true;
            edtDetails.setError(getString(R.string.str_portfolio_details_empty));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            if (portfolioId == 0) {
                progressDialog.show();
                AddPortfolioDataReqDTO addPortfolioDataReqDTO = new AddPortfolioDataReqDTO(categoryId, subCategoryId,
                        strFbLink, strYouLink, strDetails, minPrice, maxPrice);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(addPortfolioDataReqDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setData(serializedJsonString);
                mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_PORTFOLIO,
                        mSessionManager.addPortfolioUrl(), baseRequestDTO);
            } else {
                progressDialog.show();
                UpdatePortfolioReqDTO addPortfolioDataReqDTO = new UpdatePortfolioReqDTO(portfolioId,
                        categoryId, subCategoryId, strFbLink, strYouLink, strDetails, minPrice,
                        maxPrice, 1);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(addPortfolioDataReqDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setData(serializedJsonString);
                mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_UPDATE_PORTFOLIO,
                        mSessionManager.updatePortfolioUrl(), baseRequestDTO);
            }

        }
    }

    private void showDetails(String data) {
        PortfolioDetailsResDTO portfolio = PortfolioDetailsResDTO.deserializeJson(data);
        edtFbLink.setText(portfolio.getFbLink());
        edtYouLink.setText(portfolio.getYoutubeLink());
        edtMinPrice.setText(String.valueOf(portfolio.getMinPrice()));
        edtMaxPrice.setText(String.valueOf(portfolio.getMaxPrice()));
        edtDetails.setText(portfolio.getAboutUs());
        int position = categorySpinnerAdapter.getPosition(portfolio.getCategoryId());
        spnCategory.setSelection(position);
        if (portfolio.getSubCategoryId() != 0) {
            subCategoryId = portfolio.getSubCategoryId();
        }
    }
}
