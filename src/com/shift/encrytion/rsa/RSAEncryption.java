package com.shift.encrytion.rsa;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import com.shift.encrytion.core.BlockEncryption;

public class RSAEncryption extends BlockEncryption {
    private static final String ALGORITHM = "RSA";
    private static final String PADDING = "/ECB/PKCS1Padding";
    private static final int DEFAULT_KEY_SIZE = 1024;

    private static final int RSA_BLOCK_SIZE = 11;

    public static final int TYPE_ENCRYPTION_WITH_PUBLIC = 0;
    public static final int TYPE_ENCRYPTION_WITH_PRIVATE = 1;

    private int mEncryptionType = TYPE_ENCRYPTION_WITH_PUBLIC;

    private int mKeyLength = DEFAULT_KEY_SIZE;
    private PublicKey mPublicKey;
    private PrivateKey mPrivateKey;

    public RSAEncryption() {
    }

    public void setEncryptionType(int type) {
        mEncryptionType = type;
    }

    public void setKeyLength(int keyLength) {
        mKeyLength = keyLength;
    }

    private KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void generateKeys() {
        KeyPair keyPair = generateRSAKeyPair(mKeyLength);

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        try {
            // get public key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
            mPublicKey = kf.generatePublic(keySpec);

            // get private key
            PKCS8EncodedKeySpec keySpec1 = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            mPrivateKey = kf.generatePrivate(keySpec1);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Key getKey(int mode) {
        if (mEncryptionType == TYPE_ENCRYPTION_WITH_PRIVATE) {
            return mode == Cipher.ENCRYPT_MODE ? mPrivateKey : mPublicKey;
        }
        return mode == Cipher.ENCRYPT_MODE ? mPublicKey : mPrivateKey;
    }

    @Override
    public Cipher getCipher(int mode) {
        if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
            return null;

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM + PADDING);
            cipher.init(mode, getKey(mode));
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
        }

        return null;
    }

    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }

    @Override
    protected int getBlockSize() {
        return RSA_BLOCK_SIZE;
    }

    @Override
    protected int getBufferSize() {
        return (mKeyLength / 8) - RSA_BLOCK_SIZE;
    }

}
