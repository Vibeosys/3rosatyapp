package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 03-01-2017.
 */
public class AddPortfolioDataReqDTO extends BaseDTO {

    private int categoryId;
    private int subCategoryId;
    private String fbLink;
    private String youtubeLink;
    private String aboutUs;
    private int minPrice;
    private int maxPrice;

    public AddPortfolioDataReqDTO(int categoryId, int subCategoryId, String fbLink,
                                  String youtubeLink, String aboutUs, int minPrice, int maxPrice) {
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.fbLink = fbLink;
        this.youtubeLink = youtubeLink;
        this.aboutUs = aboutUs;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
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
}
