package com.aliyun.rts.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AliHttpTool {
    public static String android_http_get(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            if (httpURLConnection == null || 200 != httpURLConnection.getResponseCode()) {
                return null;
            }
            return new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8")).readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int android_http_post(String str, String[] strArr, byte[] bArr) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            if (strArr != null) {
                int length = strArr.length / 2;
                for (int i = 0; i < length; i++) {
                    int i2 = i * 2;
                    httpURLConnection.setRequestProperty(strArr[i2], strArr[i2 + 1]);
                }
            }
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(bArr);
            dataOutputStream.flush();
            dataOutputStream.close();
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            return 400;
        }
    }
}
