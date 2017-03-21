package com.xyl.common.mybatis.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.pagehelper.PageInfo;

import lombok.Data;

/**
 * Created by chenwen on 16/10/22.BasePageResDTO
 * 基本分页响应参数
 */
@Data
public class BasePageResDTO<T extends BasePageReqDTO> extends BasePageReqDTO{
    /**
     * 总条数
     */
    protected Integer total;

    /**
     * 总页数
     */
    @JSONField(name = "page_size")
    protected Integer pageSize;

    /**
     * 设置返回 当前页 和 每页数
     * @param pageInfo 请求参数
     * @return
     */
    public T init(PageInfo pageInfo){
        this.setCursor(pageInfo.getPageNum());
        this.setStep(pageInfo.getPageSize());
        this.setTotal(Math.toIntExact(pageInfo.getTotal()));
        this.setPageSize(pageInfo.getPages());
        return (T) this;
    }
}
