package com.example.functions.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date
 * @author TIAN
 */
@Service
public class ImportService {
    private final static Logger logger = LoggerFactory.getLogger(ImportService.class);
    public void importExcel(MultipartFile myFile, Map map){

        String fileName = myFile.getOriginalFilename();
        try {
            byte[] bytes = myFile.getBytes();
            System.err.println("upload："+fileName);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            fileName = fileName.substring(0,fileName.lastIndexOf("."));
            //read excel and convert it to list
            List<Map> list = readUserInfo(inputStream,fileType);
            inputStream.close();

        }catch (Exception e) {
            System.err.println("upload error"+fileName+": "+e);
        }
    }


    //read file
    private static List<Map> readUserInfo(InputStream stream, String fileType) throws IOException {
        List<Map> list = null;
        try {
            Workbook wb = null;
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(stream);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(stream);
            } else {
                return null;
            }
            Sheet sheet1 = wb.getSheetAt(0);
            list = new ArrayList<>();
            //read excel
            for (int i = 3; i <= sheet1.getLastRowNum(); i++) {
                Row row = sheet1.getRow(i);
                Map userMap = new HashMap();
                //get data
                userMap.put("id",row.getCell(0).getStringCellValue() + "");
                userMap.put("no",row.getCell(1).getStringCellValue() + "");
                list.add(userMap);
            }
        } catch (Exception e) {
            System.err.println("error");
        }
        return list;
    }

}
