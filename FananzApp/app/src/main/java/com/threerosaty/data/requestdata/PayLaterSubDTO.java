package com.threerosaty.data.requestdata;


import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 24-01-2017.
 */
public class PayLaterSubDTO extends BaseDTO {

    private long subscriberId;

    public PayLaterSubDTO(long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
    }
}
