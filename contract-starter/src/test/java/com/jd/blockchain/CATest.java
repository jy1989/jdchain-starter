package com.jd.blockchain;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.crypto.utils.CSRBuilder;
import com.jd.blockchain.crypto.utils.CertParser;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.*;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * 针对CA证书进行验证;
 * @author zhaogw
 * date 2019/8/28 16:49
 */
public class CATest {
    private final String BC = BouncyCastleProvider.PROVIDER_NAME;

    @Test
    public void testWithCSRAndCert() {

        String countryName = "CN";
        String stateName = "Beijing";
        String cityName = "Beijing";
        String organizationName = "JD.com";
        String departmentName = "Blockchain Department";
        String domainName = "ledger.jd.com";
        String emailName = "zhaoguangwei@jd.com";


        String publicKeyStr =  "30820122300d06092a864886f70d01010105000382010f003082010a0282010100b6125df2aa8616efcb534addec0e3f4b825d694d0e3714332d6eac3cde4fe0010a560e4cfc83133b5639f74b9ed6f3c1f1cb0cd5410934603fac503ff328fadb8ce10a26f27f26458e38c954a6f5fe1ae0633b9cd790c4557ce4c1052e9d4ad4ec7a1ba0a6e83580cd2223f7ef67ea43defcc318810fd0d03910e413785f8cf9e931c80ea5137dab07d4b814ed08ba61561e383df42803aa4574d5b6e57a9bf6f435bf820c9f64ed0b849255cfdae7fa41efc0c587e7509d01cd689b4cfc12887a7cfb4e9cfb6cc1a599e1290ec9cc8d61aa39dc44df944b47a792e10150351956c2e47ed142566f5d027e7707412d70c3cf5aaad385a9aab290265515308eb10203010001";

        String privateKeyStr = "308204bd020100300d06092a864886f70d0101010500048204a7308204a30201000282010100b6125df2aa8616efcb534addec0e3f4b825d694d0e3714332d6eac3cde4fe0010a560e4cfc83133b5639f74b9ed6f3c1f1cb0cd5410934603fac503ff328fadb8ce10a26f27f26458e38c954a6f5fe1ae0633b9cd790c4557ce4c1052e9d4ad4ec7a1ba0a6e83580cd2223f7ef67ea43defcc318810fd0d03910e413785f8cf9e931c80ea5137dab07d4b814ed08ba61561e383df42803aa4574d5b6e57a9bf6f435bf820c9f64ed0b849255cfdae7fa41efc0c587e7509d01cd689b4cfc12887a7cfb4e9cfb6cc1a599e1290ec9cc8d61aa39dc44df944b47a792e10150351956c2e47ed142566f5d027e7707412d70c3cf5aaad385a9aab290265515308eb102030100010282010007d4e46ba2c01fce72bd8373e64a7a9881e408ba82c905ba69d2dfdfc264472b64496d560c6a8af23b7444d9e0dc3821f8879666b9e46e775165d511db20c1219e7eb394174c36d63916e23a1cf069dcb8182ca143787ba97db4cd44aecb9f4367649d399cd45230d72207d00b6139e76f7909df4cf5e43280b1e44d9e18e6146b2896ec3f1cffbf4c4c997b48454cffd379df7d9a48481dac83d6abee7ed50556fa67d4bf56568eb3b4a5a7bb130db852bfdf5d01d03bc8baa366384efdb5b2b898ef9d84e85a9898d1aa13bfb83aaa60dd6f66f911dd378842cd0879e94a34ca4b72548b25cc35327166140a71b2c3ea7709d968303be307e307bd20dcd65d02818100f3c7fe6b180abfcc7d4aac1b836f60b997a4fefb888ee00ad380e4c130f6aad44e95bb11cdaef9ba25c9572a271d85e025fd610a12ac8f3a71c3fec09dc5a629152465a1e2d14e27637a4d076f255473f1a633fd9d1bb5b1e41710773fcb10964af94692843d3b38eebd296058f52bb8e6f09d39255b231eeb130ebfa1d1eae302818100bf3290fca2718d41f9f9acd04c0835231d665675347c50c2b6bdfb5550bbed1fbdb6a59dd180a36873e96bfe470b975fe6b684b42d48c6ed8ed834c16b5fc56ba0f338017f9a22e00e27ab9d3cd732c11eaa2f6cbbd974f2cbb22c91fda8bafb0df20ffbf7ce962c4a9e47f8c192a3c33182e673670078fdadb1409dd772b05b028180116c5dd6b1a533081e7c53c8b3a0263d9d85016460e2354910ccc98cb53f2bfe788f630b66b3f6cd431e1cc8ba7af5b28b848c86f7c6b585ebd3a2458a01325b0553d09fbd62503fa8707948eabcb4488520e7de5c783a8838511db028330f406ea35a53e677a9dfebd04140cd9ad84122e0579c59fb258b7429ac882dbc9f190281810099ed81e0827f5cb413263944f111628644b3114406df56bd8c15e3744a2d21b87e29bfa810f4b999ec47ae53e6aaa451e7126640107d18fa44183850c7bb30db7796982e4ca6b1112514370bbde19ad0f3791bf9343adbf6649bab5a55973401ceb664bbfc436b8f78fb79020205a6a60d044fb7f4e2e2858902bcfcd66955250281800b1a5e401620ab7a3d308429060d95278237c17283525932f49401f890b67bcf756bc5700e4d50cdf59218f7106407de792113b47a5c58154b92dfb59c06f508c3128cc8b67791124702e20b4029a0d42d54b31898e73adcfb0388b27dece22f019ffdbf3de14d0da2798aac929935fc1a4daf430cfa471a53a7b2cbdef34b18";

        String expectedCSR = "MIIC5DCCAcwCAQAwgZ4xCzAJBgNVBAYTAkNOMRAwDgYDVQQIDAdCZWlqaW5nMRAwDgYDVQQHDAdCZWlqaW5nMQ8wDQYDVQQKDAZKRC5jb20xHjAcBgNVBAsMFUJsb2NrY2hhaW4gRGVwYXJ0bWVudDEWMBQGA1UEAwwNbGVkZ2VyLmpkLmNvbTEiMCAGCSqGSIb3DQEJARYTemhhb2d1YW5nd2VpQGpkLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALYSXfKqhhbvy1NK3ewOP0uCXWlNDjcUMy1urDzeT+ABClYOTPyDEztWOfdLntbzwfHLDNVBCTRgP6xQP/Mo+tuM4Qom8n8mRY44yVSm9f4a4GM7nNeQxFV85MEFLp1K1Ox6G6Cm6DWAzSIj9+9n6kPe/MMYgQ/Q0DkQ5BN4X4z56THIDqUTfasH1LgU7Qi6YVYeOD30KAOqRXTVtuV6m/b0Nb+CDJ9k7QuEklXP2uf6Qe/AxYfnUJ0BzWibTPwSiHp8+06c+2zBpZnhKQ7JzI1hqjncRN+US0enkuEBUDUZVsLkftFCVm9dAn53B0EtcMPPWqrThamqspAmVRUwjrECAwEAAaAAMA0GCSqGSIb3DQEBBQUAA4IBAQAjJcHI8hMYyTxYax4RDQBue4ZjlTO5p4BKLge32DoX+fD+6JDMTiWi0MAAcV2QDTbJALnHRkkEfHHIXySh795eRGJy5cHLgXSGpuT6pcgmTK5oAxhUz2PIx9bMTh9AsIWL2sdyTkeXc0e+FRLzc9k+ff1GGHbGojXfPCbzYtw+UXNLQkCkEhh/56gccWzESRt0ygX71dk6nrCzeWa/B/KEZZiGcao58jdV7sGg7EuPqpJyO1FyIDpjFIBzhSfDwlRVkocrwETDmZjxps7sevlX11PQn/skEspGOnqphjQE9i5biQm6dx+uj3qnWmgO8v8HiolJl6EPl+Pep+f2WaED";

        String expectedUserCert = "-----BEGIN CERTIFICATE-----\nMIIEPzCCAyegAwIBAgIFIChmZlcwDQYJKoZIhvcNAQEFBQAwWTELMAkGA1UEBhMC\nQ04xMDAuBgNVBAoTJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhv\ncml0eTEYMBYGA1UEAxMPQ0ZDQSBURVNUIE9DQTExMB4XDTE5MDgyODEwMTcxMloX\nDTIxMDgyODEwMTcxMlowcTELMAkGA1UEBhMCQ04xGDAWBgNVBAoTD0NGQ0EgVEVT\nVCBPQ0ExMTERMA8GA1UECxMITG9jYWwgUkExFTATBgNVBAsTDEluZGl2aWR1YWwt\nMTEeMBwGA1UEAxQVMDUxQHN5ZkBaSDA5MzU4MDI4QDk4MIIBIjANBgkqhkiG9w0B\nAQEFAAOCAQ8AMIIBCgKCAQEAthJd8qqGFu/LU0rd7A4/S4JdaU0ONxQzLW6sPN5P\n4AEKVg5M/IMTO1Y590ue1vPB8csM1UEJNGA/rFA/8yj624zhCibyfyZFjjjJVKb1\n/hrgYzuc15DEVXzkwQUunUrU7HoboKboNYDNIiP372fqQ978wxiBD9DQORDkE3hf\njPnpMcgOpRN9qwfUuBTtCLphVh44PfQoA6pFdNW25Xqb9vQ1v4IMn2TtC4SSVc/a\n5/pB78DFh+dQnQHNaJtM/BKIenz7Tpz7bMGlmeEpDsnMjWGqOdxE35RLR6eS4QFQ\nNRlWwuR+0UJWb10CfncHQS1ww89aqtOFqaqykCZVFTCOsQIDAQABo4H1MIHyMB8G\nA1UdIwQYMBaAFPwLvESaDjGhg6mBhyceBULGv1b4MEgGA1UdIARBMD8wPQYIYIEc\nhu8qAQIwMTAvBggrBgEFBQcCARYjaHR0cDovL3d3dy5jZmNhLmNvbS5jbi91cy91\ncy0xNS5odG0wOgYDVR0fBDMwMTAvoC2gK4YpaHR0cDovLzIxMC43NC40Mi4zL09D\nQTExL1JTQS9jcmwyNjY1NS5jcmwwCwYDVR0PBAQDAgPoMB0GA1UdDgQWBBQT/FCu\nsxW5grxNqNKDQR1VV2RgwjAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQw\nDQYJKoZIhvcNAQEFBQADggEBAKzrnOCb70w+V5TwISdN446itd30RRH4v4ONU5ku\n8KtqBUGzGnLOJImKbdOcHzDSlLA0sR2ozdUqA8BR7R5tKvA8MCSDSEyRHjLOApgq\nmS8x/FGH7l/J2vmY9uvzd4pj499EQna6NcTEdQFtNO5LEaaRXioJSZvRgouYEHg1\nchaha2Z0DlBb/2TZcVDYl9QJVTuP7qiLsCntSVWRj9Ga7XOnd2t0OP4pueJ3uhOp\n/URVrkBaZ8XMEGn2punBbT20vYnMg8FjmEYobmbtv9TnjnTh7iELA0n5n2rO10wY\nPOu8kBZLpcOe2neSOLks7TPNn5ZpKdb1/tT0IWx1kOU2EmA=\n-----END CERTIFICATE-----";

        String issuerCert =
                "-----BEGIN CERTIFICATE-----\n" +
                        "MIIDzzCCAregAwIBAgIKUalCR1Mt5ZSK8jANBgkqhkiG9w0BAQUFADBZMQswCQYD\n" +
                        "VQQGEwJDTjEwMC4GA1UEChMnQ2hpbmEgRmluYW5jaWFsIENlcnRpZmljYXRpb24g\n" +
                        "QXV0aG9yaXR5MRgwFgYDVQQDEw9DRkNBIFRFU1QgQ1MgQ0EwHhcNMTIwODI5MDU1\n" +
                        "NDM2WhcNMzIwODI0MDU1NDM2WjBZMQswCQYDVQQGEwJDTjEwMC4GA1UEChMnQ2hp\n" +
                        "bmEgRmluYW5jaWFsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRgwFgYDVQQDEw9D\n" +
                        "RkNBIFRFU1QgT0NBMTEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC8\n" +
                        "jn0n8Fp6hcRdACdn1+Y6GAkC6KGgNdKyHPrmsdmhCjnd/i4qUFwnG8cp3D4lFw1G\n" +
                        "jmjSO5yVYbik/NbS6lbNpRgTK3fDfMFvLJpRIC+IFhG9SdAC2hwjsH9qTpL9cK2M\n" +
                        "bSdrC6pBdDgnbzaM9AGBF4Y6vXhj5nah4ZMsBvDp19LzXjlGuTPLuAgv9ZlWknsd\n" +
                        "RN70PIAmvomd10uqX4GIJ4Jq/FdKXOLZ2DNK/NhRyN6Yq71L3ham6tutXeZow5t5\n" +
                        "0254AnUlo1u6SeH9F8itER653o/oMLFwp+63qXAcqrHUlOQPX+JI8fkumSqZ4F2F\n" +
                        "t/HfVMnqdtFNCnh5+eIBAgMBAAGjgZgwgZUwHwYDVR0jBBgwFoAUdN7FjQp9EBqq\n" +
                        "aYNbTSHOhpvMcTgwDAYDVR0TBAUwAwEB/zA4BgNVHR8EMTAvMC2gK6AphidodHRw\n" +
                        "Oi8vMjEwLjc0LjQyLjMvdGVzdHJjYS9SU0EvY3JsMS5jcmwwCwYDVR0PBAQDAgEG\n" +
                        "MB0GA1UdDgQWBBT8C7xEmg4xoYOpgYcnHgVCxr9W+DANBgkqhkiG9w0BAQUFAAOC\n" +
                        "AQEAb7W0K9fZPA+JPw6lRiMDaUJ0oh052yEXreMBfoPulxkBj439qombDiFggRLc\n" +
                        "3g8wIEKzMOzOKXTWtnzYwN3y/JQSuJb/M1QqOEEM2PZwCxI4AkBuH6jg03RjlkHg\n" +
                        "/kTtuIFp9ItBCC2/KkKlp0ENfn4XgVg2KtAjZ7lpyVU0LPnhEqqUVY/xthjlCSa7\n" +
                        "/XHNStRxsfCTIBUWJ8n2FZyQhfV/UkMNHDBIiJR0v6C4Ai0/290WvbPEIAq+03Si\n" +
                        "fsHzBeA0C8lP5VzfAr6wWePaZMCpStpLaoXNcAqReKxQllElOqAhRxC5VKH+rnIQ\n" +
                        "OMRZvB7FRyE9IfwKApngcZbA5g==\n" +
                        "-----END CERTIFICATE-----";

        byte[] rawPublicKeyBytes = Hex.decode(publicKeyStr);
        byte[] rawPrivateKeyBytes = Hex.decode(privateKeyStr);

        CSRBuilder builder = new CSRBuilder();
        builder.init("SHA1withRSA", rawPublicKeyBytes, rawPrivateKeyBytes);

        String csr = builder.buildRequest(countryName,stateName,cityName,
                organizationName,departmentName,domainName,
                emailName);
        System.out.println("csr="+csr);

        assertEquals(expectedCSR,csr);

        CertParser parser = new CertParser();
        parser.parse(expectedUserCert,issuerCert);

        PublicKey rawPublicKeyInCert = parser.getPubKey();
        // check that the public key in inputs and the public key in certificate are consistent
        assertArrayEquals(rawPublicKeyBytes, rawPublicKeyInCert.getEncoded());

        String algoName = parser.getSigAlgName();
        int keyLength = parser.getKeyLength();
        String length = String.valueOf(keyLength);
        String algo = (algoName.contains("RSA")? (algoName + length).toUpperCase(): algoName.toUpperCase());

        CryptoAlgorithm algorithm = Crypto.getAlgorithm(algo);
        assertNotNull(algorithm);
        SignatureFunction signatureFunction = Crypto.getSignatureFunction(algorithm);

        PubKey  pubKey  = new PubKey(algorithm, rawPublicKeyBytes);
        PrivKey privKey = new PrivKey(algorithm, rawPrivateKeyBytes);

        // signTest
        byte[] data = new byte[1024];
        Random random = new Random();
        random.nextBytes(data);

        SignatureDigest signature = signatureFunction.sign(privKey, data);
        assertTrue(signatureFunction.verify(signature, pubKey, data));
    }

    @Test
    public void genPubAndPrivKey(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        int keyLength = 2048;
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA", BC);
            generator.initialize(keyLength);
            KeyPair keyPair = generator.generateKeyPair();
            PublicKey pubKey  = keyPair.getPublic();
            PrivateKey privKey = keyPair.getPrivate();
            System.out.println("pubKey="+Hex.toHexString(pubKey.getEncoded()));
            System.out.println("pubKey="+Hex.toHexString(privKey.getEncoded()));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }
}
