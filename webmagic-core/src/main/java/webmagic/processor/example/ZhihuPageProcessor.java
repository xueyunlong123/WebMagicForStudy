package webmagic.processor.example;

import webmagic.Page;
import webmagic.Site;
import webmagic.Spider;
import webmagic.processor.PageProcessor;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.6.0
 */
public class ZhihuPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("https://www\\.zhihu\\.com/question/\\d+/answer/\\d+.*").all());
        page.putField("title", page.getHtml().xpath("//h2[@class='zm-item-title']/a/text()").toString());
        page.putField("question", page.getHtml().xpath("//div[@id='zh-question-detail']//tidyText()").toString());
        page.putField("answer", page.getHtml().xpath("//div[@id='zh-question-answer-wrap']//div[@class='zm-editable-content']/tidyText()").toString());
        if (page.getResultItems().get("title")==null){
            //skip this page
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ZhihuPageProcessor()).addUrl("https://www.zhihu.com/explore").run();
    }
}