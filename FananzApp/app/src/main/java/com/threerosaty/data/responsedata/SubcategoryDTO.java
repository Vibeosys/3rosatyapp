package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * Created by akshay on 02-01-2017.
 */
public class SubcategoryDTO extends BaseDTO {
    private static final String TAG = SubcategoryDTO.class.getSimpleName();
    private int subCategoryId;
    private String subCategory;
    private int categoryId;

    public SubcategoryDTO() {
    }

    public SubcategoryDTO(int subCategoryId, String subCategory, int categoryId) {
        this.subCategoryId = subCategoryId;
        this.subCategory = subCategory;
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public static SubcategoryDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        SubcategoryDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, SubcategoryDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization SubcategoryDTO" + e.toString());
        }
        return responseDTO;
    }

    public static ArrayList<CategoryResponseDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<CategoryResponseDTO> portfolioResponses = null;
        try {
            CategoryResponseDTO[] deserializeObject = gson.fromJson(serializedString, CategoryResponseDTO[].class);
            portfolioResponses = new ArrayList<>();
            for (CategoryResponseDTO portfolio : deserializeObject) {
                portfolioResponses.add(portfolio);
            }
        } catch (JsonSyntaxException e) {
            Log.e("deserialize", "Response PortfolioResponse error" + e.toString());
        }


        return portfolioResponses;
    }
}
