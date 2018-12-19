package com.shift.encrytion.md5;

import com.shift.encrytion.core.MessageDigestEncryption;

public class MD5Encryption extends MessageDigestEncryption {
    private static final String ALGORITHM = "MD5";

    public MD5Encryption() {
        this(ALGORITHM);
    }

    public MD5Encryption(String alg) {
        super(alg);
    }

}
