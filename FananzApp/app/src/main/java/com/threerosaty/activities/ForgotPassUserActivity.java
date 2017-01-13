package com.threerosaty.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.ForgotPasswordReqDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.google.gson.Gson;

public class ForgotPassUserActivity extends BaseActivity implements
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    EditText edtMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_user);
        setTitle(getString(R.string.str_forgot_pass_title1));
        edtMail = (EditText) findViewById(R.id.edt_email);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }
    }

    public void sendMailUser(View v) {
        String email = edtMail.getText().toString();
        if (email.isEmpty()) {
            edtMail.requestFocus();
            edtMail.setError(getString(R.string.str_email_empty));
        } else {
            progressDialog.show();
            ForgotPasswordReqDTO forgotPasswordReqDTO = new ForgotPasswordReqDTO(email);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(forgotPasswordReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_USER_FORGOT_PASS,
                    mSessionManager.forgotUserPass(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_forgot_pass_error), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SUB_FORGOT_PASS:
                customAlterDialog(getString(R.string.str_forgot_pass_title), getString(R.string.str_forgot_pass_success));
                break;
        }
    }
}
