package com.lu.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    /**
     * 读取json文件，返回json串
     *
     * @param fileName 完整路径文件名
     * @return String的json串
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToTxt(String fileName, String method, String content) throws IOException {
        FileWriter fw;
        if (method.equals("a") || method.equals("a+"))
            fw = new FileWriter(fileName, true);
        else {
            fw = new FileWriter(fileName);
        }
        BufferedWriter bf = new BufferedWriter(fw);
        bf.write(content);
        bf.flush();
        bf.close();
        fw.close();
    }
}
