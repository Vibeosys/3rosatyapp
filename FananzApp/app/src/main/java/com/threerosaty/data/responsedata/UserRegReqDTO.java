package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 11-01-2017.
 */
public class UserRegReqDTO extends BaseDTO {

    private static final String TAG = UserRegReqDTO.class.getSimpleName();
    private long userId;

    public UserRegReqDTO() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static UserRegReqDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        UserRegReqDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, UserRegReqDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization UserRegReqDTO" + e.toString());
        }
        return responseDTO;
    }
}
