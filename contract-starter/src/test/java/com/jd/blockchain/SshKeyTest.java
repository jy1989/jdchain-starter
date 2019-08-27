package com.jd.blockchain;

import com.jd.blockchain.contract.SDK_Base_Demo;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.service.classic.ClassicAlgorithm;
import com.jd.blockchain.crypto.utils.classic.RSAUtils;
import com.jd.blockchain.crypto.utils.classic.SSHKeyParser;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.TransactionTemplate;
import org.bouncycastle.crypto.params.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author zhaogw
 * date 2019/8/26 18:18
 */
public class SshKeyTest extends SDK_Base_Demo {
    private SSHKeyParser parser;
    private static final CryptoAlgorithm RSA = ClassicAlgorithm.RSA;
    private static final CryptoAlgorithm ECDSA = ClassicAlgorithm.ECDSA;
    private static final CryptoAlgorithm ED25519 = ClassicAlgorithm.ED25519;

    @Before
    public void setup(){
        parser = new SSHKeyParser();
    }

    /**
     * 利用已知的公私钥构建用户，并提交;
     */
    @Test
    public void registerUser_Rsa_ByExistPubAndPrivKey() {
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);

        //use the existing pubKey and privKey;
        String rsaPubKeyWithHead = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDcqZgctnwHn5edzYMtMumszluUzIfV6UUEO/ssiP6Ldu8zx1hXOG+xcJkCq3Ys3a3pNOIvXtCI6EBnjYEysWUyvYWSi8Pi8osaqRTHZTXFsntS7rz4tNZlNJt7bGB2itR9MTi2bPipmgJQT8TdI0UWgWfd+iXIfvGcb+ovCN7qUjc5xrNr2CRysOWKxbu3Nryn\n" +
                "by/i0/EnrDUSV0O/C6uyMbLtNW6Decy82atwqq5u3SXFJ3pICKBkbFa0UbK4V5VWPZEq+F3FUDe6YdabnrDtJAtD7AF5cwHhX0VlBF5E+zkv8Fo4LDk3myYI1x1goNOARVc7rwHB/vFUI0rtaEZh zgw\n";
        String rsaPrivKeyWithHead = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpAIBAAKCAQEA3KmYHLZ8B5+Xnc2DLTLprM5blMyH1elFBDv7LIj+i3bvM8dY\n" +
                "VzhvsXCZAqt2LN2t6TTiL17QiOhAZ42BMrFlMr2FkovD4vKLGqkUx2U1xbJ7Uu68\n" +
                "+LTWZTSbe2xgdorUfTE4tmz4qZoCUE/E3SNFFoFn3folyH7xnG/qLwje6lI3Ocaz\n" +
                "a9gkcrDlisW7tza8p28v4tPxJ6w1EldDvwursjGy7TVug3nMvNmrcKqubt0lxSd6\n" +
                "SAigZGxWtFGyuFeVVj2RKvhdxVA3umHWm56w7SQLQ+wBeXMB4V9FZQReRPs5L/Ba\n" +
                "OCw5N5smCNcdYKDTgEVXO68Bwf7xVCNK7WhGYQIDAQABAoIBAEL6w65rgeh6e/Yt\n" +
                "bJTqsF2A4fGfx9eZkBaB8nMJPmKs/ly0NM981QKCUB+sVhm2TFTOdZ21ZQ05mlsU\n" +
                "a9QsXrBxMLtT62jXAHxeOG1x/kl9LMv2pavEkJN1zYBAXGN/LpK1vyY4m543nl4s\n" +
                "aHBLhWwYaaPKi295gPNgUTBnu+pZ7BlfB0yiBpy6wBlBbun5DxlfwEXLxEKtFuCH\n" +
                "caTyJ+MIqsMiS+yc+XX91wetYBU+7PpCSIvUeKufBuOJY1bHltyYRbkKs0so2HK8\n" +
                "BVC/u8Y5R+NzoDmBW7IYw9sofbe4hPBUa8p/SrLLvs4VkrOFuoLwr58tkI8UMUbQ\n" +
                "IA4IPXECgYEA7fzc5PcTDh9qIFrhfebma/kuuomZGOLJJVv581A8DjlLhBJ0HSw6\n" +
                "vgUGzGyCSDnHYexH9222hAeOG0hh9K9um88GqtYR3jwJ+uzbLk0E03OUOGgmLggf\n" +
                "TdLGhp4c+F7L3963AYN6CtASGapPjj2FfKr47Xp3/6jOUOFxLwKwqk0CgYEA7V0L\n" +
                "kNGWtGjNJiQSNPEd5OK4UsnlaGT9f+8CUPsv3QdwtLgFeGy2fgi2ovn14RWBN4+8\n" +
                "buRLO33HExIOkJe+CTaDHipgUsEi4QPGaLma2Wv1b0UZAGEHCoEL2EVpv4pz59d6\n" +
                "5hwb6MHRRf+G3fHaKHVIDmM+gFIWGXdzuPqobmUCgYAFs4MPMSQRuIrwT30LaRao\n" +
                "C4YlDdrPD8/Fx1hLjeaCaLj4ZwDFGiQmHTS2fPr6A/hUzjZDJSu0KgIrgSZnmDRM\n" +
                "Iz+DpDE1Y7bRfg8aIto0vpYUa9pOS/+D+8Kpwuvqv0qfCFTWAnJQBAALAVt6itrB\n" +
                "y86+0apCQ6b9pQjzGVtmGQKBgQCJYg1LqMN9cBstNDuFgPk4fTEKWej531oKLiA9\n" +
                "FuXBHIlTUB96cBHAKPLsqZhyyqubqQn7dQ9GQ6pxk0Xy0qEfW/nP/974hujVLAYu\n" +
                "nx7NMPtBh9DMs1wL7F0atDJctpoXWlS70xEkSuoBhcBdvZnzSM6H5VFLnDdGTikg\n" +
                "q5PJMQKBgQCrMBfYotLC2zcKlff6z+Ijt1KqZ0E6bWrDp+DEYbZvnkjCX8zSZeDK\n" +
                "1eeHzu7zs46HfjCcFOi9Bpbu7ACT9bwCkNucm2ErqtSgkyNr/SwobCrnpANakRh9\n" +
                "eKcgwUr9OtopF0PsyocPxOZGq9aAeWPLlcTNWwNEiwrsLDj+34xOsA==\n" +
                "-----END RSA PRIVATE KEY-----";

        RSAKeyParameters pubKey = (RSAKeyParameters) parser.pubKeyParse(rsaPubKeyWithHead);
        RSAPrivateCrtKeyParameters privKey = (RSAPrivateCrtKeyParameters) parser.privKeyParse(rsaPrivKeyWithHead);

        byte[] pubKeyBytes = RSAUtils.pubKey2Bytes_RawKey(pubKey);
        byte[] privKeyBytes = RSAUtils.privKey2Bytes_RawKey(privKey);

        AsymmetricKeypair asymmetricKeypair = new AsymmetricKeypair(new PubKey(RSA, pubKeyBytes), new PrivKey(RSA, privKeyBytes));
        BlockchainKeypair user = new BlockchainKeypair(asymmetricKeypair.getPubKey(), asymmetricKeypair.getPrivKey());
        System.out.println("user="+user.getAddress());
        txTemp.users().register(user.getIdentity());

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
    }

    /**
     * 利用已知的公私钥构建用户，并提交;
     * ssh-keygen -t ecdsa
     */
    @Test
    public void registerUser_ecdsa_ByExistPubAndPrivKey() {
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);

        //use the existing pubKey and privKey;
        String ecdsaPubKeyWithHead = "ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBEX3TsTK9cSYvnnxI0GhJLKk+6tRUxZt/vNpKuGuLq+o8ri4Eeacscl4en0b1V6xzq16CEsAW3y6wv5uHruFTBs= zgw";
        String ecdsaPrivKeyWithHead = "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIJLuqNFoN03hXKsOLYM5i+lxblchzOOdc83vqHfZOnRooAoGCCqGSM49\n" +
                "AwEHoUQDQgAERfdOxMr1xJi+efEjQaEksqT7q1FTFm3+82kq4a4ur6jyuLgR5pyx\n" +
                "yXh6fRvVXrHOrXoISwBbfLrC/m4eu4VMGw==\n" +
                "-----END EC PRIVATE KEY-----";

        ECPublicKeyParameters pubKey = (ECPublicKeyParameters) parser.pubKeyParse(ecdsaPubKeyWithHead);
        ECPrivateKeyParameters privKey = (ECPrivateKeyParameters) parser.privKeyParse(ecdsaPrivKeyWithHead);

        byte[] privKeyBytes = BigIntegerTo32Bytes(privKey.getD());
        byte[] pubKeyBytes = pubKey.getQ().getEncoded(false);
        AsymmetricKeypair asymmetricKeypair = new AsymmetricKeypair(new PubKey(ECDSA, pubKeyBytes), new PrivKey(ECDSA, privKeyBytes));
        BlockchainKeypair user = new BlockchainKeypair(asymmetricKeypair.getPubKey(), asymmetricKeypair.getPrivKey());
        System.out.println("user="+user.getAddress());
        txTemp.users().register(user.getIdentity());

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
    }

    // To convert BigInteger to byte[] whose length is 32
    private static byte[] BigIntegerTo32Bytes(BigInteger b){
        byte[] tmp = b.toByteArray();
        byte[] result = new byte[32];
        if (tmp.length > result.length) {
            System.arraycopy(tmp, tmp.length - result.length, result, 0, result.length);
        }
        else {
            System.arraycopy(tmp,0,result,result.length-tmp.length,tmp.length);
        }
        return result;
    }

    /**
     * 利用已知的公私钥构建用户，并提交;
     * ssh-keygen -t ecdsa
     */
    @Test
    public void registerUser_ed25519_ByExistPubAndPrivKey() {
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);

        //use the existing pubKey and privKey;
        String ed25519PubKeyWithHead = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIAk6IlhvvyZ4+IiE7qhpJ1ZBpmkRE5zfX6fHtsrNfGXc zgw";
        String ed25519PrivKeyWithHead = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
                "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW\n" +
                "QyNTUxOQAAACAJOiJYb78mePiIhO6oaSdWQaZpEROc31+nx7bKzXxl3AAAAIiIJk/EiCZP\n" +
                "xAAAAAtzc2gtZWQyNTUxOQAAACAJOiJYb78mePiIhO6oaSdWQaZpEROc31+nx7bKzXxl3A\n" +
                "AAAEDH8A3gZJKJLL3LMDtwDa4AW0wvQrkbZu0A06d4Wv4ngAk6IlhvvyZ4+IiE7qhpJ1ZB\n" +
                "pmkRE5zfX6fHtsrNfGXcAAAAA3pndwEC\n" +
                "-----END OPENSSH PRIVATE KEY-----";

        Ed25519PublicKeyParameters pubKey = (Ed25519PublicKeyParameters) parser.pubKeyParse(ed25519PubKeyWithHead);
        Ed25519PrivateKeyParameters privKey = (Ed25519PrivateKeyParameters) parser.privKeyParse(ed25519PrivKeyWithHead);

        byte[] privKeyBytes = privKey.getEncoded();
        byte[] pubKeyBytes = pubKey.getEncoded();

        AsymmetricKeypair asymmetricKeypair = new AsymmetricKeypair(new PubKey(ED25519, pubKeyBytes), new PrivKey(ED25519, privKeyBytes));
        BlockchainKeypair user = new BlockchainKeypair(asymmetricKeypair.getPubKey(), asymmetricKeypair.getPrivKey());
        System.out.println("user="+user.getAddress());
        txTemp.users().register(user.getIdentity());

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
    }
}
