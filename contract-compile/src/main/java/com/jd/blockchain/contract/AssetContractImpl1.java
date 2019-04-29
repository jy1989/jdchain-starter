package com.jd.blockchain.contract;

import com.jd.blockchain.contract.model.*;
import com.jd.blockchain.crypto.hash.HashDigest;
import com.jd.blockchain.ledger.AccountHeader;
import com.jd.blockchain.ledger.KVDataEntry;
import com.jd.blockchain.ledger.KVDataObject;
import com.jd.blockchain.utils.BaseConstant;

import java.math.BigDecimal;

/**
 * 资产智能合约（此样例仅供学习使用）;
 * 针对git用户dengjinhmm（https://github.com/dengjinhmm）在jdchain项目中反馈的issue15进行验证;
 * 说明：
 * 1）在测试时，首先需要修改ASSET_ADDRESS对应值（选择已经在链上已有的数据账户）;
 * 2) 执行需要分为几个步骤：
 * 2.1）构建fromAddr的金额；sys-contract.properties中配置：event = issue-asset，contractArgs=1000##fromAddr
 * 2.2）构建toAddr的金额；sys-contract.properties中配置：event = issue-asset，contractArgs=500##toAddr
 * 2.3）构建转账交易；sys-contract.properties中配置：event = transfer-asset，contractArgs=fromAddr##toAddr##20
 * 2.4）在gateway的浏览器中查看此数据账户，可以看到相应的fromAddr和toAddr的金额变化；
 *
 * @author zhaoguangwei
 * @date 2019-04-29 10:08
 */
@Contract
public class AssetContractImpl1 implements EventProcessingAwire {
    // 资产管理账户的地址;
    private static final String ASSET_ADDRESS = "5Sm6d4bfzzGq84UM6NYcWWc9QtkqgsB8mUFB";
    // 保存资产总数的键
    private static final String KEY_TOTAL = "TV_TOTAL";

    /**
     * 发行资产；
     * @param eventContext 合约事件上下文;
     * @throws Exception
     */
    @ContractEvent(name = "issue-asset")
    public void issue(ContractEventContext eventContext) {
        System.out.println("in AssetContractImpl issue()");
        //checkAllOwnersAgreementPermission();
        byte [] args_ = eventContext.getArgs();
        if(args_ == null){
            return;
        }
        String[] args = new String(args_).split(BaseConstant.DELIMETER_DOUBLE_ALARM);
        // 新发行的资产数量;
        long amount = Long.parseLong(args[0]);
        // 新发行的资产的持有账户；
        String assetHolderAddress = args[1];
        if (amount < 0) {
            throw new ContractException("The amount is negative!");
        }
        if (amount == 0) {
            return;
        }

        HashDigest hashDigest = eventContext.getCurrentLedgerHash();
        // 校验持有者账户的有效性;
        AccountHeader holderAccount = eventContext.getLedger().getDataAccount(hashDigest, assetHolderAddress);
        if (holderAccount == null) {
            throw new ContractException("The holder is not exist!");
        }

        // 设置总量
        eventContext.getLedger().dataAccount(ASSET_ADDRESS).set(KEY_TOTAL, amount, -1);
        // 分配资产到持有者账户
        eventContext.getLedger().dataAccount(ASSET_ADDRESS).set(assetHolderAddress, amount, -1);

    }

    @ContractEvent(name = "transfer-asset")
    public void transfer(ContractEventContext eventContext) {
        System.out.println("in AssetContractImpl transfer()");
        byte [] args_ = eventContext.getArgs();
        if(args_ == null){
            return;
        }
        String[] args = new String(args_).split(BaseConstant.DELIMETER_DOUBLE_ALARM);
        System.out.println("arg_" + new String(args_));
        // 资产转出的账户;
        String fromAddress = args[0];
        // 校验"转出账户"是否已签名;
        //checkSignerPermission(fromAddress);

        // 资产转入的账户;
        String toAddress = args[1];

        // 转账的资产数量;
        BigDecimal amount = new BigDecimal(args[2]);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ContractException("The amount is negative!");
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        HashDigest hashDigest = eventContext.getCurrentLedgerHash();

        // 查询现有的余额;
        KVDataEntry[] kvEntries = eventContext.getLedger().getDataEntries(hashDigest, ASSET_ADDRESS, KEY_TOTAL,
                fromAddress, toAddress);
        KVDataObject currTotal = (KVDataObject) kvEntries[0];
        KVDataObject fromBalance = (KVDataObject) kvEntries[1];
        KVDataObject toBalance = (KVDataObject) kvEntries[2];
        System.out.println("currTotal: " + currTotal.getValue());

        // 检查是否余额不足;
        if ((new BigDecimal(fromBalance.getValue().toString()).compareTo(amount) < 0)) {
            throw new ContractException("Insufficient balance!");
        }

        // 把数据的更改写入到账本;
        BigDecimal newFromBalance = new BigDecimal(fromBalance.getValue().toString()).subtract(amount).setScale(8);
        System.out.println("newFromBalance: " + newFromBalance);
        BigDecimal newToBalance;
        if (toBalance.isNil()) {
            newToBalance = amount;
        } else {
            newToBalance = new BigDecimal(toBalance.getValue().toString()).add(amount);
        }
        System.out.println("newToBalance: " + newToBalance);

        // 转移from资产到to账户
        long fromVersion = fromBalance.getVersion();
        long toVersion = toBalance.getVersion();
        System.out.println("fromVersion = " + fromVersion);
        System.out.println("toVersion = " + toVersion);
        System.out.println("newFromBalance.toString()="+newFromBalance.toString());
        System.out.println("newToBalance.toString()="+newToBalance.toString());
        eventContext.getLedger().dataAccount(ASSET_ADDRESS).set(fromAddress, newFromBalance.toString(), fromVersion)
                .getOperation();
        eventContext.getLedger().dataAccount(ASSET_ADDRESS).set(toAddress, newToBalance.toString(), toVersion)
                .getOperation();
    }

    @Override
    public void beforeEvent(ContractEventContext contractEventContext) {

    }

    @Override
    public void postEvent(ContractEventContext contractEventContext, ContractException e) {

    }

    @Override
    public void postEvent(ContractException e) {

    }

    @Override
    public void postEvent() {

    }
}
