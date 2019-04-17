package com.jd.blockchain.ledger;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

/**
 *
 * @Author lyy
 * @Date 2018/4/16
 */
public class MyIntegrationTest extends MyBaseTest{
    private int loopNum = 0;
    private boolean useLoop = false;

    class RegisterUserResult {
        TransactionResponse txRes;
        BlockchainKeyPair userPair;

        public TransactionResponse getTxRes() {
            return txRes;
        }

        public void setTxRes(TransactionResponse txRes) {
            this.txRes = txRes;
        }

        public BlockchainKeyPair getUserPair() {
            return userPair;
        }

        public void setUserPair(BlockchainKeyPair userPair) {
            this.userPair = userPair;
        }
    }

    /**
     * 注册用户;
     * @return RegisterUserResult
     */
    private RegisterUserResult registerUser() {
        // 随机生成一个用户的公私钥对;
        BlockchainKeyPair userPair = BlockchainKeyGenerator.getInstance().generate();
        System.out.println("userPair-address:" + userPair.getAddress());
        System.out.println("userPair-pubKey:" + userPair.getPubKey());
        System.out.println("userPair-priKey:" + userPair.getPrivKey());

        // 在本地定义注册用户的交易;
        TransactionTemplate txTpl = bcsrv.newTransaction(ledgerHash);
        txTpl.users().register(userPair.getIdentity());

        // 准备交易;
        PreparedTransaction preTx = txTpl.prepare();

        //CryptoKeyPair keyPair = getSponsorKey();
        // 使用网关的key签名;
        preTx.sign(ownerKey);

        // 提交交易并等待共识返回;
        TransactionResponse txResp = preTx.commit();

        RegisterUserResult result = new RegisterUserResult();
        result.setTxRes(txResp);
        result.setUserPair(userPair);
        return result;
    }

    /**
     * 测试注册用户;
     * @throws InterruptedException
     */
    @Test
    public void testRegisterUser() throws InterruptedException {
        RegisterUserResult result = registerUser();
        // 验证返回结果;
        assertTrue(result.getTxRes().isSuccess());
        // 休息2秒;
        Thread.sleep(2000);
        // 查询验证是否有此用户
        UserInfo userInfo = bcsrv.getUser(ledgerHash,result.getUserPair().getAddress().toBase58());
        Assert.assertEquals(result.getUserPair().getAddress(),userInfo.getAddress());
    }

    @Test
    public void query_Test() {
        LedgerInfo ledgerInfo = bcsrv.getLedger(ledgerHash);
        long ledgerNumber = ledgerInfo.getLatestBlockHeight();
        System.out.println(ledgerNumber);
    }

    /**
     * 注册数据账户;
     * @return RegisterUserResult
     */
    private RegisterUserResult registerDataAccount() {
        // 随机生成一个数据账户的公私钥对;
        BlockchainKeyPair userPair = BlockchainKeyGenerator.getInstance().generate();
        System.out.println("userPair-address:" + userPair.getAddress());
        System.out.println("userPair-pubKey:" + userPair.getPubKey());
        System.out.println("userPair-priKey:" + userPair.getPrivKey());

        // 在本地定义注册数据账户的交易;
        TransactionTemplate txTpl = bcsrv.newTransaction(ledgerHash);
        txTpl.dataAccounts().register(userPair.getIdentity());


        // 准备交易;
        PreparedTransaction preTx = txTpl.prepare();

        // 使用网关的key签名;
        preTx.sign(ownerKey);

        // 提交交易并等待共识返回;
        TransactionResponse txResp = preTx.commit();

        RegisterUserResult result = new RegisterUserResult();
        result.setTxRes(txResp);
        result.setUserPair(userPair);
        return result;
    }

    /**
     * 注册数据账户;
     * @throws InterruptedException
     */
    @Test
    public void testRegisterDataAccount() throws InterruptedException {
        RegisterUserResult result = registerDataAccount();
        // 验证返回结果;
        assertTrue(result.getTxRes().isSuccess());
        // 休息2秒;
        Thread.sleep(2000);
        // 查询验证是否有此数据账户
        AccountHeader dataAccount = bcsrv.getDataAccount(ledgerHash,result.getUserPair().getAddress().toBase58());
        Assert.assertEquals(result.getUserPair().getAddress(),dataAccount.getAddress());
    }


    private byte[] getChainCode(String path) {
        byte[] chainCode = null;
        File file;
        FileInputStream input = null;

        try {
            file = new File(path);
            input = new FileInputStream(file);
            chainCode = new byte[input.available()];
            input.read(chainCode);
        } catch (IOException var14) {
            var14.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        return chainCode;
    }



    /**
     * 在链上发布合约;
     *
     */
    private boolean deployContract(){
        // 在本地定义TX模板
        TransactionTemplate txTemp = bcsrv.newTransaction(ledgerHash);

        // 合约内容读取
        byte[] contractBytes = getChainCode(chainCodePath);

        // 生成用户
        //BlockchainIdentityData blockchainIdentity = new BlockchainIdentityData(ownerKey.getPubKey());
        BlockchainKeyPair blockchainKeyPair = BlockchainKeyGenerator.getInstance().generate();
        BlockchainIdentity blockchainIdentity = new BlockchainIdentityData(blockchainKeyPair.getPubKey());

        // 发布合约
        txTemp.contracts().deploy(blockchainIdentity, contractBytes);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();

        // 使用私钥进行签名
        prepTx.sign(ownerKey);

        // 提交交易
        TransactionResponse transactionResponse = prepTx.commit();

        // 打印合约地址
        System.out.println(blockchainIdentity.getAddress().toBase58());

        return transactionResponse.isSuccess();
    }

//    private boolean deployContract(){
//        boolean deployResult = ContractDeployExeUtil.instance.deploy(gatewayHost, gatewayPort, ledger,
//                ownerPubPath, ownerPrvPath, ownerPassword, chainCodePath, ownerPubPath);
//        return deployResult;
//    }

//    private boolean executeContract(){
//        boolean exeResult = ContractDeployExeUtil.instance.exeContract(ledger, ownerPubPath, ownerPrvPath,
//                ownerPassword, eventName,contractArgs);
//        return exeResult;
//    }

    /**
     * 在链上发布合约;
     */
    @Test
    public void testDeployContract() {
        boolean result = deployContract();
        // 验证返回结果;
        assertTrue(result);
    }

    /**
     * 在链上执行合约;
     *
     */
    private boolean executeContractIssue() {
        // 在本地定义 TX 模板
        TransactionTemplate txTemp = bcsrv.newTransaction(ledgerHash);
        // 合约地址
        String contractAddressBase58 = "5SmBnr7BJFEM2eGgWU4q99GDa6jix9M64MUw";
        // Event
        String event = "issue-asset";
        // args(注意参数的格式)
        byte[] args = "210000000##5SmKGLVW8dkgko1fapqzgFAUVGLX4nDHCHwk".getBytes();
        // 提交合约执行代码
        txTemp.contractEvents().send(contractAddressBase58, event, args); // TX 准备就绪;
        PreparedTransaction prepTx = txTemp.prepare();
        // 生成私钥并使用私钥进行签名;
        prepTx.sign(ownerKey);
        // 提交交易;
        TransactionResponse transactionResponse = prepTx.commit();

        return transactionResponse.isSuccess();
    }

    private boolean executeContractTransfer() {
        // 在本地定义 TX 模板
        TransactionTemplate txTemp = bcsrv.newTransaction(ledgerHash);
        // 合约地址
        String contractAddressBase58 = "5SmBnr7BJFEM2eGgWU4q99GDa6jix9M64MUw";
        // Event
        String event = "transfer-asset";
        // args(注意参数的格式)
        byte[] args = "5SmKGLVW8dkgko1fapqzgFAUVGLX4nDHCHwk##5SmMzH9mFKs7xhCVcHfpjPJhHmdUYHXUyLBG##500".getBytes();
        // 提交合约执行代码
        txTemp.contractEvents().send(contractAddressBase58, event, args); // TX 准备就绪;
        PreparedTransaction prepTx = txTemp.prepare();
        // 生成私钥并使用私钥进行签名;
        prepTx.sign(ownerKey);
        // 提交交易;
        TransactionResponse transactionResponse = prepTx.commit();

        return transactionResponse.isSuccess();
    }

    @Test
    public void testExecContractIssue(){
        boolean result = executeContractIssue();
        // 验证返回结果;
        assertTrue(result);
    }

    @Test
    public void testExecContractTransfer(){
        boolean result = executeContractTransfer();
        // 验证返回结果;
        assertTrue(result);
    }
}

