package com.shift.encrytion;

import java.util.ArrayList;

import com.shift.encrytion.aes.AESEncryption;
import com.shift.encrytion.core.BlockEncryption;
import com.shift.encrytion.core.EncryptionCallback;
import com.shift.encrytion.core.MessageDigestEncryption;
import com.shift.encrytion.des.DESEncryption;
import com.shift.encrytion.des3.DESedeEncryption;
import com.shift.encrytion.md5.MD5Encryption;
import com.shift.encrytion.rsa.RSAEncryption;
import com.shift.encrytion.sha.SHA1Encryption;
import com.shift.encrytion.xor.XOREncryption;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * The only entry to encrypt file or decrypt file. Use the {@code start} to start a
 * {@code AsyncTask} to operate. To avoid mistakes, all files' path are absolute. Use different
 * encryption algorithm by {@code mAlgMode}, and the default one is AES.
 * 
 * NOTE: remove all debug logs before releasing.
 */
public class FileEncryption implements EncryptionCallback {
    private static String TAG = "FileEncryption";

    public static final int MODE_INVALID = -1;
    public static final int MODE_ENCRYPT = 0;
    public static final int MODE_DECRYPT = 1;

    public static final int ALG_MODE_XOR = 0;
    public static final int ALG_MODE_AES = 1;
    public static final int ALG_MODE_DES = 2;
    public static final int ALG_MODE_3DES = 3;
    public static final int ALG_MODE_MD5 = 4;
    public static final int ALG_MODE_SHA_1 = 5;
    public static final int ALG_MODE_RSA = 6;

    private Context mContext;
    private int mAlgMode = -1;

    private boolean isOperating = false;
    private int operationMode = MODE_INVALID;

    // whether delete the source file.
    private boolean mDeleteSrcFile = true;

    private ArrayList<String> mSrcFiles = new ArrayList<>();
    private String mDestPath;

    private FileEncryptionListener mListener;

    private EncryptionTask mTask = null;

    private XOREncryption mXOREncryption;
    private BlockEncryption mBlockEncryption;
    private MessageDigestEncryption mMDEncryption;

    public FileEncryption(Context context, FileEncryptionListener listener) {
        this(context, ALG_MODE_AES, listener);
    }

    public FileEncryption(Context context, int algorithm, FileEncryptionListener listener) {
        mContext = context;
        mListener = listener;
        mAlgMode = algorithm;

        if (algorithm < 0) {
            throw new IllegalArgumentException("algorithm is invalid...");
        }

        if (listener == null) {
            throw new IllegalArgumentException("FileEncryptionListener can't be null...");
        }
    }

    public void setAlgorithm(int alg) {
        if (alg != mAlgMode) {
            mBlockEncryption = null;

            mAlgMode = alg;
        }
    }

    public void setSrcFileDelete(boolean delete) {
        mDeleteSrcFile = delete;
    }

    public void start(int mode, String srcFile) {
        ArrayList<String> files = new ArrayList<>();
        files.add(srcFile);
        start(mode, files, null);
    }

    public void start(int mode, String srcFile, String destPath) {
        ArrayList<String> files = new ArrayList<>();
        files.add(srcFile);
        start(mode, files, destPath);
    }

    public void start(int mode, ArrayList<String> srcFiles, String destPath) {
        synchronized (this) {
            if (isOperating) {
                mListener.operationStarting();
                return;
            }

            operationMode = mode;
            mSrcFiles.clear();
            mSrcFiles.addAll(srcFiles);
            mDestPath = destPath;

            needStopTask();

            mTask = new EncryptionTask();
            mTask.execute();

            isOperating = true;
        }
    }

    public void needStopTask() {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
            mTask = null;
        }
    }

    public interface FileEncryptionListener {
        void operationStarting();

        void operationFailed(int mode, String filePath);

        void operationSuccess(int mode, String fileName);

        void operationStopped(int mode);
    }

    /**
     * Encrypt files with special algorithm. The destination path maybe empty or null, choose the right
     * one in function of the algorithm.
     */
    private void encryptFiles() {
        synchronized (this) {
            boolean ret = false;
            for (String srcPath : mSrcFiles) {
                if (mAlgMode == ALG_MODE_AES
                        || mAlgMode == ALG_MODE_DES
                        || mAlgMode == ALG_MODE_3DES
                        || mAlgMode == ALG_MODE_RSA) {
                    ret = mBlockEncryption.encryptFile(srcPath, mDestPath);
                } else if (mAlgMode == ALG_MODE_XOR) {
                    ret = mXOREncryption.fileEncrypt(srcPath, mDestPath);
                } else if (mAlgMode == ALG_MODE_MD5) {
                    ret = true;
                    mListener.operationSuccess(ALG_MODE_MD5, mMDEncryption.getFileMD(mDestPath));
                } else if (mAlgMode == ALG_MODE_SHA_1) {
                    ret = true;
                    mListener.operationSuccess(ALG_MODE_SHA_1, mMDEncryption.getFileMD(mDestPath));
                }

                if (mBlockEncryption != null) {
                    Log.d(TAG, "==== alg = " + mBlockEncryption.getAlgorithm());
                }
                if (!ret) {
                    mListener.operationFailed(operationMode, srcPath);
                }
            }
        }
    }

    /**
     * Decrypt files with special algorithm. The destination path maybe empty or null, choose the right
     * function by the algorithm.
     */
    private void decryptFiles() {
        synchronized (this) {
            boolean ret = false;
            for (String srcPath : mSrcFiles) {
                if (mAlgMode == ALG_MODE_AES
                        || mAlgMode == ALG_MODE_3DES
                        || mAlgMode == ALG_MODE_3DES
                        || mAlgMode == ALG_MODE_RSA) {
                    ret = mBlockEncryption.decryptFile(srcPath, mDestPath);
                } else if (mAlgMode == ALG_MODE_XOR) {
                    ret = mXOREncryption.fileDecrypt(srcPath, mDestPath);
                }

                if (mBlockEncryption != null) {
                    Log.d(TAG, "==== alg = " + mBlockEncryption.getAlgorithm());
                }
                if (!ret) {
                    mListener.operationFailed(operationMode, srcPath);
                }
            }
        }
    }

    @Override
    public void callback(String info) {
        mListener.operationSuccess(operationMode, info);
    }

    private void operation() {
        Log.d(TAG, "==== operation, opMode = " + operationMode + ", algMode = " + mAlgMode);
        if (mAlgMode == ALG_MODE_AES && mBlockEncryption == null) {
            mBlockEncryption = new AESEncryption(mContext, mDeleteSrcFile, this);
        } else if (mAlgMode == ALG_MODE_XOR && mXOREncryption == null) {
            mXOREncryption = new XOREncryption();
        } else if (mAlgMode == ALG_MODE_DES && mBlockEncryption == null) {
            mBlockEncryption = new DESEncryption(mContext, mDeleteSrcFile, this);
        } else if (mAlgMode == ALG_MODE_3DES && mBlockEncryption == null) {
            mBlockEncryption = new DESedeEncryption(mContext, mDeleteSrcFile, this);
        } else if (mAlgMode == ALG_MODE_MD5 && mMDEncryption == null) {
            mMDEncryption = new MD5Encryption();
        } else if (mAlgMode == ALG_MODE_SHA_1 && mMDEncryption == null) {
            mMDEncryption = new SHA1Encryption();
        } else if (mAlgMode == ALG_MODE_RSA && mBlockEncryption == null) {
            mBlockEncryption = new RSAEncryption();
            ((RSAEncryption)mBlockEncryption).generateKeys();
        }

        if (operationMode == MODE_ENCRYPT) {
            encryptFiles();
        } else if (operationMode == MODE_DECRYPT) {
            decryptFiles();
        }
    }

    private class EncryptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void result) {
            isOperating = false;
            mListener.operationStopped(operationMode);
        }

        @Override
        protected Void doInBackground(Void... params) {
            operation();
            return null;
        }

    }
}
