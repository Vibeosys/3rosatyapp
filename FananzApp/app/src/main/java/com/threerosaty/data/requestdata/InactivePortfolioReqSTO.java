package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 11-01-2017.
 */
public class InactivePortfolioReqSTO extends BaseDTO {

    private long portfolioId;
    private int isActive;

    public InactivePortfolioReqSTO(long portfolioId, int isActive) {
        this.portfolioId = portfolioId;
        this.isActive = isActive;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
