package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 04-01-2017.
 */
public class SigninUserRespDTO extends BaseDTO {
    private static final String TAG = SigninUserRespDTO.class.getSimpleName();
    private long userId;
    private String firstName;
    private String lastName;

    public SigninUserRespDTO() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static SigninUserRespDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        SigninUserRespDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, SigninUserRespDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization SigninUserRespDTO" + e.toString());
        }
        return responseDTO;
    }
}
