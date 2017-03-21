package com.xyl.crawler.processor;

import com.xyl.crawler.model.AlibabaModel;
import com.xyl.crawler.pipeline.AlibabaPipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import webmagic.Page;
import webmagic.Request;
import webmagic.Site;
import webmagic.Spider;
import webmagic.downloader.HttpClientUtilProxyAbuYun;
import webmagic.processor.PageProcessor;
import webmagic.utils.HttpConstant;

import java.util.List;

/**
 * Created by xueyunlong on 17-3-20.
 */
@Slf4j
@Service
public class AliBaBaProcessor implements PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    @Override
    public void process(Page page) {

        if (page.getRawText().contains("淘宝会员（仅限会员名）请在此登录")){
            log.error("网站限制当前主机ip，请更换ip");
        }else {
            log.debug("---------------------------------解析页面开始--------------------------------");
            //发现url
            if (page.getRequest().getUrl().contains("https://s.1688.com/company/company_search.htm")) {

                List<String> detailUrls = page.getHtml().xpath("//div[@class='list-item-title']//a[@class='list-item-title-text']").links().all();
                page.addTargetRequests(detailUrls);

                String nextUrl = page.getHtml().xpath("//div[@class='page-bottom']//a[@class='page-next']").links().get();
                page.addTargetRequest(nextUrl);

                //解析公司档案页面
            } else if (page.getRequest().getUrl().contains("creditdetail")) {
                //发现
                log.debug("解析公司档案");


            } else {
                //发现公司档案页面
                log.debug("解析详情页面");
                Document document = Jsoup.parse(page.getRawText());
                Element element = document.getElementsByAttributeValue("class", "top-nav-bar-box").get(0);
                //添加公司档案url
                String companyFileUrl = element.getElementsByAttributeValue("data-page-name", "creditdetail").get(0).getElementsByTag("a").attr("href");
                page.addTargetRequest(
                        Request.builder()
                                .url(companyFileUrl)
                                .extras(new HashedMap() {{
                                    put("companyUrl", page.getRequest().getUrl());
                                }})
                                .build()
                );
            }
        }

    }

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    @Override
    public Site getSite() {
        return null;
    }

    public void run(){
        Spider.create(new AliBaBaProcessor())
                .setDownloader(new HttpClientUtilProxyAbuYun())
                .site(
                        Site.me()
                                .addHeader("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36")
                                .addCookie("cna","/Ya1EP5jrVsCAWombe313kW4")
                ).addRequest(
                        Request.builder()
                                .url("https://s.1688.com/company/company_search.htm?keywords=%B1%B1%BE%A9&n=y&spm=a260k.635.1998096057.d1")
                                .method(HttpConstant.Method.GET)
                                .build()
                ).addPipeline(new AlibabaPipeline())
                .thread(20)
                .run();
    }
}
