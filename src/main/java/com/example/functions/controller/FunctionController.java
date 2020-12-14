package com.example.functions.controller;

import com.example.functions.service.ExportService;
import com.example.functions.service.ImportService;
import com.example.functions.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * date
 * @author TIAN
 */
@Controller
@RequestMapping("/")
public class FunctionController {

    @Autowired
    ImportService importService;
    @Autowired
    ExportService exportService;

    @RequestMapping(value = "upLoadUserFile")
    @ResponseBody
    public Map<String,Object> uploadFile(@RequestParam("myFile") MultipartFile myFile, @RequestParam("isBond")String isBond) throws Exception {
        Map<String,Object> reMap = new HashMap<String,Object>();
        importService.importExcel(myFile,reMap);
        return reMap;
    }
    /**
     * download Excel
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("/downloadExcel")
    @ResponseBody
    public String downloadExcel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        System.err.println("Excel begins to generate");

        try {
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("multipart/form-data");
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=empty.xls");
            List list = exportService.getDataList();
            LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
            headers.put("id","id");
            headers.put("no","no");
            String fileTitleName = "Info" ;
            String fileName = fileTitleName+ "_" + (new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date());
            ExcelUtil.ersList2Excel(list, fileTitleName, headers,fileName,httpServletResponse);
        } catch (Exception ex) {
            System.err.println("fail "+ ex);
        }finally {
            System.err.println("Excel ends");
        }
        return "";
    }


}
