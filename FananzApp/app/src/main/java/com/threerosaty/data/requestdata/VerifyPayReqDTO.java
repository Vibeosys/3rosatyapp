package com.threerosaty.data.requestdata;

import com.threerosaty.data.BaseDTO;

/**
 * Created by akshay on 09-01-2017.
 */
public class VerifyPayReqDTO extends BaseDTO {

    private String paypalId;
    private String invoiceNo;

    public VerifyPayReqDTO(String paypalId, String invoiceNo) {
        this.paypalId = paypalId;
        this.invoiceNo = invoiceNo;
    }

    public String getPaypalId() {
        return paypalId;
    }

    public void setPaypalId(String paypalId) {
        this.paypalId = paypalId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
