package com.yikang.heartmark.common.business.other;

import android.graphics.Bitmap;
import android.os.Environment;

import com.yikang.heartmark.constant.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by guolchen on 2014/12/24.
 */
public class StorageManager {

    String rootAppDir = Environment.getExternalStorageDirectory() + File.separator + Constants.APP_DIR + File.separator;
    String rootChatDir = Environment.getExternalStorageDirectory() + File.separator + Constants.APP_DIR + File.separator + "Chat" + File.separator;

    private StorageManager() {
        createRootDir();
    }

    private static Logger logger = LoggerFactory.getLogger(StorageManager.class);

    static class StorageManagerHolder {
        static StorageManager manager = new StorageManager();
    }

    public static StorageManager getInstance() {
        return StorageManagerHolder.manager;
    }

    public boolean SaveImage(String fullName, Bitmap bitmap) {

        if (isExists(fullName)) {
            return false;
        }

        try {
            FileOutputStream fos = new FileOutputStream(fullName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean SaveImage(String fileName, Bitmap bitmap, long sessionId) {
        String fullName = getFullFileName(fileName, sessionId);
        return SaveImage(fullName, bitmap);
    }

    public boolean SaveAudio(String fullName, byte[] data) {
        if (isExists(fullName)) {
            return false;
        }

        try {
            FileOutputStream fos = new FileOutputStream(fullName);
            fos.write(data);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isExists(String fileName) {
        return isExists(fileName, true);
    }

    public boolean isExists(String fileName, boolean isRelative) {
        if (isRelative)
            fileName = rootChatDir + fileName;

        File appDir = new File(fileName);
        return appDir.exists();
    }

    public boolean isFolderExists(long sessionId) {

        String fullFolder = getFullFolder(sessionId);
        return isExists(fullFolder, false);
    }

    public boolean setupSessionFolder(long sessionId) {
        try {
            String folder = getFullFolder(sessionId);
            File rootDir = new File(rootChatDir);
            if (!rootDir.exists()) {
                rootDir.mkdir();
            }

            File sessionDir = new File(rootDir, String.valueOf(sessionId));
            if (!sessionDir.exists()) {
                boolean r = sessionDir.mkdir();
                return r;
            } else
                return false;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

    private String getFullFolder(long sessionId) {
        return rootChatDir + sessionId + File.separator;
    }


    public void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); //
            }
        }
        return;
    }

    public void clear(long sessionId) {
        String dirPath = File.separator + Constants.APP_DIR + File.separator + sessionId;
        File appDir = new File(dirPath);
        deleteDir(appDir);
    }

    public String getFullFileName(String fileName, long sessionId) {
        return rootChatDir + sessionId + File.separator + fileName;
    }

    public String getFormatFileName(String fullFileName) {
        return "file://" + fullFileName;
    }

    public String getNewAudioFileName(String ext) {
        return "audio-" + System.currentTimeMillis() + "." + ext;
    }

    public String getNewAudioFileName(String objectId, String ext) {
        return "audio-" + objectId + "." + ext;
    }

    public String createFile(String fileName, long sessionId) {
        try {
            String parentFolder = getFullFolder(sessionId);
            File parentDir = new File(parentFolder);
            if (!parentDir.exists())
                parentDir.mkdir();

            File newFile = new File(parentFolder, fileName);
            if (!newFile.exists()) {
                if (newFile.createNewFile()) {
                    return newFile.getPath();
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return "";
        }

    }

    private void deleteDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.deleteDir(files[i]);
                }
            }
            file.delete();
        }
    }


    private boolean isRootDirExists() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果不存在SD卡
            return false;
        } else {//如果存在SD卡，判断rootChatDir目录是否存在
            File chatDir = new File(rootChatDir);
            return chatDir.exists();
        }
    }

    private boolean createRootDir() {
        try {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                return false;

            File appRootDir = new File(AppCommonService.getInstance().getAppRootStorage());
            if (!appRootDir.exists()) {
                boolean r = appRootDir.mkdir();
            }

            File chatDir = new File(rootChatDir);
            if (!chatDir.exists()) {
                boolean r = chatDir.mkdir();
                return r;
            }

            return false;

        } catch (Exception e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

}
