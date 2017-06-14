package triangle.triangleapp;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by D2110175 on 13-6-2017.
 */

public class HashHelper_Test {
    private static final byte[] DATA;
    private static String salt = "1ac2";
    private static final int DATA_SIZE = 64;
    static {
        DATA = new byte[DATA_SIZE];
        for (byte i = 0; i < DATA_SIZE; i++) {
            DATA[i] = i;
        }
    }

    /**
     * Hashes string, unsalted
     */
    @Test
    public void hashStringNoSalt(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashStringNoSalt("test");
        }catch (NoSuchAlgorithmException nsae){}
        catch(UnsupportedEncodingException ueex){}
        Assert.assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", expectedHash);
    }

    /**
     * Hases byte array, unsalted
     */
    @Test
    public void hashByteArrayNoSalt(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashByteArrayNoSalt(DATA);
        }catch (NoSuchAlgorithmException nsae){}
        Assert.assertEquals("fdeab9acf3710362bd2658cdc9a29e8f9c757fcf9811603a8c447cd1d9151108", expectedHash);
    }

    /**
     * Hashes string, appends salt after hashing
     */
    @Test
    public void HashStringSaltAfter(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashStringAndSaltAfterHash("test",salt);
            System.out.println("string salt after: "+expectedHash);
        }catch (NoSuchAlgorithmException nsae){}
        catch(UnsupportedEncodingException ueex){}
        Assert.assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a081ac2", expectedHash);
    }

    /**
     * appends salt to string then hashes them.
     */
    @Test
    public void HashStringSaltBefore(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashStringAndSaltBeforeHash("test",salt);
            System.out.println("string salt before: "+expectedHash);
        }catch (NoSuchAlgorithmException nsae){}
        catch(UnsupportedEncodingException ueex){}
        Assert.assertEquals("dc96c319f268f33c3d1f1a6fd80e3d953469a851b12839e030e74bc4be639bc4", expectedHash);
    }

    /**
     * hashes byte array then appends salt
     */
    @Test
    public void hashByteArraySaltAfter(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashByteArrayAndSaltAfterHash(DATA,salt);
        }catch (NoSuchAlgorithmException nsae){}
        Assert.assertEquals("fdeab9acf3710362bd2658cdc9a29e8f9c757fcf9811603a8c447cd1d91511081ac2", expectedHash);
    }
}
