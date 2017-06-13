package triangle.triangleapp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by D2110175 on 13-6-2017.
 */

public class HashHelper {

    public HashHelper(){}

    public String hashStringAndSaltAfterHash(String text, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String saltedHash = hashStringNoSalt(text)+salt;
        return saltedHash;
    }
    public String hashStringAndSaltBeforeHash(String text, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String saltedHash = hashStringNoSalt(text+salt);
        return saltedHash;
    }

    public String hashByteArrayAndSaltAfterHash(byte[] toBeHashed, String salt) throws NoSuchAlgorithmException {
        String saltedHash = hashByteArrayNoSalt(toBeHashed)+salt;
        return saltedHash;
    }
   @Deprecated
    public String hashByteArrayAndSaltBeforeHash(byte[] toBeHashed, String salt) throws NoSuchAlgorithmException {
        //Note: the salt will not be hashed with the bytestream here
        String saltedHash = salt+hashByteArrayNoSalt(toBeHashed);
        return saltedHash;
    }

    public String hashByteArrayNoSalt(byte[] toBeHashed) throws NoSuchAlgorithmException {
        String strash= SHAsum(toBeHashed);
        return strash;
    }
    public String hashStringNoSalt(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return byteArray2Hex(sha1hash);
    }

    private String SHAsum(byte[] convertme) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(convertme));
    }
    private String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
