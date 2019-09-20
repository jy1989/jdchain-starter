package com.jd.blockchain.contract;

import com.jd.blockchain.crypto.KeyGenUtils;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class SDKDemo_Constant {

    //151.39;
//    public static final String GW_IPADDR = "192.168.151.39";
//    public static final int GW_PORT = 18081;
//    public static final String PUB_KEY = "3snPdw7i7PbUX47tXkHVuRd2mP8mXhEJbaBM441h9wa67TAeMfiqUX";
//    public static final String PRIV_KEY = "177gjuzAyvF8W2KYST8tVPmvGBsPLhXsf55HpHxSbHF7Va995ekXvxjNimEYNt5wP6GxTpW";
//    public static final String PASSWORD = "abc";

    //localhost;
//    public static final String GW_IPADDR = "localhost";
//    public static final int GW_PORT = 11000;
//    public static final String PUB_KEY = "3snPdw7i7PjVKiTH2VnXZu5H8QmNaSXpnk4ei533jFpuifyjS5zzH9";
//    public static final String PRIV_KEY = "177gjzHTznYdPgWqZrH43W3yp37onm74wYXT4v9FukpCHBrhRysBBZh7Pzdo5AMRyQGJD7x";
//    public static final String PASSWORD = "abc";

    //peer3
    public static final String GW_IPADDR = "localhost";
    public static final int GW_PORT = 11000;
    public static final String PUB_KEY = "3snPdw7i7PifPuRX7fu3jBjsb3rJRfDe9GtbDfvFJaJ4V4hHXQfhwk";
    public static final String PRIV_KEY = "177gk1pudweTq5zgJTh8y3ENCTwtSFsKyX7YnpuKPo7rKgCkCBXVXh5z2syaTCPEMbuWRns";
    public static final String PASSWORD = "abc";

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
