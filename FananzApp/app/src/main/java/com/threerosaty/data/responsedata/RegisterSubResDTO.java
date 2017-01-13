package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 09-01-2017.
 */
public class RegisterSubResDTO extends BaseDTO {

    private static final String TAG = RegisterSubResDTO.class.getSimpleName();
    private long subscriberId;

    public RegisterSubResDTO() {
    }

    public long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public static RegisterSubResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        RegisterSubResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, RegisterSubResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization RegisterSubResDTO" + e.toString());
        }
        return responseDTO;
    }
}
