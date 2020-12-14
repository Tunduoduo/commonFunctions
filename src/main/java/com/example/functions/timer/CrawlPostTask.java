package com.example.functions.timer;

import com.example.functions.service.CrawlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CrawlPostTask {
    private static Logger logger = LoggerFactory.getLogger(CrawlPostTask.class);
    @Resource
    CrawlingService crawlingService;

    @Scheduled(cron = "0 0 * * * *")
    public void task(){
        long startTime = System.currentTimeMillis() / 1000;
        logger.info("task begins");
        crawlingService.getPost();
        long endTime = System.currentTimeMillis() / 1000;
        logger.info("task ends, time: " +(endTime-startTime));
    }

}
