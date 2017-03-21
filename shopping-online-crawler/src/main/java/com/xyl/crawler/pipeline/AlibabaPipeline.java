package com.xyl.crawler.pipeline;

import lombok.extern.slf4j.Slf4j;
import webmagic.ResultItems;
import webmagic.Task;
import webmagic.pipeline.Pipeline;

/**
 * alibaba供应商信息处理
 * Created by xueyunlong on 17-3-21.
 */
@Slf4j
public class AlibabaPipeline implements Pipeline{
    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {

        log.debug("get result is {}" , resultItems.getAll().toString());
    }
}
