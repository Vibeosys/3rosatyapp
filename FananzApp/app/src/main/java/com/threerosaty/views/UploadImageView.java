package com.threerosaty.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.threerosaty.R;
import com.threerosaty.data.responsedata.ImageDataReqDTO;
import com.threerosaty.utils.CustomVolleyRequestQueue;
import com.threerosaty.utils.DialogUtils;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.SessionManager;

/**
 * Created by akshay on 12-01-2017.
 */
public class UploadImageView extends View implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private TextView btnChange, btnDelete;
    private ImageDataReqDTO imgData;
    private ImageLoader mImageLoader;
    private ProgressDialog progressDialog;
    private NetworkImageView img;
    protected ServerSyncManager mServerSyncManager = null;
    protected static SessionManager mSessionManager = null;
    private long portId;

    public UploadImageView(Context context) {
        super(context);
        init(context);
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.upload_img_view, null);
        progressDialog = DialogUtils.getFragmentDialog(getContext());
        btnChange = (TextView) this.findViewById(R.id.btnChange);
        btnDelete = (TextView) this.findViewById(R.id.btnDelete);
        img = (NetworkImageView) this.findViewById(R.id.img);
        mSessionManager = SessionManager.getInstance(context);
        mServerSyncManager = new ServerSyncManager(context, mSessionManager);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        if (this.imgData != null) {
            setupUi();
        } else {
            img.setDefaultImageResId(R.drawable.default_img);
        }
        btnChange.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnChange:
                //requestGrantPermission();
                break;
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }

    public ImageDataReqDTO getImgData() {
        return imgData;
    }

    public void setImgData(ImageDataReqDTO imgData) {
        this.imgData = imgData;
    }

    public long getPortId() {
        return portId;
    }

    public void setPortId(long portId) {
        this.portId = portId;
    }

    private void setupUi() {
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext())
                .getImageLoader();
        String url = "";
        try {
            url = imgData.getPhotoUrl();
        } catch (NullPointerException e) {
            url = "";
        }

        if (url == "null" || url == null || url.equals("") || url == "") {
            img.setDefaultImageResId(R.drawable.default_img);
        } else if (TextUtils.isEmpty(url)) {
            img.setImageResource(R.drawable.default_img);
        } else if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(img,
                        R.drawable.default_img, R.drawable.default_img));
                img.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                img.setImageResource(R.drawable.default_img);
            }
        } else {
            img.setImageResource(R.drawable.default_img);
        }
    }

    public interface OnChangeListener {
        void onChangeClick();
    }
}
