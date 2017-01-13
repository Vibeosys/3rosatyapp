package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 05-01-2017.
 */
public class CustomerReqDTO extends BaseDTO {

    private long userId;
    private String emailId;

    public CustomerReqDTO(long userId, String emailId) {
        this.userId = userId;
        this.emailId = emailId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
