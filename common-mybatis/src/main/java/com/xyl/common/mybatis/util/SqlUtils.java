package com.xyl.common.mybatis.util;

import com.alpha.common.utils.CollectionUtils;
import com.alpha.common.utils.DateUtil;
import com.alpha.common.utils.ObjectUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Collection;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by chenwen on 16/11/2.
 */
@Slf4j
public class SqlUtils {
    /**
     * 生成sql需要的in参数
     * @param ids
     * @return
     */
    public static <T> String in(Collection<T> ids){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(0");
        for(T id : ids){
            stringBuilder.append(",").append(id);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * 生成sql需要的in参数
     * @param ids
     * @return
     */
    public static String inStrWithout0(Collection<String> ids){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for(String id : ids){
            stringBuilder.append("'").append(id).append("'").append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1) + ")";
    }

    /**
     * 生成sql需要的in参数
     * @param ids
     * @return
     */
    public static String inWithout0(Collection<Long> ids){
        return in(ids).replaceFirst("\\(0,","(");
    }

    /**
     * 为sql添加where子句
     * @param sql
     * @param where
     * @return
     */
    public static String addWhere(String sql, String where){
        if (StringUtils.isEmpty(where)){
            return sql;
        }

        if (sql.toLowerCase().contains("where")){
            int index = sql.toLowerCase().lastIndexOf("where");

            String first = sql.substring(0,index);

            String second = sql.substring(index + 5, sql.length());

            log.info("first = {}" , first);

            log.info("second = {}" , second);

            log.info("where = {}" , where);

            String result = String.format("%s where %s and %s",first,where,second);

            log.info("result = {}" , result);

            return result;
        }
        return sql;
    }

    /**
     * 添加条件语句
     * @param sql
     * @param name
     * @param value
     */
    public static void addWhere(SQL sql, String name, Object value){
        if (value == null){
            return;
        }
        if (value instanceof Collection){
            if (CollectionUtils.isNotEmpty((Collection) value)){
                if (Long.class.getClass().isAssignableFrom(value.getClass().getClass())) {
                    sql.AND().WHERE(name + " in " + inWithout0((Collection) value));
                }else if (String.class.getClass().isAssignableFrom(value.getClass().getClass())){
                    sql.AND().WHERE(name + " in " + inStrWithout0((Collection<String>) value));
                }
            }
        }else if (value instanceof String){
            if (((String) value).length() == 0){
                return;
            }
            sql.AND().WHERE(name + " = '" + ObjectUtil.getStrValue(value) + "'");
        }else {
            sql.AND().WHERE(name + " = " + ObjectUtil.getStrValue(value));
        }
    }

    /**
     * 添加时间区间调价语句
     * @param sql
     * @param name
     * @param startTime
     * @param endTime
     */
    public static void addWhereTime(SQL sql, String name, Date startTime, Date endTime){
        if (startTime == null || endTime == null){
            return;
        }
        sql.AND().WHERE(name + " >= '" + DateUtil.getDate(DateUtil.DEFAULT_FORMAT, startTime) + "'");
        sql.AND().WHERE(name + " <= '" + DateUtil.getDate(DateUtil.DEFAULT_FORMAT, endTime) + "'");
    }

    public static void main(String[] args) {
        log.info(SqlUtils.addWhere("select * from ca_permission where 1 = 1", "2 = 2"));
    }
}
