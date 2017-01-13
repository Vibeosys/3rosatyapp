package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 05-01-2017.
 */
public class SendMessageReqDTO extends BaseDTO {

    private int portfolioId;
    private String message;

    public SendMessageReqDTO(int portfolioId, String message) {
        this.portfolioId = portfolioId;
        this.message = message;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
