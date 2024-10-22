package com.happychat.entity.query;

public class BaseQuery {
    private SimplePage simplePage;
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;

    public BaseQuery() {
    }

    public BaseQuery(SimplePage simplePage, Integer pageNo, Integer pageSize, String orderBy) {
        this.simplePage = simplePage;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }
    public SimplePage getSimplePage() {
        return simplePage;
    }
    public void setSimplePage(SimplePage simplePage) {
        this.simplePage = simplePage;
    }
    public Integer getPageNo() {
        return pageNo;
    }
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
