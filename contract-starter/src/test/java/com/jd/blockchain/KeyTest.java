package com.jd.blockchain;

import com.jd.blockchain.crypto.KeyGenUtils;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.security.ShaUtils;
import org.junit.Assert;
import org.junit.Test;

import static com.jd.blockchain.crypto.KeyGenUtils.encodePubKey;
import static com.jd.blockchain.crypto.KeyGenUtils.encryptPrivKey;

/**
 * @author zhaogw
 * date 2019/7/26 13:39
 */
public class KeyTest {

    @Test
    public void genKeyPlusPasswd(){
        String pubKeyStr = "3snPdw7i7PbUX47tXkHVuRd2mP8mXhEJbaBM441h9wa67TAeMfiqUX";
        String privKeyStr = "177gjuzAyvF8W2KYST8tVPmvGBsPLhXsf55HpHxSbHF7Va995ekXvxjNimEYNt5wP6GxTpW";
        String passwd = "abc";
        // 生成连接网关的账号
        PrivKey privKey = KeyGenUtils.decodePrivKeyWithRawPassword(privKeyStr, passwd);
        PubKey pubKey = KeyGenUtils.decodePubKey(pubKeyStr);
        BlockchainKeypair adminKey = new BlockchainKeypair(pubKey, privKey);

        Assert.assertEquals(pubKeyStr,encodePubKey(adminKey.getPubKey()));
        byte[] pwdBytes = ShaUtils.hash_256(passwd.getBytes());
        Assert.assertEquals(privKeyStr,encodePrivKey(adminKey.getPrivKey(),pwdBytes));
    }

//    public static String encodePubKey(PubKey pubKey) {
//        byte[] pubKeyBytes = BytesUtils.concat(KeyGenUtils.PUB_KEY_FILE_MAGICNUM, pubKey.toBytes());
//        String base58PubKey = Base58Utils.encode(pubKeyBytes);
//        return base58PubKey;
//    }

    public static String encodePrivKey(PrivKey privKey, byte[] pwdBytes) {
        byte[] encodedPrivKeyBytes = encryptPrivKey(privKey, pwdBytes);
        String base58PrivKey = Base58Utils.encode(encodedPrivKeyBytes);
        return base58PrivKey;
    }
}
