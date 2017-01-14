package com.threerosaty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.threerosaty.MainActivity;
import com.threerosaty.R;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.PortfolioPhotosReqDTO;
import com.threerosaty.data.responsedata.ImageDataReqDTO;
import com.threerosaty.fragments.UploadImageFragment;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.SubscriberType;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddPortfolioPhotosActivity extends BaseActivity implements
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived, View.OnClickListener {
    public static final String TAG = AddPortfolioPhotosActivity.class.getSimpleName();
    public static final String PORTFOLIO_ID = "portfolioId";
    private boolean isPhotoAvail;
    FrameLayout frameCover;
    private long portfolioId;
    private LinearLayout mainView;
    private TextView txtTotalImages;
    private int length = 0;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_photos);
        setTitle(getString(R.string.str_add_portfolio_title));
        frameCover = (FrameLayout) findViewById(R.id.frame_cover);
        mainView = (LinearLayout) findViewById(R.id.mainView);
        txtTotalImages = (TextView) findViewById(R.id.txtNoOfPhotos);
        btnDone = (Button) findViewById(R.id.btn_done);
        portfolioId = getIntent().getExtras().getLong(PORTFOLIO_ID);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        btnDone.setOnClickListener(this);
        if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            callPhotosFromServer();
        } else {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }
        if (mSessionManager.getSType().equals(SubscriberType.TYPE_CORPORATE)) {
            length = 8;
        } else {
            length = 4;
        }
        txtTotalImages.setText("You can upload up to " + length + " images");
    }

    private void callPhotosFromServer() {
        progressDialog.show();
        PortfolioPhotosReqDTO portfolioPhotosReqDTO = new PortfolioPhotosReqDTO(portfolioId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(portfolioPhotosReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_PORT_PHOTOS,
                mSessionManager.getPhotosUrl(), baseRequestDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_PORT_PHOTOS:
                setUpDynamicUi(new ArrayList<ImageDataReqDTO>());
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_PORT_PHOTOS:
                ArrayList<ImageDataReqDTO> images = ImageDataReqDTO.deserializeToArray(data);
                setUpDynamicUi(images);
                break;
        }
    }

    public void setUpDynamicUi(ArrayList<ImageDataReqDTO> images) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (images.size() > 0) {

            int position = getCoverImage(images);
            if (position != -1) {
                UploadImageFragment coverImage = new UploadImageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(UploadImageFragment.IMAGE_DATA, images.get(position));
                bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                bundle.putBoolean(UploadImageFragment.IS_COVER_IMG, true);
                coverImage.setArguments(bundle);
                fragmentTransaction.add(R.id.frame_cover, coverImage, "Upload");
                images.remove(position);
            }

        } else {
            UploadImageFragment image3 = new UploadImageFragment();
            Bundle bundleImg3 = new Bundle();
            bundleImg3.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
            bundleImg3.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
            bundleImg3.putBoolean(UploadImageFragment.IS_COVER_IMG, true);
            image3.setArguments(bundleImg3);
            fragmentTransaction.add(R.id.frame_cover, image3, "Upload");
        }

        for (int i = 0; i <= length; i++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setId(i);
            try {
                UploadImageFragment coverImage = new UploadImageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(UploadImageFragment.IMAGE_DATA, images.get(i));
                bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                coverImage.setArguments(bundle);
                fragmentTransaction.add(ll.getId(), coverImage, "" + i);
            } catch (Exception e) {
                UploadImageFragment image3 = new UploadImageFragment();
                Bundle bundleImg3 = new Bundle();
                bundleImg3.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                bundleImg3.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
                bundleImg3.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
                image3.setArguments(bundleImg3);
                fragmentTransaction.add(ll.getId(), image3, "" + i);
            }
            mainView.addView(ll);
        }
        fragmentTransaction.commit();

    }

    private int getCoverImage(ArrayList<ImageDataReqDTO> images) {
        int position = -1;
        for (int i = 0; i < images.size(); i++) {
            int isCover = images.get(i).isCoverImg();
            if (isCover == 1) {
                position = i;
            }
        }
        return position;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_done:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
        }
    }
}
