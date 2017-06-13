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

    static {
        DATA = new byte[64];
        for (byte i = 0; i < 64; i++) {
            DATA[i] = i;
        }
    }

    @Test
    public void validateTests(){
        Assert.assertEquals(3,2+1);
    }
    @Test
    public void hashString(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashStringNoSalt("test");
        }catch (NoSuchAlgorithmException nsae){
            //sha alghortihm doesnt work
        }catch(UnsupportedEncodingException ueex){
            //invalid encoding
        }
        Assert.assertEquals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", expectedHash);
    }
    @Test
    public void hashByteArray(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        //data raw hash: c6138d514ffa2135bfce0ed0b8fac65669917ec7
        //data.tosting hash: 0d57d4d01e8ecc1643ffbee9ad4d25355d58f5f8
        try {
            expectedHash = hh.hashByteArrayNoSalt(DATA);
        }catch (NoSuchAlgorithmException nsae){
            //sha alghortihm doesnt work
        }
        Assert.assertEquals("c6138d514ffa2135bfce0ed0b8fac65669917ec7", expectedHash);
    }
    @Test
    public void HashStringSaltAfter(){
        HashHelper hh = new HashHelper();
        String expectedHash="";

        try {
            expectedHash = hh.hashStringAndSaltAfterHash("test",salt);
            System.out.println("string salt after: "+expectedHash);
        }catch (NoSuchAlgorithmException nsae){
            //sha alghortihm doesnt work
        }catch(UnsupportedEncodingException ueex){
            //invalid encoding
        }
        Assert.assertEquals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd31ac2", expectedHash);
    }
    @Test
    public void HashStringSaltBefore(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashStringAndSaltBeforeHash("test",salt);
            System.out.println("string salt before: "+expectedHash);
        }catch (NoSuchAlgorithmException nsae){
            //sha alghortihm doesnt work
        }catch(UnsupportedEncodingException ueex){
            //invalid encoding
        }
        Assert.assertEquals("fb56a08de4048cca6b480a6f28f6ee0301963044", expectedHash);
    }
    @Test
    public void hashByteArraySaltAfter(){
        HashHelper hh = new HashHelper();
        String expectedHash="";
        try {
            expectedHash = hh.hashByteArrayAndSaltAfterHash(DATA,salt);
        }catch (NoSuchAlgorithmException nsae){
            //sha alghortihm doesnt work
        }
        Assert.assertEquals("c6138d514ffa2135bfce0ed0b8fac65669917ec71ac2", expectedHash);
    }
}
