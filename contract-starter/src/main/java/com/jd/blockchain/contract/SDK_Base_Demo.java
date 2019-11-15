package com.jd.blockchain.contract;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.KeyGenUtils;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.ledger.*;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;

public abstract class SDK_Base_Demo {

    protected BlockchainKeypair adminKey;

    protected HashDigest ledgerHash;

    protected BlockchainService blockchainService;

    public SDK_Base_Demo() {
        init();
    }

    public void init() {
        // 生成连接网关的账号
        adminKey = SDKDemo_Constant.adminKey;

        // 连接网关
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(SDKDemo_Constant.GW_IPADDR,
                SDKDemo_Constant.GW_PORT, false, adminKey);

        // 获取网关对应的Service处理类
        blockchainService = serviceFactory.getBlockchainService();

        HashDigest[] ledgerHashs = blockchainService.getLedgerHashs();
        // 获取当前账本Hash
        ledgerHash = ledgerHashs[0];
    }

    public TransactionResponse commit(TransactionTemplate txTpl) {
        PreparedTransaction ptx = txTpl.prepare();
        ptx.sign(adminKey);
        return ptx.commit();
    }

    /**
     * 生成一个区块链用户，并注册到区块链；
     */
    public BlockchainKeypair registerUser() {
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
        BlockchainKeypair user = BlockchainKeyGenerator.getInstance().generate();
        System.out.println("user'id="+user.getAddress());
        txTemp.users().register(user.getIdentity());
        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
        return user;
    }

    /**
     * 生成一个区块链用户，并注册到区块链；
     */
    public BlockchainKeypair registerUserByNewSigner(BlockchainKeypair signer) {
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
        BlockchainKeypair user = BlockchainKeyGenerator.getInstance().generate();
        System.out.println("user'id="+user.getAddress());
        txTemp.users().register(user.getIdentity());
        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(signer);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        return user;
    }
}
