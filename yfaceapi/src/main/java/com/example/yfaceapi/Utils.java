package com.example.yfaceapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Utils {
    public static String readGpioPG(String path) {
        String str = "";
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[16];
            fileInputStream.read(buffer);
            fileInputStream.close();
            str = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void writeStringFileFor7(File file, String content) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
