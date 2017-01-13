package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 07-01-2017.
 */
public class UpdatePhotosReqDTO extends BaseDTO {
    private long subscriberId;
    private String emailId;
    private String password;
    private long photoId;

    public UpdatePhotosReqDTO(long subscriberId, String emailId, String password, long photoId) {
        this.subscriberId = subscriberId;
        this.emailId = emailId;
        this.password = password;
        this.photoId = photoId;
    }

    public long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }
}
