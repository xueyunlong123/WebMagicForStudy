package com.xyl.crawler.dao.api;

import com.xyl.crawler.model.AlibabaModel;

public interface AlibabaModelInterface {
    int deleteByPrimaryKey(String url);

    int insert(AlibabaModel record);

    int insertSelective(AlibabaModel record);

    AlibabaModel selectByPrimaryKey(String url);

    int updateByPrimaryKeySelective(AlibabaModel record);

    int updateByPrimaryKey(AlibabaModel record);
}