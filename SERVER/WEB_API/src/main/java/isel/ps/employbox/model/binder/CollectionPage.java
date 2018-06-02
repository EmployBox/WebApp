package isel.ps.employbox.model.binder;

import java.security.InvalidParameterException;
import java.util.stream.Stream;

public class CollectionPage<T>{
    public static final short PAGE_SIZE = 5;
    private final long totalElements ;
    private final int currentPage;
    private final int lastPageNumber;
    private final Stream<T> pageList;

    public CollectionPage(long totalElements, int currentPage, Stream<T> pageList) {

        if(currentPage < 0 || currentPage > totalElements / PAGE_SIZE)
            throw new InvalidParameterException("Current page number provided is invalid");

        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageList = pageList;
        if(totalElements != 0 && totalElements % PAGE_SIZE == 0)
            this.lastPageNumber = (int)totalElements / PAGE_SIZE - 1;
        else
            this.lastPageNumber = (int)totalElements / PAGE_SIZE;
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

    public Stream<T> getPageList() {
        return pageList;
    }
}
