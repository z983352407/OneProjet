package com;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class HttpFileUpLoad {
    public static String sendPost(String requestUrl, Map<String, String> requestHeader, Map<String, String> formTexts, Map<String, String> files, String requestEncoding, String responseEncoding) {
        OutputStream out = null;
        BufferedReader reader = null;
        String result = "";
        try {
            if (requestUrl == null || requestUrl.isEmpty()) {
                return result;
            }
            URL realUrl = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept", "text/html, application/xhtml+xml, image/jxr, */*");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
            if (requestHeader != null && requestHeader.size() > 0) {
                for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            if (requestEncoding == null || requestEncoding.isEmpty()) {
                requestEncoding = "UTF-8";
            }
            if (responseEncoding == null || responseEncoding.isEmpty()) {
                responseEncoding = "UTF-8";
            }
            if (requestHeader != null && requestHeader.size() > 0) {
                for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (files == null || files.size() == 0) {
                connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                out = new DataOutputStream(connection.getOutputStream());
                if (formTexts != null && formTexts.size() > 0) {
                    String formData = "";
                    for (Map.Entry<String, String> entry : formTexts.entrySet()) {
                        formData += entry.getKey() + "=" + entry.getValue() + "&";
                    }
                    formData = formData.substring(0, formData.length() - 1);
                    out.write(formData.toString().getBytes(requestEncoding));
                }
            } else {
                String boundary = "-----------------------------" + String.valueOf(new Date().getTime());
                connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
                out = new DataOutputStream(connection.getOutputStream());
                if (formTexts != null && formTexts.size() > 0) {
                    StringBuilder sbFormData = new StringBuilder();
                    for (Map.Entry<String, String> entry : formTexts.entrySet()) {
                        sbFormData.append("--" + boundary + "\r\n");
                        sbFormData.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                        sbFormData.append(entry.getValue() + "\r\n");
                    }
                    out.write(sbFormData.toString().getBytes(requestEncoding));
                }
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    String fileName = entry.getKey();
                    String filePath = entry.getValue();
                    if (fileName == null || fileName.isEmpty() || filePath == null || filePath.isEmpty()) {
                        continue;
                    }
                    File file = new File(filePath);
                    if (!file.exists()) {
                        continue;
                    }
                    out.write(("--" + boundary + "\r\n").getBytes(requestEncoding));
                    out.write(("Content-Disposition: form-data; name=\"" + fileName + "\"; filename=\"" + file.getName() + "\"\r\n").getBytes(requestEncoding));
                    out.write(("Content-Type: application/x-msdownload\r\n\r\n").getBytes(requestEncoding));
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                    out.write(("\r\n").getBytes(requestEncoding));
                }
                out.write(("--" + boundary + "--").getBytes(requestEncoding));
            }
            out.flush();
            out.close();
            out = null;
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), responseEncoding));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}

