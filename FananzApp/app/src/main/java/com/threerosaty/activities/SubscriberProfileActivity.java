package com.threerosaty.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.UpdateProfileReqDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.SubscriberType;
import com.google.gson.Gson;

public class SubscriberProfileActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private EditText edtName, edtEmail, edtPass, edtTel, edtMo, edtWeb, edtNick, edtCountry;
    private LinearLayout layNickName;
    private TextView txtSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_profile);
        setTitle(getString(R.string.my_profile));
        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        edtTel = (EditText) findViewById(R.id.edt_telephone);
        edtMo = (EditText) findViewById(R.id.edt_mobile);
        edtWeb = (EditText) findViewById(R.id.edt_web_site);
        edtNick = (EditText) findViewById(R.id.edt_nic_name);
        edtCountry = (EditText) findViewById(R.id.edt_country);
        txtSave = (TextView) findViewById(R.id.btn_save);
        layNickName = (LinearLayout) findViewById(R.id.nick_view);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        txtSave.setOnClickListener(this);

        String subType = mSessionManager.getSType();
        if (subType.equals(SubscriberType.TYPE_CORPORATE)) {
            layNickName.setVisibility(View.GONE);
        } else {
            layNickName.setVisibility(View.VISIBLE);
            edtNick.setText(mSessionManager.getNickName());
        }
        edtName.setText(mSessionManager.getName());
        edtEmail.setText(mSessionManager.getEmail());
        edtEmail.setEnabled(false);
        edtPass.setText(mSessionManager.getPassword());
        edtTel.setText(mSessionManager.getTelNo());
        edtMo.setText(mSessionManager.getMobNo());
        edtWeb.setText(mSessionManager.getWebUrl());
        edtCountry.setText(mSessionManager.getCountry());
        String nickName = mSessionManager.getNickName();
        if (nickName.isEmpty()) {
            layNickName.setVisibility(View.GONE);
        } else {
            layNickName.setVisibility(View.VISIBLE);
            edtNick.setText(nickName);
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
        customAlterDialog(getString(R.string.str_update_profile_err), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_UPDATE_PROFILE:
                customAlterDialog(getString(R.string.str_update_profile_success), getString(R.string.str_profile_updated));
                String strName = edtName.getText().toString();
                String strNickName = edtNick.getText().toString();
                String password = edtPass.getText().toString();
                String email = edtEmail.getText().toString();
                String strPhone = edtTel.getText().toString();
                String strMob = edtMo.getText().toString();
                String strWeb = edtWeb.getText().toString();
                String strCountry = edtCountry.getText().toString();
                mSessionManager.setName(strName);
                mSessionManager.setNickName(strNickName);
                mSessionManager.setEmail(email);
                mSessionManager.setPassword(password);
                mSessionManager.setTelNo(strPhone);
                mSessionManager.setMobNo(strMob);
                mSessionManager.setWebUrl(strWeb);
                mSessionManager.setCountry(strCountry);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                            getResources().getString(R.string.str_err_net_msg));
                } else {
                    updateProfile();
                }

                break;
        }
    }

    private void updateProfile() {
        String strName = edtName.getText().toString();
        String strNickName = edtNick.getText().toString();
        String password = edtPass.getText().toString();
        String email = edtEmail.getText().toString();
        String strPhone = edtTel.getText().toString();
        String strMob = edtMo.getText().toString();
        String strWeb = edtWeb.getText().toString();
        String strCountry = edtCountry.getText().toString();

        View focusView = null;
        boolean check = false;
        if (strName.isEmpty()) {
            edtName.setError(getString(R.string.str_name_empty_error));
            focusView = edtName;
            check = true;
        } else if (email.isEmpty()) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_email_empty));
        } else if (password.isEmpty()) {
            focusView = edtPass;
            check = true;
            edtPass.setError(getString(R.string.str_pass_empty));
        } else if (strMob.isEmpty()) {
            focusView = edtMo;
            check = true;
            edtMo.setError(getString(R.string.str_mob_empty));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            UpdateProfileReqDTO updateProfileReqDTO = new UpdateProfileReqDTO(strName, email, password, "",
                    strPhone, strMob, strWeb, strCountry,
                    strNickName);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(updateProfileReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_UPDATE_PROFILE,
                    mSessionManager.updateSubProfile(), baseRequestDTO);
        }
    }
}
