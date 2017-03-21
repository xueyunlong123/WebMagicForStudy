package com.xyl.application.crawler.service.impl;

import com.xyl.application.crawler.service.CrawlerBootstrapService;
import com.xyl.crawler.processor.AliBaBaProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xueyunlong on 17-2-22.
 */
@Slf4j
@Service
public class CrawlerBootstrapServiceImpl implements CrawlerBootstrapService {

    @Autowired
    AliBaBaProcessor aliBaBaProcessor;

    @Override
    public void start(){
        try {
            aliBaBaProcessor.run();
        }catch (Exception e){

            log.error("启动失败", e);
            System.exit(-1);

        }
    }

}
