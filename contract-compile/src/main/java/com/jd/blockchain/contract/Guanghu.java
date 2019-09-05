package com.jd.blockchain.contract;

/*广互链码 add by syf 2019.09.03*/

@Contract
public interface Guanghu {
    @ContractEvent(name = "putval")
    String putval(String address, String account, String content);

    @ContractEvent(name = "getval")
    String getval(String address, String account);
}
