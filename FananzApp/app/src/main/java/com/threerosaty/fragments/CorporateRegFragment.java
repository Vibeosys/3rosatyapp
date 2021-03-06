package com.threerosaty.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.activities.SubscriberLoginActivity;
import com.threerosaty.activities.SubscriberRegisterActivity;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.PayLaterSubDTO;
import com.threerosaty.data.requestdata.RegisterSubscriberReq;
import com.threerosaty.data.requestdata.UserRequestDTO;
import com.threerosaty.data.responsedata.InitializePaymentResDTO;
import com.threerosaty.data.responsedata.RegisterSubResDTO;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.SubscriberType;
import com.threerosaty.utils.Validator;
import com.google.gson.Gson;

/**
 * Created by akshay on 29-12-2016.
 */
public class CorporateRegFragment extends BaseFragment implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    private TextView mTxtTerms;
    private static final String TAG = CorporateRegFragment.class.getSimpleName();
    private EditText edtBusinessName, edtContactPerson, edtEmail, edtPh, edtMob, edtWeb, edtCountry, edtPass;
    private Button btnSubmit;
    private CheckBox checkBox;
    private boolean checkTerms = false;
    private SubscriberRegisterActivity activity;
    private String email, password;
    private long subId;
    private UserRequestDTO userRequestDTO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (SubscriberRegisterActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_corporate_reg, container, false);
        mTxtTerms = (TextView) view.findViewById(R.id.txt_terms);
        edtBusinessName = (EditText) view.findViewById(R.id.edt_business_name);
        edtContactPerson = (EditText) view.findViewById(R.id.edt_represet_name);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtPass = (EditText) view.findViewById(R.id.edt_pass);
        edtPh = (EditText) view.findViewById(R.id.edt_phone);
        edtMob = (EditText) view.findViewById(R.id.edt_mob_no);
        edtWeb = (EditText) view.findViewById(R.id.edt_web_site);
        edtCountry = (EditText) view.findViewById(R.id.edt_country_of_residence);
        checkBox = (CheckBox) view.findViewById(R.id.chk_terms);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mTxtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder ssWebsite = new SpannableStringBuilder(getString(R.string.str_user_agreement));
        ssWebsite.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        }, 11, 30, 0);
        mTxtTerms.setText(ssWebsite, TextView.BufferType.SPANNABLE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkTerms = b;
            }
        });
        return view;

    }

    private void showTermsDialog() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
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
                boolean result = registerUser();
                if (result) {
                    if (!checkBox.isChecked()) {
                        customAlterDialog(getResources().getString(R.string.str_sub_registration), getResources().getString(R.string.str_terms_condition));
                    } else if (checkBox.isChecked()) {
                        callToPrepareJson();
                    }
                }
                break;
        }
    }

    private void callToPrepareJson() {
        String strBusinessName = edtBusinessName.getText().toString();
        String strContactPerson = edtContactPerson.getText().toString();
        password = edtPass.getText().toString();
        email = edtEmail.getText().toString();
        String strPhone = edtPh.getText().toString();
        String strMob = edtMob.getText().toString();
        String strWeb = edtWeb.getText().toString();
        String strCountry = edtCountry.getText().toString();
        progressDialog.show();
        RegisterSubscriberReq subscriberReq = new RegisterSubscriberReq(strBusinessName, email, password, strContactPerson
                , SubscriberType.TYPE_CORPORATE, strPhone, strMob, strWeb, strCountry, "");
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(subscriberReq);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_SUBSCRIBER,
                mSessionManager.addSubsriberUrl(), baseRequestDTO);
    }

    private boolean registerUser() {

        String strBusinessName = edtBusinessName.getText().toString();
        String strContactPerson = edtContactPerson.getText().toString();
        password = edtPass.getText().toString();
        email = edtEmail.getText().toString();
        String strPhone = edtPh.getText().toString();
        String strMob = edtMob.getText().toString();
        String strWeb = edtWeb.getText().toString();
        String strCountry = edtCountry.getText().toString();

        View focusView = null;
        boolean check = false;


        if (strBusinessName.isEmpty()) {
            edtBusinessName.requestFocus();
            edtBusinessName.setError(getString(R.string.str_name_empty_error));
            check = true;
            return false;
        } else if (email.isEmpty()) {
            edtEmail.requestFocus();
            check = true;
            edtEmail.setError(getString(R.string.str_email_empty));
            return false;
        } else if (!Validator.isValidMail(email)) {
            edtEmail.requestFocus();
            check = true;
            edtEmail.setError(getString(R.string.str_email_wrong));
            return false;
        } else if (password.isEmpty()) {
            edtPass.requestFocus();
            check = true;
            edtPass.setError(getString(R.string.str_pass_empty));
            return false;
        } else if (strMob.isEmpty()) {
            edtMob.requestFocus();
            check = true;
            edtMob.setError(getString(R.string.str_mob_empty));
            return false;
        } else if (!Validator.isValidPhone(strMob)) {
            focusView = edtMob;
            edtMob.requestFocus();
            check = true;
            edtMob.setError(getString(R.string.str_mob_wrong));
            return false;
        } else if (!strPhone.isEmpty()) {
            if (!Validator.isValidTel(strPhone)) {
                edtPh.requestFocus();
                check = true;
                edtPh.setError(getString(R.string.str_ph_invalid));
                return false;
            }
        } /*else if (!mCheckBox.isChecked()) {
            focusView = mCheckBox;
            check = true;
            customAlterDialog(getResources().getString(R.string.str_sub_registration), getResources().getString(R.string.str_terms_condition));
            return false;
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            RegisterSubscriberReq subscriberReq = new RegisterSubscriberReq(strBusinessName, email, password, strContactPerson
                    , SubscriberType.TYPE_CORPORATE, strPhone, strMob, strWeb, strCountry, "");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(subscriberReq);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_SUBSCRIBER,
                    mSessionManager.addSubsriberUrl(), baseRequestDTO);
        }*/

        return true;
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestToken.REQUEST_INITIALIZE_PAYMENT:
                customSubAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestToken.REQUEST_PAY_LATER:
                Intent iLogin = new Intent(getContext(), SubscriberLoginActivity.class);
                startActivity(iLogin);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                customAlterDialog(getString(R.string.str_register_err_title), errorMessage);
                break;
            case ServerRequestToken.REQUEST_INITIALIZE_PAYMENT:
                customSubAlterDialog(getString(R.string.str_subscription_error), getString(R.string.str_subscription_msg));
                break;
            case ServerRequestToken.REQUEST_PAY_LATER:
                Intent iLogin = new Intent(getContext(), SubscriberLoginActivity.class);
                startActivity(iLogin);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                RegisterSubResDTO registerSubResDTO = RegisterSubResDTO.deserializeJson(data);
                subId = registerSubResDTO.getSubscriberId();
                showSubDialog();
                Toast.makeText(getContext(), getString(R.string.str_sub_register_success), Toast.LENGTH_SHORT).show();
                break;
            case ServerRequestToken.REQUEST_INITIALIZE_PAYMENT:
                InitializePaymentResDTO paymentResDTO = InitializePaymentResDTO.deserializeJson(data);
                activity.subscribeNow(paymentResDTO, userRequestDTO);
                break;
            case ServerRequestToken.REQUEST_PAY_LATER:
                Intent iLogin = new Intent(getContext(), SubscriberLoginActivity.class);
                startActivity(iLogin);
                getActivity().finish();
                break;
        }

    }

    public void showSubDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_subscription_user);
        dialog.setTitle(getString(R.string.str_subscribe));
        TextView txtSubDesc = (TextView) dialog.findViewById(R.id.txt_subscription_desc);
        Button btnSubscribeLater = (Button) dialog.findViewById(R.id.btn_subscribe_later);
        Button btnSubscribe = (Button) dialog.findViewById(R.id.btn_subscribe);
        txtSubDesc.setText(getString(R.string.str_corporate_annual_fee));
        btnSubscribeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                PayLaterSubDTO payLaterSubDTO = new PayLaterSubDTO(subId);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(payLaterSubDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setData(serializedJsonString);
                mServerSyncManager.uploadSubToServer(ServerRequestToken.REQUEST_PAY_LATER,
                        mSessionManager.payLater(), baseRequestDTO);
            }
        });
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                userRequestDTO = new UserRequestDTO(subId, email, password);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(userRequestDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setUser(serializedJsonString);
                mServerSyncManager.uploadSubToServer(ServerRequestToken.REQUEST_INITIALIZE_PAYMENT,
                        mSessionManager.initPaymentUrl(), baseRequestDTO);
            }
        });
        dialog.show();
    }

    protected void customSubAlterDialog(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("" + title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSubDialog();
            }
        });
        builder.show();
    }
}
