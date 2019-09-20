package com.jd.chain.contract;

import com.jd.blockchain.contract.ContractEventContext;
import com.jd.blockchain.contract.EventProcessingAware;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.KVDataEntry;

/*
 * 广互链码实现类 add by syf 2019.09.03
 * */
public class GuanghuImpl implements EventProcessingAware,Guanghu {

    private ContractEventContext eventContext;
    private HashDigest ledgerHash;

    @Override
    public String putval(String address, String account, String content) {
        KVDataEntry[] kvDataEntries=eventContext.getLedger().getDataEntries(ledgerHash,address,account);
        if(kvDataEntries!=null && kvDataEntries.length>0){
            long currVersion = kvDataEntries[0].getVersion();
            if (currVersion > -1) {
                throw new IllegalStateException(String.format("%s -> %s already have created !!!", address, account));
            }
            eventContext.getLedger().dataAccount(address).setText(account,content,-1L);
        }

        //set the random;
        long curRandom = System.currentTimeMillis()%2;
        System.out.println("curRandom="+curRandom);
        eventContext.getLedger().dataAccount(address).setText(account+"-time",curRandom+"",-1L);

        return String.format("DataAccountAddress[%s] -> Create(By Contract Operation) Account = %s and Money = %s Success!!! \r\n",
                address, account, content);
    }

    @Override
    public String getval(String address, String account) {
        KVDataEntry[] kvDataEntries = eventContext.getLedger().getDataEntries(ledgerHash, address, account);
        if (kvDataEntries == null || kvDataEntries.length == 0) {
            return "";
        }
        return kvDataEntries[0].getValue().toString();
    }

    @Override
    public void beforeEvent(ContractEventContext contractEventContext) {
        this.eventContext = contractEventContext;
        this.ledgerHash = contractEventContext.getCurrentLedgerHash();
    }

    @Override
    public void postEvent(ContractEventContext contractEventContext, Exception e) {

    }
}
