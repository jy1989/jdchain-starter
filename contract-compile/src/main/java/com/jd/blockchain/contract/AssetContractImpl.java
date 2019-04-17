package com.jd.blockchain.contract;

import com.jd.blockchain.contract.model.*;
import com.jd.blockchain.crypto.hash.HashDigest;
import com.jd.blockchain.ledger.*;
import com.jd.blockchain.utils.BaseConstant;

/**
 * 资产智能合约;
 */
@Contract
public class AssetContractImpl implements EventProcessingAwire {
    // 资产管理账户的地址;
    private static final String ASSET_ADDRESS = "5Sm1VK2RowVN3GVdicCkGMrwVt4pfjn4SGm3";
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
        //checkAllOwnersAgreementPermission();
        byte [] args_ = eventContext.getArgs();
        if(args_ == null){
            return;
        }
        String[] args = new String(args_).split(BaseConstant.DELIMETER_DOUBLE_ALARM);
        // 资产转出的账户;
        String fromAddress = args[0];
        // 校验"转出账户"是否已签名;
        //checkSignerPermission(fromAddress);

        // 资产转入的账户;
        String toAddress = args[1];

        // 转账的资产数量;
        long amount = Long.parseLong(args[2]);
        if (amount < 0) {
            throw new ContractException("The amount is negative!");
        }
        if (amount == 0) {
            return;
        }

        HashDigest hashDigest = eventContext.getCurrentLedgerHash();

        // 查询现有的余额;
//		Set<String> keys = new HashSet<>();
//		keys.add(fromAddress);
//		keys.add(toAddress);
//		StateMap origBalances = eventContext.getLedger().getStates(hashDigest, ASSET_ADDRESS, keys);
//		KVDataObject fromBalance = origBalances.get(fromAddress);
//		KVDataObject toBalance = origBalances.get(toAddress);

        KVDataEntry[] kvEntries = eventContext.getLedger().getDataEntries(hashDigest, ASSET_ADDRESS, KEY_TOTAL, fromAddress, toAddress);
        KVDataObject currTotal = (KVDataObject) kvEntries[0];
        KVDataObject fromBalance = (KVDataObject) kvEntries[1];
        KVDataObject toBalance = (KVDataObject) kvEntries[2];
        System.out.println("currTotal: " + currTotal.longValue());

        // 检查是否余额不足;
        if ((fromBalance.longValue() - amount) < 0) {
            throw new ContractException("Insufficient balance!");
        }

        // 把数据的更改写入到账本;
//		 SimpleStateMap newBalances = new SimpleStateMap(origBalances.getAccount(),
//		 origBalances.getAccountVersion(),
//		 origBalances.getStateVersion());
        long newFromBalance = fromBalance.longValue() - amount;
        System.out.println("newFromBalance: " + newFromBalance);
        long newToBalance;
        if (toBalance.isNil()) {
            newToBalance = amount;
        } else {
            newToBalance = toBalance.longValue() + amount;
        }
        System.out.println("newToBalance: " + newToBalance);

//		 newBalances.setValue(newFromBalance);
//		 newBalances.setValue(newToBalance);
//
//		 eventContext.getLedger().updateState(ASSET_ADDRESS).setStates(newBalances);
        // 转移from资产到to账户
        long fromVersion = fromBalance.getVersion();
        long toVersion = toBalance.getVersion();
        System.out.println("fromVersion = "+fromVersion);
        System.out.println("toVersion = "+toVersion);
        eventContext.getLedger().dataAccount(ASSET_ADDRESS).set(fromAddress, newFromBalance, fromVersion);
        eventContext.getLedger().dataAccount(ASSET_ADDRESS).set(toAddress, newToBalance, toVersion);
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
