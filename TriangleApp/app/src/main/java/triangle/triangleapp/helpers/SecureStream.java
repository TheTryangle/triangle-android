//package triangle.triangleapp.helpers;
//
//import android.util.Log;
//import java.io.File;
//import javax.crypto.Cipher;
//import javax.crypto.NoSuchPaddingException;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import android.provider.Settings.Secure;
//
///**
// * Created by danie on 13-6-2017.
// */
//
//public class SecureStream {
//
//    private Cipher cipher;
//    private PublicKey publicKey;
//    private PrivateKey privateKey;
//    private String android_id = Secure.getString(this.getContentResolver(),
//            Secure.ANDROID_ID);
//
//    public SecureStream() throws NoSuchPaddingException, NoSuchAlgorithmException {
//        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//
//        KeyPair keyPair = KeyPairGenerator.getInstance("").generateKeyPair();
//
//
//    }
//
//    public void generateKey() {
//
//    }
//
//    public PublicKey getPublicKey() {
//        return this.publicKey;
//    }
//
//    public void setPublicKey(PublicKey value) {
//        this.publicKey = value;
//    }
//
//    public PrivateKey getPrivateKey() {
//        return this.privateKey;
//    }
//
//    public void setPrivateKey(PrivateKey value) {
//        this.privateKey = value;
//    }
//
//    public byte[] encryptFile(File filename) throws Exception {
//
//        byte[] encrypted = null;
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        ;
//
//        return encrypted;
//    }
//}