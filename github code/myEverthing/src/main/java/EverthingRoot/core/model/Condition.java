package EverthingRoot.core.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 用于检索条件的模型类
 *
 * 查询的时候用Condition 因为只需要名字和类型
 * 插入的时候用thing   因为要插入全部信息
 */

public class Condition {
    private String name;

    private String fileType;

    //限制的查询结果数量
    private Integer limit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getOrderByDepthAsc() {
        return orderByDepthAsc;
    }

    public void setOrderByDepthAsc(Boolean orderByDepthAsc) {
        this.orderByDepthAsc = orderByDepthAsc;
    }

    //是否按照Depth升序排列
    public Boolean orderByDepthAsc;
    @Override
    public String toString() {
        return "Condition{" +
                "name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
