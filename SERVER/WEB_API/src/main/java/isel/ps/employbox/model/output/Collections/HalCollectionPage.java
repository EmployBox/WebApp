package isel.ps.employbox.model.output.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.model.binders.CollectionPage;
import org.springframework.hateoas.Link;

import java.util.List;

import static isel.ps.employbox.model.output.OutputDto.HOSTNAME;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class HalCollectionPage<T> {
    @JsonIgnore
    private final Class selfController;
    @JsonIgnore
    private final Object [] parameters;
    @JsonIgnore
    private final CollectionPage<T> elementsPage;
    @JsonProperty
    private final long size;
    @JsonProperty
    private final int page_size;
    @JsonProperty
    private final int current_page;
    @JsonProperty
    private final long last_page;
    @JsonProperty
    private final _Links _links;
    @JsonProperty
    private _Embedded _embedded = null;

    @JsonCreator
    public HalCollectionPage(
            CollectionPage<T> elementsPage,
            List<Object> embeddedItems,
            Class selfController,
            Object ... parameters)
    {
        this.size = elementsPage.getTotalNumberOfElement();
        this.current_page = elementsPage.getCurrentPage();
        this.last_page = elementsPage.getLastPageNumber();
        this.selfController = selfController;
        this.parameters = parameters;
        this.elementsPage = elementsPage;
        this._links = new _Links();
        if(elementsPage.getTotalNumberOfElement() != 0)
            this._embedded = new _Embedded(embeddedItems);
        System.out.println(embeddedItems);
        this.page_size = elementsPage.pageSize;
    }

    private String getPageQueryString(int page, int pageSize){
        return String.format("?page=%d+&pageSize=%d+", page);
    }


    private class _Embedded {
        @JsonProperty
        public List<Object> items;

        @JsonCreator
        public _Embedded(List<Object> items){
            this.items = items;
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        @JsonProperty
        private Next next = null;

        @JsonProperty
        private Prev prev = null;

        @JsonProperty
        private First first = null;

        @JsonProperty
        private Last last = null;


        public _Links(){
            if(current_page > 0) {
                this.prev = new Prev();
                this.first = new First();
            }
            if(current_page != elementsPage.getLastPageNumber()) {
                this.next = new Next();
                if ((current_page + 1) != last_page)
                    this.last = new Last();
            }
        }

        private class Self{
            @JsonProperty
            final String href = HOSTNAME + linkTo(selfController, parameters).slash(getPageQueryString(current_page, page_size)).withSelfRel().getHref();
        }

        private class Next{
            @JsonProperty
            final String href = HOSTNAME + linkTo(selfController, parameters).slash(getPageQueryString(current_page + 1, page_size)).withRel(Link.REL_NEXT).getHref();
        }

        private class Prev{
            @JsonProperty
            final String href = HOSTNAME + linkTo(selfController, parameters).slash(getPageQueryString(current_page - 1, page_size)).withRel(Link.REL_PREVIOUS).getHref();
        }

        private class First{
            @JsonProperty
            final String href = HOSTNAME + linkTo(selfController, parameters).slash(getPageQueryString(0, page_size)).withSelfRel().getHref();
        }

        private class Last {
            @JsonProperty
            final String href = HOSTNAME + linkTo(selfController, parameters).slash(getPageQueryString(elementsPage.getLastPageNumber(), page_size)).withRel(Link.REL_LAST).getHref();
        }
    }
}
