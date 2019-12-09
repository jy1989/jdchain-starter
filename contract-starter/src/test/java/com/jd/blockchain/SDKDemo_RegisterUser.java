package com.jd.blockchain;

import com.jd.blockchain.contract.SDKDemo_Constant;
import com.jd.blockchain.contract.SDK_Base_Demo;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.*;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhaogw
 * date 2019/12/9 14:21
 */
public class SDKDemo_RegisterUser extends SDK_Base_Demo {
    //because it need to connect the web, so make the switch;
    private boolean isTest = true;
    private String strDataAccount;
    private BlockchainKeypair existUser;

    @Before
    public void setup(){
        existUser = BlockchainKeyGenerator.getInstance().generate();
    }

    private void registerRole(String roleName){
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);

        // 定义角色权限；
        txTemp.security().roles().configure(roleName).enable(LedgerPermission.APPROVE_TX)
                .enable(LedgerPermission.REGISTER_USER).enable( LedgerPermission.REGISTER_DATA_ACCOUNT)
                .enable(LedgerPermission.WRITE_DATA_ACCOUNT)
                .enable(TransactionPermission.DIRECT_OPERATION);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
    }

    /**
     * rigister the exist user to ledger;
     */
    private void registerExistUser(String roleName){
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
        System.out.println("user'id="+existUser.getAddress());

        txTemp.users().register(existUser.getIdentity());
//        txTemp.security().authorziations().forUser(newUser.getIdentity()).authorize("ROLE-ADD-DATA").setPolicy(RolesPolicy.INTERSECT);
        txTemp.security().authorziations().forUser(existUser.getIdentity()).unauthorize("DEFAULT").authorize(roleName);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
        System.out.println("registerExistUser() done.");
    }

    private void checkInsertDataByExistUser() {
        System.out.println("checkInsertDataByExistUser() start...");
        if(!isTest) return;
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
        //采用KeyGenerator来生成BlockchainKeypair;
        BlockchainKeypair dataAccount = BlockchainKeyGenerator.getInstance().generate();

        txTemp.dataAccounts().register(dataAccount.getIdentity());
        txTemp.dataAccount(dataAccount.getAddress()).setText("key1","value1",-1);
        //add some data for retrieve;
        this.strDataAccount = dataAccount.getAddress().toBase58();
        System.out.println("current dataAccount="+dataAccount.getAddress());
        txTemp.dataAccount(dataAccount.getAddress()).setText("cc-fin01-01","{\"dest\":\"KA001\",\"id\":\"cc-fin01-01\",\"items\":\"FIN001|5000\",\"source\":\"FIN001\"}",-1);

        // TX 准备就绪
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(existUser);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        if(transactionResponse.isSuccess()){
            System.out.println("result="+transactionResponse.isSuccess());
        }else {
            System.out.println("exception="+transactionResponse.getExecutionState().toString());
        }
    }

    private void writeDataAcount() {
        boolean SECURE = false;
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(SDKDemo_Constant.GW_IPADDR, SDKDemo_Constant.GW_PORT, SECURE,
                existUser);
        BlockchainService service2 = serviceFactory.getBlockchainService();
        HashDigest[] ledgerHashs = service2.getLedgerHashs();

        TransactionTemplate txTemp = service2.newTransaction(ledgerHashs[0]);
        BlockchainKeypair dataAccount = BlockchainKeyGenerator.getInstance().generate();
        txTemp.dataAccounts().register(dataAccount.getIdentity());
        txTemp.dataAccount(dataAccount.getAddress()).setText("key2", "value2", -1);

        // TX 准备就绪
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(existUser);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        if(transactionResponse.isSuccess()){
            System.out.println("writeDataAcount() done.");
        }else {
            System.out.println(transactionResponse.getExecutionState());
        }

    }

    @Test
    public void checkPermission(){
        String roleName = "ROLE-ADD-DATA-3";
        registerRole(roleName);
        registerExistUser(roleName);
//        checkInsertDataByExistUser();
        writeDataAcount();
    }
}
