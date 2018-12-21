package com.ys.rkapi.Utils;

import android.os.StatFs;
import android.util.Log;


import com.ys.rkapi.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by RYX on 2017/8/30.
 */

public class StorageUtils {


    // 获取存储空间相关
    private static long readBlockSize(String path, int flag) {
        StatFs sf = new StatFs(path);
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        if (flag == 0) { // sum
            return blockSize * blockCount / 1024;
        } else if (flag == 1) { // avail
            return blockSize * availCount / 1024;
        } else {
            return (blockSize * blockCount / 1024) - (blockSize * availCount / 1024);
        }
    }

    public static String getRealSizeOfNand() {
        String size = "0G";
        if (readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) < 3) {
            size = "4G";
        } else if (readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) >= 3 && readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) < 7) {
            size = "8G";
        } else if (readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) >= 7 && readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) < 15) {
            size = "16G";
        } else if (readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) >= 15 && readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) < 31) {
            size = "32G";
        } else if (readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) >= 31 && readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) < 63) {
            size = "64G";
        } else if (readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) >= 63 && readBlockSize(Constant.NAND_PATH, 0) / (1024 * 1024) < 127) {
            size = "128G";
        } else {
            size = "8G";
        }

        return size;
    }


    // 读取mount节点
    public static List<String> getAllUSBStorageLocations() {
        List<String> mMounts = new ArrayList<String>();
        try {
            File mountFile = new File("/proc/mounts");
            if (mountFile.exists()) {
                Scanner scanner = new Scanner(mountFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/dev/block/vold/")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[1];
                        if (element.contains("USB")||element.contains("media_rw"))
                            mMounts.add(element);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mMounts;
    }

    public static void setValueToEEPROM(String val) {
        Class<?> classType;
        try {
            classType = Class.forName("android.os.Custom");
            Method method = classType.getDeclaredMethod("setUsrbuf", new Class[]{String.class});
            method.invoke(classType, new Object[]{val});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValueFromEEPROM() {
        String str = "";
        File file = new File(Constant.EEPROM_FILE_NAME);
        long fileSize = file.length();
        Log.d("EEPROM读取长度", "值 = " + fileSize);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[(int) fileSize];
            fileInputStream.read(buffer);
            fileInputStream.close();
            str = new String(buffer);
            Log.d("EEPROM读取", "值 = " + str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static long getTotalMemorySize() {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) / 1024L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getRealMeoSize() {
        String size = "0";
        if (getTotalMemorySize() <= 512) {
            size = "512M";
        } else if (getTotalMemorySize() > 512 && getTotalMemorySize() <= 1024) {
            size = "1G";
        } else if (getTotalMemorySize() > 1024 && getTotalMemorySize() <= 2048) {
            size = "2G";
        } else if (getTotalMemorySize() > 2048 && getTotalMemorySize() <= 6114) {
            size = "4G";
        } else if (getTotalMemorySize() > 6114) {
            size = "6G";
        }
        return size;
    }
}
