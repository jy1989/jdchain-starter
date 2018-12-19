package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.contract.model.ContractDeployExeUtil;
import com.jd.blockchain.crypto.asymmetric.PrivKey;
import com.jd.blockchain.crypto.asymmetric.PubKey;
import com.jd.blockchain.crypto.hash.HashDigest;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.tools.keygen.KeyGenCommand;
import my.utils.codec.Base58Utils;
import my.utils.io.ContractConfigure;
import my.utils.io.FileUtils;
import org.junit.Before;

/**
 * @Author zhaogw
 * @Date 2018/12/4 14:13
 */
public class BaseTest {
    BlockchainService bcsrv;
    String host;
    int port;
    String ledger;
    String ownerPubPath;
    String ownerPrvPath;
    String ownerPassword;
    String chainCodePath;
    String contractPub = null;
    String contractArgs;
    //    String contractDeployAddress;
    String eventName;
    String param1 = "param1";
    String param1Val = "param1Val";
    BlockchainKeyPair ownerKey;
    HashDigest ledgerHash;

    @Before
    public void setup(){
        host = ContractConfigure.instance.values("host");
        port = Integer.parseInt(ContractConfigure.instance.values("port"));;
        ledger = ContractConfigure.instance.values("ledgerHash");
        ownerPubPath = ContractConfigure.instance.values("ownerPubPath");
        ownerPrvPath = ContractConfigure.instance.values("ownerPrvPath");
        ownerPassword = FileUtils.readText(ContractConfigure.instance.values("ownerPassword"));
        chainCodePath = ContractConfigure.instance.values("chainCodePath");
        contractArgs = ContractConfigure.instance.values("contractArgs");
//        contractDeployAddress = ContractConfigure.instance.values("contractDeployAddress");
        eventName = ContractConfigure.instance.values("event");

        register();
        ledgerHash = new HashDigest(Base58Utils.decode(ledger));
        ownerKey = getKeyPair(ownerPubPath, ownerPrvPath, ownerPassword);
        bcsrv = ContractDeployExeUtil.instance.initBcsrv(host,port);
    }

    private void register(){
        DataContractRegistry.register(TransactionContent.class);
        DataContractRegistry.register(TransactionContentBody.class);
        DataContractRegistry.register(TransactionRequest.class);
        DataContractRegistry.register(NodeRequest.class);
        DataContractRegistry.register(EndpointRequest.class);
        DataContractRegistry.register(TransactionResponse.class);
        DataContractRegistry.register(DataAccountKVSetOperation.class);
        DataContractRegistry.register(DataAccountKVSetOperation.KVWriteEntry.class);
        DataContractRegistry.register(Operation.class);
        DataContractRegistry.register(ContractCodeDeployOperation.class);
        DataContractRegistry.register(ContractEventSendOperation.class);
        DataContractRegistry.register(DataAccountRegisterOperation.class);
        DataContractRegistry.register(UserRegisterOperation.class);
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
