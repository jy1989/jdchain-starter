# JDChain创建新用户连接网关
## 1.参数列表
| 序号 | key | value | 备注 |
| ------ | ------ | ------ | ------ |
| 1 | jdchain-starter项目地址 | https://github.com/blockchain-jd-com/jdchain-starter |
| 2 | 测试用pubKeyStr | 3snPdw7i7PXwePqNj5f6ZbYBGXSHbH4XJefqf735WtcDQvkDdwPSGU |
| 3 | 测试用privKeyStr | 177gjvtAB5pQ23Kpw1icRcYa8vRg5aQiWfJzwj5AbD4hJbMnN8eLtcvkaf5w2T4poU5imR9 |
| 4 | 测试用Passwd | DYu3G8aGTMBW1WrTw76zxQJQU4DHLw9MLyy7peG4LKkY |
| 5 | 参考视频 |  |

## 2.简介
本文的测试样例为SDKDemo_RegisterUser.java。  
通过管理工具或者手工方式来构建JDChain区块链时，需要事先生成四组公私钥，然后填充至配置文件，最终注册到区块链中。近期物流研发部的马威同学，提到一个问题：想让其他用户使用sdk连接区块链，但是又不想将自己的公私钥信息给对方，如何处理？（2019.12.4）。这是一个很常用的场景，本文就针对这个问题给出一些详细的说明，方便广大使用者能够快速掌握。在此对马威的热心参与表示感谢。

## 3.步骤
### 3.1前提假设
假定已经构建完成JDChain区块链环境，并且通过绑定4个参与方之一的公私钥可以正常通过SDK访问到JDChain。
### 3.2创建一个新用户
通过bin/keygen.sh可获得，格式：sh keygen.sh –n xxx  
生成的公私钥格式见：参数列表-2/3/4。  
### 3.3组织BlockchainKeypair对象
创建的这个新用户与区块链是隔离的，我们需要注册这个用户，链才能感知到它的存在。  
参照：jdchain-starter中SDKDemo_RegisterUser.java的private BlockchainKeypair getExistUser(){方法构建keypair对象。格式如下：  
```
private BlockchainKeypair getExistUser(){
//        通过bin/keygen.sh可获得如下公私钥，格式：sh keygen.sh –n xxx
        String pubKeyStr = "3snPdw7i7PXwePqNj5f6ZbYBGXSHbH4XJefqf735WtcDQvkDdwPSGU";
        String privKeyStr = "177gjvtAB5pQ23Kpw1icRcYa8vRg5aQiWfJzwj5AbD4hJbMnN8eLtcvkaf5w2T4poU5imR9";
        String passwd = "DYu3G8aGTMBW1WrTw76zxQJQU4DHLw9MLyy7peG4LKkY";
        // 生成连接网关的账号
        PrivKey privKey = KeyGenUtils.decodePrivKey(privKeyStr, passwd);
        PubKey pubKey = KeyGenUtils.decodePubKey(pubKeyStr);
        return new BlockchainKeypair(pubKey, privKey);
    }
```
### 3.4注册权限
将这个BlockchainKeypair对象注册进链。
参照：jdchain-starter中SDKDemo_RegisterUser.java的private void registerRole(String roleName)方法。格式如下：
```
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
```
### 3.5注册进链
将这个BlockchainKeypair对象注册进链。
参照：jdchain-starter中SDKDemo_RegisterUser.java的public void registerExistUser(String roleName)方法。格式如下：
```
private void registerExistUser(String roleName){
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
        System.out.println("user'id="+existUser.getAddress());

        txTemp.users().register(existUser.getIdentity());
        txTemp.security().authorziations().forUser(existUser.getIdentity()).unauthorize("DEFAULT").authorize(roleName);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
    }
```
### 3.6使用新用户连接SDK并签名
此时existUser用户已经上链，下一步就可以采用这个用户签名，连接网关。
参照：jdchain-starter中SDKDemo_RegisterUser.java的private void writeDataAcount()方法。格式如下：
```
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
```
### 3.7整体执行样例
上述过程完成之后，即可来验证整个授权过程。可以修改registerRole(roleName)内部的不同角色来进行验证。如果想进行简单验证，只需使用动态生成用户即可。  
参照：jdchain-starter中SDKDemo_RegisterUser.java的public void checkPermission()方法。格式如下：
```
@Test
    public void checkPermission(){
        String roleName = "ROLE-ADD-DATA";
        registerRole(roleName);
        registerExistUser(roleName);
        writeDataAcount();
    }
```
