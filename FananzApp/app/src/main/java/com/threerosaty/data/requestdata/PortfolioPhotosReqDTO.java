package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 07-01-2017.
 */
public class PortfolioPhotosReqDTO extends BaseDTO {
    private long portfolioId;

    public PortfolioPhotosReqDTO(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
