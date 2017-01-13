package com.threerosaty.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.threerosaty.R;
import com.threerosaty.data.responsedata.PortfolioResponse;
import com.threerosaty.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by akshay on 02-01-2017.
 */
public class SubPortfolioAdapter extends BaseAdapter {

    private ArrayList<PortfolioResponse> mData;
    private Context mContext;
    private ImageLoader mImageLoader;
    private OnItemClick onItemClick;

    public SubPortfolioAdapter(ArrayList<PortfolioResponse> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View row = convertView;
        ViewHolder viewHolder = null;
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        if (row == null) {

            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_portfolio_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);
            viewHolder.txtSubcategory = (TextView) row.findViewById(R.id.txtSubCategory);
            viewHolder.txtMinMaxPrice = (TextView) row.findViewById(R.id.txtMinMaxPrice);
            viewHolder.btnInactive = (TextView) row.findViewById(R.id.btnInactive);
            viewHolder.btnModify = (TextView) row.findViewById(R.id.btnModify);
            viewHolder.coverImg = (NetworkImageView) row.findViewById(R.id.coverImg1);
            viewHolder.coverImg.setImageResource(R.drawable.default_img);
            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.coverImg.setImageUrl(null, mImageLoader);
        }
        viewHolder.coverImg.setImageResource(R.drawable.default_img);
        final PortfolioResponse portfolioResponse = mData.get(position);
        String category = portfolioResponse.getCategory();
        String subCategory = portfolioResponse.getSubCategory();
        double maxPrice = portfolioResponse.getMaxPrice();
        double minPrice = portfolioResponse.getMinPrice();
        String maxMinPrice = String.format("%.0f-%.0f", minPrice, maxPrice);
        viewHolder.txtCategory.setText(category);
        viewHolder.txtSubcategory.setText(subCategory);
        viewHolder.txtMinMaxPrice.setText(maxMinPrice);
        int isActive = portfolioResponse.getIsActive();

        if (isActive == 0) {
            viewHolder.btnInactive.setText(mContext.getString(R.string.str_active));
            viewHolder.btnInactive.setTextColor(mContext.getResources().getColor(R.color.greenBtnColour));
        } else if (isActive == 1) {
            viewHolder.btnInactive.setText(mContext.getString(R.string.str_inactive));
            viewHolder.btnInactive.setTextColor(mContext.getResources().getColor(R.color.request_now_btn));
        }
        try {
            String url = portfolioResponse.getCoverImageUrl();
            if (url == "null" || url == null || url.equals("") || url == "") {
                viewHolder.coverImg.setDefaultImageResId(R.drawable.default_img);
            } else if (TextUtils.isEmpty(url)) {
                viewHolder.coverImg.setImageResource(R.drawable.default_img);
            } else if (url != null && !url.isEmpty()) {
                try {
                    mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.coverImg,
                            R.drawable.default_img, R.drawable.default_img));
                    viewHolder.coverImg.setImageUrl(url, mImageLoader);
                } catch (Exception e) {
                    viewHolder.coverImg.setImageResource(R.drawable.default_img);
                }
            } else {
                viewHolder.coverImg.setImageResource(R.drawable.default_img);
            }
        } catch (NullPointerException e) {
            viewHolder.coverImg.setDefaultImageResId(R.drawable.default_img);
        }

        viewHolder.btnInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null) {
                    onItemClick.onInactiveClickListener(portfolioResponse, position);
                }
            }
        });
        viewHolder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null) {
                    onItemClick.onModifyClickListener(portfolioResponse, position);
                }
            }
        });
        return row;
    }

    private class ViewHolder {
        TextView txtCategory, txtSubcategory, txtMinMaxPrice;
        NetworkImageView coverImg;
        TextView btnInactive, btnModify;
    }

    public interface OnItemClick {
        public void onInactiveClickListener(PortfolioResponse portfolioResponse, int position);

        public void onModifyClickListener(PortfolioResponse portfolioResponse, int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
