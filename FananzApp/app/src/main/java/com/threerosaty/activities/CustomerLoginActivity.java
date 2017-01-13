package com.threerosaty.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.threerosaty.MainActivity;
import com.threerosaty.R;
import com.threerosaty.data.UserDTO;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.RegisterUserReqDTO;
import com.threerosaty.data.requestdata.SigninUserReqDTO;
import com.threerosaty.data.responsedata.SigninUserRespDTO;
import com.threerosaty.data.responsedata.UserRegReqDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.UserAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CustomerLoginActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private static final String TAG = CustomerLoginActivity.class.getSimpleName();
    private TextView mTxtRegister, txtForgotPass;
    ImageView fbImg;
    CallbackManager callbackManager;
    private EditText edtEmail, edtPass;
    Button btnSignIn;
    String email, password, firstname, lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        setTitle(getString(R.string.str_sign_in));
        mTxtRegister = (TextView) findViewById(R.id.txt_register);
        fbImg = (ImageView) findViewById(R.id.fbImg);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        txtForgotPass = (TextView) findViewById(R.id.txtForgotPass);
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }
        btnSignIn.setOnClickListener(this);
        fbImg.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getTheDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Cancel Login");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.getMessage());
            }
        });

    }

    public void registerNow(View v) {
        startActivity(new Intent(this, UserRegisterActivity.class));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fbImg:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile,user_birthday"));
                break;
            case R.id.btn_sign_in:
                loginUser();
                break;
            case R.id.txtForgotPass:
                startActivity(new Intent(getApplicationContext(), ForgotPassUserActivity.class));
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
            SigninUserReqDTO signinUserReqDTO = new SigninUserReqDTO(email, password, 0);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(signinUserReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SIGN_IN_USER,
                    mSessionManager.signInUserUrl(), baseRequestDTO);
        }
    }

    public static void LogoutFacebook() {
        try {
            LoginManager.getInstance().logOut();
            Log.d("FBLOGIN", "Log out");

        } catch (FacebookException e) {
            e.printStackTrace();
            Log.d(TAG, "Facebook logout exception");
        }

    }

    private void getTheDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoginActivity", "##" + object.toString());
                        try {
                            email = object.getString("email");
                            firstname = object.getString("first_name");
                            lastname = object.getString("last_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callToRegister(firstname, email, lastname);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callToRegister(String firstname, String email, String lastname) {
        progressDialog.show();
        RegisterUserReqDTO registerUserReqDTO = new RegisterUserReqDTO(firstname,
                lastname, email, "", 1, "");
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(registerUserReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_USER,
                mSessionManager.addUserUrl(), baseRequestDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_USER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestToken.REQUEST_ADD_USER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_USER:
                customAlterDialog(getString(R.string.str_login_err_title), errorMessage);
                break;
            case ServerRequestToken.REQUEST_ADD_USER:
                customAlterDialog(getString(R.string.str_login_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_USER:
                Log.d(TAG, "## Success Login");
                SigninUserRespDTO signinUserRespDTO = SigninUserRespDTO.deserializeJson(data);
                UserDTO subscriberDTO = new UserDTO(signinUserRespDTO.getUserId(),
                        signinUserRespDTO.getFirstName(), signinUserRespDTO.getLastName());
                subscriberDTO.setEmail(email);
                subscriberDTO.setPassword(password);
                UserAuth userAuth = new UserAuth();
                userAuth.saveUserInfo(subscriberDTO, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case ServerRequestToken.REQUEST_ADD_USER:
                UserRegReqDTO registerSubResDTO = UserRegReqDTO.deserializeJson(data);
                UserDTO userDTO = new UserDTO(registerSubResDTO.getUserId(),
                        firstname, lastname);
                userDTO.setEmail(email);
                userDTO.setPassword(password);
                UserAuth auth = new UserAuth();
                auth.saveUserInfo(userDTO, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
