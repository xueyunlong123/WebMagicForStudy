package com.xyl.application.crawler;

import com.xyl.application.crawler.service.CrawlerBootstrapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xueyunlong on 17-3-20.
 * 非web应用启动
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {"com.xyl"}
)
@Slf4j
public class Application extends AbstractApplication{
    /**
     * 样例服务注入
     */
    @Autowired
    CrawlerBootstrapService crawlerBootstrapService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void start(String... strings) {
        crawlerBootstrapService.start();
    }
}
