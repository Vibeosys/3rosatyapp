package com.threerosaty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.threerosaty.R;
import com.threerosaty.data.responsedata.SubcategoryDTO;

import java.util.List;

/**
 * Created by akshay on 2-01-2017.
 */
public class SubcategorySpinnerAdapter extends BaseAdapter {
    private Context mContext;
    List<SubcategoryDTO> subcategoryDTOs;

    public SubcategorySpinnerAdapter(Context mContext, List<SubcategoryDTO> subcategoryDTOs) {
        this.mContext = mContext;
        this.subcategoryDTOs = subcategoryDTOs;
    }

    @Override
    public int getCount() {
        return subcategoryDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return subcategoryDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        if (row == null) {

            LayoutInflater theLayoutInflater = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflater.inflate(R.layout.spinner_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.spinnerChild = (TextView) row.findViewById(R.id.txtSpinnerChild);
            row.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();
        SubcategoryDTO subcategoryDTO = subcategoryDTOs.get(position);
        viewHolder.spinnerChild.setText(subcategoryDTO.getSubCategory());

        return row;
    }

    private class ViewHolder {
        TextView spinnerChild;
    }

    public int getPosition(int subCategoryId) {
        int position = 0;
        for (int i = 0; i < subcategoryDTOs.size(); i++) {
            if (subCategoryId == subcategoryDTOs.get(i).getSubCategoryId()) {
                position = i;
                return position;
            }
        }
        return position;
    }
}