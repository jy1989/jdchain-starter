package com.jd.blockchain.ledger;

public enum UserInfoEnum {
    USER1("5Sm58Zv97PyvGcUapNhYiVqZQPSEc2NNfvA7",
            "mbC3tWLqjjUxBsexxyfJdvG17BJNUWghFzQBo8QBaZkUyK","mbFsq75ZBmdR2Z2bnuyuE6pJN2N84KGRjtABxVNuJ6F887"),
    USER2("5SmLUxQdFMUMurruybyiwsG3zzm4nbQvkbYF",
            "mb6pDQweto539sarmjbuwhToKmXrShzrP7mdo5BJsKZygz","mbE3gXcaCY8iVLPrEK3MTm7G4PTthpjZ8R7vNABkLaZeg7"),
    USER3("5SmPESKd15Tp2P7GkJGUW9G9rYBEZ3pebAsd",
            "mb1iwRMH51Vs366o4CSwfafQwzVMKTQqmoA3bjNdXCLx6D","mbWbakwTAWJEwAhMQTQ5fd5AatwPgTxr9GuaK6K1AJTojM"),
    USER4("5SmN7anr51mUqSkmuwrsLpnCkKRLuk17Y3Gi",
            "mb6yrJK5urT4kBks1W4NJzvTspHZsVa6XRXLUXwVXFWnFt","mbQtb4ZNt2295ZK6M5mnu6iuWE6HyYsSJAGBrpuGVoofvC");

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
