package com.xyl.crawler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlibabaModel {
    private String url;

    private String name;

    private String province;

    private String number;

    private String managementmodel;

    private String connperson;

    private String phonenumber;

    private String address;

    private String creattime;

    private String registeredcapital;

    private String businessscope;

    private String registeraddress;

    private String productandnum;

    private String officearea;

    private String yearmoney;

}