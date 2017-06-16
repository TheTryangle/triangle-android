package triangle.triangleapp.helpers;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.jce.provider.BouncyCastleProvider;


import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by marco on 15-6-2017.
 */

public class CertifcateHelper {
    private static final String TAG = "CertificateHelper";

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        return generateRSAKeyPair();
    }

    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);

        return generator.generateKeyPair();
    }

    public static String encrypt(PrivateKey privateKey, byte[] data) throws Exception {
        //Security.addProvider(new BouncyCastleProvider());

        RSAEngine engine = new RSAEngine();
        //engine.init(true, privateKey); //true if encrypt

        byte[] hexEncodedCipher = engine.processBlock(data, 0, data.length);

        return getHexString(hexEncodedCipher);
    }

    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

}
