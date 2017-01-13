package com.threerosaty.data.requestdata;


import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 30-12-2016.
 */
public class BaseRequestDTO extends BaseDTO {

    private String user;
    private String data;

    public BaseRequestDTO() {
    }

    public BaseRequestDTO(String user, String data) {
        this.user = user;
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
