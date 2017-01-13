package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 09-01-2017.
 */
public class PaymentSuccessResDTO extends BaseDTO {

    private static final String TAG = PaymentSuccessResDTO.class.getSimpleName();
    private boolean paymentSuccess;

    public PaymentSuccessResDTO() {
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    public static PaymentSuccessResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        PaymentSuccessResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, PaymentSuccessResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization PaymentSuccessResDTO" + e.toString());
        }
        return responseDTO;
    }
}
