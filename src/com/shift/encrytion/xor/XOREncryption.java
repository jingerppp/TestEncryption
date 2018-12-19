package com.shift.encrytion.xor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.shift.encrytion.core.FileOperation;

import android.text.TextUtils;
import android.util.Log;

public class XOREncryption {
    private static final String TAG = "XOREncryption";
    private static final String DEFAULT_KEY = "shiftencryt0926";
    private final String mKey;

    private static final int BUFFER_SIZE = 4096;

    public XOREncryption() {
        this(DEFAULT_KEY);
    }

    public XOREncryption(String key) {
        mKey = key;

        if (TextUtils.isEmpty(mKey)) {
            throw new IllegalArgumentException("key is empty...");
        }
    }

    public String strEncrypt(final String strSource) {
        int i, j;
        StringBuilder sb = new StringBuilder();
        for (i = 0, j = 0; i < strSource.length(); i++) {
            j = i % mKey.length();
            sb.append((char) (strSource.charAt(i) ^ mKey.charAt(j)));
        }
        return sb.toString();
    }

    public String strDecrypt(final String strSource) {
        return strEncrypt(strSource);
    }

    public boolean fileEncrypt(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (!FileOperation.isFileExists(srcFile)) {
            Log.w(TAG, "source path " + srcPath + " doesn't exist.");
            return false;
        }

        // get valid {@value destPath}
        boolean isSameFile = false;
        if (TextUtils.equals(srcPath, destPath) || TextUtils.equals(srcFile.getParent(), destPath)) {
            isSameFile = true;
            destPath = "";
        }
        // get a random destination path
        if (TextUtils.isEmpty(destPath)) {
            destPath = srcFile.getParent();
            destPath = FileOperation.getRandomFilePath(destPath, srcFile.getName());
        }
        File destFile = new File(destPath);
        if (destFile.isDirectory()) {
            destPath = FileOperation.getRandomFilePath(destPath, srcFile.getName());
            destFile = new File(destPath);
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(destFile);

            int cnt;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((cnt = fileInputStream.read(buf)) >= 0) {
                byte[] rawData = buf;
                if (cnt != buf.length)
                    rawData = FileOperation.subBytes(buf, 0, cnt);
                byte[] encryptData = encrypt(rawData);
                fileOutputStream.write(encryptData, 0, encryptData.length);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (isSameFile) {
            destFile.renameTo(srcFile);
        }

        return true;
    }

    public boolean fileDecrypt(String srcPath, String destPath) {
        fileEncrypt(srcPath, destPath);
        return true;
    }

    private byte[] encrypt(byte[] source) {
        byte[] dest = new byte[source.length];
        int keyLength = mKey.length();
        for (int i = 0, j = 0; i < source.length; i++) {
            j = i % keyLength;
            dest[i] = (byte) (source[i] ^ mKey.charAt(j));
        }
        return dest;
    }
}
