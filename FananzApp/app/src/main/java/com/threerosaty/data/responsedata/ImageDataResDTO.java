package com.threerosaty.data.responsedata;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.threerosaty.data.BaseDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by akshay on 07-01-2017.
 */
public class ImageDataResDTO extends BaseDTO implements Serializable {
    private static final String TAG = ImageDataResDTO.class.getSimpleName();
    private long photoId;
    private String photoUrl;
    private boolean isCoverImage;

    public ImageDataResDTO(long photoId, String photoUrl, boolean isCoverImg) {
        this.photoId = photoId;
        this.photoUrl = photoUrl;
        this.isCoverImage = isCoverImg;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isCoverImg() {
        return isCoverImage;
    }

    public void setCoverImg(boolean coverImg) {
        isCoverImage = coverImg;
    }

    public static ImageDataResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ImageDataResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, ImageDataResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization ImageDataReqDTO" + e.toString());
        }
        return responseDTO;
    }

    public static ArrayList<ImageDataResDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<ImageDataResDTO> imageDataReqDTOs = null;
        try {
            ImageDataResDTO[] deserializeObject = gson.fromJson(serializedString, ImageDataResDTO[].class);
            imageDataReqDTOs = new ArrayList<>();
            for (ImageDataResDTO imageDataReqDTO : deserializeObject) {
                imageDataReqDTOs.add(imageDataReqDTO);
            }
        } catch (JsonSyntaxException e) {
            Log.e("deserialize", "Response ImageDataReqDTO error" + e.toString());
        }


        return imageDataReqDTOs;
    }
}
