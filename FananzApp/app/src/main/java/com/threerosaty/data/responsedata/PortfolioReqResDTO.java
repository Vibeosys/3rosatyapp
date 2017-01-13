package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 11-01-2017.
 */
public class PortfolioReqResDTO extends BaseDTO {
    private static final String TAG = PortfolioReqResDTO.class.getSimpleName();
    private boolean emailSuccess;

    public PortfolioReqResDTO() {
    }

    public boolean isEmailSuccess() {
        return emailSuccess;
    }

    public void setEmailSuccess(boolean emailSuccess) {
        this.emailSuccess = emailSuccess;
    }

    public static PortfolioReqResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        PortfolioReqResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, PortfolioReqResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization PortfolioReqResDTO" + e.toString());
        }
        return responseDTO;
    }
}
