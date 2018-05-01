package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class HalCollection extends ResourceSupport{

    @JsonProperty
    private final long size;

    @JsonUnwrapped
    private final Resources<EmbeddedWrapper> embeddedItems;

    @JsonCreator
    public HalCollection(long size, Resources<EmbeddedWrapper> embeddedItems, Class selfController, Object[] parameters) {
        this.size = size;
        this.embeddedItems = embeddedItems;
        this.add( linkTo(selfController, parameters).withSelfRel());
    }
}
