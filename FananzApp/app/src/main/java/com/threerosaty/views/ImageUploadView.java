package com.threerosaty.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.threerosaty.R;
import com.threerosaty.data.responsedata.ImageDataReqDTO;
import com.threerosaty.fragments.BaseFragment;
import com.threerosaty.fragments.UploadImageFragment;

/**
 * Created by akshay on 07-01-2017.
 */
public class ImageUploadView extends View {
    private Context mContext;
    private ImageDataReqDTO imgData;
    private FragmentActivity activity;

    public ImageUploadView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ImageUploadView(Context context, ImageDataReqDTO imgData, FragmentActivity activity) {
        super(context);
        this.mContext = context;
        this.imgData = imgData;
        this.activity = activity;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.row_images_portfolio, null);
        BaseFragment uploadImageFragment = new UploadImageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UploadImageFragment.IMAGE_DATA, imgData);
        activity.getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_frame_lay, uploadImageFragment, "Upload" + imgData.getPhotoId()).commit();
    }

    public ImageUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageUploadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
