package com.example.functions.service;

import com.example.functions.util.DateUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Date;

/**
 * date
 * @author TIAN
 */
@Service
public class CrawlingService {
    private final static Logger logger = LoggerFactory.getLogger(CrawlingService.class);
    private final static String url = "https://news.p2peye.com/wdzl";
    public void getPost(){
        Date now = new Date();
        Connection conn = Jsoup.connect(url).timeout(50000);
        setHeader(conn);
        //connect and get the content of the url
        try {
            String html = conn.execute().body();
            Document doc = Jsoup.parse(html);

            String module = doc.select("div[class=mod-title-desc]").html();
            module = module.replaceAll("<a([\\s\\S]*?)>","")
                    .replaceAll("&nbsp;","")
                    .replaceAll("\\n","")
                    .replaceAll("&gt;","")
                    .replaceAll("</a>",">");
            Elements elements = doc.select("div[class=main]").select("div[class=inner]");
            for (Element e: elements){
                //阅读数>100
                int replyCount = Integer.parseInt(e.select("div[class=fd-right]").select("span").get(1).html());
                if(replyCount<100){
                    continue;
                }
                //发布日期不超过3个自然日
                String[] postPubInfo = e.select("div[class=fd-left]").html().split("<span>\\|</span>");
                String pubDateStr = postPubInfo[1].replaceAll("<span>","").replaceAll("</span>","").replaceAll("\\n","");
                Date pubTime = DateUtil.str2Date(pubDateStr+":00");
                int date = Integer.parseInt("1");
                if(System.currentTimeMillis()-pubTime.getTime()>date*24*60*60*1000){
                    break;
                }
                System.err.println(e.select("div[class=hd]").select("a").attr("abs:href"));
                System.err.println((e.select("div[class=hd]").select("a").attr("title")));
                System.err.println((Integer.parseInt(e.select("div[class=fd-right]").select("span").get(4).html())));
                //content
                conn = Jsoup.connect(e.select("div[class=hd]").select("a").attr("abs:href")).timeout(5000);
                setHeader(conn);
                String contentHtml = conn.execute().body();
                Document contentDoc = Jsoup.parse(contentHtml);
                Element td = contentDoc.getElementById("article_content");
                String content = td.html().replaceAll("<span([\\s\\S]*?)>","")
                        .replaceAll("</span>","")
                        .replaceAll("<a([\\s\\S]*?)>","")
                        .replaceAll("</a>","")
                        .replaceAll("<div([\\s\\S]*?)>","")
                        .replaceAll("</div>","")
                        .replaceAll("<strong>","")
                        .replaceAll("</strong>","")
                        .replaceAll("<p([\\s\\S]*?)>","")
                        .replaceAll("</p>","");
                if(content.length()>8000){
                    continue;
                }
                content = content.replaceAll("<img([\\s\\S]*?)/>","");
                //发布作者以及时间
                Elements pubInfo = contentDoc.select("p[class=xg1]");
                String[] pubList = pubInfo.get(0).html().split("<span class=\"pipe\">\\|</span>");
                String author = pubList[0].replaceAll("发布者:","")
                        .replaceAll("<a([\\s\\S]*?)>","")
                        .replaceAll("</a>","").trim();
            }

        }catch (Exception e){
            logger.error("getP2peyePost error ",e);
        }
    }
    private static void setHeader(Connection conn){
        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.header("Accept-Encoding", "gzip, deflate, sdch");
        conn.header("Accept-Language", "zh-CN,zh;q=0.8");
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
    }
}
