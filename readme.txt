1.说明
1.1 注册用户、添加数据
    1.1.1 修改sys-contract.properties文件，"常规使用"部分相关参数，改为当前工程的相应地址即可。
    1.1.2 执行单元测试IntegrationTest，对应方法如下：
        + registerUser_more;//注册用户；
        + register_dataAccount_more; //添加数据;
        + batchInsertData_Test; //批量插入数据;
        + query_Test; //查询数据;
1.2 部署执行合约
    1.2.1 编译参数配置已经放至pom.xml中;修改<configuration>节点中对应参数与实际一致。
    1.2.2 然后在命令行中执行：mvn clean compile ，会在/target/contract中生成相应的合约;
    1.2.3 修改sys-contract.properties文件中合约相关部分;
    1.2.4 执行单元测试IntegrationTest，对应方法如下：
        + one_deploy_exe_contract_on_test_gateway; //通过网关发布和部署合约
        + one_deploy_exeContract_registerDataAccount_on_test_gateway; //通过网关发布和部署合约+添加数据集;

2.新编写一个合约，如何执行？
2.1 按照AssetContract1代码格式编写合约,比如新编合约名：AssetContract；
2.2 在pom.xml中修改对应参数：比如：<cParam>改为：com.jd.blockchain.contract.AssetContract3等
2.3 sys-contract.properties中确认合约输出位置以及合约执行的输入参数：
chainCodePath=E:\\gitCode\\block\\jdchain-starter\\target\\contract\\AssetContract3.contract
ledgerHash=6Gw3cK4uazegy4HjoaM81ck9NgYLNoKyBMb7a1TK1jt3d
contractArgs=10##4##abc
2.4 执行单元测试，如果结果无异常，则表示一切OK。

3.maven插件中通过ContractAllAutoMojo做简单的编译、发布和执行测试
3.1 仿照AssetContract3.java编写简单合约；
3.2 修改pom.xml文件中<properties>节点中相应参数，一般为“变化频繁的参数”；
3.3 控制台执行：mvn clean compile。

4.mac和win切换时，需要修改的配置信息
4.1 sys-config.properties文件的路径地址；
4.2 pom.xml中cpParam参数地址修改；