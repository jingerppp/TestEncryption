package com.shift.encrytion.des3;

import javax.crypto.Cipher;

import com.shift.encrytion.core.BlockEncryption;
import com.shift.encrytion.core.BlockEncryptionHelper;
import com.shift.encrytion.core.EncryptionCallback;

import android.content.Context;

public class DESedeEncryption extends BlockEncryption {
    // length of key must be 16 bytes or 24(3 * 8) bytes.
    private static final String DEFAULT_KEY = "shiftencrypt0926";
    private static final String ALGORITHM = "DESede";
    private static final int DES_BLOCK_SIZE = 8;

    public DESedeEncryption() {
    }

    public DESedeEncryption(Context context, boolean deleteSrcFile, EncryptionCallback callback) {
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
        return DES_BLOCK_SIZE;
    }
}
