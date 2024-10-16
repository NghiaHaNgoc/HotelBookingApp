package com.hotel.hotel_booking_app.util;

import android.net.Uri;

import java.io.ByteArrayOutputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Base64;

public class FileUtil {
    public static String inputStreamToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
//        return byteBuffer.toByteArray();
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
