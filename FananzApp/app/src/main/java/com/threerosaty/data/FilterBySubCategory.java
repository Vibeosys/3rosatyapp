package com.threerosaty.data;


import com.threerosaty.data.responsedata.PortfolioResponse;

import java.util.ArrayList;

/**
 * Created by akshay on 08-02-2017.
 */
public class FilterBySubCategory implements FilterByCriteria {
    @Override
    public ArrayList<PortfolioResponse> meetCriteria(int value, ArrayList<PortfolioResponse> data) {
        ArrayList<PortfolioResponse> filterData = new ArrayList<>();
        for (PortfolioResponse portfolioResponse : data) {
            if (portfolioResponse.getSubcategoryId() == value) {
                filterData.add(portfolioResponse);
            }
        }
        return filterData;
    }
}
