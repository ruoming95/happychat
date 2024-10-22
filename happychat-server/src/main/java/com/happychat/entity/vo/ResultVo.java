package com.happychat.entity.vo;

import java.util.*;

public class ResultVo<T> {
    private Integer totalCount;
    private Integer pageSize;
    private Integer PageNo;
    private Integer pageTotal;
    private List<T> list = new ArrayList<T>();

    public ResultVo() {
    }

    public ResultVo(Integer totalCount, Integer pageSize, Integer PageNo, List<T> list) {
        if (PageNo == 0) {
            PageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.PageNo = PageNo;
        this.list = list;
    }

    public ResultVo(Integer totalCount, Integer pageSize, Integer PageNo, Integer pageTotal, List<T> list) {
        if (PageNo == 0) {
            PageNo = 1;
        }
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.PageNo = PageNo;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return PageNo;
    }

    public void setPageNo(Integer PageNo) {
        this.PageNo = PageNo;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
