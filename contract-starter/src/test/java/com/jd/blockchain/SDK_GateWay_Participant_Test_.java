package com.jd.blockchain;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.contract.SDKDemo_Constant;
import com.jd.blockchain.crypto.*;
import com.jd.blockchain.ledger.*;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;
import com.jd.blockchain.tools.keygen.KeyGenCommand;
import com.jd.blockchain.utils.net.NetworkAddress;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 注册参与方测试
 * @author zhangshuang
 * @create 2019/7/4
 * @since 1.0.0
 */

public class SDK_GateWay_Participant_Test_ {

    private PrivKey privKey;
    private PubKey pubKey;

    private BlockchainKeypair CLIENT_CERT = null;

    private String GATEWAY_IPADDR = null;

    private int GATEWAY_PORT;

    private boolean SECURE;

    private BlockchainService service;

    //根据密码工具产生的公私钥
    static String PUB = "3snPdw7i7PX5gizwwxqsXcdBc617EQqVkiLMbzNGoZzhzqTbPDze6d";
    String PRIV = "177gjwrexzfdB4TwNZTqTPWFETaJfj9iDM32ryn1Dqu9XYtQ3xFPh33KJijJuALzdpxQcxk";

    @Before
    public void init() {

        privKey = SDKDemo_Constant.gwPrivkey0;
        pubKey = SDKDemo_Constant.gwPubKey0;

        CLIENT_CERT = new BlockchainKeypair(SDKDemo_Constant.gwPubKey0, SDKDemo_Constant.gwPrivkey0);
        GATEWAY_IPADDR = "192.168.151.39";
        GATEWAY_PORT = 18081;
        SECURE = false;
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(GATEWAY_IPADDR, GATEWAY_PORT, SECURE,
                CLIENT_CERT);
        service = serviceFactory.getBlockchainService();

        DataContractRegistry.register(TransactionContent.class);
        DataContractRegistry.register(TransactionContentBody.class);
        DataContractRegistry.register(TransactionRequest.class);
        DataContractRegistry.register(NodeRequest.class);
        DataContractRegistry.register(EndpointRequest.class);
        DataContractRegistry.register(TransactionResponse.class);
    }

    @Test
    public void registerParticipant_Test() {
        HashDigest[] ledgerHashs = service.getLedgerHashs();
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = service.newTransaction(ledgerHashs[0]);

        //existed signer
        AsymmetricKeypair keyPair = new BlockchainKeypair(pubKey, privKey);

        PrivKey privKey = SDKDemo_Constant.gwPrivkey0;

        PubKey pubKey = SDKDemo_Constant.gwPubKey0;

        System.out.println("Address = "+ AddressEncoding.generateAddress(pubKey));

        BlockchainKeypair user = new BlockchainKeypair(pubKey, privKey);

        NetworkAddress networkAddress = new NetworkAddress(GATEWAY_IPADDR, 20000);

        // 注册参与方
        txTemp.participants().register("5.com", user.getIdentity(), networkAddress);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();

        // 使用私钥进行签名；
        prepTx.sign(keyPair);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        assertTrue(transactionResponse.isSuccess());
    }

    /**
     * first join the ledger, then handle the init, then update the state;
     */
    @Test
    public void updateParticipantState_Test() {
        HashDigest[] ledgerHashs = service.getLedgerHashs();
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = service.newTransaction(ledgerHashs[0]);

        //existed signer
        AsymmetricKeypair keyPair = new BlockchainKeypair(pubKey, privKey);

        PrivKey privKey = SDKDemo_Constant.gwPrivkey0;

        PubKey pubKey = SDKDemo_Constant.gwPubKey0;

        System.out.println("Address = "+AddressEncoding.generateAddress(pubKey));

        BlockchainKeypair user = new BlockchainKeypair(pubKey, privKey);

        NetworkAddress networkAddress = new NetworkAddress("127.0.0.1", 20000);

        txTemp.states().update(user.getIdentity(),networkAddress, ParticipantNodeState.ACTIVED);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();

        // 使用私钥进行签名；
        prepTx.sign(keyPair);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        assertTrue(transactionResponse.isSuccess());

    }

}
