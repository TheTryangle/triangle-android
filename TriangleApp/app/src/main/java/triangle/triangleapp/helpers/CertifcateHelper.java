package triangle.triangleapp.helpers;

import android.util.Base64;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;

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
        Signature dsa = Signature.getInstance("SHA1withRSA");
        dsa.initSign(privateKey);
        dsa.update(data, 0, data.length);
        byte[] signedData = dsa.sign();

        return getBase64String(signedData);
    }

    public static String getBase64String(byte[] b) throws Exception {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

}
