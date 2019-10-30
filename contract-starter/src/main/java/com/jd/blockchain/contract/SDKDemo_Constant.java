package com.jd.blockchain.contract;

import com.jd.blockchain.crypto.KeyGenUtils;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class SDKDemo_Constant {

    //151.39;
    public static final String GW_IPADDR = "192.168.151.39";
    public static final int GW_PORT = 18081;
    public static final String PUB_KEY = "3snPdw7i7PdFoVTPbLGEutzxJkPmp58oWRJ1nzYF3YwsAPxsCkzufo";
    public static final String PRIV_KEY = "177gjysz11aDQy6MaxkstvGVbQXFohAGvkqedwD584ASGkdGy59PcHkHy6KPCtydKhGBHb5";
    public static final String PASSWORD = "abc";

    //localhost;
//    public static final String GW_IPADDR = "localhost";
//    public static final int GW_PORT = 11000;
//    public static final String PUB_KEY = "3snPdw7i7PjVKiTH2VnXZu5H8QmNaSXpnk4ei533jFpuifyjS5zzH9";
//    public static final String PRIV_KEY = "177gjzHTznYdPgWqZrH43W3yp37onm74wYXT4v9FukpCHBrhRysBBZh7Pzdo5AMRyQGJD7x";
//    public static final String PASSWORD = "abc";

    public static PrivKey privkey0 = KeyGenUtils.decodePrivKeyWithRawPassword(PRIV_KEY, PASSWORD);
    public static PubKey pubKey0 = KeyGenUtils.decodePubKey(PUB_KEY);

    public static final byte[] readChainCodes(String contractZip) {
        // 构建合约的字节数组;
        try {
            ClassPathResource contractPath = new ClassPathResource(contractZip);
            File contractFile = new File(contractPath.getURI());
            return FileUtils.readFileToByteArray(contractFile);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
