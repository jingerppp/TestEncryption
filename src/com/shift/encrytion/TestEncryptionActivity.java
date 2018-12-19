package com.shift.encrytion;

import com.shift.encrytion.FileEncryption.FileEncryptionListener;
import com.shift.encrytion.des.DESEncryption;
import com.shift.encrytion.des3.DESedeEncryption;
import com.shift.encrytion.md5.MD5Encryption;
import com.shift.encrytion.rsa.RSAEncryption;
import com.shift.encrytion.sha.SHA1Encryption;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestEncryptionActivity extends Activity implements OnClickListener, FileEncryptionListener {
    private static final String TAG = "TestEncryptionActivity";

    private TextView mOperationTitle;
    private TextView mBeforeOperation;
    private TextView mAfterOperation;

    private String mStrEncrypted;

    private FileEncryption mFileEncryption;

    private RSAEncryption mRSAEncryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();
    }

    private void init() {
        mFileEncryption = new FileEncryption(getApplicationContext(), this);
        initShowStrTest();

        testEncryptionXOR();

        testEncryptionAES();

        testEncryptionDES();

        testEncryption3DES();

        testEncryptionMD5();

        testEncryptionSHA1();

        testEncryptionRSA();
    }

    private void initShowStrTest() {
        mOperationTitle = (TextView) findViewById(R.id.operation_title);
        mBeforeOperation = (TextView) findViewById(R.id.before_operation);
        mAfterOperation = (TextView) findViewById(R.id.after_operation);
    }

    private void testEncryptionXOR() {
        Button exor = (Button) findViewById(R.id.encrypt_xor);
        exor.setOnClickListener(this);

        Button dxor = (Button) findViewById(R.id.decrypt_xor);
        dxor.setOnClickListener(this);
    }

    private void xorEncryption() {
//        //encrypt string
//        {
////            String strSource = "hliuhiufhliuhsd;jfijso;goshgosjogijsgo;j";
//            String strSource = "最最込込";
//            if (mXOREncryption != null) {
//                Log.d(TAG, "==== strSource = " + strSource);
//                mStrEncrypted = mXOREncryption.strEncrypt(strSource);
//                Log.d(TAG, "==== strEncrypted = " + mStrEncrypted);
//            }
//        } // test string

        // encrypt file
        {
            if (mFileEncryption != null) {
                Log.d(TAG, "==== test file, encryption with xor ...");
                String filePath = "/storage/emulated/0/hehe.png";
                String destPath = "/storage/emulated/0/2.png";
                mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_XOR);
                mFileEncryption.start(FileEncryption.MODE_ENCRYPT, filePath, destPath);
                Log.d(TAG, "==== test file, encryption end");
            }
        } // test file
    }

    private void xorDecrypted() {
//        //decrypt string
//        {
//            if (mXOREncryption != null) {
//                String ret = mXOREncryption.strDecrypt(mStrEncrypted);
//                Log.d(TAG, "==== strDecrypted = " + ret);
//            }
//        } // test string

        // decrypt file
        {
            if (mFileEncryption != null) {
                Log.d(TAG, "==== test file, decryption with xor ...");
                String filePath = "/storage/emulated/0/hehe.png";
                String destPath = "/storage/emulated/0/2.png";
                mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_XOR);
                mFileEncryption.start(FileEncryption.MODE_DECRYPT, destPath, filePath);
                Log.d(TAG, "==== test file, decryption end");
            }
        } // test file
    }

    private void testEncryptionAES() {
        Button eaes = (Button) findViewById(R.id.encrypt_aes);
        eaes.setOnClickListener(this);

        Button daes = (Button) findViewById(R.id.decrypt_aes);
        daes.setOnClickListener(this);

    }

    private void aesEncryption() {
        if (mFileEncryption != null) {
            Log.d(TAG, "==== test file, encryption with aes ...");
            String filePath = "/storage/emulated/0/hehe.png";
            String destPath = "/storage/emulated/0/2.png";
            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_AES);
            mFileEncryption.start(FileEncryption.MODE_ENCRYPT, filePath, destPath);
            Log.d(TAG, "==== test file, encryption end");
        }
    }

    private void aesDecryption() {
        if (mFileEncryption != null) {
            Log.d(TAG, "==== test file, decryption with aes ...");
            String filePath = "/storage/emulated/0/hehe.png";
            String destPath = "/storage/emulated/0/2.png";
            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_AES);
            mFileEncryption.start(FileEncryption.MODE_DECRYPT, destPath, filePath);
            Log.d(TAG, "==== test file, decryption end");
        }
    }

    private void testEncryptionDES() {
        Button edes = (Button) findViewById(R.id.encrypt_des);
        edes.setOnClickListener(this);

        Button ddes = (Button) findViewById(R.id.decrypt_des);
        ddes.setOnClickListener(this);
    }

    private void desEncryption() {
        {
            mOperationTitle.setText(getString(R.string.encrypt_des));
            String strSource = "hliuhiufhliuhsd;jfijso;goshgosjogijsgo;j";
            mBeforeOperation.setText(getString(R.string.before_operation, strSource));

            DESEncryption encryption = new DESEncryption();
            mStrEncrypted = encryption.strEncryption(strSource);
            mAfterOperation.setText(getString(R.string.after_operation, mStrEncrypted));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, encryption with des ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            String destPath = "/storage/emulated/0/2.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_DES);
//            mFileEncryption.start(FileEncryption.MODE_ENCRYPT, filePath, destPath);
//            Log.d(TAG, "==== test file, encryption end");
//        }
    }

    private void desDecryption() {
        {
            mOperationTitle.setText(getString(R.string.decrypt_des));
            mBeforeOperation.setText(getString(R.string.before_operation, mStrEncrypted));

            DESEncryption encryption = new DESEncryption();
            String strDecrypted = encryption.strDecryption(mStrEncrypted);
            mAfterOperation.setText(getString(R.string.after_operation, strDecrypted));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, decryption with des ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            String destPath = "/storage/emulated/0/2.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_DES);
//            mFileEncryption.start(FileEncryption.MODE_DECRYPT, destPath, filePath);
//            Log.d(TAG, "==== test file, decryption end");
//        }
    }

    private void testEncryption3DES() {
        Button edesede = (Button) findViewById(R.id.encrypt_desede);
        edesede.setOnClickListener(this);

        Button ddesede = (Button) findViewById(R.id.decrypt_desede);
        ddesede.setOnClickListener(this);
    }

    private void desedeEncryption() {
        {
            mOperationTitle.setText(getString(R.string.encrypt_desede));
            String strSource = "hliuhiufhliuhsd;jfijso;goshgosjogijsgo;j";
            mBeforeOperation.setText(getString(R.string.before_operation, strSource));

            DESedeEncryption encryption = new DESedeEncryption();
            mStrEncrypted = encryption.strEncryption(strSource);
            mAfterOperation.setText(getString(R.string.after_operation, mStrEncrypted));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, encryption with 3des ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            String destPath = "/storage/emulated/0/2.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_3DES);
//            mFileEncryption.start(FileEncryption.MODE_ENCRYPT, filePath, destPath);
//            Log.d(TAG, "==== test file, encryption end");
//        }
    }

    private void desedeDecryption() {
        {
            mOperationTitle.setText(getString(R.string.decrypt_des));
            mBeforeOperation.setText(getString(R.string.before_operation, mStrEncrypted));

            DESedeEncryption encryption = new DESedeEncryption();
            String strDecrypted = encryption.strDecryption(mStrEncrypted);
            mAfterOperation.setText(getString(R.string.after_operation, strDecrypted));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, decryption with 3des ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            String destPath = "/storage/emulated/0/2.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_3DES);
//            mFileEncryption.start(FileEncryption.MODE_DECRYPT, destPath, filePath);
//            Log.d(TAG, "==== test file, decryption end");
//        }
    }

    private void testEncryptionMD5() {
        Button md5 = (Button) findViewById(R.id.encrypt_md5);
        md5.setOnClickListener(this);
    }

    private void md5Encryption() {
        mOperationTitle.setText(getString(R.string.encrypt_md5));

        {
            String str1 = "";
            String result = "";
            MD5Encryption encryption = new MD5Encryption();
            result = encryption.getStringMD(str1);
            Log.d(TAG, "==== md5: " + result);

            String strSource = "hliuhiufhliuhsd;jfijso;goshgosjogijsgo;j";
            mBeforeOperation.setText(getString(R.string.before_operation, strSource));

            result = encryption.getStringMD(strSource);
            mAfterOperation.setText(getString(R.string.after_operation, result));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, encryption with md5 ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_MD5);
//            mFileEncryption.start(FileEncryption.MODE_ENCRYPT, "", filePath);
//            Log.d(TAG, "==== test file, encryption end");
//        }
    }

    private void testEncryptionSHA1() {
        Button sha1 = (Button) findViewById(R.id.encrypt_sha1);
        sha1.setOnClickListener(this);
    }

    private void sha1Encryption() {
        mOperationTitle.setText(getString(R.string.encrypt_sha1));

        {
            String str1 = "";
            String result = "";
            SHA1Encryption encryption = new SHA1Encryption();
            result = encryption.getStringMD(str1);
            Log.d(TAG, "==== sha1: " + result);

            String strSource = "hliuhiufhliuhsd;jfijso;goshgosjogijsgo;j";
            mBeforeOperation.setText(getString(R.string.before_operation, strSource));

            result = encryption.getStringMD(strSource);
            mAfterOperation.setText(getString(R.string.after_operation, result));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, encryption with sha1 ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_SHA_1);
//            mFileEncryption.start(FileEncryption.MODE_ENCRYPT, "", filePath);
//            Log.d(TAG, "==== test file, encryption end");
//        }
    }

    private void testEncryptionRSA() {
        Button ersa = (Button) findViewById(R.id.encrypt_rsa);
        ersa.setOnClickListener(this);

        Button drsa = (Button) findViewById(R.id.decrypt_rsa);
        drsa.setOnClickListener(this);
    }

    private void rsaEncryption() {
        {
            mOperationTitle.setText(getString(R.string.encrypt_rsa));
            String strSource = "hliuhiufhliuhsd;jfijso;goshgosjogijsgo;j";
            mBeforeOperation.setText(getString(R.string.before_operation, strSource));

            if (mRSAEncryption == null) {
                mRSAEncryption = new RSAEncryption();
                mRSAEncryption.generateKeys();
            }

            mStrEncrypted = mRSAEncryption.strEncryption(strSource);
            Log.d(TAG, "==== mStrEncrypted = " + mStrEncrypted);
            mAfterOperation.setText(getString(R.string.after_operation, mStrEncrypted));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, encryption with rsa ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            String destPath = "/storage/emulated/0/2.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_RSA);
//            mFileEncryption.start(FileEncryption.MODE_ENCRYPT, filePath, destPath);
//            Log.d(TAG, "==== test file, encryption end");
//        }
    }

    private void rsaDecryption() {
        {
            mOperationTitle.setText(getString(R.string.decrypt_rsa));
            mBeforeOperation.setText(getString(R.string.before_operation, mStrEncrypted));

            if (mRSAEncryption == null) {
                mRSAEncryption = new RSAEncryption();
                mRSAEncryption.generateKeys();
            }

            String strDecrypted = mRSAEncryption.strDecryption(mStrEncrypted);
            Log.d(TAG, "==== strDecrypted = " + strDecrypted);
            mAfterOperation.setText(getString(R.string.after_operation, strDecrypted));
        }

//        if (mFileEncryption != null) {
//            Log.d(TAG, "==== test file, decryption with rsa ...");
//            String filePath = "/storage/emulated/0/hehe.png";
//            String destPath = "/storage/emulated/0/2.png";
//            mFileEncryption.setAlgorithm(FileEncryption.ALG_MODE_RSA);
//            mFileEncryption.start(FileEncryption.MODE_DECRYPT, destPath, filePath);
//            Log.d(TAG, "==== test file, decryption end");
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.encrypt_xor:
                xorEncryption();
                break;
            case R.id.decrypt_xor:
                xorDecrypted();
                break;
            case R.id.encrypt_aes:
                aesEncryption();
                break;
            case R.id.decrypt_aes:
                aesDecryption();
                break;
            case R.id.encrypt_des:
                desEncryption();
                break;
            case R.id.decrypt_des:
                desDecryption();
                break;
            case R.id.encrypt_desede:
                desedeEncryption();
                break;
            case R.id.decrypt_desede:
                desedeDecryption();
                break;
            case R.id.encrypt_md5:
                md5Encryption();
                break;
            case R.id.encrypt_sha1:
                sha1Encryption();
                break;
            case R.id.encrypt_rsa:
                rsaEncryption();
                break;
            case R.id.decrypt_rsa:
                rsaDecryption();
                break;

            default:
                break;
        }

    }

    @Override
    public void operationStarting() {
        Log.d(TAG, "==== operationStarting");
    }

    @Override
    public void operationFailed(int mode, String filePath) {
        Log.d(TAG, "==== operationFailed");
    }

    @Override
    public void operationSuccess(int mode, String fileName) {
        Log.d(TAG, "==== operationSuccess, fileName = " + fileName);
    }

    @Override
    public void operationStopped(int mode) {
        Log.d(TAG, "==== operationStopped");
    }
}
