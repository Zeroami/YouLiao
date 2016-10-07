package com.zeroami.commonlib.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：文件工具类</p>
 */
public final class LFileUtils {

    public static final long GB = 1073741824; // 1024 * 1024 * 1024
    public static final long MB = 1048576; // 1024 * 1024
    public static final long KB = 1024;

    public static final int ICON_TYPE_ROOT = 1;
    public static final int ICON_TYPE_FOLDER = 2;
    public static final int ICON_TYPE_MP3 = 3;
    public static final int ICON_TYPE_MTV = 4;
    public static final int ICON_TYPE_JPG = 5;
    public static final int ICON_TYPE_FILE = 6;

    public static final String REG_VIDEO = "^.*\\.(swf|flv|mp4|rmvb|avi|mpeg|ra|ram|mov|wmv)$";
    public static final String REG_MUSIC = "^.*\\.(mp3|wav|wma|ogg|ape|acc)$";
    public static final String REG_IMAGE = "^.*\\.(gif|jpg|jpeg|bmp|png)$";

    private static final String FILENAME_REGIX = "^[^\\/?\"*:<>\\]{1,255}$";

    public static final String FILE_EXTENSION_SEPARATOR = ".";

    private LFileUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 是否存在SDCard
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 删除文件或递归删除文件夹
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 删除文件或递归删除文件夹
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] childFile = file.listFiles();
        if (childFile == null || childFile.length == 0) {
            return file.delete();
        }
        for (File f : childFile) {
            deleteFile(f);
        }
        return file.delete();
    }

    /**
     * 重命名文件和文件夹
     *
     * @param file
     * @param newFileName
     * @return
     */
    public static boolean renameFile(File file, String newFileName) {
        if (newFileName.matches(FILENAME_REGIX)) {
            File newFile = null;
            if (file.isDirectory()) {
                newFile = new File(file.getParentFile(), newFileName);
            } else {
                String temp = newFileName
                        + file.getName().substring(
                        file.getName().lastIndexOf('.'));
                newFile = new File(file.getParentFile(), temp);
            }
            if (file.renameTo(newFile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件大小，不存在返回-1
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }


    /**
     * 获取文件大小，不存在返回-1
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 用于递归查找文件夹下面的符合条件的文件
     *
     * @param folder
     * @param filter
     * @return
     */
    public static List<HashMap<String, Object>> recursionFolder(File folder,
                                                                FileFilter filter) {

        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        // 获得文件夹下的所有目录和文件集合
        File[] files;
        // 判断适配器是否为空
        if (filter != null) {
            files = folder.listFiles(filter);
        } else {
            files = folder.listFiles();
        }
        // 找到合适的文件返回
        if (files != null) {
            for (int m = 0; m < files.length; m++) {
                File file = files[m];
                if (file.isDirectory()) {
                    // 是否递归调用
                    list.addAll(recursionFolder(file, filter));

                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("file", file);
                    // 设置图标种类
                    if (file.getAbsolutePath().toLowerCase().matches(REG_MUSIC)) {
                        map.put("iconType", ICON_TYPE_MP3);
                    } else if (file.getAbsolutePath().toLowerCase()
                            .matches(REG_VIDEO)) {
                        map.put("iconType", ICON_TYPE_MTV);
                    } else if (file.getAbsolutePath().toLowerCase()
                            .matches(REG_IMAGE)) {
                        map.put("iconType", ICON_TYPE_JPG);
                    } else {
                        map.put("iconType", ICON_TYPE_FILE);
                    }
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 资源管理器,查找该文件夹下的文件和目录，不递归查找
     *
     * @param folder
     * @param filter
     * @return
     */
    public static List<HashMap<String, Object>> unrecursionFolder(File folder,
                                                                  FileFilter filter) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        // 如果是SD卡路径,不添加父路径
        if (!folder.getAbsolutePath().equals(
                Environment.getExternalStorageDirectory().getAbsolutePath())) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("file", folder.getParentFile());
            map.put("iconType", ICON_TYPE_ROOT);
            list.add(map);
        }
        File[] files;
        // 判断适配器是否为空
        // 获得文件夹下的所有目录和文件集合
        if (filter != null) {
            files = folder.listFiles(filter);
        } else {
            files = folder.listFiles();
        }
        if (files != null && files.length > 0) {
            for (File p : files) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("file", p);
                // 设置图标种类
                if (p.isDirectory()) {
                    map.put("iconType", ICON_TYPE_FOLDER);
                } else {
                    if (p.getAbsolutePath().toLowerCase().matches(REG_MUSIC)) {
                        map.put("iconType", ICON_TYPE_MP3);
                    } else if (p.getAbsolutePath().toLowerCase()
                            .matches(REG_VIDEO)) {
                        map.put("iconType", ICON_TYPE_MTV);
                    } else if (p.getAbsolutePath().toLowerCase()
                            .matches(REG_IMAGE)) {
                        map.put("iconType", ICON_TYPE_JPG);
                    } else {
                        map.put("iconType", ICON_TYPE_FILE);
                    }
                }
                // 添加
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 示例:"^.*\\.(mp3|mp4|3gp)$"
     *
     * @param reg        匹配的正则
     * @param isAllowDir 是否允许文件夹存在
     * @return
     */
    public static FileFilter getFileFilter(final String reg, boolean isAllowDir) {
        if (isAllowDir) {
            return new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    return pathname.getAbsolutePath().toLowerCase()
                            .matches(reg)
                            || pathname.isDirectory();
                }
            };
        } else {
            return new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    return pathname.getAbsolutePath().toLowerCase()
                            .matches(reg)
                            && pathname.isFile();
                }
            };
        }
    }


    /**
     * 读取文件
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static String readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(
                    file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent == null ? null : fileContent.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 写入文件
     *
     * @param filePath
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFile(String filePath, String content,
                                    boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 写入文件
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * 复制文件
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
            File file = new File(destFilePath);
            makeDirs(file.getAbsolutePath());
            outputStream = new FileOutputStream(file, false);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            outputStream.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }

    }

    /**
     * 输入流转byte[]
     *
     * @param inputStream
     * @return
     */
    public static final byte[] input2byte(InputStream inputStream) {
        if (inputStream == null)
            return null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swapStream.toByteArray();
    }

    /**
     * 读取文件到List
     *
     * @param filePath
     * @param charsetName
     * @return 返回List或null
     * @throws RuntimeException
     */
    public static List<String> readFileToList(String filePath,
                                              String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (!file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(
                    file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }


    /**
     * 获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 获取文件夹名称
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 获取扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder
                .mkdirs();
    }


    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * 判断文件夹是否存在
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }
}