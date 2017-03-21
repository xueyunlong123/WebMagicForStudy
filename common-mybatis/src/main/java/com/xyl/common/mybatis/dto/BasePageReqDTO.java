package com.xyl.common.mybatis.dto;

/**
 * Created by chenwen on 16/10/22.
 * 基本分页请求参数
 */
public class BasePageReqDTO {
    /**
     * 访问第几页
     */
    protected Integer cursor;

    /**
     * 每页显示多少条
     */
    protected Integer step;

    public Integer getCursor() {
        return cursor == null || cursor <= 0 ? 1 : cursor;
    }

    public Integer getStep() {
        return step == null || step <= 0 ? 1 : step > 50 ? 50 : step;
    }

    public void setCursor(Integer cursor) {
        this.cursor = cursor;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
