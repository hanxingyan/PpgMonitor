package com.ppg.android;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * write the red infrared data
 */
public class FileUtils {

    public static void createFile(String filePath,String fileName) {
        if (filePath == null || "".equals(filePath)) {
            return;
        }
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(filePath + File.separator + fileName+".csv");
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static void writeToFile(String path,String fileName,ArrayList<String> entrys) throws IOException {
        createFile(path,fileName);
        FileWriter fw=new FileWriter(new File(path + File.separator + fileName+".csv"));
        BufferedWriter bw=new BufferedWriter(fw);
        for(String arr:entrys){
            bw.write(arr+"\t\n");
        }
        bw.close();
        fw.close();
    }

}
