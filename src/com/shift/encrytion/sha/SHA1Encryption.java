package com.shift.encrytion.sha;

import com.shift.encrytion.core.MessageDigestEncryption;

public class SHA1Encryption extends MessageDigestEncryption {
    private static final String ALGORITHM = "SHA-512";

    public SHA1Encryption() {
        this(ALGORITHM);
    }

    public SHA1Encryption(String alg) {
        super(alg);
    }

}
