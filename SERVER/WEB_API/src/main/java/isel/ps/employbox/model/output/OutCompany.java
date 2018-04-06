package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CompanyController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutCompany extends ResourceSupport {

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final float rating;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String specialization;

    @JsonProperty
    private final short yearFounded;

    @JsonProperty
    private final String logoUrl;

    @JsonProperty
    private final String webpageUrl;

    @JsonProperty
    private final String description;

    public OutCompany(long accountId, String email, float rating, String name, String specialization, short yearFounded, String logoUrl, String webpageUrl, String description) {
        this.accountId = accountId;
        this.email = email;
        this.rating = rating;
        this.name = name;
        this.specialization = specialization;
        this.yearFounded = yearFounded;
        this.logoUrl = logoUrl;
        this.webpageUrl = webpageUrl;
        this.description = description;
        this.add( linkTo(CompanyController.class).slash(accountId).withSelfRel());
    }
}
