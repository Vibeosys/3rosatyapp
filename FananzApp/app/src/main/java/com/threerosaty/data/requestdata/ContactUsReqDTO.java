package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 24-01-2017.
 */
public class ContactUsReqDTO extends BaseDTO {

    private String name;
    private String phone;
    private String emailId;
    private String message;

    public ContactUsReqDTO(String name, String phone, String emailId, String message) {
        this.name = name;
        this.phone = phone;
        this.emailId = emailId;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
