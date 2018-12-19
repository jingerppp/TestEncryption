package com.shift.encrytion.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;

import com.shift.encrytion.core.EncryptionCallback;
import com.shift.encrytion.core.FileOperation;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public abstract class BlockEncryption {
    private static final String TAG = "BlockEncryption";

    private Cipher mEncrytCipher = null;
    private Cipher mDecrytCipher = null;

    private Context mContext;
    private EncryptionCallback mCallback;

    // whether delete the source file.
    private boolean mDeleteSrcFile = true;

    private static final int BUFFER_SIZE = 4096;

    public BlockEncryption() {
    }

    public BlockEncryption(Context context, boolean deleteSrcFile, EncryptionCallback callback) {
        mContext = context;
        mDeleteSrcFile = deleteSrcFile;
        mCallback = callback;
    }

    private int roundUpBlockSize(int bufferSize) {
        return bufferSize +  getBlockSize();
    }

    public abstract String getAlgorithm();
    public abstract Cipher getCipher(int mode);

    /**
     * Set the block size for padding.
     * Different size for different algorithm. e.g 16 bytes for AES.
     */
    protected abstract int getBlockSize();

    /**
     * Different algorithms may use different buffer size.
     */
    protected int getBufferSize() {
        return BUFFER_SIZE;
    }

    private byte[] encrypt(byte[] rawData, Cipher cipher) {
        return BlockEncryptionHelper.encrypt(rawData, cipher);
    }

    /**
     * Decrypt files with algorithm AES or DES. When {@value destPath} is null or empty, save the file
     * into current directory or into original directory.
     */
    public boolean decryptFile(String srcPath, String destPath) {
        Log.d(TAG, "==== decryptFile, srcPath = " + srcPath + ", destPath = " + destPath);
        File srcFile = new File(srcPath);
        if (!FileOperation.isFileExists(srcFile)) {
            return false;
        }

        if (mDecrytCipher == null) {
            mDecrytCipher = getCipher(Cipher.DECRYPT_MODE);
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        File destFile = null;

        boolean isSameFile = false;
        if (TextUtils.equals(srcPath, destPath)) {
            destPath = "";
            isSameFile = true;
        }

        try {
            fileInputStream = new FileInputStream(srcFile);

            if (TextUtils.isEmpty(destPath)) {
                destPath = srcFile.getParent();
                destPath = FileOperation.getRandomFilePath(destPath, srcFile.getName());
            }

            destFile = new File(destPath);
            FileOperation.createFile(destFile);
            if (destFile.isDirectory()) {
                destPath = FileOperation.getRandomFilePath(destPath, srcFile.getName());
                destFile = new File(destPath);
            }
            fileOutputStream = new FileOutputStream(destFile);

            // decrypt file
            int cnt;
            byte[] buf = new byte[roundUpBlockSize(getBufferSize())];
            while ((cnt = fileInputStream.read(buf)) >= 0) {
                byte[] rawData = buf;
                if (cnt != buf.length)
                    rawData = FileOperation.subBytes(buf, 0, cnt);
                byte[] decryptData = encrypt(rawData, mDecrytCipher);
                if (decryptData != null)
                    fileOutputStream.write(decryptData, 0, decryptData.length);
                else
                    return false;
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

        if (isSameFile && destFile != null) {
            destFile.renameTo(srcFile);
        } else if (mDeleteSrcFile) {
            FileOperation.deleteFiles(srcFile);
        }

        if (mCallback != null) {
            mCallback.callback(srcFile.getName());
        }

        return true;
    }

    /**
     * Encrypt file with algorithm AES or DES. When {@value destPath} is null or empty, create a new
     * file in current directory, and the file name is random with 32 characters.
     */
    public boolean encryptFile(String srcPath, String destPath) {
        Log.d(TAG, "==== encryptFile, srcPath = " + srcPath + ", destPath = " + destPath);
        File srcFile = new File(srcPath);
        if (!FileOperation.isFileExists(srcFile)) {
            return false;
        }

        if (mEncrytCipher == null) {
            mEncrytCipher = getCipher(Cipher.ENCRYPT_MODE);
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        boolean isSameFile = false;
        // the name of encryption file maybe invariant.
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

        try {
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(destFile);

            // encrypt file
            int cnt;
            byte[] buf = new byte[getBufferSize()];
            while ((cnt = fileInputStream.read(buf)) >= 0) {
                byte[] rawData = buf;
                if (cnt != buf.length)
                    rawData = FileOperation.subBytes(buf, 0, cnt);
                byte[] encryptData = encrypt(rawData, mEncrytCipher);
                fileOutputStream.write(encryptData, 0, encryptData.length);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
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
        } else if (mDeleteSrcFile) {
            FileOperation.deleteFiles(srcFile);// delete the source file after encrypting successfully.
        }

        if (mCallback != null) {
            mCallback.callback(destFile.getName());
        }

        return true;
    }

    public String strEncryption(String src) {
        byte[] plainText = src.getBytes();
        if (mEncrytCipher == null) {
            mEncrytCipher = getCipher(Cipher.ENCRYPT_MODE);
        }

        byte[] cipherText = encrypt(plainText, mEncrytCipher);

        try {
            return new String(Base64.encode(cipherText, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public String strDecryption(String src) {
        byte[] cipherText = null;
        try {
            cipherText = Base64.decode(src.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (mDecrytCipher == null) {
            mDecrytCipher = getCipher(Cipher.DECRYPT_MODE);
        }

        byte[] plainText = encrypt(cipherText, mDecrytCipher);

        try {
            return new String(plainText, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
