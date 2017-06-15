package triangle.triangleapp.helpers;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;


import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by marco on 15-6-2017.
 */

public class CertifcateHelper {
    private static final String TAG = "CertificateHelper";

    public static AsymmetricCipherKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters
                (
                        new BigInteger("10001", 16),//publicExponent
                        SecureRandom.getInstance("SHA1PRNG"),//prng
                        1024,//strength
                        80//certainty
                ));

        return generator.generateKeyPair();
    }

    public static String encrypt(AsymmetricKeyParameter privateKey, byte[] data) throws Exception {
        //Security.addProvider(new BouncyCastleProvider());

        RSAEngine engine = new RSAEngine();
        engine.init(true, privateKey); //true if encrypt

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
