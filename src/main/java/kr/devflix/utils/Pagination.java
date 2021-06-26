package kr.devflix.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class Pagination {

    private final int currentPage;
    private final int resultMax;
    private final long totalCount;
    private final int pageListSize;
    private List<Integer> pageNumList = new ArrayList<>();

    private static final int DEFAULT_RESULT_MAX = 20;
    private static final int DEFAULT_CURRENT_PAGE = 0;
    private static final int DEFAULT_PAGE_LIST_SIZE = 5;

    public Pagination(int currentPage, int resultMax, long totalCount, int pageListSize) {
        this.currentPage = currentPage;
        this.resultMax = resultMax;
        this.totalCount = totalCount;
        this.pageListSize = pageListSize;

        setPageNumList();
    }

    public Pagination(int currentPage, int resultMax, long totalCount) {
        this.currentPage = currentPage;
        this.resultMax = resultMax;
        this.totalCount = totalCount;
        this.pageListSize = DEFAULT_PAGE_LIST_SIZE;

        setPageNumList();
    }

    public Pagination(long totalCount) {
        this.currentPage = DEFAULT_CURRENT_PAGE;
        this.resultMax = DEFAULT_RESULT_MAX;
        this.totalCount = totalCount;
        this.pageListSize = DEFAULT_PAGE_LIST_SIZE;

        setPageNumList();
    }

    private void setPageNumList() {
        // pageNumList 계산
        if (pageListSize > 0 && hasNextPage()) {
            int page = currentPage / pageListSize * pageListSize;

            while (page * resultMax < totalCount && page < (currentPage / pageListSize + 1) * pageListSize) {
                pageNumList.add(page++);
            }
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public boolean isFirstList() {
        if (pageListSize == 0) return true;

        return currentPage / pageListSize == 0;
    }

    public boolean isLastList() {
        return pageNumList.size() < pageListSize || pageNumList.size() > pageListSize;
    }

    public boolean hasNextPage() {
        return pageListSize > 0 && (currentPage + 1) * resultMax < totalCount;
    }

    public boolean hasPreviousPage() {
        return pageListSize > 0 && currentPage / pageListSize > 0;
    }

    public Integer getNextPageNum() {
        return hasNextPage()? (currentPage / pageListSize + 1) * pageListSize : null;
    }

    public Integer getPreviousPageNum() {
        int ppn = currentPage / pageListSize * pageListSize - 1;

        if (ppn * resultMax > totalCount) {
            ppn = (int) (totalCount / resultMax) / pageListSize * pageListSize - 1;
        }

        return hasPreviousPage()? ppn : null;
    }

    public List<Integer> getPageNumList() {
        return pageNumList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("currentPage", currentPage)
                .append("resultMax", resultMax)
                .append("totalCount", totalCount)
                .append("pageListSize", pageNumList)
                .append("hasNextPage", hasNextPage())
                .append("hasPreviousPage", hasPreviousPage())
                .append("isFirstList", isFirstList())
                .append("isLastList", isLastList())
                .append("nextPageNum", getNextPageNum())
                .append("previousPageNum", getPreviousPageNum())
                .append("pageNumList", pageNumList)
                .toString();
    }
}
