package com.threerosaty.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.threerosaty.R;
import com.threerosaty.data.requestdata.BaseRequestDTO;
import com.threerosaty.data.requestdata.GetPortfolioDetailReqDTO;
import com.threerosaty.data.requestdata.SendMessageReqDTO;
import com.threerosaty.data.responsedata.PortfolioDetailsResDTO;
import com.threerosaty.data.responsedata.PortfolioReqResDTO;
import com.threerosaty.utils.NetworkUtils;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.UserAuth;
import com.threerosaty.utils.Validator;
import com.google.gson.Gson;

import java.util.HashMap;

public class ArtistDetailsActivity extends BaseActivity implements
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived, View.OnClickListener {
    public static final String PORTFOLIO_ID = "portfolioId";
    private static final String TAG = ArtistDetailsActivity.class.getSimpleName();
    private SliderLayout mDemoSlider;
    private TextView txtArtistName, txtArtistCategory, txtMinMaxPrice, txtFbLink,
            txtYoutubeLink, txtArtistDetails;
    private TextView btnRequestNow;
    private int portfolioId;
    private PortfolioDetailsResDTO portfolioDetailsResDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        portfolioId = getIntent().getExtras().getInt(PORTFOLIO_ID);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        txtArtistName = (TextView) findViewById(R.id.txtArtistName);
        txtArtistCategory = (TextView) findViewById(R.id.txtArtistCategory);
        txtMinMaxPrice = (TextView) findViewById(R.id.txtMinMaxPrice);
        txtFbLink = (TextView) findViewById(R.id.txtFbLink);
        txtYoutubeLink = (TextView) findViewById(R.id.txtYoutubeLink);
        txtArtistDetails = (TextView) findViewById(R.id.txtArtistDetails);
        btnRequestNow = (TextView) findViewById(R.id.btnRequestNow);

        btnRequestNow.setOnClickListener(this);
        txtFbLink.setOnClickListener(this);
        txtYoutubeLink.setOnClickListener(this);

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            progressDialog.show();
            GetPortfolioDetailReqDTO getPortfolioReqDTO = new GetPortfolioDetailReqDTO(portfolioId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(getPortfolioReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_DETAILS_PORTFOLIO,
                    mSessionManager.getPortfolioDetailUrl(), baseRequestDTO);
        } else {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        }

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
            case ServerRequestToken.REQUEST_SIGN_IN_SUB:
                customAlterDialog(getString(R.string.str_show_details_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_DETAILS_PORTFOLIO:
                showDetails(data);
                break;
            case ServerRequestToken.REQUEST_SEND_MESSAGE:
                PortfolioReqResDTO portfolioReqResDTO = PortfolioReqResDTO.deserializeJson(data);
                if (portfolioReqResDTO.isEmailSuccess()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_request_send_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_request_send_fail),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void showDetails(String data) {
        portfolioDetailsResDTO = PortfolioDetailsResDTO.deserializeJson(data);
        txtArtistName.setText(portfolioDetailsResDTO.getSubscriberName());
        txtArtistCategory.setText(portfolioDetailsResDTO.getCategory());
        String maxMinPrice = String.format("%d - %d", portfolioDetailsResDTO.getMinPrice(), portfolioDetailsResDTO.getMaxPrice());
        txtMinMaxPrice.setText(maxMinPrice);
        txtFbLink.setText(portfolioDetailsResDTO.getFbLink());
        txtYoutubeLink.setText(portfolioDetailsResDTO.getYoutubeLink());
        txtArtistDetails.setText(portfolioDetailsResDTO.getAboutUs());
        setTitle(portfolioDetailsResDTO.getSubscriberName());
        try {
            HashMap<String, String> file_maps = new HashMap<String, String>();
            String[] photos = portfolioDetailsResDTO.getPhotos();
            for (int i = 0; i < photos.length; i++) {
                file_maps.put("Image" + i, photos[i]);
            }
            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "##Photos not found");
            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
            for (int i = 0; i < 3; i++) {
                file_maps.put("Image" + i, R.drawable.default_img);
            }
            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }

        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        // mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    public void bookNow() {
        if (UserAuth.isUserLoggedIn()) {
            final Dialog dialog = new Dialog(getApplicationContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_request_now);
            TextView txtArtistName = (TextView) dialog.findViewById(R.id.txtArtistName);
            final EditText edtMessage = (EditText) dialog.findViewById(R.id.edt_write_text);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btn_submit);
            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
            String artistName = portfolioDetailsResDTO.getSubscriberName();
            String categoryName = portfolioDetailsResDTO.getCategory();
            txtArtistName.setText("For " + categoryName + " By " + artistName);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = edtMessage.getText().toString();
                    if (message.isEmpty()) {
                        edtMessage.requestFocus();
                        edtMessage.setError(getString(R.string.str_msg_empty));
                    } else {
                        dialog.dismiss();
                        callToMessage(message, portfolioDetailsResDTO.getPortfolioId());
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            startActivity(new Intent(getApplicationContext(), CustomerLoginActivity.class));
            finish();
            Toast.makeText(getApplicationContext(), getString(R.string.str_login_emoty), Toast.LENGTH_SHORT).show();
        }
    }

    private void callToMessage(String message, int portfolioId) {
        progressDialog.show();
        SendMessageReqDTO sendMessageReqDTO = new SendMessageReqDTO(portfolioId, message);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(sendMessageReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SEND_MESSAGE,
                mSessionManager.sendMessageUrl(), baseRequestDTO);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.txtYoutubeLink:
                if (Validator.isValidUrl(portfolioDetailsResDTO.getYoutubeLink())) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + portfolioDetailsResDTO.getYoutubeLink()));
                        startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(this, "No application can handle this request."
                                + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_url_not_valid), Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.txtFbLink:
                if (Validator.isValidUrl(portfolioDetailsResDTO.getFbLink())) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + portfolioDetailsResDTO.getFbLink()));
                        startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(this, "No application can handle this request."
                                + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_url_not_valid), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRequestNow:
                bookNow();
                break;
        }
    }
}
