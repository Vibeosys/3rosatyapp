package com.threerosaty.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.RegisterUserReqDTO;
import com.threerosaty.data.responsedata.UserRegReqDTO;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.google.gson.Gson;

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private static final String TAG = UserRegisterActivity.class.getSimpleName();
    private CheckBox chkTerms;
    private TextView mTxtTerms;
    private EditText edtFirstName, edtLastName, edtEmail, edtMob, edtPass, edtConfirmPass;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        setTitle(getResources().getString(R.string.str_register_now));
        chkTerms = (CheckBox) findViewById(R.id.chk_terms);
        mTxtTerms = (TextView) findViewById(R.id.txt_terms);

        edtFirstName = (EditText) findViewById(R.id.edt_first_name);
        edtLastName = (EditText) findViewById(R.id.edt_last_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtMob = (EditText) findViewById(R.id.edt_mob_no);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        edtConfirmPass = (EditText) findViewById(R.id.edt_confirm_pass);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        btnSubmit.setOnClickListener(this);

        mTxtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder ssWebsite = new SpannableStringBuilder(getString(R.string.str_user_agreement));
        ssWebsite.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        }, 11, 30, 0);
        mTxtTerms.setText(ssWebsite, TextView.BufferType.SPANNABLE);
    }

    private void showTermsDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_terms);
        WebView txtTerms = (WebView) dialog.findViewById(R.id.txt_terms_and_conditions);
        txtTerms.loadUrl("file:///android_res/raw/term.html");
        txtTerms.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String strFName = edtFirstName.getText().toString();
        String strLName = edtLastName.getText().toString();
        String strPassword = edtPass.getText().toString();
        String strEmail = edtEmail.getText().toString();
        String strMob = edtMob.getText().toString();
        String strConfirmPass = edtConfirmPass.getText().toString();
        View focusView = null;
        boolean check = false;
        if (strFName.isEmpty()) {
            edtFirstName.setError(getString(R.string.str_fname_empty_error));
            focusView = edtFirstName;
            check = true;
        } else if (strEmail.isEmpty()) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_email_empty));
        } else if (strPassword.isEmpty()) {
            focusView = edtPass;
            check = true;
            edtPass.setError(getString(R.string.str_pass_empty));
        } else if (strMob.isEmpty()) {
            focusView = edtMob;
            check = true;
            edtMob.setError(getString(R.string.str_mob_empty));
        } else if (strConfirmPass.equals(strPassword)) {
            focusView = edtConfirmPass;
            check = true;
            edtConfirmPass.setError(getString(R.string.str_pass_confirm_incorrect));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            RegisterUserReqDTO registerUserReqDTO = new RegisterUserReqDTO(strFName,
                    strLName, strEmail, strPassword, 0, strMob);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(registerUserReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_USER,
                    mSessionManager.addUserUrl(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_USER:
                Log.d(TAG, "## Success Register");
                UserRegReqDTO registerSubResDTO = UserRegReqDTO.deserializeJson(data);
                startActivity(new Intent(getApplicationContext(), CustomerLoginActivity.class));
                Toast.makeText(getApplicationContext(), getString(R.string.str_sub_register_success), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
