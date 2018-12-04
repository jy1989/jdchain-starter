package com.jd.blockchain.ledger;

import com.jd.blockchain.domain.ProductInfo;
import com.jd.blockchain.domain.TraceInfo;
import my.utils.serialize.json.JSONSerializeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

/**
 * @Author zhaogw
 * @Date 2018/12/4 14:11
 */
public class TracingTest extends BaseTest{
    CloseableHttpClient httpclient = HttpClients.createDefault();;
    HttpGet httpGet = new HttpGet("http://192.168.151.39:8082/api/generate");

    private TraceInfo invokeTracing() throws IOException{
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        // Create a custom response handler
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
        TraceInfo traceInfo = null;
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager.
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            String responseBody = httpclient.execute(httpGet, responseHandler);
            System.out.println(responseBody);
            traceInfo = JSONSerializeUtils.deserializeAs(responseBody, TraceInfo.class);
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
        return traceInfo;
    }

    public void handleProduct(ProductInfo productInfo) {
        TransactionTemplate txTemp = bcsrv.newTransaction(ledgerHash);

        String key1 = productInfo.getSkuInfo().getSku()+"_"+productInfo.getSkuInfo().getCode()+"_"+productInfo.getProcess();
        String _productInfo = JSONSerializeUtils.serializeToJSON(productInfo);
        System.out.println("key1="+key1+",value="+_productInfo);
        byte[] val1 = _productInfo.getBytes();

        // 定义交易,传输最简单的数字、字符串、提取合约中的地址;
        txTemp.dataAccount(productInfo.getUserId()).set(key1, val1, -1);

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();
        prepTx.sign(ownerKey);
        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();
        assertTrue(transactionResponse.isSuccess());
    }

    /**
     * 模拟溯源;
     */
    @Test
    public void mock_tracing() throws IOException {
        TraceInfo traceInfo = this.invokeTracing();
        //针对溯源数据进行深度解析;
        if(traceInfo!=null && "success".equals(traceInfo.getMessage())){
            ProductInfo[] productInfos = traceInfo.getData();
            for(ProductInfo productInfo : productInfos){
                handleProduct(productInfo);
            }
        }
    }
}
