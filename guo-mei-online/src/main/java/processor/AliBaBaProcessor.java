package processor;

import lombok.extern.slf4j.Slf4j;
import webmagic.Page;
import webmagic.Site;
import webmagic.Spider;
import webmagic.processor.PageProcessor;

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
                )
                .addUrl("https://keqiu88.1688.com/page/creditdetail.htm")
                .run();
    }
}
