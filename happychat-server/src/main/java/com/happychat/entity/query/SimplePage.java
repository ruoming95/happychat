package com.happychat.entity.query;

import com.happychat.enums.PageSize;

public class SimplePage {
    private Integer pageNo;
    private Integer pageSize;
    private Integer countTotal;
    private Integer pageTotal;
    private Integer start;
    private Integer end;

    public SimplePage() {
    }

    public SimplePage(Integer pageNo, Integer pageSize, Integer countTotal, Integer pageTotal, Integer start, Integer end) {
        if (null == pageNo) {
            pageNo = 0;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.countTotal = countTotal;
        this.pageTotal = pageTotal;
        this.start = start;
        this.end = end;
        action();
    }

    public SimplePage(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public SimplePage(Integer pageNo, Integer countTotal, Integer pageSize) {
        if (null == pageNo) {
            pageNo = 0;
        }
        this.countTotal = countTotal;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        action();
    }

    public void action() {
        if (this.pageSize <= 0) {
            this.pageSize = PageSize.SIZE20.getSize();
        }
        if (this.countTotal > 0) {
            this.pageTotal = this.countTotal % this.pageSize == 0 ? this.countTotal / this.pageSize
                    : this.countTotal / this.pageSize + 1;
        } else {
            this.pageTotal = 1;
        }

        if (pageNo <= 1) {
            pageNo = 1;
        }
        if (pageNo > pageTotal) {
            pageNo = pageTotal;
        }
        this.start = (pageNo - 1) * pageSize;
        this.end = pageNo * pageSize;
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

    public Integer getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
        action();
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
}
