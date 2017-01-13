package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 09-01-2017.
 */
public class InitializePaymentResDTO extends BaseDTO {

    private static final String TAG = InitializePaymentResDTO.class.getSimpleName();
    private String clientId;
    private String invoiceNumber;
    private double amount;
    private String currency;
    private String amountDesc;
    private String environment;

    public InitializePaymentResDTO() {
    }

    public static String getTAG() {
        return TAG;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmountDesc() {
        return amountDesc;
    }

    public void setAmountDesc(String amountDesc) {
        this.amountDesc = amountDesc;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public static InitializePaymentResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        InitializePaymentResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, InitializePaymentResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization InitializePaymentResDTO" + e.toString());
        }
        return responseDTO;
    }
}
