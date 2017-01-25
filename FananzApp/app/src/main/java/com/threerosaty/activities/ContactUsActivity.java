package com.threerosaty.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.threerosaty.R;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.ContactUsReqDTO;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.Validator;

public class ContactUsActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived, View.OnClickListener {

    EditText editName, editEmail, editPhone, editMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setTitle(getString(R.string.str_send_msg));
        editName = (EditText) findViewById(R.id.edt_name);
        editEmail = (EditText) findViewById(R.id.edt_email);
        editPhone = (EditText) findViewById(R.id.edt_phone);
        editMessage = (EditText) findViewById(R.id.edt_msg);
        btnSend = (Button) findViewById(R.id.btn_send);

        btnSend.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_contactus_error), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_CONTACT_US:
                Toast.makeText(getApplicationContext(), getString(R.string.str_contact_success),
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        String strName = editName.getText().toString();
        String email = editEmail.getText().toString();
        String strMob = editPhone.getText().toString();
        String strMsg = editMessage.getText().toString();

        View focusView = null;
        boolean check = false;
        if (strName.isEmpty()) {
            editName.setError(getString(R.string.str_name_empty_error));
            focusView = editName;
            check = true;
        } else if (email.isEmpty()) {
            focusView = editEmail;
            check = true;
            editEmail.setError(getString(R.string.str_email_empty));
        } else if (!Validator.isValidMail(email)) {
            focusView = editEmail;
            check = true;
            editEmail.setError(getString(R.string.str_email_wrong));
        } else if (strMob.isEmpty()) {
            focusView = editPhone;
            check = true;
            editPhone.setError(getString(R.string.str_mob_empty));
        } else if (!Validator.isValidPhone(strMob)) {
            focusView = editPhone;
            check = true;
            editPhone.setError(getString(R.string.str_ph_invalid));
        } else if (strMsg.isEmpty()) {
            focusView = editMessage;
            check = true;
            editMessage.setError(getString(R.string.str_msg_empty));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            ContactUsReqDTO contactUsReqDTO = new ContactUsReqDTO(strName, email, strMob, strMsg);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(contactUsReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_CONTACT_US,
                    mSessionManager.contactUsUrl(), baseRequestDTO);
        }

    }
}
