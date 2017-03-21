package com.xyl.crawler.processor;

import lombok.extern.slf4j.Slf4j;
import webmagic.Page;
import webmagic.Request;
import webmagic.Site;
import webmagic.Spider;
import webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by xueyunlong on 17-3-17.
 */
@Slf4j
public class GuoMeiPhoneProcessor implements PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    public void process(Page page) {
        log.info("获取页面 page is {}",page.getRequest().getUrl());

        /*
        * step 1 判断url属性
        * */
        if (page.getRequest().getUrl().contains("list.gome.com.cn")){
            List<String> firstUrls = page.getHtml().links().all();

            firstUrls.forEach(url->{
                if (url.contains("item.gome.com.cn")){

                }
            });

            log.info("获取到的一级url size 是 {}" , firstUrls.size());
        }


    }

    /**page.getHtml().xpath("//div[@class='lisnav']//li")
     * get the site settings
     *
     * @return site
     * @see Site
     */
    public Site getSite() {
        return null;
    }

    public static void main(String[] args) {
        Spider.create(new GuoMeiPhoneProcessor())
                .site(
                        Site.me()
                )
                .addRequest(
                        Request.builder()
                        .url("https://www.gome.com.cn/")
                        .build()
                )

//                .addPipeline()
//                .setScheduler()
                .run();
    }
}
