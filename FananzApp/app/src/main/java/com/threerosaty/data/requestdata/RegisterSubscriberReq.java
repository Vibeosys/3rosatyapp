package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 02-01-2017.
 */
public class RegisterSubscriberReq extends BaseDTO {

    private String name;
    private String emailId;
    private String password;
    private String contactPerson;
    private String subType;
    private String telNo;
    private String mobileNo;
    private String websiteUrl;
    private String country;
    private String nickName;

    public RegisterSubscriberReq(String name, String emailId, String password, String contactPerson,
                                 String subType, String telNo, String mobileNo, String websiteUrl,
                                 String country, String nickName) {
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.contactPerson = contactPerson;
        this.subType = subType;
        this.telNo = telNo;
        this.mobileNo = mobileNo;
        this.websiteUrl = websiteUrl;
        this.country = country;
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
