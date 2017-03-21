package com.xyl.crawler.processor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import com.xyl.crawler.model.GoodModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import webmagic.Page;
import webmagic.Request;
import webmagic.Site;
import webmagic.Spider;
import webmagic.pipeline.ConsolePipeline;
import webmagic.processor.PageProcessor;
import webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xueyunlong on 17-3-17.
 */
@Slf4j
public class YiHaoDianProcessor implements PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    public void process(Page page) {
        log.info("获取页面 page is {}",page.getRequest().getUrl());

        /*
        * step 1 初始页面逻辑
        * */
        if (page.getRequest().getUrl().startsWith("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?")){

            List<String> firstUrls = new ArrayList<>();
            String html = page.getRawText();
            html = html.substring(0,html.length()-1);
            html = html.substring(22,html.length());
            String jsonObject = JSON.parseObject(html).getString("value");

            Document document = Jsoup.parse(jsonObject);
            List<Element> elements = document.getElementsByAttributeValue("class","hd_sort_list_wrap clearfix").get(0).getElementsByAttributeValue("class","hd_sort_list");
            elements.forEach(element -> {
                element.getElementsByTag("dl").forEach(node->{
                    node.children().forEach(child->{
                       List<Element> elements1 =  child.children();
                       if (elements1.size() == 1 ){
                           String url = elements1.get(0).attr("href");
                           if (url !=null && url.startsWith("http://list.yhd.com")){
                               firstUrls.add(url);
                           }
                       }
                    });
                });
            });
            page.addTargetRequests(firstUrls);
            log.debug("获取到的一级url size 是 {}" , firstUrls.size());
        }else if (page.getRequest().getUrl().startsWith("http://list.yhd.com/themeBuy.do")){
            /*
            * step 2 列表页页面逻辑
            * */
            log.info("获取页面 page is {}",page.getRequest().getUrl());
            List<Selectable> selectables = page.getHtml().xpath("//div[@class='scenarized-shop-col']").nodes();
            selectables.forEach(selectable -> {
                page.getHtml().xpath("//div[@class='inner-wrap']").nodes().forEach(node->{
                    String price = node.xpath("div//[@class='pro-img']//em//text()").get().replace("￥","");
                    String shopName = node.xpath("//div[@class='clearfix pro-d2']//a//text()").get();
                    String totalComment =node.xpath("//div[@class='clearfix pro-d1']//a//text()").get();
                    Map map = new HashMap(){{
                        put("price",price);
                        put("shopName",shopName);
                        put("totalComment",totalComment);
                    }};
                    // 获取商品页面url,并附加商品价格
                    Request request = Request.builder()
                            .url(page.getHtml().xpath("//div[@class='inner-wrap']").nodes().get(0).xpath("div//[@class='pro-img']").links().get())
                            .extras(map)
                            .build();
                    page.addTargetRequest(request);
                });

                //获取查看更多页面url
                List<String> moreGoodUrls = selectable.xpath("//div[@class='shop-col-list']//div[@class='shop-col-more']").links().all();
                page.addTargetRequests(moreGoodUrls);
            });
        }else if (page.getRequest().getUrl().startsWith("http://list.yhd.com")){
            /*
            * step 2.1 另外一种列表页页面逻辑
            * */
            log.info("获取页面 page is {}",page.getRequest().getUrl());

            List<Selectable> nodes = page.getHtml().xpath("//div[@class='mod_search_pro']").nodes();
            nodes.forEach(node->{
                String price = page.getHtml().xpath("//div[@class='mod_search_pro']").nodes().get(0).xpath("//p[@class='proPrice']//em//text()").get();
                String shopName = page.getHtml().xpath("//div[@class='mod_search_pro']").nodes().get(0).xpath("//p[@class='storeName']//a//text()").get();
                String totalComment = page.getHtml().xpath("//div[@class='mod_search_pro']").nodes().get(0).xpath("//p[@class='proPrice']//span[@class='comment']//a//text()").get();

                Map map = new HashMap(){{
                    put("price",price);
                    put("shopName",shopName);
                    put("totalComment",totalComment);
                }};
                // 获取商品页面url,并附加商品价格
                Request request = Request.builder()
                        .url(page.getHtml().xpath("//div[@class='mod_search_pro']").nodes().get(0).xpath("//div[@class='proImg']//a").links().get())
                        .extras(map)
                        .build();
                page.addTargetRequest(request);

            });

        }else if (page.getRequest().getUrl().startsWith("http://search.yhd.com/")){
            /*
            * step 3 查看更多页面逻辑
            * */
            log.info("获取页面 page is {}",page.getRequest().getUrl());

            List<Selectable> nodes = page.getHtml().xpath("//div[@class='mod_product_list clearfix']//div[@class='mod_search_pro']//p[@class='proName']").nodes();
            nodes.forEach(node->{
                String goodUrl = node.xpath("//a").nodes().size() > 0 ? node.xpath("//a").nodes().get(0).links().all().get(0) : "";
                page.addTargetRequest(goodUrl);
            });
        }else if (page.getRequest().getUrl().startsWith("http://item.yhd.com/")){
            /*
            * step 4 商品页面逻辑
            * */
            log.info("获取页面 page is {}",page.getRequest().getUrl());
            GoodModel goodModel = GoodModel.builder()
                    .goodName(page.getHtml().xpath("//div[@class='mod_detailInfo_proName']//h1//text()").get())
                    .goodCurrentPrice(page.getRequest().getExtra("price").toString())
                    .goodOriginPrice(page.getHtml().xpath("//div[@class='price']//del//text()").regex("\\d+.*").get())
                    .goodPostage("网页未提供")
                    .goodUrl(page.getRequest().getUrl())
                    .shopName(page.getRequest().getExtra("shopName").toString())
                    .totalCommentNumber(page.getRequest().getExtra("totalComment").toString())
                    .build();
            page.getResultItems().put("result",goodModel);
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

        Spider.create(new YiHaoDianProcessor())
                .site(
                        Site.me()
                )
                .addRequest(
                        Request.builder()
                                .url("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?callback=GLOBALLEFTMENU_137454&categoryId=137454&cindex=3&leftMenuProvinceId=1&isFixTopNav=true")
                                .build()
//                        Request.builder()
//                                .url("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?callback=GLOBALLEFTMENU_137469&categoryId=137469&cindex=4&leftMenuProvinceId=1&isFixTopNav=true")
//                                .build(),
//                        Request.builder()
//                                .url("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?callback=GLOBALLEFTMENU_137470&categoryId=137470&cindex=5&leftMenuProvinceId=1&isFixTopNav=true")
//                                .build(),
//                        Request.builder()
//                                .url("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?callback=GLOBALLEFTMENU_137468&categoryId=137468&cindex=6&leftMenuProvinceId=1&isFixTopNav=true")
//                                .build(),
//                        Request.builder()
//                                .url("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?callback=GLOBALLEFTMENU_137555&categoryId=137555&cindex=1&leftMenuProvinceId=1&isFixTopNav=true")
//                                .build(),
//                        Request.builder()
//                                .url("http://www.yhd.com/header/ajaxGetGlobalLeftFloatMenuDataV12.do?callback=GLOBALLEFTMENU_137778&categoryId=137778&cindex=2&leftMenuProvinceId=1&isFixTopNav=true")
//                                .build()
                )
                .addPipeline(new ConsolePipeline())
//                .setScheduler()
                .thread(20)
                .run();
    }
}
