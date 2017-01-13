package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 04-01-2017.
 */
public class SigninUserReqDTO extends BaseDTO {
    private String emailId;
    private String password;
    private int isFacebookLogin;

    public SigninUserReqDTO(String emailId, String password, int isFacebookLogin) {
        this.emailId = emailId;
        this.password = password;
        this.isFacebookLogin = isFacebookLogin;
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

    public int getIsFacebookLogin() {
        return isFacebookLogin;
    }

    public void setIsFacebookLogin(int isFacebookLogin) {
        this.isFacebookLogin = isFacebookLogin;
    }
}
