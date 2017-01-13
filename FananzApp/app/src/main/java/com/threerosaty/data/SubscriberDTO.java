package com.threerosaty.data;

/**
 * Created by akshay on 03-01-2017.
 */
public class SubscriberDTO {

    private long mSubscriberId;
    private String mName;
    private String mNickName;
    private String mSType;
    private String mSubscriptionDate;
    private boolean mIsSubscribed;
    private String mEmail;
    private String mPassword;
    private String telNo;
    private String mobileNo;
    private String websiteUrl;
    private String country;

    public SubscriberDTO(long mSubscriberId, String mName, String mNickName, String mSType,
                         String mSubscriptionDate, boolean mIsSubscribed, String telNo,
                         String mobileNo, String websiteUrl, String country) {
        this.mSubscriberId = mSubscriberId;
        this.mName = mName;
        this.mNickName = mNickName;
        this.mSType = mSType;
        this.mSubscriptionDate = mSubscriptionDate;
        this.mIsSubscribed = mIsSubscribed;
        this.telNo = telNo;
        this.mobileNo = mobileNo;
        this.websiteUrl = websiteUrl;
        this.country = country;
    }

    public long getSubscriberId() {
        return mSubscriberId;
    }

    public void setSubscriberId(long mSubscriberId) {
        this.mSubscriberId = mSubscriberId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public String getSType() {
        return mSType;
    }

    public void setSType(String mSType) {
        this.mSType = mSType;
    }

    public String getSubscriptionDate() {
        return mSubscriptionDate;
    }

    public void setSubscriptionDate(String mSubscriptionDate) {
        this.mSubscriptionDate = mSubscriptionDate;
    }

    public boolean isIsSubscribed() {
        return mIsSubscribed;
    }

    public void setIsSubscribed(boolean mIsSubscribed) {
        this.mIsSubscribed = mIsSubscribed;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
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
}
