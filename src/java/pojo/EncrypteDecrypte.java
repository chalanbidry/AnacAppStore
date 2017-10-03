/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author CHABI Emmanel Landry
 */
public class EncrypteDecrypte {
//     private static MessageDigest digester;
//
//    static {
//        try {
//            digester = MessageDigest.getInstance("MD5");
//        }
//        catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }

//    public static String crypt(String str) {
//        if (str == null || str.length() == 0) {
//            throw new IllegalArgumentException("String to encript cannot be null or zero length");
//        }
//
//        digester.update(str.getBytes());
//        byte[] hash = digester.digest();
//        StringBuffer hexString = new StringBuffer();
//        for (int i = 0; i < hash.length; i++) {
//            if ((0xff & hash[i]) < 0x10) {
//                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
//            }
//            else {
//                hexString.append(Integer.toHexString(0xFF & hash[i]));
//            }
//        }
//        return hexString.toString();
//    }
    private static byte[] sharedvector = {
        0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11
    };

    
    public static String DecryptText(String EncText) {

        String RawText = "";
        byte[] keyArray = new byte[24];
        byte[] temporaryKey;
        String key = "developersnotedotcom";
        byte[] toEncryptArray = null;

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            temporaryKey = m.digest(key.getBytes("UTF-8"));

            if (temporaryKey.length < 24) // DESede require 24 byte length key
            {
                int index = 0;
                for (int i = temporaryKey.length; i < 24; i++) {
                    keyArray[i] = temporaryKey[index];
                }
            }

            Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
            byte[] decrypted = c.doFinal(Base64.decodeBase64(EncText));

            RawText = new String(decrypted, "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
            JOptionPane.showMessageDialog(null, NoEx);
        }

        return RawText;

    }
    
    
    public static String sha256(String base) {
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    } catch(Exception ex){
       throw new RuntimeException(ex);
    }
}
}
