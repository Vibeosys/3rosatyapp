package com.threerosaty.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by akshay on 22-06-2016.
 */
public class PropertyFileReader {

    private static PropertyFileReader mPropertyFileReader = null;
    private static Context mContext;
    protected static Properties mProperties;

    public static PropertyFileReader getInstance(Context context) {
        if (mPropertyFileReader != null)
            return mPropertyFileReader;

        mContext = context;
        mProperties = getProperties();
        mPropertyFileReader = new PropertyFileReader();
        return mPropertyFileReader;
    }

    protected static Properties getProperties() {
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("app.properties");
            mProperties = new Properties();
            mProperties.load(inputStream);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return mProperties;
    }

    protected String getEndPointUri() {
        return mProperties.getProperty(PropertyTypeConstants.API_ENDPOINT_URI);
    }

    public Float getVersion() {
        String versionNumber = mProperties.getProperty(PropertyTypeConstants.VERSION_NUMBER);
        return Float.valueOf(versionNumber);
    }

    public String getPortfolioList() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.PORTFOLIO_LIST);
    }

    public String getCategoryList() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.CATEGORY_LIST);
    }

    public String getAddSubscriber() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_SUBSCRIBER);
    }

    public String getSignInSub() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SIGN_IN_SUBSCRIBER);
    }

    public String addPortfolioUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_PORTFOLIO);
    }

    public String getPortfolioDetailsUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.PORTFOLIO_DETAILS);
    }


    public String getSubPortfolioListUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SUB_PORTFOLIO_LIST);
    }

    public String getSigninUserUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SIGN_IN_USER);
    }

    public String addUserUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_USER_URL);
    }

    public String sendMessageUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SEND_MSG_URL);
    }

    public String getPhotosUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_PHOTOS_URL);
    }

    public String getUploadPhotoUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.UPLOAD_PHOTOS);
    }

    public String getChangePhotoUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.CHANGE_PHOTOS);
    }

    public String getUpdatePortfolio() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.UPDATE_PORTFOLIO);
    }

    public String initPayUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.INIT_PAYMENT);
    }

    public String verifyPayment() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.VERIFY_PAYMENT);
    }

    public String deletePhoto() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.DELETE_PHOTO);
    }

    public String inactivePort() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.INACTIVE_PORTFOLIO);
    }

    public String forgotPassSub() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.FORGOT_SUB_PASS);
    }

    public String forgotPassUser() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.FORGOT_USER_PASS);
    }

    public String updateProfile() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.UPDATE_SUB_PROFILE);
    }
}
