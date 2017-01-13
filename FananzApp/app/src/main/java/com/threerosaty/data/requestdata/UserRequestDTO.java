package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 02-01-2017.
 */
public class UserRequestDTO extends BaseDTO {

    private long subscriberId;
    private String emailId;
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(long subscriberId, String emailId, String password) {
        this.subscriberId = subscriberId;
        this.emailId = emailId;
        this.password = password;
    }

    public long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
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
}
