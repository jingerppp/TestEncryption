package com.shift.encrytion.core;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;
import android.util.Log;

public class BlockEncryptionHelper {
    /**
     * get a random secret key.
     * must use the same key for encrypting and decrypting.
     */
    private static SecretKeySpec getRandomSecretKeySpec(String alg, String key) {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(key.getBytes("UTF-8"));

            KeyGenerator gen = KeyGenerator.getInstance(alg);
            gen.init(128, sr);

            SecretKey secKey = gen.generateKey();
            return new SecretKeySpec(secKey.getEncoded(), alg);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get a stationary secret key.
     * must use the same key for encryption and decryption.
     */
    private static SecretKey getSecretKey(String alg, String key) {
        try {
            byte[] rawKey = key.getBytes("UTF-8");

//            if (TextUtils.equals(alg, "DES")) {
//                DESKeySpec keySpec = new DESKeySpec(rawKey);
//                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//                SecretKey skey = keyFactory.generateSecret(keySpec);
//                return skey;
//            }
            return new SecretKeySpec(rawKey, alg);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
        }

        return null;
    }

    public static Cipher getCipher(int mode, String alg, String key) {
        return getCipher(mode, alg, "CBC/PKCS5Padding", key);
    }

    /**
     * get the instance of {@code Cipher}.
     * @param mode The encryption mode or the decryption mode. the value is {@value Cipher.ENCRYPT_MODE}
     * or {@value Cipher.DECRYPT_MODE}.
     * @param alg The name of algorithm.
     * @param padding The working mode and the padding of algorithm. e.g "/CBC/PKCS5Padding", "/EBC/PKCS5Padding"
     * @param key The key for encryption and decryption.
     */
    public static Cipher getCipher(int mode, String alg, String padding, String key) {
        if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
            return null;

        SecretKey secretKey = getSecretKey(alg, key);
        if (secretKey == null)
            return null;

        if (TextUtils.equals(alg, "DES") || TextUtils.equals(alg, "DESede")) {
            key = key.substring(0, 8);
        }
        try { 
            IvParameterSpec ivp = new IvParameterSpec(key.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance(alg + padding);
            cipher.init(mode, secretKey, ivp);

            return cipher;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] encrypt(byte[] rawValue, Cipher cipher) {
        if (cipher == null)
            return null;

        byte[] encrypted = null;
        try {
            encrypted = cipher.doFinal(rawValue);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encrypted;
    }

}
