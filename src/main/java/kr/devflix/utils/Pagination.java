package kr.devflix.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class Pagination {

    private final int currentPage;
    private final int perPage;
    private final long totalCount;
    private final int pageListSize;
    private List<Integer> pageNumList = new ArrayList<>();

    private static final int DEFAULT_PER_PAGE = 20;
    private static final int DEFAULT_CURRENT_PAGE = 0;
    private static final int DEFAULT_PAGE_LIST_SIZE = 5;

    public Pagination(int currentPage, int perPage, long totalCount, int pageListSize) {
        this.currentPage = currentPage;
        this.perPage = perPage;
        this.totalCount = totalCount;
        this.pageListSize = pageListSize;

        setPageNumList();
    }

    public Pagination(int currentPage, int perPage, long totalCount) {
        this.currentPage = currentPage;
        this.perPage = perPage;
        this.totalCount = totalCount;
        this.pageListSize = DEFAULT_PAGE_LIST_SIZE;

        setPageNumList();
    }

    public Pagination(long totalCount) {
        this.currentPage = DEFAULT_CURRENT_PAGE;
        this.perPage = DEFAULT_PER_PAGE;
        this.totalCount = totalCount;
        this.pageListSize = DEFAULT_PAGE_LIST_SIZE;

        setPageNumList();
    }

    private void setPageNumList() {
        int page = pageListSize > 0? currentPage / pageListSize * pageListSize : 0;
        int pageEnd = (int) (totalCount % perPage == 0? totalCount / perPage : totalCount / perPage + 1);

        for (; page < pageEnd; page++) {
            pageNumList.add(page);
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public boolean hasNextPage() {
        return pageListSize > 0 && perPage * pageListSize < totalCount;
    }

    public boolean hasPreviousPage() {
        return pageListSize > 0 && currentPage / pageListSize > 0;
    }

    public Integer getNextPageNum() {
        return hasNextPage()? (currentPage / pageListSize + 1) * pageListSize : null;
    }

    public Integer getPreviousPageNum() { return hasPreviousPage()? currentPage / pageListSize * pageListSize - 1 : null;}

    public List<Integer> getPageNumList() {
        return pageNumList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("currentPage", currentPage)
                .append("perPage", perPage)
                .append("totalCount", totalCount)
                .append("pageListSize", pageNumList)
                .append("hasNextPage", hasNextPage())
                .append("hasPreviousPage", hasPreviousPage())
                .append("nextPageNum", getNextPageNum())
                .append("previousPageNum", getPreviousPageNum())
                .append("pageNumList", pageNumList)
                .toString();
    }
}
