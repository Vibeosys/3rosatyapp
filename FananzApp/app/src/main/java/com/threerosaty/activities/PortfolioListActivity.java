package com.threerosaty.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.adapters.SubPortfolioAdapter;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.InactivePortfolioReqSTO;
import com.threerosaty.data.responsedata.PortfolioResponse;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.SubscriberType;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PortfolioListActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, SubPortfolioAdapter.OnItemClick {

    private static final String TAG = PortfolioListActivity.class.getSimpleName();
    private ListView listPortfolio;
    private SubPortfolioAdapter adapter;
    private ArrayList<PortfolioResponse> portfolioResponses = new ArrayList<>();
    private LinearLayout addPortfolioLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_list);
        setTitle(getString(R.string.str_portfolio));
        listPortfolio = (ListView) findViewById(R.id.listPortfolio);
        addPortfolioLay = (LinearLayout) findViewById(R.id.addPortfolioLay);
        progressDialog.show();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            if (mSessionManager.getSType().equals(SubscriberType.TYPE_FREELANCER)) {
                addPortfolioLay.setVisibility(View.GONE);
            }
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST,
                    mSessionManager.getSubPortfolioListUrl(), baseRequestDTO);
        } else {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }


    }

    public void addPortFolio(View v) {
        Intent addPortfolio = new Intent(getApplicationContext(), AddPortfolioDataActivity.class);
        startActivity(addPortfolio);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestToken.REQUEST_SEND_MESSAGE:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST:
                customAlterDialog(getString(R.string.str_portfolio_list_err_title), errorMessage);
                if (mSessionManager.getSType().equals(SubscriberType.TYPE_FREELANCER)) {
                    addPortfolioLay.setVisibility(View.VISIBLE);
                }
                break;
            case ServerRequestToken.REQUEST_INACTIVE_PORTFOLIO:
                customAlterDialog(getString(R.string.str_inactive_error_response), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        Log.d(TAG, data);
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST:

                portfolioResponses = PortfolioResponse.deserializeToArray(data);
                adapter = new SubPortfolioAdapter(portfolioResponses, getApplicationContext());
                adapter.setOnItemClick(this);
                if (mSessionManager.getSType().equals(SubscriberType.TYPE_FREELANCER)) {
                    if (portfolioResponses.size() > 0) {
                        addPortfolioLay.setVisibility(View.GONE);
                    } else {
                        addPortfolioLay.setVisibility(View.VISIBLE);
                    }
                }
                listPortfolio.setAdapter(adapter);
                break;
            case ServerRequestToken.REQUEST_INACTIVE_PORTFOLIO:
                recreate();
                break;
        }
    }

    @Override
    public void onInactiveClickListener(PortfolioResponse portfolioResponse, int position) {
        Log.d(TAG, "## Inactive click");
        int isActive = (portfolioResponse.getIsActive() == 0) ? 1 : 0;
        InactivePortfolioReqSTO inactivePortfolioReqSTO = new InactivePortfolioReqSTO(portfolioResponse.getPortfolioId(), isActive);
        progressDialog.show();
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(inactivePortfolioReqSTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_INACTIVE_PORTFOLIO,
                mSessionManager.inactivePortUrl(), baseRequestDTO);
    }

    @Override
    public void onModifyClickListener(PortfolioResponse portfolioResponse, int position) {
        Log.d(TAG, "## Modify click");
        Bundle bundle = new Bundle();
        bundle.putInt(AddPortfolioDataActivity.PORTFOLIO_DETAILS, portfolioResponse.getPortfolioId());
        Intent iAddPort = new Intent(getApplicationContext(), AddPortfolioDataActivity.class);
        iAddPort.putExtra(AddPortfolioDataActivity.PORTFOLIO_DETAILS_BUNDLE, bundle);
        startActivity(iAddPort);
    }
}
