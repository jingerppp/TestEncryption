package com.shift.encrytion.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.text.TextUtils;
import android.util.Log;

public class FileOperation {
    private static final String TAG = "TestFileOps";

    private static final String COMMENTS_TAG = "#";

    public static final int TYPE_INVALID = -1;
    public static final int TYPE_PHOTO = 0;
    public static final int TYPE_MUSIC = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_FILE  = 3;
    public static final int TYPE_TXT   = 4;
    public static final int TYPE_DOC   = 5;
    public static final int TYPE_PPT   = 6;
    public static final int TYPE_EXCEL = 7;
    public static final int TYPE_PDF   = 8;
    public static final int TYPE_UNKNOWN = 9;

    /* file type extensions */
    //video from layer
    public static final String[] video_extensions = {
            ".3gp",
            ".3g2",
            ".divx",
            ".h264",
            ".h265",
            ".avi",
            ".m2ts",
            ".mkv",
            ".mov",
            ".mp4",
            ".mpg",
            ".mpeg",
            ".rm",
            ".rmvb",
            ".wmv",
            ".ts",
            ".tp",
            ".dat",
            ".vob",
            ".flv",
            ".bit",
            ".vc1",
            ".m4v",
            ".f4v",
            ".asf",
            ".lst",
            ".mts",
            ".webm",
            ".mpe",
            ".pmp",
            ".mvc",
            /* "" */
    };
    // music
    private static final String[] music_extensions = {
            ".mp3",
            ".wma",
            ".m4a",
            ".aac",
            ".ape",
            ".mp2",
            ".ogg",
            ".flac",
            ".alac",
            ".wav",
            ".mid",
            ".xmf",
            ".mka",
            ".aiff",
            ".aifc",
            ".aif",
            ".pcm",
            ".adpcm"
    };
    // photo
    private static final String[] photo_extensions = {
            ".jpg",
            ".jpeg",
            ".bmp",
            ".tif",
            ".tiff",
            ".png",
            ".gif",
            ".giff",
            ".jfi",
            ".jpe",
            ".jif",
            ".jfif",
            ".mpo",
            ".3dg",
            "3dp"
    };

    /** get file type op*/
    public static boolean isVideo(String type) {
        for (String ext : video_extensions) {
            if (ext.equals(type))
                return true;
        }
        return false;
    }

    public static boolean isMusic(String type) {
        for (String ext : music_extensions) {
            if (ext.equals(type))
                return true;
        }
        return false;
    }

    public static boolean isPhoto(String type) {
        for (String ext : photo_extensions) {
            if (ext.equals(type))
                return true;
        }
        return false;
    }

    public static boolean isTxt(String type) {
        return ".txt".equals(type);
    }

    public static boolean isWord(String type) {
        return ".doc".equals(type) || ".docx".equals(type);
    }

    public static boolean isPPT(String type) {
        return ".ppt".equals(type) || ".pptx".equals(type);
    }

    public static boolean isExcel(String type) {
        return ".xls".equals(type) || ".xlsx".equals(type);
    }

    public static boolean isPDF(String type) {
        return ".pdf".equals(type);
    }

    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return isFileExists(file);
    }

    public static int getFileTypeInt(String fileName) {
        Log.d(TAG, "filename = " + fileName);
        int ret = TYPE_FILE;
        String type = getFileType(fileName);
        Log.d(TAG, "type = " + type);
        if (isPhoto(type)) {
            ret = TYPE_PHOTO;
        } else if (isMusic(type)) {
            ret = TYPE_MUSIC;
        } else if (isVideo(type)) {
            ret = TYPE_VIDEO;
        } else {
            ret = TYPE_FILE;
        }
        return ret;
    }

    public static int getDocFileType(String fileName) {
        int ret;
        String type = getFileType(fileName);
        if (isTxt(type)) {
            ret = TYPE_TXT;
        } else if (isWord(type)) {
            ret = TYPE_DOC;
        } else if (isExcel(type)) {
            ret = TYPE_EXCEL;
        } else if (isPPT(type)) {
            ret = TYPE_PPT;
        } else if (isPDF(type)) {
            ret = TYPE_PDF;
        } else {
            ret = TYPE_UNKNOWN;
        }
        return ret;
    }

    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    public static String getFileType(String fileName) {
        if (fileName == null)
            return "";
        int index = fileName.lastIndexOf(".");
        if (index < 0)
            return "";
        String ext = fileName.substring(index, fileName.length()).toLowerCase();
        return ext;
    }

    public static String getFileNameFromPath(String filePath) {
        String ret = "";
        int index = filePath.lastIndexOf("/");
        if (index < 0 || index >= filePath.length() - 1)
            return ret;
        ret = filePath.substring(index + 1);
        return ret;
    }

    public static String getDestFilePath(String srcPath, String fileDir) {
        if (!isFileExists(fileDir))
            return null;

        File file = new File(srcPath);
        if (!isFileExists(file))
            return null;

        String fileName = file.getName();
        if (TextUtils.isEmpty(fileName))
            return null;

        return fileDir + "/" + fileName;
    }

    public static String getRandomFilePath(String fileDir, String fileName) {
        if (!isFileExists(fileDir)) {
            return null;
        }

        String filePath = fileDir + "/" + getRandomFileName() + getFileType(fileName);

        while (isFileExists(filePath)) {
            filePath = fileDir + "/" + getRandomFileName() + getFileType(fileName);
        }

        return filePath;
    }

    private static String getRandomFileName() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        return uuidStr;
    }

    public static boolean createDir(String dirPath) {
        File file = new File(dirPath);
        if (isFileExists(file)) {
            return true;
        }
        return file.mkdirs();
    }

    public static boolean createFile(File file) {
        if (isFileExists(file))
            return true;

        return doCreate(file);
    }

    public static boolean createFile(String filePath) {
        File file = new File(filePath);

        return createFile(file);
    }

    public static boolean createNewFile(String filePath) {
        File file = new File(filePath);

        // create a new file, if the file exists, delete it.
        if (isFileExists(file)) {
            deleteFiles(file);
        }

        return doCreate(file);
    }

    private static boolean doCreate(File file) {
        try {
            File parentFile = file.getParentFile();
            if (!isFileExists(parentFile)) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ArrayList<String> readFile(String filepath, String regularExpression) {
        return readFile(new File(filepath), regularExpression);
    }

    public static ArrayList<String> readFile(String filepath, String regularExpression, boolean ignoreAnno,
            boolean onlyOneLine) {
        return readFile(new File(filepath), regularExpression, ignoreAnno, onlyOneLine);
    }

    public static ArrayList<String> readFile(File file, String regularExpression) {
        return readFile(file, regularExpression, true, false);
    }

    public static ArrayList<String> readFile(File file, String regularExpression, boolean ignoreAnno,
            boolean onlyOneLine) {
        if (file == null || !file.exists() || !file.canRead()) {
            Log.e(TAG, "read file failed, filepath = " + file.getPath());
            return null;
        }

        ArrayList<String> contents = new ArrayList<String>();

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null && (line = line.trim()) != null) {
                if (ignoreAnno && (line.isEmpty() || line.startsWith(COMMENTS_TAG))) {
                    continue;
                }

                if (!TextUtils.isEmpty(regularExpression)) {
                    if (line.matches(regularExpression)) {
                        contents.add(line);
                    }
                } else {
                    contents.add(line);
                }

                if (onlyOneLine)
                    break;
            }
        } catch (IOException e) {
            Log.e(TAG, "Read File error, caused by: " + e.getMessage());
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                Log.e(TAG, "Close the reader error, caused by: " + e.getMessage());
            }
        }
        return contents;
    }

    public static boolean writeFile(String filepath, String content, boolean append) {
        File file = new File(filepath);
        if (file == null || !file.exists() || !file.canWrite())
            return false;

        if (TextUtils.isEmpty(content))
            return false;

        FileWriter fw = null;
        try {
            fw = new FileWriter(file, append);
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
        }
        return true;
    }

    public static boolean deleteFiles(String dirPath) {
        return deleteFiles(dirPath, null, false);
    }

    public static boolean deleteFiles(File file) {
        return deleteFiles(file, null, false);
    }

    public static boolean deleteFiles(String dirPath, String ext, boolean delDir) {
        return deleteFiles(new File(dirPath), ext, delDir);
    }

    public static boolean deleteFiles(File dir, String ext, boolean delDir) {
        Log.d(TAG, "delete file " + dir.getPath());
        boolean ret = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null)
                return false;

            for (int i = 0; i < children.length; i++) {
                deleteFiles(new File(dir, children[i]), ext, delDir);
            }
            if (delDir) {
                ret = dir.delete();
            }
        } else {
            if (dir.isFile() && (TextUtils.isEmpty(ext) || dir.getName().endsWith(ext))) {
                ret = dir.delete();
            }
        }

        return ret;
    }

    public static int getFileSize(File file) throws IOException {
        int size = 0;
        if (file != null && file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            try {
                size = fis.available();
            } finally {
                if (fis != null)
                    fis.close();
            }
        }
        return size;
    }

    public static boolean copyFile(String srcFile, String dstFile) {
        return copyFile(new File(srcFile), new File(dstFile));
    }

    public static boolean copyFile(File srcFile, String dstFile) {
        return copyFile(srcFile, new File(dstFile));
    }

    public static boolean copyFile(File srcFile, File dstFile) {
        if (!isFileExists(srcFile))
            return false;

        try {
            InputStream inputStream = new FileInputStream(srcFile);
            if (isFileExists(dstFile)) {
                dstFile.delete();
            }

            OutputStream outputStream = new FileOutputStream(dstFile);
            try {
                int cnt;
                byte[] buf = new byte[4096];
                while ((cnt = inputStream.read(buf)) >= 0) {
                    outputStream.write(buf, 0, cnt);
                }
            } finally {
                outputStream.close();
                inputStream.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] data = new byte[count];
        for (int i = begin; i < begin + count; i++)
            data[i - begin] = src[i];
        return data;
    }
}
