package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 07-01-2017.
 */
public class UpdatePortfolioReqDTO extends BaseDTO {
    private long portfolioId;
    private int categoryId;
    private int subCategoryId;
    private String fbLink;
    private String youtubeLink;
    private String aboutUs;
    private int minPrice;
    private int maxPrice;
    private int isActive;

    public UpdatePortfolioReqDTO(long portfolioId, int categoryId, int subCategoryId,
                                 String fbLink, String youtubeLink, String aboutUs, int minPrice,
                                 int maxPrice, int isActive) {
        this.portfolioId = portfolioId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.fbLink = fbLink;
        this.youtubeLink = youtubeLink;
        this.aboutUs = aboutUs;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.isActive = isActive;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
