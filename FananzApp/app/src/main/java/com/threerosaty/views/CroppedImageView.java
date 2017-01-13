package com.threerosaty.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.threerosaty.R;
import com.threerosaty.activities.BaseActivity;

/**
 * Created by akshay on 30-12-2016.
 */
public class CroppedImageView extends ImageView implements View.OnClickListener {
    private Context mContext;
    private BaseActivity activity;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private static final String TAG = CroppedImageView.class.getSimpleName();
    private static final int REQUEST_SELECT_PICTURE = 0x01;

    public CroppedImageView(Context context) {
        super(context);
        mContext = context;
    }

    public CroppedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public CroppedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    mContext.getResources().getString(R.string.permission_read_storage_rationale),
                    activity.REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            activity.startActivityForResult(Intent.createChooser(intent, mContext.getResources().getString(R.string.str_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    public void setBaseActivity(BaseActivity activity) {
        this.activity = activity;
    }
}
