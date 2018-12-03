package com.jd.blockchain.ledger;

public enum UserInfoEnum {
    USER1("5Sm8Ak9adoqetf3GRMMz3LLwiNXbt4yqEY2A",
            "mbBXv3QhZG3sCZP2s4BsfXFcZJJyYiYSJALSo1rcbTu87F", "mbX44uzKFBrhB2uZ9hsswiVp9GfZGU8MvcvJufPuZjuhwB"),
    USER2("5Sm7AQw8aB1JzehBsGhFTGJBtCvAHYNMRyom",
            "mbCfVfnKf4EUoiNjYksXbjTr8NYyPM47nqGbk59JVwLW3A", "mbH2h3qR3AfzACwgDDiUkPtpqMZTMLRD6mdZ1bbsYWbUwC"),
    USER3("5Sm81Q7Mw2dKXwQ5hsEwb7V5KbZA3448RYaP",
            "mb3LhEwBXP3rWXpmpC1QzXwZWfEH4CrH1xZ6YrdwpayfMH", "mbUbc3tCoat1grsamfE3VRVjSyHQsYBg6PvBtiNeGCivHm"),
    USER4("5SmK9PNYEgGNgTq9TDWCBhWWDmTA8PJDdhdB",
            "mb8cbnpfjDtGniFr7HzXnjLZjVPkekenqo2KbP7B5pkNjA", "mbTczRLhnQommNXj4tZ3vpnE3186TnYAYHZDJcC9PfMo7h");

    private String address;
    private String pubKey;
    private String privKey;

    UserInfoEnum(String address, String pubKey, String privKey) {
        this.address = address;
        this.pubKey = pubKey;
        this.privKey = privKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPrivKey() {
        return privKey;
    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

    public static UserInfoEnum getEnumByAddr(String addr) {
        for (UserInfoEnum userInfoEnum : UserInfoEnum.values()) {
            if (addr.equals(userInfoEnum.getAddress())) {
                return userInfoEnum;
            }
        }
        return null;
    }
}
