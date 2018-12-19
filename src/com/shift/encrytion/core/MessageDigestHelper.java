package com.shift.encrytion.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestHelper {
    private static byte[] getDigest(String alg, String strSource) {
        if (strSource == null) {
            strSource = "";
        }

        try {
            MessageDigest md = MessageDigest.getInstance(alg);
            return md.digest(strSource.getBytes());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] getDigest(String alg, File file) {
        if (!FileOperation.isFileExists(file))
            return null;

        FileInputStream fileIs = null;
        byte buffer[] = new byte[4096];
        int len;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(alg);
            fileIs = new FileInputStream(file);
            while ((len = fileIs.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fileIs != null) {
                try {
                    fileIs.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String digest2String(byte[] digest) {
        String result = "";
        if (digest == null)
            return result;

        for (byte b : digest) {
            String temp = Integer.toHexString(b & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result += temp;
        }

        return result;
    }

    public static String getStringMD(String alg, String strSource) {
        return digest2String(getDigest(alg, strSource));
    }

    public static String getFileMD(String alg, File file) {
        return digest2String(getDigest(alg, file));
    }

    public static String getFileMD(String alg, String filePath) {
        File file = new File(filePath);
        if (!FileOperation.isFileExists(file))
            return "";

        return digest2String(getDigest(alg, file));
    }
}
