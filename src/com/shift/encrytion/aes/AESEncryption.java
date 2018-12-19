package com.shift.encrytion.aes;

import javax.crypto.Cipher;

import com.shift.encrytion.core.BlockEncryption;
import com.shift.encrytion.core.BlockEncryptionHelper;
import com.shift.encrytion.core.EncryptionCallback;

import android.content.Context;

public class AESEncryption extends BlockEncryption{
    private static final String DEFAULT_KEY = "shiftencrypt0926";
    private static final String ALGORITHM = "AES";
    private static final int AES_BLOCK_SIZE = 16;

    public AESEncryption(Context context, boolean deleteSrcFile, EncryptionCallback callback) {
        super(context, deleteSrcFile, callback);
    }

    @Override
    public Cipher getCipher(int mode) {
        return BlockEncryptionHelper.getCipher(mode, ALGORITHM, DEFAULT_KEY);
    }

    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }

    @Override
    protected int getBlockSize() {
        return AES_BLOCK_SIZE;
    }
}
