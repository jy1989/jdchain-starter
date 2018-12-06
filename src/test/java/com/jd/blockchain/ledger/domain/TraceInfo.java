package com.jd.blockchain.ledger.domain;

/**
 * @Author zhaogw
 * @Date 2018/12/3 15:28
 */
public class TraceInfo {
    private ProductInfo [] data;
    private String message;

    public ProductInfo[] getData() {
        return data;
    }

    public void setData(ProductInfo[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
