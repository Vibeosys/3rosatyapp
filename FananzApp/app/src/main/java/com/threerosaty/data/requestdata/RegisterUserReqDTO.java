package com.threerosaty.data.requestdata;

/**
 * Created by akshay on 04-01-2017.
 */
public class RegisterUserReqDTO {

    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private int isFacebookLogin;
    private String phoneNo;

    public RegisterUserReqDTO(String firstName, String lastName, String emailId,
                              String password, int isFacebookLogin, String phoneNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
        this.isFacebookLogin = isFacebookLogin;
        this.phoneNo = phoneNo;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
