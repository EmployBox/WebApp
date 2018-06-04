package isel.ps.employbox.model.binder;

import java.security.InvalidParameterException;
import java.util.List;

public class CollectionPage<T>{
    public  final int pageSize;
    private final long totalElements ;
    private final int currentPage;
    private final int lastPageNumber;
    private final List<T> pageList;

    public CollectionPage(long totalElements,int pageSize, int currentPage, List<T> pageList) {

        this.pageSize = pageSize;
        if(currentPage < 0 || currentPage > totalElements / this.pageSize)
            throw new InvalidParameterException("Current page number provided is invalid");

        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageList = pageList;
        if(totalElements != 0 && totalElements % this.pageSize == 0)
            this.lastPageNumber = (int)totalElements / this.pageSize - 1;
        else
            this.lastPageNumber = (int)totalElements / this.pageSize;
    }

    public long getTotalNumberOfElement() {
        return totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public List<T> getPageList() {
        return pageList;
    }
}
