#JDChain创建新用户连接网关

## 1.参数列表
| 序号 | key | value | 备注 |
| ------ | ------ | ------ | ------ |
| 1 | jdchain-starter项目地址 | https://github.com/blockchain-jd-com/jdchain-starter |
| 2 | 测试用pubKeyStr | 3snPdw7i7PXwePqNj5f6ZbYBGXSHbH4XJefqf735WtcDQvkDdwPSGU |
| 3 | 测试用privKeyStr | 177gjvtAB5pQ23Kpw1icRcYa8vRg5aQiWfJzwj5AbD4hJbMnN8eLtcvkaf5w2T4poU5imR9 |
| 4 | 测试用Passwd | DYu3G8aGTMBW1WrTw76zxQJQU4DHLw9MLyy7peG4LKkY |
| 5 | 参考视频 | https://3.cn/VCWQfqhYt |

## 2.简介
通过管理工具或者手工方式来构建JDChain区块链时，需要事先生成四组公私钥，然后填充至配置文件，最终注册到区块链中。近期物流研发部的马威同学，提到一个问题：想让其他用户使用sdk连接区块链，但是又不想将自己的公私钥信息给对方，如何处理？（2019.12.4）。这是一个很常用的场景，本文就针对这个问题给出一些详细的说明，方便广大使用者能够快速掌握。在此对马威的热心参与表示感谢。

## 3.步骤
### 3.1前提假设
假定已经构建完成JDChain区块链环境，并且通过绑定4个参与方之一的公私钥可以正常通过SDK访问到JDChain。
### 3.2创建一个新用户
通过bin/keygen.sh可获得，格式：sh keygen.sh –n xxx  
生成的公私钥格式见：参数列表-2/3/4。  
### 3.3组织BlockchainKeypair对象
创建的这个新用户与区块链是隔离的，我们需要注册这个用户，链才能感知到它的存在。
参照：jdchain-starter中SDKTest.java的BlockchainKeypair getExistUser()方法构建keypair对象。格式如下：  
```
private BlockchainKeypair getExistUser(){
        String pubKeyStr = "3snPdw7i7PXwePqNj5f6ZbYBGXSHbH4XJefqf735WtcDQvkDdwPSGU";
        String privKeyStr = "177gjvtAB5pQ23Kpw1icRcYa8vRg5aQiWfJzwj5AbD4hJbMnN8eLtcvkaf5w2T4poU5imR9";
        String passwd = "DYu3G8aGTMBW1WrTw76zxQJQU4DHLw9MLyy7peG4LKkY";
        // 生成连接网关的账号
        PrivKey privKey = KeyGenUtils.decodePrivKey(privKeyStr, passwd);
        PubKey pubKey = KeyGenUtils.decodePubKey(pubKeyStr);
        return new BlockchainKeypair(pubKey, privKey);
}
```
### 3.4注册进链
将这个BlockchainKeypair对象注册进链。
参照：jdchain-starter中SDKTest.java的public void registerExistUser()方法。格式如下：
```
public void registerExistUser(){
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
        BlockchainKeypair existUser = getExistUser();
        System.out.println("user'id="+existUser.getAddress());
        txTemp.users().register(existUser.getIdentity());
        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(adminKey);

        // 提交交易；
        prepTx.commit();
    }
```
### 3.5使用新用户签名连接SDK
此时existUser用户已经上链，下一步就可以采用这个用户签名，连接网关。
参照：jdchain-starter中SDKTest.java的public void registerNewUserByExistUser()方法。格式如下：
```
    public void registerNewUserByExistUser(){
        //first invoke the case: registerExistUser(), to rigister user in the ledger;
        //now use the existUser to sign;
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);

        BlockchainKeypair user = BlockchainKeyGenerator.getInstance().generate();
        System.out.println("user'id="+user.getAddress());
        txTemp.users().register(user.getIdentity());
        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(getExistUser());

        // 提交交易；
        prepTx.commit();
    }
```
