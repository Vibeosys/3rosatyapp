package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 04-01-2017.
 */
public class GetPortfolioDetailReqDTO extends BaseDTO {

    private long portfolioId;

    public GetPortfolioDetailReqDTO(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }
}
