package triangle.triangleapp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by D2110175 on 13-6-2017.
 */

public class HashHelper {

    //Default value
    public String hashAlgorithm ="SHA-256";

    /**
     * Gets the aslgorithm used for hashing, default is SHA-256
     * @return String with hash algorithm
     */
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * sets the hash algorithm used for hashing, default is SHA-256
     * @param hashAlgorithm
     */
    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    /**
     * Hashes a string then appends salt to it. salts appended this way should be randomly generated.
     * @param text string to be hashed
     * @param salt salt to be used
     * @return hash with salt appended
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public String hashStringAndSaltAfterHash(String text, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String saltedHash = hashStringNoSalt(text)+salt;
        return saltedHash;
    }

    /**
     * Salts a string then hashes it.
     * @param text string to be hashed
     * @param salt salt to be used
     * @return salted hash
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public String hashStringAndSaltBeforeHash(String text, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String saltedHash = hashStringNoSalt(text+salt);
        return saltedHash;
    }

    /**
     * Hashes byte array and places salt after the has, does not hash salt along with byte array.
     * @param toBeHashed byte array to be hashed
     * @param salt to make hash more difficult to crack
     * @return salted hash from byte array
     * @throws NoSuchAlgorithmException
     */
    public String hashByteArrayAndSaltAfterHash(byte[] toBeHashed, String salt) throws NoSuchAlgorithmException {
        String saltedHash = hashByteArrayNoSalt(toBeHashed)+salt;
        return saltedHash;
    }

    /**
     * Hashes a byte array and places the raw salt in front of hash, does not hash salt.
     * @param toBeHashed byte array which to hash
     * @param salt to salt hash
     * @return hashed string of byte array
     * @throws NoSuchAlgorithmException
     */
    public String hashByteArrayAndSaltBeforeHash(byte[] toBeHashed, String salt) throws NoSuchAlgorithmException {
        //Note: the salt will not be hashed with the bytestream here
        String saltedHash = salt+hashByteArrayNoSalt(toBeHashed);
        return saltedHash;
    }

    /**
     * Hashes a byte array without salting it.
     * @param toBeHashed byte array that is to be hashed
     * @return Unsalted Hashed String
     * @throws NoSuchAlgorithmException
     */
    public String hashByteArrayNoSalt(byte[] toBeHashed) throws NoSuchAlgorithmException {
        String strash= SHAsum(toBeHashed);
        return strash;
    }

    /**
     * Hashes a string without salting it.
     * @param text String that is to be hashed
     * @return Unsalted hashed String
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String hashStringNoSalt(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return byteArray2Hex(sha1hash);
    }

    /**
     * Is called upon by other functions to hash byte-arrays with the SHA-1 algorithm
     * @param convertme byte array that will be hashed
     * @return SHA-1 digest
     * @throws NoSuchAlgorithmException
     */
    private String SHAsum(byte[] convertme) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        return byteArray2Hex(md.digest(convertme));
    }

    /**
     * Performs a byte array conversion
     * @param hash digest in byte array format
     * @return String in hex format
     */
    private String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
