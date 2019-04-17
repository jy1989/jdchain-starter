package com.jd.blockchain.ledger;

import com.jd.blockchain.contract.ContractConfigure;
import com.jd.blockchain.contract.model.ContractDeployExeUtil;
import com.jd.blockchain.crypto.asymmetric.PrivKey;
import com.jd.blockchain.crypto.asymmetric.PubKey;
import com.jd.blockchain.crypto.hash.HashDigest;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.tools.keygen.KeyGenCommand;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.io.FileUtils;
import org.junit.Before;

/**
 * @Author lyy
 * @Date 2019/4/16
 */
public class MyBaseTest {
    BlockchainService bcsrv;
    String gatewayHost;
    int gatewayPort;
    String ledger;
    String ownerPubPath;
    String ownerPrvPath;
    String ownerPassword;

    BlockchainKeyPair ownerKey;
    HashDigest ledgerHash;

    String chainCodePath;

    @Before
    public void setup(){
        gatewayHost = ContractConfigure.instance.values("host");
        gatewayPort = Integer.parseInt(ContractConfigure.instance.values("port"));
        ledger = ContractConfigure.instance.values("ledgerHash");
        ownerPubPath = ContractConfigure.instance.values("ownerPubPath");
        ownerPrvPath = ContractConfigure.instance.values("ownerPrvPath");
        ownerPassword = FileUtils.readText(ContractConfigure.instance.values("ownerPassword"));

        ledgerHash = new HashDigest(Base58Utils.decode(ledger));
        ownerKey = getKeyPair(ownerPubPath, ownerPrvPath, ownerPassword);

        // 合约代码的路径;
        chainCodePath = ContractConfigure.instance.values("chainCodePath");

        // 创建服务代理;
//        NetworkAddress address = new NetworkAddress(gatewayHost, gatewayPort);
//        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(address);
//        bcsrv = serviceFactory.getBlockchainService();
        bcsrv = ContractDeployExeUtil.instance.initBcsrv(gatewayHost,gatewayPort);
    }



    public BlockchainKeyPair getKeyPair(String pubPath, String prvPath, String rawPassword){
        PubKey pub = null;
        PrivKey prv = null;
        try {
            prv = KeyGenCommand.readPrivKey(prvPath, KeyGenCommand.encodePassword(rawPassword));
            pub = KeyGenCommand.readPubKey(pubPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BlockchainKeyPair(pub, prv);
    }
}
