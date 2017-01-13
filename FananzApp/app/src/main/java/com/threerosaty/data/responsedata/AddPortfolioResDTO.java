package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 07-01-2017.
 */
public class AddPortfolioResDTO extends BaseDTO {
    private static final String TAG = AddPortfolioResDTO.class.getSimpleName();
    private long portfolioId;

    public AddPortfolioResDTO() {
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public static AddPortfolioResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        AddPortfolioResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, AddPortfolioResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization AddPortfolioResDTO" + e.toString());
        }
        return responseDTO;
    }
}
