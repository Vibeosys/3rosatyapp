package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by akshay on 02-01-2017.
 */
public class PortfolioResponse extends BaseDTO {

    private static final String TAG = PortfolioResponse.class.getSimpleName();
    private int portfolioId;
    private String subscriberName;
    private int categoryId;
    private String category;
    private int subcategoryId;
    private String subcategory;
    private String subCategory;
    private double minPrice;
    private double maxPrice;
    private String coverImageUrl;
    private int isActive;

    public PortfolioResponse() {
    }

    public PortfolioResponse(int portfolioId, String subscriberName, int categoryId,
                             String category, int subcategoryId, String subcategory,
                             double minPrice, double maxPrice, String coverImageUrl) {
        this.portfolioId = portfolioId;
        this.subscriberName = subscriberName;
        this.categoryId = categoryId;
        this.category = category;
        this.subcategoryId = subcategoryId;
        this.subcategory = subcategory;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.coverImageUrl = coverImageUrl;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public static PortfolioResponse deserializeJson(String serializedString) {
        Gson gson = new Gson();
        PortfolioResponse responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, PortfolioResponse.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization PortfolioResponse" + e.toString());
        }
        return responseDTO;
    }

    public static ArrayList<PortfolioResponse> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<PortfolioResponse> portfolioResponses = null;
        try {
            PortfolioResponse[] deserializeObject = gson.fromJson(serializedString, PortfolioResponse[].class);
            portfolioResponses = new ArrayList<>();
            for (PortfolioResponse portfolio : deserializeObject) {
                portfolioResponses.add(portfolio);
            }
        } catch (JsonSyntaxException e) {
            Log.e("deserialize", "Response PortfolioResponse error" + e.toString());
        }


        return portfolioResponses;
    }


    public static class PriceComparator implements Comparator<PortfolioResponse> {
        @Override
        public int compare(PortfolioResponse p1, PortfolioResponse p2) {
            double minPrice1 = p1.getMinPrice();
            double minPrice2 = p2.getMinPrice();

            if (minPrice1 == minPrice2)
                return 0;
            else if (minPrice1 > minPrice2)
                return 1;
            else
                return -1;
        }
    }


   /* @Override
    public Comparator<PortfolioResponse> reversed() {
        return null;
    }*/
}
