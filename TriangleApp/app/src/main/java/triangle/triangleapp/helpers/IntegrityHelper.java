package triangle.triangleapp.helpers;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Created by marco on 15-6-2017.
 */

public class IntegrityHelper {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA1withRSA";
    public static final int KEY_SIZE = 1024;

    private static final String TAG = "IntegrityHelper";


    /**
     * Generates an keypair using the constant KEY_ALGORITHM and KEY_SIZE
     *
     * @return Generated KeyPair using the algorithm
     */
    public static KeyPair generateKeyPair() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            generator.initialize(KEY_SIZE);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error getting KeyPairGenerator instance", e);
        }

        return null;
    }

    /**
     * Encrypts the data and gets the base64 string for the signed data
     *
     * @param data       The data to sign and sign
     * @param privateKey The private key used for signing
     * @return Base64 string with encrypted signed hash
     */
    public static String sign(byte[] data, PrivateKey privateKey) {
        Signature dsa;
        try {
            dsa = Signature.getInstance(SIGN_ALGORITHM);
            dsa.initSign(privateKey);
            dsa.update(data, 0, data.length);

            byte[] signedData = dsa.sign();
            return getBase64String(signedData);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error getting Signature instance", e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Error invalid key!", e);
        } catch (SignatureException e) {
            Log.e(TAG, "Error with signature", e);
        }

        return "";
    }

    /**
     * Creates a base64 string from data
     *
     * @param data The data to convert to base64
     * @return Base64 string from the data
     */
    public static String getBase64String(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

}
