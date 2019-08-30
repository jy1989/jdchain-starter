package com.jd.blockchain.contract;

import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.tools.keygen.KeyGenCommand;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class SDKDemo_Constant {

    public static final String GW_IPADDR = "192.168.151.39";

    public static final int GW_PORT = 18081;

    public static final String[] PUB_KEYS = {
            "3snPdw7i7PfAP5uNJoXwN3qzjhcWzec9FvitGn6nL9timnvf7FSQnC"};

    public static final String[] PRIV_KEYS = {
            "177gjvnDU2z3D9knsvgE2tp3pRuGG25aiX4E1DdHSzgRXSb8yxnDojLxYixVd8ox9pHguUz"};

    public static final String PASSWORD = "abc";

    public static PrivKey privkey0 = KeyGenCommand.decodePrivKeyWithRawPassword(PRIV_KEYS[0], PASSWORD);
    public static PubKey pubKey0 = KeyGenCommand.decodePubKey(PUB_KEYS[0]);

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
