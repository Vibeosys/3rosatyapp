package com.threerosaty.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.adapters.MainActivityAdapter;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.UserRequestDTO;
import com.threerosaty.data.requestdata.VerifyPayReqDTO;
import com.threerosaty.data.responsedata.InitializePaymentResDTO;
import com.threerosaty.data.responsedata.PaymentSuccessResDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class SubscriberRegisterActivity extends BaseActivity implements
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    private TabLayout tab_layout;
    private static PayPalConfiguration config;
    private UserRequestDTO userRequestDTO;
    private InitializePaymentResDTO initializeDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_register);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText(getResources().getString(R.string.corporate)));
        tab_layout.addTab(tab_layout.newTab().setText(getResources().getString(R.string.freelance)));
        setTitle(getResources().getString(R.string.str_subscription_acc));
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_layout.setSelectedTabIndicatorHeight(4);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        final MainActivityAdapter mainActivityAdapteradapter =
                new MainActivityAdapter(getSupportFragmentManager(), tab_layout.getTabCount());
        viewPager.setAdapter(mainActivityAdapteradapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void subscribeNow(InitializePaymentResDTO initializeDTO, UserRequestDTO userRequestDTO) {
        this.userRequestDTO = userRequestDTO;
        this.initializeDTO = initializeDTO;
        config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(initializeDTO.getEnvironment())
                .clientId(this.initializeDTO.getClientId());

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        PayPalPayment payment = new PayPalPayment(new BigDecimal(this.initializeDTO.getAmount()),
                this.initializeDTO.getCurrency(), this.initializeDTO.getAmountDesc(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        payment.invoiceNumber(this.initializeDTO.getInvoiceNumber());
        Intent intentPay = new Intent(this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intentPay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intentPay.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intentPay, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    progressDialog.show();
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    JSONObject confirmJson = confirm.toJSONObject();
                    JSONObject response = confirmJson.getJSONObject("response");
                    String paypalId = response.getString("id");
                    VerifyPayReqDTO verifyPayReqDTO = new VerifyPayReqDTO(paypalId, this.initializeDTO.getInvoiceNumber());
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    Gson gson = new Gson();
                    String serializedJsonString = gson.toJson(verifyPayReqDTO);
                    String serializedUserString = gson.toJson(this.userRequestDTO);
                    BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                    baseRequestDTO.setData(serializedJsonString);
                    baseRequestDTO.setUser(serializedUserString);
                    mServerSyncManager.uploadSubToServer(ServerRequestToken.REQUEST_VERIFY_PAY,
                            mSessionManager.verifyPayUrl(), baseRequestDTO);

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
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
        customAlterDialog(getString(R.string.str_register_err_title), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_VERIFY_PAY:
                PaymentSuccessResDTO paymentSuccessResDTO = PaymentSuccessResDTO.deserializeJson(data);
                if (paymentSuccessResDTO.isPaymentSuccess()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_success_pay), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SubscriberLoginActivity.class));
                    finish();
                }
                break;
        }
    }
}
