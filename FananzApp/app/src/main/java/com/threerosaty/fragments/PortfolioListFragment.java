package com.threerosaty.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.threerosaty.MainActivity;
import com.threerosaty.R;
import com.threerosaty.activities.ArtistDetailsActivity;
import com.threerosaty.activities.CustomerLoginActivity;
import com.threerosaty.activities.FilterActivity;
import com.threerosaty.adapters.UserPortfolioListAdapter;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.SendMessageReqDTO;
import com.threerosaty.data.responsedata.PortfolioReqResDTO;
import com.threerosaty.data.responsedata.PortfolioResponse;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.UserAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by akshay on 04-01-2017.
 */
public class PortfolioListFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, UserPortfolioListAdapter.OnItemClick, MainActivity.OnFilterClickListener {
    private ListView listPortfolio;
    private ArrayList<PortfolioResponse> portfolioResponses = new ArrayList<>();
    private UserPortfolioListAdapter adapter;
    private ImageView noPortfolio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFilterClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_portfolio_list, container, false);

        listPortfolio = (ListView) view.findViewById(R.id.listPortfolio);
        noPortfolio = (ImageView) view.findViewById(R.id.no_portfolio);
        progressDialog.show();
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_PORTFOLIO_LIST,
                mSessionManager.getPortfolioListUrl());
        return view;
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
            case ServerRequestToken.REQUEST_PORTFOLIO_LIST:
                noPortfolio.setVisibility(View.VISIBLE);
                listPortfolio.setVisibility(View.GONE);
                break;
            case ServerRequestToken.REQUEST_SEND_MESSAGE:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_PORTFOLIO_LIST:
                portfolioResponses = PortfolioResponse.deserializeToArray(data);
                adapter = new UserPortfolioListAdapter(portfolioResponses, getContext());
                adapter.setOnItemClick(this);
                listPortfolio.setAdapter(adapter);
                break;
            case ServerRequestToken.REQUEST_SEND_MESSAGE:
                PortfolioReqResDTO portfolioReqResDTO = PortfolioReqResDTO.deserializeJson(data);
                if (portfolioReqResDTO.isEmailSuccess()) {
                    Toast.makeText(getContext(), getString(R.string.str_request_send_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.str_request_send_fail),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onShowDetailsClickListener(PortfolioResponse portfolioResponse, int position) {
        Intent artistDetails = new Intent(getContext(), ArtistDetailsActivity.class);
        artistDetails.putExtra(ArtistDetailsActivity.PORTFOLIO_ID, portfolioResponse.getPortfolioId());
        startActivity(artistDetails);
    }

    @Override
    public void onRequestNowClickListener(final PortfolioResponse portfolioResponse, int position) {
        if (UserAuth.isUserLoggedIn()) {
            final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_request_now);
            TextView txtArtistName = (TextView) dialog.findViewById(R.id.txtArtistName);
            final EditText edtMessage = (EditText) dialog.findViewById(R.id.edt_write_text);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btn_submit);
            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
            String artistName = portfolioResponse.getSubscriberName();
            String categoryName = portfolioResponse.getCategory();
            txtArtistName.setText("For " + categoryName + " By " + artistName);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = edtMessage.getText().toString();
                    if (message.isEmpty()) {
                        edtMessage.requestFocus();
                        edtMessage.setError(getString(R.string.str_msg_empty));
                    } else {
                        dialog.dismiss();
                        callToMessage(message, portfolioResponse.getPortfolioId());
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            startActivity(new Intent(getContext(), CustomerLoginActivity.class));
            getActivity().finish();
            Toast.makeText(getContext(), getString(R.string.str_login_emoty), Toast.LENGTH_SHORT).show();
        }

    }

    private void callToMessage(String message, int portfolioId) {
        progressDialog.show();
        SendMessageReqDTO sendMessageReqDTO = new SendMessageReqDTO(portfolioId, message);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(sendMessageReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SEND_MESSAGE,
                mSessionManager.sendMessageUrl(), baseRequestDTO);
    }

    @Override
    public void onFilterClick() {
        startActivityForResult(new Intent(getContext(), FilterActivity.class), MainActivity.FILTER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.FILTER_RESULT) {
            adapter.setData(portfolioResponses);
            int selectedPrice = data.getIntExtra(FilterActivity.SELECTED_PRICE, 0);
            int selectedSort = data.getIntExtra(FilterActivity.SELECTED_SORT, 0);
            if (selectedPrice > 1000) {
                adapter.filterAdapter(selectedPrice);
            }
            adapter.sortAdapter(selectedSort);

        }
    }
}
