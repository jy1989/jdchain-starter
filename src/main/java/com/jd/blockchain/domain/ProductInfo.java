package com.jd.blockchain.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * @Author zhaogw
 * @Date 2018/12/3 15:28
 */
public class ProductInfo {
    @JSONField(name="product")
    private SkuInfo skuInfo;

    @JSONField(name="user_id")
    private String userId;

    private int process;

    public SkuInfo getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(SkuInfo skuInfo) {
        this.skuInfo = skuInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }
}
