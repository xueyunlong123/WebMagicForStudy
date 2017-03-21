package com.xyl.crawler.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xueyunlong on 17-3-17.
 */
@Data
@Builder
public class GoodModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shopName;

    private String shopType;

    private String shopCity;

    private String shopOwnerName;

    private String shopDsr;

    private String goodName;

    private String goodUrl;

    private String goodOriginPrice;

    private String goodCurrentPrice;

    //库存
    private String goodStock;

    private String goodPostage;

    private String monthlySales;

    private String monthlyComment;

    private String totalCommentNumber;

}
