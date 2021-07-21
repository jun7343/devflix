package kr.devflix.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;

public class Pagination {

    private final int currentPage;
    private final int size;
    private final long totalCount;
    private final int listSize;
    private final ArrayList<Integer> pageNumList = new ArrayList<>();
    private final boolean firstList;
    private final boolean lastList;
    private final Integer previousListPageNum;
    private final Integer nextListPageNum;

    public Pagination(int currentPage, int size, int listSize, long totalCount) {
        this.currentPage = currentPage;
        this.size = size;
        this.listSize = listSize;
        this.totalCount = totalCount;

        int pageStart = listSize > 0? currentPage / listSize * listSize : 0;

        for (int page = pageStart; page * size < totalCount && pageNumList.size() < listSize; page++) {
            pageNumList.add(page);
        }

        firstList = listSize > 0? currentPage / listSize == 0 : true;
        lastList = listSize > 0? (currentPage / listSize + 1) * listSize * size >= totalCount : true;
        previousListPageNum = firstList? null : currentPage / listSize * listSize - 1;
        nextListPageNum = lastList? null : (currentPage / listSize + 1) * listSize;
    }

    public static PaginationBuilder builder(long totalCount) {
        return new PaginationBuilder(totalCount);
    }

    public static class PaginationBuilder {
        private final int DEFAULT_SIZE = 20;
        private final int DEFAULT_CURRENT_PAGE = 0;
        private final int DEFAULT_LIST_SIZE = 5;

        private Integer currentPage;
        private Integer size;
        private Integer listSize;
        private final long totalCount;

        PaginationBuilder(Long totalCount) {
            this.totalCount = totalCount;
        }

        public PaginationBuilder currentPage(Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public PaginationBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public PaginationBuilder listSize(Integer listSize) {
            this.listSize = listSize;
            return this;
        }

        public Pagination build() {
            if (currentPage == null) currentPage = DEFAULT_CURRENT_PAGE;
            if (size == null) size = DEFAULT_SIZE;
            if (listSize == null) listSize = DEFAULT_LIST_SIZE;

            return new Pagination(currentPage, size, listSize, totalCount);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("currentPage", currentPage)
                .append("size", size)
                .append("totalCount", totalCount)
                .append("listSize", listSize)
                .append("pageNumList", pageNumList)
                .append("firstList", firstList)
                .append("lastList", lastList)
                .append("previousListPageNum", previousListPageNum)
                .append("nextListPageNum", nextListPageNum)
                .toString();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getSize() {
        return size;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public int getListSize() {
        return listSize;
    }

    public ArrayList<Integer> getPageNumList() {
        return pageNumList;
    }

    public boolean isFirstList() {
        return firstList;
    }

    public boolean isLastList() {
        return lastList;
    }

    public Integer getPreviousListPageNum() {
        return previousListPageNum;
    }

    public Integer getNextListPageNum() {
        return nextListPageNum;
    }
}
