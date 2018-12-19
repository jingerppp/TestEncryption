package com.shift.encrytion.core;

import java.io.File;

public class MessageDigestEncryption {
    private String mAlgorithm;

    public MessageDigestEncryption(String alg) {
        mAlgorithm = alg;
    }

    public void setAlgorithm(String alg) {
        mAlgorithm = alg;
    }

    public String getStringMD(String string) {
        return MessageDigestHelper.getStringMD(mAlgorithm, string);
    }

    public String getFileMD(String filePath) {
        return MessageDigestHelper.getFileMD(mAlgorithm, filePath);
    }

    public String getFileMD(File file) {
        return MessageDigestHelper.getFileMD(mAlgorithm, file);
    }
}
