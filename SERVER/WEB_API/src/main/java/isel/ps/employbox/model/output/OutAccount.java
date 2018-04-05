package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.AccountController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutAccount extends ResourceSupport {

    @JsonProperty
    private final long accountID;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final float rating;

    public OutAccount(long accountID, String email, float rating) {
        this.accountID = accountID;
        this.email = email;
        this.rating = rating;
        this.add(linkTo( AccountController.class).slash(accountID).withSelfRel());
    }
}
