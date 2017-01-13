package com.threerosaty.data.responsedata;

import android.util.Log;

import com.threerosaty.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by akshay on 07-01-2017.
 */
public class ImageDataReqDTO extends BaseDTO implements Serializable {
    private static final String TAG = ImageDataReqDTO.class.getSimpleName();
    private long photoId;
    private String photoUrl;
    private int isCoverImage;

    public ImageDataReqDTO(long photoId, String photoUrl, int isCoverImg) {
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

    public int isCoverImg() {
        return isCoverImage;
    }

    public void setCoverImg(int coverImg) {
        isCoverImage = coverImg;
    }

    public static ImageDataReqDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ImageDataReqDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, ImageDataReqDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization ImageDataReqDTO" + e.toString());
        }
        return responseDTO;
    }

    public static ArrayList<ImageDataReqDTO> deserializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<ImageDataReqDTO> imageDataReqDTOs = null;
        try {
            ImageDataReqDTO[] deserializeObject = gson.fromJson(serializedString, ImageDataReqDTO[].class);
            imageDataReqDTOs = new ArrayList<>();
            for (ImageDataReqDTO imageDataReqDTO : deserializeObject) {
                imageDataReqDTOs.add(imageDataReqDTO);
            }
        } catch (JsonSyntaxException e) {
            Log.e("deserialize", "Response ImageDataReqDTO error" + e.toString());
        }


        return imageDataReqDTOs;
    }

    public static class IsCoverComparator implements Comparator<ImageDataReqDTO> {

        @Override
        public int compare(ImageDataReqDTO p1, ImageDataReqDTO p2) {
            double isCover1 = p1.isCoverImg();
            double isCover2 = p2.isCoverImg();

            if (isCover1 == isCover2)
                return 0;
            else if (isCover1 > isCover2)
                return 1;
            else
                return -1;
        }
    }
}
