package com.jd.blockchain.contract;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.ledger.BlockchainKeyGenerator;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.TransactionTemplate;
import com.jd.blockchain.sdk.BlockchainTransactionService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;
import com.jd.blockchain.utils.net.NetworkAddress;

public class SDKDemo_User extends SDK_Base_Demo{

	public static void main(String[] args) {
		SDKDemo_User sdkDemo_user = new SDKDemo_User();
		sdkDemo_user.registerUser();
	}
	/**
	 * 生成一个区块链用户，并注册到区块链；
	 */
	public void registerUser() {
		// 在本地定义注册账号的 TX；
		TransactionTemplate txTemp = blockchainService.newTransaction(ledgerHash);
		SignatureFunction signatureFunction = Crypto.getSignatureFunction("ED25519");
		AsymmetricKeypair cryptoKeyPair = signatureFunction.generateKeypair();
		BlockchainKeypair user = new BlockchainKeypair(cryptoKeyPair.getPubKey(), cryptoKeyPair.getPrivKey());

		txTemp.users().register(user.getIdentity());

		// TX 准备就绪；
		PreparedTransaction prepTx = txTemp.prepare();
		prepTx.sign(adminKey);

		// 提交交易；
		prepTx.commit();
	}
}