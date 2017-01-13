package com.threerosaty.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.threerosaty.MainActivity;
import com.threerosaty.R;
import com.threerosaty.data.SubscriberDTO;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.SigninSubReqDTO;
import com.threerosaty.data.responsedata.SigninSubResDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.UserAuth;
import com.google.gson.Gson;

public class SubscriberLoginActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private static final String TAG = SubscriberLoginActivity.class.getSimpleName();
    private EditText edtEmail, edtPass;
    private Button btnSignIn;
    private String email, password;
    private TextView txtForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_login);
        setTitle(getString(R.string.str_login));
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        txtForgotPass = (TextView) findViewById(R.id.txtForgotPass);
        btnSignIn.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }
    }

    public void registerNow(View v) {
        startActivity(new Intent(this, SubscriberRegisterActivity.class));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_sign_in:
                loginUser();
                break;
            case R.id.txtForgotPass:
                startActivity(new Intent(getApplicationContext(), ForgotPassSubActivity.class));
                break;
        }
    }

    private void loginUser() {
        View focusView = null;
        boolean check = false;
        email = edtEmail.getText().toString();
        password = edtPass.getText().toString();
        if (email.isEmpty()) {
            check = true;
            focusView = edtEmail;
            edtEmail.setError(getString(R.string.str_email_empty));
        } else if (password.isEmpty()) {
            check = true;
            focusView = edtPass;
            edtPass.setError(getString(R.string.str_pass_empty));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            SigninSubReqDTO signinSubReqDTO = new SigninSubReqDTO(email, password);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(signinSubReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SIGN_IN_SUB,
                    mSessionManager.signInSubUrl(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_SUB:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_SUB:
                customAlterDialog(getString(R.string.str_login_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_SUB:
                Log.d(TAG, "## Success Login");
                SigninSubResDTO signinSubResDTO = SigninSubResDTO.deserializeJson(data);
                SubscriberDTO subscriberDTO = new SubscriberDTO(signinSubResDTO.getSubscriberId(),
                        signinSubResDTO.getName(), signinSubResDTO.getNickName(), signinSubResDTO.getsType(),
                        signinSubResDTO.getSubscriptionDate(), signinSubResDTO.isSubscribed(),
                        signinSubResDTO.getTelNo(), signinSubResDTO.getMobileNo(), signinSubResDTO.getWebsiteUrl(),
                        signinSubResDTO.getCountry());
                subscriberDTO.setEmail(email);
                subscriberDTO.setPassword(password);
                UserAuth userAuth = new UserAuth();
                userAuth.saveSubscriberInfo(subscriberDTO, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
    }
}
