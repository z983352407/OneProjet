package com;


import java.util.HashMap;
import java.util.Map;

public class FileUpLoad {
    public static void main(String[] args) {

        //身份认证参数
        Map headers = new HashMap();
        headers.put("EngineCode", "");
        headers.put("EngineSecret", "");
        //SchemaCode 为表单编码
        //FilePropertyName 为控件编码
        //BizObjectId  上传数据的ID
        String url = "https://www.h3yun.com/OpenApi/UploadAttachment?SchemaCode=&FilePropertyName=&BizObjectId=";
        // 示例 String url = "https://www.h3yun.com/OpenApi/UploadAttachment?SchemaCode=D000867ecbc44d64af9413586350c71585ac04c&FilePropertyName=F0000001&BizObjectId=872998cb-fdd6-478a-adab-1763bf15ee1a";
/*
        调用上传方法
        @param requestUrl         请求url
        @param requestHeader      请求头
        @param formTexts          表单数据
        @param files              上传文件
        @param requestEncoding    请求编码
        @param responseEncoding   响应编码
        @return                   页面响应html
*/
        //数据请求头,Content-Type:application/json
        Map<String, String> formTexts = new HashMap<>();
        formTexts.put("Content-Type", "application/json");

        Map<String, String> files = new HashMap<>();
        //Key为任意值,Value为上传文件绝对路径
        files.put("", "");

        //requestEncoding 请求格式编码:UTF-8
        //responseEncoding 相应格式编码:UTF-8
        String result = HttpFileUpLoad.sendPost(url, headers, formTexts, files, "utf-8", "utf-8");

        System.out.println(result);
    }
}