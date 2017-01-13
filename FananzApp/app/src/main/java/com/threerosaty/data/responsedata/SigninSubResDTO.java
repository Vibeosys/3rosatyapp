package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 03-01-2017.
 */
public class SigninSubResDTO extends BaseDTO {
    private static final String TAG = SigninSubResDTO.class.getSimpleName();
    private long subscriberId;
    private String name;
    private String nickName;
    private String sType;
    private String subscriptionDate;
    private boolean isSubscribed;
    private String telNo;
    private String mobileNo;
    private String websiteUrl;
    private String country;

    public SigninSubResDTO() {
    }

    public long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static SigninSubResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        SigninSubResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, SigninSubResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization SigninSubResDTO" + e.toString());
        }
        return responseDTO;
    }
}
