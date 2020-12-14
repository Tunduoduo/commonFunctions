package com.example.functions.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * date
 * @author TIAN
 */
@Service
public class ExportService {
    private static Logger logger = LoggerFactory.getLogger(ExportService.class);

    //query data
    public List<Map> getDataList(){
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("id",1);
        map.put("no",1);
        list.add(map);
        return list;
    }
}
