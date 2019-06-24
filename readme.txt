1.说明
1.1 编写合约
    +contract-compile
    ++src/main/java/com.jd.blockchain.contract
        根据样例编写合约；
1.2 部署执行合约
    1.2.1 在模块contract-compile中package：com.jd.blockchain.contract下编写合约;
    1.2.2 命令行中执行：mvn clean package ，会在/target中生成相应的合约contract.jar;
    1.2.3 将contract.jar放至如下位置：
        + contract-starter
        ++ src/main/resources/
    1.2.4 修改SDKDemo_Constant.java文件中对应的GW_IPADDR/GW_PORT/PUB_KEYS/PRIV_KEYS为实际值；
    1.2.5 执行文件：SDK_Contract_Demo.java中main方法，位置如下：
        + contract-starter
        ++ src/main/java/com.jd.blockchain.contract/SDK_Contract_Demo.java

