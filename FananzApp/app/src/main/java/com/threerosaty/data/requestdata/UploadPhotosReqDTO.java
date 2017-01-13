package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 07-01-2017.
 */
public class UploadPhotosReqDTO extends BaseDTO {
    private long portfolioId;
    private long subscriberId;
    private String emailId;
    private String password;
    private boolean isCoverImageUpload;

    public UploadPhotosReqDTO(long portfolioId, long subscriberId, String emailId,
                              String password, boolean isCoverImageUpload) {
        this.portfolioId = portfolioId;
        this.subscriberId = subscriberId;
        this.emailId = emailId;
        this.password = password;
        this.isCoverImageUpload = isCoverImageUpload;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
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

    public boolean isCoverImageUpload() {
        return isCoverImageUpload;
    }

    public void setCoverImageUpload(boolean coverImageUpload) {
        isCoverImageUpload = coverImageUpload;
    }
}
