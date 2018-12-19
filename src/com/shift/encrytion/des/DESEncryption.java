package com.shift.encrytion.des;

import javax.crypto.Cipher;

import com.shift.encrytion.core.BlockEncryption;
import com.shift.encrytion.core.BlockEncryptionHelper;
import com.shift.encrytion.core.EncryptionCallback;

import android.content.Context;

public class DESEncryption extends BlockEncryption {
    // length of key must be 8.
    private static final String DEFAULT_KEY = "shift926";
    private static final String ALGORITHM = "DES";
    private static final int DES_BLOCK_SIZE = 8;

    public DESEncryption() {
    }

    public DESEncryption(Context context, boolean deleteSrcFile, EncryptionCallback callback) {
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
