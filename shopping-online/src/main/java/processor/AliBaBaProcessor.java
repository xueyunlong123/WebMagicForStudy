package processor;

import lombok.extern.slf4j.Slf4j;
import webmagic.Page;
import webmagic.Request;
import webmagic.Site;
import webmagic.Spider;
import webmagic.processor.PageProcessor;
import webmagic.utils.HttpConstant;

import java.util.List;

/**
 * Created by xueyunlong on 17-3-20.
 */
@Slf4j
public class AliBaBaProcessor implements PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    @Override
    public void process(Page page) {

        log.debug("下载页面");
        //发现url
        if (page.getRequest().getUrl().contains("https://s.1688.com/company/company_search.htm")){

            List<String> detailUrls = page.getHtml().xpath("//div[@class='list-item-title']//a[@class='list-item-title-text']").links().all();
            page.addTargetRequests(detailUrls);

            String nextUrl = page.getHtml().xpath("//div[@class='page-bottom']//a[@class='page-next']").links().get();
            page.addTargetRequest(nextUrl);

            //解析页面
        }else{
            log.debug("解析页面");


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

    public static void main(String[] args) {
        Spider.create(new AliBaBaProcessor())
                .site(
                        Site.me()
                                .setDomain(".1688.com")
                        .addCookie("cna","/Ya1EP5jrVsCAWombe313kW4")
                ).addRequest(
                        Request.builder()
//                                .url("https://s.1688.com/company/company_search.htm?keywords=%B1%B1%BE%A9&n=y&spm=a260k.635.1998096057.d1")
                                .url("https://markheng.1688.com/?spm=0.0.0.0.H00Jt1")
                                .method(HttpConstant.Method.GET)
                                .build()
                )
                .run();
    }
}
