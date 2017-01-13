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
public class CategoryResponseDTO extends BaseDTO {
    private static final String TAG = CategoryResponseDTO.class.getSimpleName();
    private int categoryId;
    private String category;
    private ArrayList<SubcategoryDTO> subCategories;

    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(int categoryId, String category, ArrayList<SubcategoryDTO> subCategories) {
        this.categoryId = categoryId;
        this.category = category;
        this.subCategories = subCategories;
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

    public ArrayList<SubcategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubcategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }

    public static CategoryResponseDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        CategoryResponseDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, CategoryResponseDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization CategoryResponseDTO" + e.toString());
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
                if(portfolio.getSubCategories()!=null)
                {
                    for (int i = 0; i < portfolio.getSubCategories().size(); i++) {
                        portfolio.getSubCategories().get(i).setCategoryId(portfolio.categoryId);
                    }
                }

            }

        } catch (JsonSyntaxException e) {
            Log.e("deserialize", "Response PortfolioResponse error" + e.toString());
        }


        return portfolioResponses;
    }

}
